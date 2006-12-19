<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyage</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<script src="../record-editor.js" language="javascript" type="text/javascript"></script>
	<script src="../utils.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	<h:form id="form">

		<h2>Outcome</h2>

		<s:recordEditor id="ship"
			schema="#{AdminVoyageBean.shipSchema}"
			values="#{AdminVoyageBean.shipValues}" />
			
		<%--
			
		<h2>Outcome</h2>
			
		<s:recordEditor id="outcome"
			schema="#{EditorTestBean.outcomeSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Itinerary</h2>
			
		<s:recordEditor id="itinerary"
			schema="#{EditorTestBean.itinerarySchema}"
			values="#{EditorTestBean.values}" />

		<h2>Dates</h2>
			
		<s:recordEditor id="dates"
			schema="#{EditorTestBean.datesSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Crew</h2>

		<s:recordEditor id="crew"
			schema="#{EditorTestBean.crewSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Slave (numbers)</h2>

		<s:recordEditor id="slaveNumbers"
			schema="#{EditorTestBean.slaveNumbersSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Slave (characteritics)</h2>

		<s:recordEditor id="slaveChars"
			schema="#{EditorTestBean.slaveCharacteriticsSchema}"
			values="#{EditorTestBean.values}" />

		<h2>Sources</h2>

		<s:recordEditor id="sources"
			schema="#{EditorTestBean.sourcesSchema}"
			values="#{EditorTestBean.values}" />
			
		--%>

		<h:commandButton value="Test submit" />
	
	</h:form>
</f:view>
</body>
</html>