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
				<f:param value="essays-seasonality-01" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Introduction</h2>

					<p>The trans-Atlantic slave trade brings to mind images of
					haphazard, disorganized plunder that randomly scattered about
					eleven million African people throughout the Americas. When one
					examines information contained in the
					<a href="../database/search.faces">Voyages Database</a>, however, one
					detects patterns in this forced diaspora. Many vessels sailing
					under Portuguese flag, for example, transported enslaved Africans
					from West-Central Africa to Brazil; many Dutch ships sailed from
					the Gold Coast to Surinam. Colonial power and mercantilism provide
					one reason to explain these trans-Atlantic routes made by slaving
					vessels. Portugal controlled coastal territories in Angola, such as
					the ports <a href="../resources/images-detail.faces?image=luanda">Luanda</a>
					and <a href="../resources/images-detail.faces?image=benguela">Benguela</a>,
					and shipped enslaved Africans from these sites across the South
					Atlantic to their colonial cities in Brazil. The Dutch controlled
					the Gold Coast fort
					<a href="../resources/images-detail.faces?image=elmina">Elmina</a>
					and ordered their captains to proceed with slaves to their South
					American colony Surinam. Portuguese and Dutch laws restricted their
					slave trades to national carriers.</p>

					<p>The <a href="../database/search.faces">Voyages Database</a>
					also reveals that in all markets on the African coast, more slaves
					were embarked on board ship during some months than others.
					Similarly, in all markets in the Americas, more Africans, year after
					year, were disembarked during certain months. Why were there
					seasonal patterns? Focusing on the agricultural histories of
					African and American societies helps to explain monthly
					fluctuations in the supply of and demand for enslaved Africans.
					Whether soils supported subsistence or cash crops, each stage in
					the agricultural calendar—clearing land, planting, weeding,
					harvesting—requires different numbers of farmers, different “labor
					inputs.” On both sides of the Atlantic, seasonal crop cycles created
					seasonal demands for agriculturalists. The trans-Atlantic slave
					trade reconciled supply and demand for agricultural labor when
					captains transferred farmers from “in crop” seasons in Africa to
					“in crop” seasons in the Americas.</p>

					<p>In shifting captives between Old and New World ecological
					zones, captains created systematic trans-Atlantic patterns when
					African and American crop cycles differed by the time needed to
					sail the Middle Passage. In Africa, the numbers of slaves embarked
					on board ships usually increased during the harvest and in the
					immediate post-harvest months. During these times fewer and fewer
					farmers were needed and food stocks began increasing. African
					merchants purchased slaves whose agricultural labor became
					temporarily redundant, and they bought seasonal provisions to keep
					their captives alive. In turn, New World plantation crop production
					required greater numbers of slaves to cut, gather, and process cane,
					berries, or leaves. Slaving captains attempted to trade “in season”
					in both Africa and the Americas by identifying American markets
					whose cash crop harvests seasons took place 1-3 months after
					harvest cycles in Africa. Those captains who linked Old and New
					World food-production cycles sailed along regular trans-Atlantic
					pathways and synchronized agricultural calendars.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-02.faces">Agriculture in the era of the trans-Atlantic slave trade</a>
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