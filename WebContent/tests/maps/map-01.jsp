<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%

session.setAttribute("test.map", "D:\\Library\\SlaveTrade\\shapefiles\\test.map");
session.setAttribute("test.map", "C:\\Documents and Settings\\jzich\\My Documents\\Library\\SlaveTrade\\shapefiles\\test.map");
session.setAttribute("test.map", "C:\\Documents and Settings\\jzich\\My Documents\\SlaveTrade\\shapefiles\\test.map");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Map test</title>
	<script src="../../utils.js" type="text/javascript" language="javascript"></script>
	<script src="../../map.js" type="text/javascript" language="javascript"></script>
	<script src="../../graphics.js" type="text/javascript" language="javascript"></script>
	<script src="../../graphics-vml.js" type="text/javascript" language="javascript"></script>
	<script src="../../graphics-svg.js" type="text/javascript" language="javascript"></script>
	<link href="../../map.css" rel="stylesheet" type="text/css">
	
	<style>
	body {font-family: Verdana; font-size: 8pt;}
	</style>
	
</head>
<body>
<f:view>
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top">
			
				<s:map
					id="testMap"
					mapFile="test.map"
					miniMap="true"
					miniMapHeight="100"
					miniMapWidth="100"
					miniMapPosition="bottom right"
					serverBaseUrl="../../servlet/maptile" />

				<h:commandButton value="OK" />

			</td>
			<td valign="top">

				<div style="margin-left: 10px;" id="debug"></div>
			
			</td>
		</tr>
		</table>
		
	
	</h:form>
</f:view>
</body>
</html>