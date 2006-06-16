<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>

<script language="javascript" type="text/javascript">

window.onload = loadFromBuilder;

function getHiddenList()
{
	return window.opener.document.forms[formName].elements[hiddenFieldName];
}

function getDisplayList()
{
	return window.opener.document.forms[formName].elements[displayFieldName];
}

function loadFromBuilder()
{

	var list = getHiddenList().value.split(",");

	var lookup = new Array();
	for (var i = 0; i < list.length; i++)
		lookup[list[i]] = true;

	var els = document.forms["form"];
	for (var i = 0; i < els.length; i++)
	{
		var el = els[i];
		if (el.type == "checkbox")
			if (lookup[el.value])
				el.checked = true;
	}

}

function saveAndClose()
{

	var els = document.forms["form"];

	var list = new Array();
	for (var i = 0; i < els.length; i++)
	{
		var el = els[i];
		if (el.type == "checkbox")
			if (el.checked)
				list.push(els[i].value);
	}

	getHiddenList().value = list.join(",");
	getDisplayList().value = list.join(",");
	window.close();
	
}

</script>

</head>
<body>

<f:view>
	<h:form id="form">

		<s:dictionatyList attribute="#{DictionaryListBean.attribute}" list="#{DictionaryListBean.list}"  />
		
		<input type="button" onclick="saveAndClose()" value="OK">

	</h:form>
</f:view>

</body>
</html>