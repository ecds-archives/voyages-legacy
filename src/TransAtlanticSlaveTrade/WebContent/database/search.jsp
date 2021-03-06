<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Search the Voyages Database</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/info-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-no-results-info-box.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/database-voyage.css" rel="stylesheet" type="text/css">

	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-query.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-table.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-basic-stats.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-graph.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	<link href="../styles/timeline.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-search-tableview.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/popup.css" rel="stylesheet" type="text/css">
	<link href="../styles/popup-permlink.css" rel="stylesheet" type="text/css">

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/search-bundle.min.js" language="javascript" type="text/javascript"></script>

<%  
	// AJAX disabled for launch
	// <script src="../scripts/lib/aa.js" type="text/javascript" language="javascript"></script>
%>

<% 
	// replaced by search-bundle.min.js	
	// <script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/eventline.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/map.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/query-builder.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/popup.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/tooltip.js" language="javascript" type="text/javascript"></script>
	// <script src="../scripts/attributes-menu.js" language="javascript" type="text/javascript"></script>
%>

</head>
<body>
<f:view>

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Search the Voyages Database" />
	</s:siteHeader>

	<h:form id="form" style="margin: 0px;">
	
		<table border="0" cellspacing="0" cellpadding="0" class="search-main-table">
		<tr>
			<td class="search-side-panel">

				<%@ include file="search-selection.jsp" %>

			</td>
			<td class="search-main-panel">
			
				<s:panelTabSet
					id="voyagesListTabs"
					selectedSectionId="#{SearchBean.mainSectionId}"
					rendered="#{!SearchBean.showVoygeDetail}">

					<s:panelTab title="#{res.database_search_listing}" sectionId="listing">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-table.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.database_search_summarystats}" sectionId="basic-statistics">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-summary.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
					
					<s:panelTab title="#{res.database_search_tableview}" sectionId="tableview">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-tableview.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
					<s:panelTab title="#{res.database_search_customgraphs}" sectionId="custom-graphs">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-graph.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
					<s:panelTab title="#{res.database_search_timeline}" sectionId="basic-graph">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-timeline.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
					<s:panelTab title="#{res.database_search_maps}" sectionId="map-ports">
						<t:htmlTag value="div" styleClass="search-panel" rendered="#{!SearchBean.noResult}">
							<%@ include file="search-tab-map.jsp" %>
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="no-result" rendered="#{SearchBean.noResult}">
							<%@ include file="search-no-results.jsp" %>
						</t:htmlTag>
					</s:panelTab>
				
				</s:panelTabSet>
				
				<s:panelTabSet
					id="voyageDetailTabs"
					selectedSectionId="#{VoyageDetailBean.selectedTab}"
					rendered="#{SearchBean.showVoygeDetail}">
		
					<s:panelTab title="Back to the list of voyages" sectionId="backToList" cssClass="back-to-list" />

					<s:panelTab title="#{res.database_voyage_voyagevariables}" sectionId="variables">
		
						<t:htmlTag value="div" styleClass="detail-title">
							<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
						</t:htmlTag>
						
						<t:div style="margin-right: 10px;">
							<s:voyageDetail data="#{VoyageDetailBean.detailData}" />
						</t:div>
		
					</s:panelTab>
		
					<s:panelTab title="#{res.database_voyage_voyagemap}" sectionId="map">
		
						<t:htmlTag value="div" styleClass="detail-title">
							<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
						</t:htmlTag>
		
						<t:htmlTag value="table"
							style="border-collapse: collapse; margin: 0px 0px 0px 15px;">
		
							<t:htmlTag value="tr">
		
								<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">
		
									<s:map id="mapVoyage"
										zoomLevels="#{VoyageDetailBean.zoomLevels}"
										pointsOfInterest="#{VoyageDetailBean.voyageMapPoints}"
										lines="#{VoyageDetailBean.voyageMapLines}" miniMap="true"
										miniMap="true"
										miniMapZoomLevel="#{VoyageDetailBean.miniMapZoomLevel}"
										miniMapHeight="100" miniMapWidth="100"
										miniMapPosition="bottom right" />
		
								</t:htmlTag>
		
								<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">
								
									<s:voyageRouteLegend route="#{VoyageDetailBean.voyageRoute}" />
								
								</t:htmlTag>
		
							</t:htmlTag>
						</t:htmlTag>
		
					</s:panelTab>
		
					<s:panelTab title="#{res.database_voyage_voyageimages}" sectionId="images">
		
						<t:htmlTag value="div" styleClass="detail-title">
							<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
						</t:htmlTag>
		
						<s:gallery images="#{VoyageDetailBean.imagesGallery}"
							columnsCount="5" showLabels="true" thumbnailHeight="100"
							thumbnailWidth="100" action="#{VoyageDetailBean.openImageDetail}"
							selectedImageId="#{VoyageDetailBean.selectedImageId}" />
		
					</s:panelTab>
		
				</s:panelTabSet>
						
			</td>
		</tr>
		</table>
		
	</h:form>
	
	<div id="debug"></div>

</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>