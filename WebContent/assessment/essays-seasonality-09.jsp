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
				<f:param value="essays-seasonality-09" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Conclusion</h2>

					<p>In many markets in the Atlantic world monthly cycles of
					slave exports and imports, documented in the <a
						href="../database/search.faces">Voyages Database</a>, link to dry
					season crop harvests. African and European dealers on the African
					coast purchased provisions and slaves. Some markets, such as those
					along the Senegal and Gambia Rivers, had distinct
					provisioning-slaving seasons. Ecological conditions set
					agricultural calendars and the dates when workers gathered and
					stored foodstuffs. African middlemen pegged their slave-trading
					seasons to in-crop months, and some agricultural workers, sold into
					the overseas slave trade, may have been forced to consume the foods
					they produced. By moving captives between harvests on the Atlantic
					littoral, slaving ship captains created regular pathways, such as
					those between yam-growing Bight of Biafra and the sugar islands of
					the Caribbean, or those between millet-rice Upper Guinea region and
					North American rice and tobacco lands. In examining slave trading
					routes, historians need to consider agricultural calendars on both
					sides of the Atlantic.</p>

					<p>Though there were monthly cycles of slave exports and
					imports, year-round shipments took place in all markets during the
					350-year history of the trans-Atlantic slave trade. In the most
					seasonal African slaving region—Senegambia—about fifteen percent of
					all enslaved Africans departed the coast in the out-of-crop, rainy,
					September-November quarter. Even in the most seasonal market in the
					Atlantic slaving world—the northern plantations of Virginia and
					Maryland (36-39° N)—small numbers of forced migrants arrived in the
					winter, when no crops were grown. In the large Bight of Biafra –
					Jamaica migration stream, forty percent of enslaved Africans
					arrived on the island during the June-November out-of-crop season.
					And many would have sailed from Bonny, Old Calabar or New Calabar
					from April to July when yam stocks were low or depleted.
					Variability in the Middle Passage voyage time, due to contrary
					winds, caused some captains to arrive out of season; one assumes
					that there also was variability in the time taken to march captives
					towards the African coast.</p>

					<p>It is important to examine these unseasonal slave trades. In
					Africa, they remind us that the slave trade was a predatory
					activity. Warfare between African states often took place after the
					principal grain harvest and during the dry season, but conflicts
					could erupt at any time, and during every day of the year raiders
					could attack communities or kidnap people. Seasonal rainfall and
					crop-growing constraints did not completely limit the plunder of
					people. Captains who traded towards the end of “in-crop” seasons in
					Africa, such as Robert Doegood, risked purchasing greater numbers
					of malnourished men, women and children. Doegood traded at New
					Calabar when yam supplies were low; his logbook reveals that eighty
					Africans died on the Middle Passage (of 348 people) and four more
					in harbor at Barbados. Historians should examine more closely the
					links between provisioning-slaving seasons and mortality. In the
					Americas, investors were willing to purchase enslaved labor from
					any African region during any day of the year—the labor of enslaved
					Africans maintained the Colonial System. Trading during out of crop
					seasons, on both sides of the Atlantic, increased the chances that
					irregular, non-systematic migration patterns occurred—a true
					diaspora or “scattering” of African peoples in the Americas.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-08.faces">The Enslavement of Africans</a>
						</td>
						<td class="essay-next">
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
</body>
</html>