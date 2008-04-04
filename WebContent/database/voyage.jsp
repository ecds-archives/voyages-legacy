<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Voyage</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-voyage.css" rel="stylesheet" type="text/css">
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/lib/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/lib/scriptaculous.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tooltip.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
	<h:form id="form">

		<f:loadBundle basename="SlaveTradeResources" var="res" />
		
		<s:siteHeader>
			<h:outputLink value="../index.faces"><h:outputText value="Home page" /></h:outputLink>
			<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
			<h:outputText value="Voyage Detail" />
		</s:siteHeader>

		<div id="content"><s:panelTabSet id="mainPanelSection"
			selectedSectionId="#{VoyageDetailBean.selectedTab}">

			<s:panelTab title="#{res.database_voyage_voyagevariables}" sectionId="variables">

				<t:htmlTag value="div" styleClass="detail-link-back">
					<h:commandLink value="#{VoyageDetailBean.backLinkText}" action="#{VoyageDetailBean.back}" />
					<h:outputText value=" / Voyage detail" />
				</t:htmlTag>

				<t:htmlTag value="div" styleClass="detail-title">
					<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
				</t:htmlTag>

				<s:voyageDetail data="#{VoyageDetailBean.detailData}" />

			</s:panelTab>

			<s:panelTab title="#{res.database_voyage_voyagemap}" sectionId="map">
			
				<t:htmlTag value="div" styleClass="detail-link-back">
					<h:commandLink value="#{VoyageDetailBean.backLinkText}" action="#{VoyageDetailBean.back}" />
					<h:outputText value=" / Voyage detail" />
				</t:htmlTag>

				<t:htmlTag value="div" styleClass="detail-title">
					<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
				</t:htmlTag>

				<t:htmlTag value="table"
					style="border-collapse: collapse; margin: 0px 0px 0px 15px;">

					<t:htmlTag value="tr">

						<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">

							<s:map id="map" zoomLevels="#{VoyageDetailBean.zoomLevels}"
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
				
				<t:htmlTag value="div" styleClass="detail-link-back">
					<h:commandLink value="#{VoyageDetailBean.backLinkText}" action="#{VoyageDetailBean.back}" />
					<h:outputText value=" / Voyage detail" />
				</t:htmlTag>

				<t:htmlTag value="div" styleClass="detail-title">
					<h:outputText escape="false" value="#{VoyageDetailBean.pageTitle}" />
				</t:htmlTag>

				<s:pictures images="#{VoyageDetailBean.imagesGallery}"
					columnsCount="5" showLabels="true" thumbnailHeight="100"
					thumbnailWidth="100" action="#{VoyageDetailBean.openImageDetail}"
					selectedImageId="#{VoyageDetailBean.selectedImageId}" />

			</s:panelTab>

		</s:panelTabSet></div>

	</h:form>
</f:view>
</body>
</html>