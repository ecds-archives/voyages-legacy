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

<link href="import.css" rel="stylesheet" type="text/css">

<script language="javascript" type="text/javascript" src="jslib/jsonrpc.js"></script>
<script language="javascript" type="text/javascript">

var json = null;
var noOfMessages = 0;
var noOfWarnings = 0;
var importDir = null;

window.onload = function()
{
	
	json = new JSONRpcClient("JSON-RPC");
	
	var form = document.forms["form"];
	importDir = form.elements["form:currentImportDir"].value;
	noOfMessages = parseInt(form.elements["form:currentNoOfMessages"].value);
	noOfWarnings = parseInt(form.elements["form:currentNoOfWarnings"].value);
	
	refresh();

}

function refresh()
{
	json.ImportLog.loadDetail(detailLoaded, importDir, noOfMessages);
}

function detailLoaded(detail, exception)
{

	var logItems = detail.logItems;
	var logItemsTbl = document.getElementById("form:logItems");
	for (var i = 0; i < logItems.length; i++)
	{
		
		var tr = logItemsTbl.insertRow(1);
		var logItem = logItems[i];
		
		if (logItem.type == 1) noOfWarnings++;
		
		var icon = document.createElement("img");
		icon.width = icon.height = 16;
		icon.src = logItem.typeImg;
		
		tr.insertCell(0).appendChild(icon);
		tr.insertCell(1).appendChild(document.createTextNode(logItem.timeText));
		tr.insertCell(2).appendChild(document.createTextNode(logItem.stageLabel));
		tr.insertCell(3).appendChild(document.createTextNode(logItem.message));
	
	}

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
	<h:form id="form">
	
		<h1>Import detail</h1>
		
		<h:inputHidden id="currentImportDir" value="#{ImportLog.currentImportDir}" />
		<h:inputHidden id="currentNoOfMessages" value="#{ImportLog.currentNoOfMessages}" />
		<h:inputHidden id="currentNoOfWarnings" value="#{ImportLog.currentNoOfWarnings}" />

		<div class="main-container">

			<h2>Overview</h2>
	
			<table border="0" cellspacing="5" cellpadding="0">
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
			
			<h2>Import log</h2>
	
			<h:dataTable id="logItems" value="#{ImportLog.currentLogItems}" var="item" border="0" cellpadding="0" cellspacing="5">
				<h:column>
					<h:graphicImage width="16" height="16" url="#{item.typeImg}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Time" />
					</f:facet>
					<h:outputText value="#{item.timeText}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Stage" />
					</f:facet>
					<h:outputText value="#{item.stageLabel}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Message" />
					</f:facet>
					<h:outputText value="#{item.message}" />
				</h:column>
			</h:dataTable>
			
			<h:commandButton action="back" value="Back" />
			
		</div>

	</h:form>
</f:view>
</body>
</html>