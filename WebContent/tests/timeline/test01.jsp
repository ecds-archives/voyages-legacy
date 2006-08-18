<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Timeline</title>
<script type="text/javascript" src="../../timeline.js"></script>
<script type="text/javascript" src="../../utils.js"></script>

<style type="text/css">

body {
	font-family: Verdana, sans-serif;
	font-size: 8pt; }
	
div.timetime-slider-container {
	position: relative;
	height: 20px;
	background-color: #EEEEEE; }
	
div.timetime-knob {
	position: absolute;
	height: 20px;
	width: 10px;
	background-color: Red; }

div.timetime-selection {
	height: 20px;
	position: absolute;
	background-color: Blue; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:timeline
			id="tl"
			markers="1500,1600,1650,1700,1725,1750,1775,1800,1825,1850,1875,1900"
			leftExtent="0"		
			rightExtent="0"
			markerWidth="60" />

	</h:form>
</f:view>
</body>
</html>