<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lookup</title>

<style type="text/css">

body {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }

</style>

<script>

function add()
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
	window.close();

}

</script>

</head>
<body>
<f:view>
	<h:form id="form">
		<h:inputHidden id="sourceId" value="#{LookupBean.sourceId}" />
		<h:inputHidden id="lookupSelectId" value="#{LookupBean.lookupSelectId}" />
	
		Search for:
		<h:inputText id="searchForValue" value="#{LookupBean.searchForValue}" />
		<h:commandButton id="searchButton" action="#{LookupBean.search}" value="Search" />
		
		<br>
		
		<h:selectManyListbox id="items">
			<f:selectItems value="#{LookupBean.items}" />
		</h:selectManyListbox>
		
		<input type="button" value="OK" onclick="add()" >
		
	</h:form>
</f:view>
</body>
</html>