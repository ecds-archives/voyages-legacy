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
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/sections.css" rel="stylesheet" type="text/css">
	<link href="../styles/menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	<link href="../styles/voyage.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

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
	
	<div class="detail-title">
		<h:outputText id="text1_res" value="#{res.database_voyage_voyagedetail} (ID = #{VoyageDetailBean.detailVoyageInfo[0].value})"/> 
	</div>

	<div class="detail-link-back">
		&lt; <h:commandLink id="backFromDetailMap" value="go back" action="#{VoyageDetailBean.back}" />
	</div>

	<div style="margin: 10px 10px 0px 10px;">
	<s:sectionGroup
		id="detailPanelSection"
		backgroundStyle="dark"
		tabsStyle="middle"
		buttonsStyle="middle"
		selectedSectionId="listing">

		<s:section title="#{res.database_voyage_voyagevariables}" sectionId="listing">
			<s:voyageDetail data="#{VoyageDetailBean.detailData}" />
		</s:section>

		<s:section title="#{res.database_voyage_voyagemap}" sectionId="maps">

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
	</div>

	</h:form>

</f:view>
</body>
</html>