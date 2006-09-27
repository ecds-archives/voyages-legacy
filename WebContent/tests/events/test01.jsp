<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EventLine test</title>

<script src="../../eventline.js" language="javascript" type="text/javascript"></script>

<style type="text/css">

body {
	font-size: 8pt;
	font-family: Verdana; }
	
table.event-line-left-label {
	-background-color: #AAFFFF; }

table.event-line-top-label {
	-background-color: #AAFFFF; }

div.event-line-graph {
	-background-color: #EEFFFF; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:eventLine
			graphHeight="#{EventLineTestBean.graphHeight}"
			barWidth="1"
			horizontalLabels="#{EventLineTestBean.horizontalLabels}"
			verticalLabels="#{EventLineTestBean.verticalLabels}"
			graphs="#{EventLineTestBean.graphs}"
			events="#{EventLineTestBean.events}" />

	</h:form>
</f:view>
</body>
</html>