<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<title>Map test</title>
	<script src="../../scripts/utils.js" type="text/javascript" language="javascript"></script>
	<script src="../../scripts/map.js" type="text/javascript" language="javascript"></script>
	<link href="../../styles/map.css" rel="stylesheet" type="text/css">
	
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
					zoomLevels="#{TestMapBean.zoomLevels}"
					pointsOfInterest="#{TestMapBean.cities}"
					miniMap="true"
					miniMapZoomLevel="#{TestMapBean.miniMapZoomLevel}"
					miniMapHeight="100"
					miniMapWidth="100"
					miniMapPosition="bottom right" />

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