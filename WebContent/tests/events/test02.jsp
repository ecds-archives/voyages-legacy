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
<script src="../../utils.js" language="javascript" type="text/javascript"></script>

<style type="text/css">

body {
	margin: 0px;
	font-size: 8pt;
	font-family: Verdana; }

input {	
	font-size: 8pt;
	font-family: Verdana; }

table.event-line-left-label {
	-background-color: #AAFFFF; }

table.event-line-top-label {
	-background-color: #AAFFFF; }

div.event-line-graph {
	-background-color: #EEFFFF; }
	
div.event-line-indicator {
	-moz-opacity: 0.1;
	background-color: #333333; }
	
div.event-line-indicator-container {
	-background-color: #EEEEEE;}

div.event-line-indicator-label {
	background-color: White;
	border: 1px solid Black;
	padding: 5px; }
	
div.event-line-mark {
	cursor: pointer;
	background-image: url(../../event-line-mark.png);
	background-repeat: no-repeat; }

div.event-line-mark-pressed {
	cursor: pointer;
	background-image: url(../../event-line-mark-pressed.png);
	background-repeat: no-repeat; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:eventLine
			graphHeight="#{EventLineTestBean2.graphHeight}"
			barWidth="#{EventLineTestBean2.barWidth}"
			horizontalLabels="#{EventLineTestBean2.horizontalLabels}"
			verticalLabels="#{EventLineTestBean2.verticalLabels}"
			graphs="#{EventLineTestBean2.currentGraphs}"
			events="#{EventLineTestBean2.events}" />
			
		<div style="margin-left: 70px; margin-top: 10px">
			<h:commandButton action="#{EventLineTestBean2.moveLeft}" value="#{EventLineTestBean2.moveLeftLabel}" />
			<h:commandButton action="#{EventLineTestBean2.zoomPlus}" value="#{EventLineTestBean2.moveZoomPlusLabel}" />
			<h:commandButton action="#{EventLineTestBean2.zoomMinus}" value="#{EventLineTestBean2.moveZoomMinusLabel}" />
			<h:commandButton action="#{EventLineTestBean2.moveRight}" value="#{EventLineTestBean2.moveRightLabel}" />
		</div>
			
		<div id="debug"></div>

	</h:form>
</f:view>
</body>
</html>