<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.emory.library.tast.web.searchJSON.Search" %>
<%@ page import="com.metaparadigm.jsonrpc.JSONRPCBridge" %>
<% JSONRPCBridge.getGlobalBridge().registerClass("Search", Search.class); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Search</title>

<link href="common.css" rel="stylesheet" type="text/css">

<script language="javascript" type="text/javascript" src="jslib/jsonrpc.js"></script>
<script language="javascript" type="text/javascript" src="jslib/prototype.js"></script>
<script language="javascript" type="text/javascript" src="jslib/scriptaculous.js"></script>

<style type="text/css">

table.condition {
	border-bottom: 2px solid #EEEEEE; }

table.results {
	border-collapse: collapse; }

table.results td {
	border-bottom: 2px solid #EEEEEE;
	padding: 5px; }
	
table.results tr.head {
	background-color: #DDDDDD;
	font-weight: bold; }

table.results tr.head td {
	border-top-width: 0px;
	border-bottom-width: 0px; }

div.section {
	font-weight: bold;
	background-color: #DDDDDD;
	padding: 5px; }

table.history {
	cursor: pointer;
	border-bottom: 2px solid #EEEEEE; }
	
table.history td.delete {
	font-weight: bold;
	text-align: right; }

table.history:hover {
	background-color: #EEEEEE; }

</style>

<script language="javascript" type="text/javascript"><!--

var json = null;

window.onload = restorePreviousState;

function ensureJSONRPC()
{
	if (!json) json = new JSONRpcClient("JSON-RPC");
}

function restorePreviousState()
{
	ensureJSONRPC();
	json.Search.getSearchHistory(restorePreviousStateLoaded);
}

function restorePreviousStateLoaded(history)
{
	if (!history)
		return;

	for (var i=0; i<history.list.length; i++)
		addHistoryItem(history.list[i]);
		
	if (history.list.length > 0)
	{
	
		var conditions =
			history.list[history.list.length - 1].
				conditions;
			
		addConditions(conditions);
			
		if (conditions.length > 0)
			search(false);
		
	}
	
}

function addConditionSelected()
{

	var frm = document.forms["mainForm"];
	var sel = frm.elements["fields"];
	
	var newCondition = new Condition(sel.options[sel.selectedIndex].value, "");
	addCondition(newCondition, true);

}

function addConditions(conditions)
{
	for (var i=0; i<conditions.length; i++)
		addCondition(conditions[i], false);
}

function addCondition(condition, withEffect)
{

	var frame = document.getElementById("conditionsDiv");
	
	var tbl = document.createElement("table");
	tbl.cellSpacing = 5;
	if (withEffect) tbl.style.display = "none";
	tbl.className = "condition";
	frame.appendChild(tbl);

	var tr = tbl.insertRow(0);
	var labelTd = tr.insertCell(0);
	var textboxTd = tr.insertCell(1);
	var removeTd = tr.insertCell(2);
	
	labelTd.width = 60;
	labelTd.innerHTML = condition.field;
	
	var textbox = document.createElement("input");
	textbox.type = "hidden";
	textbox.name = "field";
	textbox.value = condition.field;
	textboxTd.appendChild(textbox);

	var textbox = document.createElement("input");
	textbox.type = "text";
	textbox.name = condition.field;
	textbox.value = condition.searchFor;
	textboxTd.appendChild(textbox);

	var removeBtn = document.createElement("input");
	removeBtn.type = "button";
	removeBtn.value = "Remove";
	removeBtn.onclick = removeCondition;
	removeBtn["conditionTable"] = tbl;
	textboxTd.appendChild(removeBtn);
	
	if (withEffect)
		Effect.Appear(tbl);

}

function removeCondition()
{
	$("conditionsDiv").removeChild(this["conditionTable"]);
}

function clearConditions()
{
	$("conditionsDiv").innerHTML = "";
}

function takeFromHistory()
{
	clearConditions();
	addConditions(this["conditions"]);
}

function delFromHistory()
{
	json.Search.deleteHistoryItem(historyItemDeleted, this["historyItemId"]);
	$("historyDiv").removeChild(this["historyTbl"]);
}

function historyItemDeleted(result)
{
}

function addHistoryItem(historyItem)
{

	var conditions = historyItem.conditions;
	
	var text = "";	
	for (var i=0; i<conditions.length; i++)
	{
		if (i > 0) text += ", ";
		text += conditions[i].field + " = " +
			"<b>" + conditions[i].searchFor + "</b>";
	}
	
	var historyDiv = $("historyDiv");

	var tbl = document.createElement("table");
	tbl.className = "history";
	tbl.style.display = "none";
	tbl.border = 0;
	tbl.cellSpacing = 5;
	tbl.cellPadding = 0;
	tbl.width = "100%";
	var tr = tbl.insertRow(0);

	var textTd = tr.insertCell(0);
	textTd.innerHTML = text;
	textTd.onclick = takeFromHistory;
	textTd["conditions"] = conditions;
	
	var removeTd = tr.insertCell(1);
	removeTd.className = "delete";
	removeTd.innerHTML = "&times;";
	removeTd.onclick = delFromHistory;
	removeTd["historyTbl"] = tbl;
	removeTd["historyItemId"] = historyItem.id;

	historyDiv.insertBefore(tbl, historyDiv.firstChild);
	new Effect.Appear(tbl);	

}

function newSearch()
{
	search(true);
}

function search(newSearch)
{
	ensureJSONRPC();
	
	var frm = document.forms["mainForm"];

	var conditions = new Array();
	var fields = Form.getInputs(frm, "hidden", "field");

	for (var i=0; i<fields.length; i++)
	{
		var field = fields[i].value;
		var searchFor = Form.getInputs(frm, "text", fields[i].value).pop().value;
		//var searchFor = frm.elements[fields[i].value].value;
		conditions.push(new Condition(field, searchFor));
	}
	
	$("searchResultsTitle").innerHTML = "Search results (searching, please wait ...)";
	json.Search.search(searchLoaded, conditions, newSearch ? 1 : 0);

}

function gotoPrevPage()
{
	json.Search.gotoPrevPage(searchLoaded);
}

function gotoNextPage()
{
	json.Search.gotoNextPage(searchLoaded);
}

function searchLoaded(resultSet, exception)
{

	if (exception)
	{
		alert(exception);
		return;
	}

	if (resultSet.history)
		addHistoryItem(resultSet.history);

	$("searchResultsTitle").innerHTML = "Search results";

	var searchResultsDiv = $("searchResultsDiv");
	searchResultsDiv.innerHTML = "";
	
	var tbl = document.createElement("table");
	tbl.className = "results";
	
	var tr = tbl.insertRow(tbl.rows.length);
	tr.insertCell(0).innerHTML = "Voyage ID";
	tr.insertCell(1).innerHTML = "Nationality";
	tr.insertCell(2).innerHTML = "Shipname";
	tr.insertCell(3).innerHTML = "Captain";
	tr.insertCell(4).innerHTML = "From";
	tr.insertCell(5).innerHTML = "To";
	tr.className = "head";
	
	var results = resultSet.results;
	for (var i=0; i<results.list.length; i++)
	{
		var tr = tbl.insertRow(tbl.rows.length);
		tr.insertCell(0).innerHTML = results.list[i].voyageId;
		tr.insertCell(1).innerHTML = results.list[i].natinimp;
		tr.insertCell(2).innerHTML = results.list[i].shipname;
		tr.insertCell(3).innerHTML = results.list[i].captaina;
		tr.insertCell(4).innerHTML = results.list[i].majbuyrg;
		tr.insertCell(5).innerHTML = results.list[i].majbyimp;
	}
	searchResultsDiv.appendChild(tbl);
	
}

function Condition(field, searchFor)
{
	this.javaClass = "edu.emory.library.tas.web.search3.SearchCondition";
	this.field = field;
	this.searchFor = searchFor;
}

-->
</script>

</head>
<body>
<form name="mainForm" onsubmit="newSearch(); return false;">

<table border="0" cellspacing="0" cellpadding="0">
<tr>
	<td valign="top" style="padding-right: 5px;">
	
		<div class="section">Add condition</div>
		<table border="0" cellspacing="5" cellpadding="0" style="margin-bottom: 10px;">
		<tr>
			<td>Field</td>
			<td><select name="fields">
				<option value="voyageid">Voyage ID</option>
				<option value="natinimp">Nationality</option>
				<option value="shipname">Shipname</option>
				<option value="captaina">Captain</option>
				<option value="majbuyrg">From</option>
				<option value="majbyimp">To</option>
			</select></td>
			<td><input type="button" value="Add" onclick="addConditionSelected()"></td>
		</tr>
		</table>
		
		<div class="section">Search conditions</div>
		<div id="conditionsDiv"></div>
		<div style="margin-top: 5px; margin-bottom: 10px;"><input type="button" onclick="newSearch()" value="Search"></div>

		<div class="section">Search history</div>
		<div id="historyDiv"></div>

	</td>
	<td valign="top" style="padding-left: 5px; border-left: 2px solid #EEEEEE;">
	
		<div class="section" style="margin-bottom: 5px;" id="searchResultsTitle">Search results</div>
		<div id="searchResultsDiv"></div>
		<table border="0" cellspacing="5" cellpadding="0">
		<tr>
			<td><input type="button" onclick="gotoPrevPage()" value="Prev"></td>
			<td><input type="button" onclick="gotoNextPage()" value="Next"></td>
		</tr>
		</table>

	</td>
</tr>
</table>

</form>
</body>
</html>