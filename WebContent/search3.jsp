<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.emory.library.tas.web.search3.Search" %>
<%@ page import="com.metaparadigm.jsonrpc.JSONRPCBridge" %>
<% JSONRPCBridge.getGlobalBridge().registerClass("Search", Search.class); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>
<link href="common.css" rel="stylesheet" type="text/css">
<script language="javascript" src="jsonrpc.js"></script>

<script language="javascript" type="text/javascript">

var json = null;
window.onload = newSearch; //restorePreviousState;

function ensureJSONRPC()
{
	if (!json) json = new JSONRpcClient("JSON-RPC");
}

function restorePreviousState()
{
	ensureJSONRPC();
	json.Search.getLastQuery(lastQueryLoaded);
}

function lastQueryLoaded(result)
{
	alert(result);
}

function addCondition()
{

	var frame = document.getElementById("conditionsDiv");
	
	var tbl = document.createElement("table");
	frame.appendChild(tbl);

	var tr = tbl.insertRow(0);
	var labelTd = tr.insertCell(0);
	var textboxTd = tr.insertCell(1);
	var removeTd = tr.insertCell(2);
	
	labelTd.innerHTML = field.label;
	
	var textbox = document.createElement("input");
	textbox.type = "hidden";
	textbox.name = field.name;
	textboxTd.appendChild(textbox);

	var textbox = document.createElement("input");
	textbox.type = "text";
	textbox.name = "field";
	textbox.value = field.name;
	textboxTd.appendChild(textbox);

	var removeBtn = document.createElement("input");
	removeBtn.type = "button";
	removeBtn.value = "Remove";
	removeBtn.onclick = removeCondition;
	removeBtn["conditionTable"] = tbl;
	textboxTd.appendChild(removeBtn);

}

function removeCondition()
{
	var frame = document.getElementById("conditionsDiv");
	frame.removeChild(this["conditionTable"]);
}

function newSearch()
{

	var searchQuery =
		[{
			javaClass : "edu.emory.library.tas.web.search3.SearchCondition",
			field : "name", 
			searchFor : "a"
		},
		{
			javaClass : "edu.emory.library.tas.web.search3.SearchCondition",
			field : "name", 
			searchFor : "b"
		}];

	ensureJSONRPC();
	json.Search.newSearch(searchLoaded, searchQuery);
}

function searchLoaded(result)
{
	for (var i=0; i<result.list.length; i++)
		alert(result.list[i]);
}

function gotoPage()
{
	ensureJSONRPC();
}

</script>

</head>
<body>
<form name="mainForm">

<table border="0" cellspacing="0" cellpadding="0">
<tr>
	<td valign="top" style="padding-right: 5px;">
	
		<div style="font-weight: bold; background-color: #EEEEEE; padding: 5px;">Add condition</div>
		<table border="0" cellspacing="5" cellpadding="0">
		<tr>
			<td>Field</td>
			<td><select name="fields">
				<option value="name">Name</option>
				<option value="color">Color</option>
				<option value="size">Size</option>
			</select></td>
			<td><input type="button" value="Add" onclick="addCondition()"></td>
		</tr>
		</table>
		
		<div style="font-weight: bold; background-color: #EEEEEE; padding: 5px;">Search conditions</div>
		<div id="conditionsDiv"></div>

	</td>
	<td valign="top" style="padding-left: 5px; border-left: 2px solid #EEEEEE;">
	
		<div style="font-weight: bold; background-color: #EEEEEE; padding: 5px;">Search results</div>
		<div id="searchResults"></div>

	</td>
</tr>
</table>

</form>
</body>
</html>