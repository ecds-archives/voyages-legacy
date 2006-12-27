<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Estimates</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="sections.css" rel="stylesheet" type="text/css">
	<link href="estimates.css" rel="stylesheet" type="text/css">
	<link href="search-table.css" rel="stylesheet" type="text/css">
	<link href="map.css" rel="stylesheet" type="text/css">
	<script src="utils.js" language="javascript" type="text/javascript"></script>
	<script src="eventline.js" language="javascript" type="text/javascript"></script>
	<script src="checkbox-list.js" language="javascript" type="text/javascript"></script>
	<script src="checkbox-list-expandable.js" language="javascript" type="text/javascript"></script>
	<script src="checkbox-list-popup.js" language="javascript" type="text/javascript"></script>
	<script src="map.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>

	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>
	
	<div class="header-links">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="header-link-active"><a href="estimates.faces">Estimates</a></div></td>
			<td><div class="header-link"><a href="search.faces">Database search</a></div></td>
			<td><div class="header-link"><a href="galleryp.faces?obj=1&id=1&set=1&pict=0">Images database</a></div></td>
		</tr>
		</table>
	</div>
	
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
		<tr>
			<td style="width: 240px; vertical-align: top;">
				<div style="margin: 10px 10px 0px 10px;">
					<%@ include file="estimates-selection.jsp" %>
				</div>
			</td>
			<td style="vertical-align: top;">
				<div style="margin: 10px 10px 0px 0px;">
					<%@ include file="estimates-main-panel.jsp" %>
				</div>
			</td>
		</tr>
		</table>
		
	</h:form>
	
</f:view>
</body>
</html>