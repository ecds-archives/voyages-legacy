<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editor</title>
<script src="../../utils.js" language="javascript" type="text/javascript"></script>
<script src="../../record-editor.js" language="javascript" type="text/javascript"></script>

<style type="text/css">

body, input, select, texarea {
	font-family: Verdana, sans-serif;
	font-size: 8pt; }
	
table.reditor {
	border-collapse: collapse; }

td.reditor-label {
	padding: 5px 0px 5px 0px;
	border-top: 2px solid #CCCCCC; }

td.reditor-field {
	padding: 5px 0px 5px 10px;
	border-top: 2px solid #CCCCCC; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">

		<h2>Outcome</h2>

		<s:recordEditor id="ship"
			schema="#{EditorTestBean.shipSchema}"
			values="#{EditorTestBean.values}" />
			
		<h2>Outcome</h2>
			
		<s:recordEditor id="outcome"
			schema="#{EditorTestBean.outcomeSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Itinerary</h2>
			
		<s:recordEditor id="itinerary"
			schema="#{EditorTestBean.itinerarySchema}"
			values="#{EditorTestBean.values}" />

		<h:commandButton value="Test submit" />
	
	</h:form>
</f:view>
</body>
</html>