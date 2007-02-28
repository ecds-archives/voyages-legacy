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
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-expandable-box.css" rel="stylesheet" type="text/css">

	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-tabs.css" rel="stylesheet" type="text/css">

	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-estimates.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/eventline.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list-expandable.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list-popup.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

		<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="assessment" activePageId="estimates" /></td>
		</tr>
		</table>
	</div>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
		<tr>
			<td class="td-side-panel">
				<div style="margin: 10px 10px 0px 10px;">
					<%@ include file="estimates-selection.jsp" %>
				</div>
			</td>
			<td class="td-main-panel">
				<div>
					<%@ include file="estimates-main-panel.jsp" %>
				</div>
			</td>
		</tr>
		</table>
	</div>
</h:form>
	
</f:view>
</body>
</html>