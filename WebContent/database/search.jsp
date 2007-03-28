<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Search</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-query.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-table.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-basic-stats.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-graph.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	<link href="../styles/timeline.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/lib/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/lib/scriptaculous.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/lib/aa.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/eventline.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/query-builder.js" language="javascript" type="text/javascript"></script>
	
	<script src="../scripts/attributes-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="database" activePageId="search" /></td>
		</tr>
		</table>
	</div>

	<h:form id="form" style="margin: 0px;">
	
		<table border="0" cellspacing="0" cellpadding="0" class="search-main-table">
		<tr>
			<td class="search-side-panel">

				<%@ include file="search-selection.jsp" %>

			</td>
			<td class="search-main-panel">
			
				<s:panelTabSet id="mainPanelSection" selectedSectionId="#{SearchBean.mainSectionId}">

					<s:panelTab title="#{res.database_search_listing}" sectionId="listing">
						<t:htmlTag value="div" styleClass="search-panel">
							<%@ include file="search-tab-table.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.database_search_timeline}" sectionId="basic-graph">
						<t:htmlTag value="div" styleClass="search-panel">
							<%@ include file="search-tab-timeline.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
					<s:panelTab title="#{res.database_search_customgraphs}" sectionId="custom-graphs">
						<t:htmlTag value="div" styleClass="search-panel">
							<%@ include file="search-tab-graph.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
					<s:panelTab title="#{res.database_search_summarystats}" sectionId="basic-statistics">
						<t:htmlTag value="div" styleClass="search-panel">
							<%@ include file="search-tab-summary.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.database_search_maps}" sectionId="map-ports">
						<t:htmlTag value="div" styleClass="search-panel">
							<%@ include file="search-tab-map.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
				</s:panelTabSet>
				
			</td>
		</tr>
		</table>
		
	</h:form>
	
	<div id="debug"></div>

</f:view>
</body>
</html>