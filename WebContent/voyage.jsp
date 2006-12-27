<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyage</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="sections.css" rel="stylesheet" type="text/css">
	<link href="menu.css" rel="stylesheet" type="text/css">
	<link href="map.css" rel="stylesheet" type="text/css">
	<link href="search.css" rel="stylesheet" type="text/css">
	<script src="utils.js" language="javascript" type="text/javascript"></script>
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
			<td><div class="header-link"><a href="estimates.faces">Estimates</a></div></td>
			<td><div class="header-link"><a href="search.faces">Database search</a></div></td>
			<td><div class="header-link"><a href="galleryp.faces?obj=1&id=1&set=1&pict=0">Images database</a></div></td>
		</tr>
		</table>
	</div>
	
	<h:form id="form">
	
	<h:panelGrid style="padding-left: 5px;" columns="2">
		<h:outputText value="Detail information about voyage" style="font-size: 150%;" />
		<t:htmlTag value="div" styleClass="section-inside-footer">
			<h:commandButton id="backFromDetailMap" value="Back to results" action="#{VoyageDetailBean.back}" />
		</t:htmlTag>
	</h:panelGrid>

	<h:dataTable value="#{VoyageDetailBean.detailVoyageInfo}" var="info" style="padding-left: 10px;">
		<h:column>
			<h:outputText value="#{info.attribute}:" />
		</h:column>
		<h:column>
			<h:outputText value="#{info.value==null? \"not set\" : info.value}" />
		</h:column>
	</h:dataTable>

	<s:sectionGroup id="detailPanelSection" backgroundStyle="dark" tabsStyle="middle" buttonsStyle="middle"
		selectedSectionId="listing">

		<s:section title="Voyage details" sectionId="listing">

			<s:voyageDetail data="#{VoyageDetailBean.detailData}" />

		</s:section>

		<s:section title="Voyage map" sectionId="maps">

			<h:outputText value="&nbsp;" escape="false" />

			<t:htmlTag value="table" id="DetailMap" style="border-collapse: collapse; padding-left: 20px; margin-left: auto; margin-right: auto;">

				<t:htmlTag value="tr">
					<t:htmlTag value="td">
						<s:map mapFile="#{VoyageDetailBean.mapPath}" pointsOfInterest="#{VoyageDetailBean.pointsOfInterest}"
							serverBaseUrl="servlet/maptile" miniMap="true" />
						<h:outputText value="&nbsp;" escape="false" />
					</t:htmlTag>

					<t:htmlTag value="td" style="vertical-align: top;">
						<s:legend id="mapLegend-component-detailvoyage" 
								styleClass="map-legend-div"
								legend="#{VoyageDetailBean.legend}"
								layers="#{VoyageDetailBean.layers}"
								refreshAction="#{VoyageDetailBean.refresh}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>


		</s:section>

	</s:sectionGroup>
		
	</h:form>

</f:view>
</body>
</html>