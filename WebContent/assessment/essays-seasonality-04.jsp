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

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-seasonality-04" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Rainfall, crop type and agricultural calendars</h2>

					<p>Rainfall, temperature, sunlight, humidity and soil type
					determines crop choice and regulates agricultural calendars.
					Maximizing plants’ nutritional content requires precise growing
					cycles under optimal ecological conditions. For most subsistence
					and cash crops, farmers plant just before or during rainy months
					and harvest during dry, sunny months. Crops needing long periods of
					sunshine to maximize yield include grains, cereals, and cane
					starches. Intense ultraviolet light damages coffee and other
					berries. Yams and other tubers are long-growing tropical foods,
					requiring 8-10 months underground. Except for rice, most crops do
					not grow well in rainforests, because downpours leach soils of
					nutrients and roots cannot tolerate waterlogging. Cold winters in
					the continental climates of the New World kill sugar, coffee,
					cacao, and cotton plants. Rainfall loosens soils to facilitate
					digging and sowing, and all seeds and cuttings require water to
					propagate; as sunlight and warm, dry weather ripen plants, caloric
					content increases. In ecosystems that support short-growing plants
					and have two rainy seasons, farmers can produce two crops per year.</p>

					<p>Millet, sorghum, rice, maize, yams, and cassava, principal
					African crops, grow in ecosystems that dictate agricultural
					calendars. Millet and sorghum are often the only food plants grown
					in the semi-arid and arid 10-15° N belt, three hundred miles inland
					from the African Atlantic coastline. Farmers plant these cereals
					during the first rains in June, which soften the rock-hard soils,
					and in early November, at the end of the rainy season when
					floodwaters begin to recede. The short-season crops flower in
					90-180 days; harvests occur in September-December and February-May,
					depending on rainfall. The two cereals also thrive in the long dry
					winter seasons of the Congo savannah, and may have grown further
					west before being displaced by manioc. Rice is the staple from the
					Lower Gambia south to Sierra Leone and along the Windward Coast,
					rainy coastlines that allow rice to grow in its requisite water
					depth of 4-6 inches. It grows from June (rainy season) to November
					(onset of dry season). Maize, a New World crop imported in the
					1600s, requires sufficiently long, dry, sunny periods, and thrived
					mainly in the central Gold Coast. South of 10° N one finds ideal
					conditions, as in much of Nigeria, for yam cultivation: 85° F
					temperatures, rainfall totalling 60 inches, a 2-3 month dry season,
					sufficient sunlight, and free-draining soils.</p>

					<p>Sugar, tobacco, coffee, and rice were the major New World
					cash crops. In the tropical Americas, sugar (with its by-products
					rum and molasses) was the principal plantation commodity. Planting
					occurred during rainy months, June-October in most of the West
					Indies, and the cane grows over a 14-18 month period. Saccharine
					matter reaches its greatest content during the ripening period when
					stalks dry. In the West Indies, dry seasons usually occur from
					January to May, though there are microclimates in the larger and
					mountainous islands, such as Haiti (before 1804, French St.
					Domingue), Dominica, and Jamaica. The best ecosystems for tobacco
					were located in the Chesapeake Lowcountry and Bahia, where high
					summer humidity keeps growing leaves moist and drier fall air
					allows them to dry and be cut. In the late 1700s, coffee groves
					became important in well-draining, shaded mountain ecosystems, the
					six-month fruit cycles ending during dry-season berry picking. Wet
					rice proved profitable in humid, low-lying areas prone to flooding,
					as in the coastal Carolinas, Georgia, Surinam, and northeast Brazil.
					On South American rice fields, slaves cleared land during the
					August-November dry season, planted in winter rains, and harvested
					between March and May. The Carolina rice and indigo cycles began in
					February and ended in November. Though a crop associated strongly
					with plantation slavery, cotton did not dominate many areas until
					the 1800s, and comparatively few African-born slaves worked on
					cotton plantations.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-03.faces">Seasonal rainfall in the Atlantic slaving world</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-05.faces">Agricultural calendars and labor requirements</a>
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