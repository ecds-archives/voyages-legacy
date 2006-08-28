<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lookup</title>
<script type="text/javascript" src="utils.js"></script>

<style type="text/css">

body, input, select {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }
	
body {
	padding: 0px;
	margin: 0px; }

</style>

<script type="text/javascript">

function addItems()
{

	var items = new Array();
	var form = document.forms["form"];
	var options = form.elements["form:items"].options;
	var lookupSelectId = form.elements["form:lookupSelectId"].value;
	
	for (var i=0; i<options.length; i++)
	{
		var option = options[i];
		if (option.selected)
		{
			var item = new Object();
			item.value = option.value;
			item.text = option.text;
			items.push(item);
		}
	}

	window.opener.LookupSelectGlobals.addItems(lookupSelectId, items);

}

function closeWindow()
{
	window.close();
}

function init()
{
	document.forms["form"].elements["form:searchForValue"].focus();
	document.forms["form"].elements["form:items"].ondblclick = addItems;
	positionAfterResize();
}

function positionAfterResize()
{

	var w = ElementUtils.getRealPageWidth();
	var h = ElementUtils.getRealPageHeight();
	
	var heightSearchBox = 30;
	var heightInfoBox = 20;
	var heightButtonsBox = 30;
	var heightItems = h - heightSearchBox - heightInfoBox - heightButtonsBox;
	if (heightItems < 0) heightItems = 0;
	
	var searchBox = document.getElementById("searchBox");
	var infoBox = document.getElementById("infoBox");
	var items = document.getElementById("form:items");
	var buttonsBox = document.getElementById("buttonsBox");
	
	searchBox.style.top = 0 + "px";
	searchBox.style.height = (heightSearchBox) + "px";
	
	infoBox.style.top = (heightSearchBox) + "px";
	infoBox.style.height = (heightInfoBox) + "px";
	
	items.style.top = (heightSearchBox + heightInfoBox) + "px";
	items.style.height = (heightItems) + "px";
	items.style.width = (w) + "px";
	
	buttonsBox.style.top = (heightSearchBox + heightInfoBox + heightItems) + "px";
	buttonsBox.style.height = (heightButtonsBox) + "px";
	
}

</script>

</head>
<body onresize="positionAfterResize()" onload="init()">
<f:view>
	<h:form id="form">
	
		<h:inputHidden id="sourceId" value="#{LookupBean.sourceId}" />
		<h:inputHidden id="lookupSelectId" value="#{LookupBean.lookupSelectId}" />
	
		<table border="0" cellspacing="0" cellpadding="0" id="searchBox" style="position: absolute;">
		<tr>
			<td style="padding-left: 5px;">Search for:</td>
			<td style="padding-left: 5px;"><h:inputText id="searchForValue" value="#{LookupBean.searchForValue}" style="width: 150px;" /></td>
			<td style="padding-left: 5px;"><h:commandButton id="searchButton" action="#{LookupBean.search}" value="Search" /></td>
		</tr>
		</table>
		
		<div id="infoBox" style="position: absolute;">
			<h:outputText value="#{LookupBean.infoLine}" />
		</div>
		
		<h:selectManyListbox id="items" style="position: absolute;">
			<f:selectItems value="#{LookupBean.items}" />
		</h:selectManyListbox>
		
		<table border="0" cellspacing="0" cellpadding="0" id="buttonsBox" style="position: absolute;">
		<tr>
			<td style="padding-left: 5px;"><input type="button" value="Add selected" onclick="addItems()" ></td>
			<td style="padding-left: 5px;"><input type="button" value="Close window" onclick="closeWindow()" ></td>
		</tr>
		</table>
		
	</h:form>
</f:view>
</body>
</html>