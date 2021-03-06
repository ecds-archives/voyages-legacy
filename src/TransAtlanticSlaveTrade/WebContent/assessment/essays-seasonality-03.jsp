<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-seasonality-03" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Seasonality in the Trans-Atlantic Slave Trade</h1>
					
					<div class="essay-info">
						<span class="essay-author">Stephen D. Behrendt</span>
						<span class="essay-location">(Victoria University of Wellington)</span>,
						<span class="essay-date">2008</span>
					</div>
					
					<h2>Seasonal rainfall in the Atlantic slaving world</h2>

					<p>In most regions in the Atlantic slaving world the growth of
					crops and sufficient pasture for livestock depended upon seasonal
					rainfall. Those people living in rainforests would have been
					familiar with intense summer downpours; those living towards
					deserts or in rain shadows would have known droughts lasting many
					months. Most Africans experienced greater precipitation extremes
					than those living in the Western Hemisphere. For people who
					survived their trans-Atlantic passage, ninety-five per cent would
					labor in tropical and sub-tropical regions in the Western
					Hemisphere marked by seasonal rainfall. Comparatively few enslaved
					Africans experienced the temperate year-round rains in Chesapeake
					and mid-Atlantic lands to the north or those temperate rains in the
					Rio de la Plata, the southernmost American slaving market.</p>

					<p>In Atlantic Africa, shifting air masses produce July-October
					rains north of the equator and January-April rains south of the
					equator. In the principal slave-trading zone, from 15° North
					Latitude (above Senegal) to 15° South Latitude (below Benguela),
					coastal rains double the volume of precipitation just 20-30 miles
					inland. Deluges from Conakry (in modern Guinea) to Bassa (Liberia)
					match the rainfall in the Douala estuary of Cameroon—these are the
					rainiest pockets in the Atlantic world. Travelling south from the
					Senegal River, where twenty inches of rain fall annually, mostly in
					the summer, one reaches the northern extent of the rainforest above
					Sierra Leone (8-9° N), where ninety inches of rain fall in July and
					August. Heavy summer rains continue in dense rainforests stretching
					from Sierra Leone to the Windward Coast. Rainy season deluges
					commence along the eastern Bight of Benin and in the Bight of
					Biafra, precipitation amounts increasing during summer as one sails
					towards the equator. Once south of the Zaire River, one enters the
					driest coastline in Atlantic Africa, where farmers can expect to
					see less than fifteen inches of rain each year, mostly in
					February-April.</p>

					<p>In contrast to precipitation in Atlantic Africa, there is
					less rainfall in most New World slaving markets and few droughts.
					The greatest downpours occur in the spring-summer in the tropical
					rainforests of northern South America from Surinam (5-6° N) to Pará
					(1-3° S). The 1,250-mile Brazilian coastline from Paraíba (6° S) to
					São Vicente (24° S) includes a tropical zone, with a hot and humid
					climate and no pronounced dry season; the hot and humid subtropics,
					with a two-month dry season; and a temperate region, with a warm
					climate and dry winters. In Bahia, there are two moderate rainy
					seasons, separated by heavy May rainfall. Farther south, in Rio de
					Janeiro, the year’s first substantial rain occurs in March. Most
					rain falls in the West Indies in the fall and winter, but the
					wettest low-lying areas only reach the precipitation levels of the
					driest rainy seasons in Atlantic Africa. In the Carolinas and the
					Chesapeake, June-August summers are the rainiest times, but there is
					also significant December-February precipitation, levels similar to
					those experienced during winters in maritime climates of Europe.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-02.faces">Agriculture in the era of the trans-Atlantic slave trade</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-04.faces">Rainfall, crop type and agricultural calendars</a>
						</td>
					</tr>
					</table>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>