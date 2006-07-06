<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Import</title>

<script language="javascript" type="text/javascript" src="jslib/jsonrpc.js"></script>
<script language="javascript" type="text/javascript">

var json = null;
var noOfMessages = 0;
var noOfWarnings = 0;

window.onload = function()
{
	json = new JSONRpcClient("JSON-RPC");
	refresh();
}

function refresh()
{
	//alert("before ...");
	json.ImportLogDetail.load(detailLoaded, importDirName, noOfMessages);
}

function detailLoaded(detail, exception)
{

	//alert("loaded ...");

	var logItems = detail.logItems.list;
	var logItemsTbl = document.getElementById("logItemsTbl");
	//var logItemsTbl = document.createElement("table");
	for (var i = 0; i < logItems.length; i++)
	{
		var tr = logItemsTbl.insertRow(logItemsTbl.rows.length);
		var logItem = logItems[i];
		if (logItem.type == 1) noOfWarnings ++;
		tr.insertCell(0).appendChild(document.createTextNode(logItem.time));
		tr.insertCell(1).appendChild(document.createTextNode(logItem.stage));
		tr.insertCell(2).appendChild(document.createTextNode(logItem.type));
		tr.insertCell(3).appendChild(document.createTextNode(logItem.message));
	}
	document.getElementById("xx").appendChild(logItemsTbl);

	/*
	var html = new Array();
	html.push("<table>");
	for (var i = 0; i < logItems.length; i++)
	{
		var logItem = logItems[i];
		if (logItem.type == 1) noOfWarnings ++;
		html.push("<tr>");
		html.push("<td>");
		html.push(logItem.time);
		html.push("</td>");
		html.push("<td>");
		html.push(logItem.time);
		html.push("</td>");
		html.push("<td>");
		html.push(logItem.type);
		html.push("</td>");
		html.push("<td>");
		html.push(logItem.message);
		html.push("</td>");
		html.push("</tr>");
	}
	html.push("</table>");
	document.getElementById("xx").innerHTML = html.join("");
	*/
	
	document.getElementById("voyagesPresent").innerHTML = detail.voyagesPresent ? "importing" : "not importing";
	document.getElementById("slavesPresent").innerHTML = detail.slavesPresent ? "importing" : "not importing";
	document.getElementById("started").innerHTML = detail.timeStart;
	document.getElementById("finished").innerHTML = detail.timeFinish;
	document.getElementById("duration").innerHTML = detail.duration;
	document.getElementById("noOfWarnings").innerHTML = noOfWarnings;
	document.getElementById("status").innerHTML = detail.statusText;
	
	noOfMessages += logItems.length;
	if (!detail.finished) window.setTimeout("refresh()", 1000);
	
}

</script>

</head>
<body>
<f:view>

<b>Overview</b>

<table border="1" cellspacing="5" cellpadding="0">
<tr>
	<td>Voyages data file</td>
	<td id="voyagesPresent">?</td>
</tr>
<tr>
	<td>Slaves data file</td>
	<td id="slavesPresent">?</td>
</tr>
<tr>
	<td>Started</td>
	<td id="started">?</td>
</tr>
<tr>
	<td>Finished</td>
	<td id="finished">?</td>
</tr>
<tr>
	<td>Duration</td>
	<td id="duration">?</td>
</tr>
<tr>
	<td>Number of warnings</td>
	<td id="noOfWarnings">?</td>
</tr>
<tr>
	<td>Status</td>
	<td id="status">?</td>
</tr>
</table>

<div id="xx"></div>

<b>Log</b>

<table border="1" cellspacing="5" cellpadding="0" id="logItemsTbl">
<tr>
	<td>Time</td>
	<td>Stage</td>
	<td>Type</td>
	<td>Message</td>
</tr>
</table>

<script language="javascript" type="text/javascript">
<h:outputFormat escape="false" value="var importDirName = \"{0}\";">
	<f:param value="#{ImportLog.currentImportDirName}" />
</h:outputFormat>
</script>

</f:view>
</body>
</html>