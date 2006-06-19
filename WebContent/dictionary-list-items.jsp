<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="search-jsp.css" rel="stylesheet" type="text/css">

<style type="text/css">
</style>

<script language="javascript" type="text/javascript">

window.onload = loadFromBuilder;

function getHiddenList()
{
	return window.parent.opener.document.forms[formName].elements[hiddenFieldName];
}

function getDisplayList()
{
	return window.parent.opener.document.forms[formName].elements[displayFieldName];
}

function loadFromBuilder()
{

	var list = getHiddenList().value.split(",");

	var lookup = new Array();
	for (var i = 0; i < list.length; i++)
		lookup[list[i]] = true;

	var els = document.forms["form"].elements;
	for (var i = 0; i < els.length; i++)
	{
		var el = els[i];
		if (el.type == "checkbox")
		{
			if (lookup[el.value])
			{
				el.checked = true;
			}
		}
	}

}

function saveAndClose()
{

	var els = document.forms["form"];

	var values = new Array();
	var labels = new Array();
	for (var i = 0; i < els.length; i++)
	{
		var el = els[i];
		if (el.type == "checkbox")
		{
			if (el.checked)
			{
				var id = els[i].value;
				values.push(id);
				labels.push(document.getElementById("label_" + id).innerHTML);
			}
		}
	}

	getHiddenList().value = values.join(",");
	getDisplayList().value = labels.join(", ");
	window.parent.close();

}

function selectAll()
{
	setSelectedAll(true);
}

function clearAll()
{
	setSelectedAll(false);
}

function setSelectedAll(sel)
{

	var els = document.forms["form"].elements;
	var tbl = document.getElementById(tblListId);
	var rows = tbl.rows;

	for (var i = 0; i < rows.length; i++)
	{
		var row = rows[i];
		if (row.style.display != "none")
		{
			var el = row.cells[0].firstChild;
			if (el.type == "checkbox")
			{
				el.checked = sel;
			}
		}
	}

}

var labels = null;

function ensureLabelsArray()
{
	if (!labels)
	{
		labels = new Array();
		var tbl = document.getElementById(tblListId);
		for (var i = 0; i < tbl.rows.length; i++)
			labels.push(tbl.rows[i].cells[1].firstChild.innerHTML.toUpperCase());
	}
}

function searchFor(value)
{

	ensureLabelsArray();
	value = value.toUpperCase();

	var tbl = document.getElementById(tblListId);
	var rows = tbl.rows;
	
	for (var i = 0; i < labels.length; i++)
		rows[i].style.display =
			labels[i].indexOf(value) == -1 ?
			"none" : "";

}

</script>

</head>
<body>
<f:view>
	<h:form id="form">
		<div style="margin: 5px;">
		<s:dictionatyList
			attributeName="#{DictionaryListBean.attributeName}"
			formName="#{DictionaryListBean.formName}"
			hiddenFieldName="#{DictionaryListBean.hiddenFieldName}"
			displayFieldName="#{DictionaryListBean.displayFieldName}"
			dictionary="#{DictionaryListBean.dictionary}"  />
		</div>
	</h:form>
</f:view>
</body>
</html>