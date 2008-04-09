<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Slaves database</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-expandable-box.css" rel="stylesheet" type="text/css">

	<link href="../styles/resources.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-slaves.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-slaves-table.css" rel="stylesheet" type="text/css">	
	
	<script src="../scripts/lib/aa.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/lookup-checkbox-list.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/slaves-search.js" language="javascript" type="text/javascript"></script>
	
	<script src="../scripts/tooltip.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="resources">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="slaves.faces"><h:outputText value="Resources" /></h:outputLink>
		<h:outputText value="Names Database" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
		<tr>
			<td class="td-side-panel">
				<div style="margin: 10px 10px 0px 10px;">
					<%@ include file="slaves-selection.jsp" %>
				</div>
			</td>
			<td class="td-main-panel">
				<div>
					<%@ include file="slaves-main-panel.jsp" %>
				</div>
			</td>
		</tr>
		</table>
	</div>
</h:form>
	
</f:view>
</body>
</html>