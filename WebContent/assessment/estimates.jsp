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
	
	<script src="../scripts/lib/aa.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/eventline.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list-expandable.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/checkbox-list-popup.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/popup.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tigra_hints.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map-hints.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Estimates" />
	</s:siteHeader>
	
	<div id="content">

		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
		<tr>
			<td class="td-side-panel">

				<div style="margin: 10px 10px 0px 10px;">
					<%@ include file="estimates-selection.jsp" %>
				</div>

			</td>
			<td class="td-main-panel">

				<s:panelTabSet selectedSectionId="#{EstimatesSelectionBean.selectedTab}" id="mainPanelSection">
					
					<s:panelTab title="#{res.estimates_main_table}" sectionId="table">
						<t:htmlTag value="div" styleClass="results-panel">
							<%@ include file="estimates-tab-table.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.estimates_main_timeline}" sectionId="timeline">
						<t:htmlTag value="div" styleClass="results-panel">
							<%@ include file="estimates-tab-timeline.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.estimates_main_map}" sectionId="map">
						<t:htmlTag value="div" styleClass="results-panel">
							<%@ include file="estimates-tab-map.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
				</s:panelTabSet>			

			</td>
		</tr>
		</table>

</h:form>
	
</f:view>
</body>
</html>