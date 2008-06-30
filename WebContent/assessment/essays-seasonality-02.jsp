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
				<f:param value="essays-seasonality-02" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Agriculture in the era of the trans-Atlantic slave trade</h2>

					<p>In examining seasonality in the trans-Atlantic slave trade,
					it is important to focus on agricultural history because the
					majority of people in the Atlantic world lived on farms, producing
					crops and raising livestock. During the era of the slave trade,
					1514-1866, most sub-Saharan Africans from rural communities, forced
					across the Atlantic, continued their farming lives by working New
					World lands. They grew some familiar provisions, including crops
					imported from Africa, like Guinea corn (millet) or West African
					rice. However, many saw crops such as sugar, tobacco, coffee,
					indigo, cacao, or cotton, for the first time.</p>

					<p>Though historians lack data on precolonial African
					demography, it is reasonable to suggest that most Africans forced
					overseas were farmers or pastoralists. Men and women, adults and
					children, helped to produce yearly supplies of millet, sorghum,
					rice, maize, yams, cassava, plantains, or other crops. The ratio of
					men, women, and children working on farms varied by crops and
					region, but all villagers worked together clearing land, planting,
					weeding, and storing crops to produce sufficient amounts of food to
					enable communities to survive through the out-of-crop hungry
					seasons. Smaller numbers of enslaved Africans transported across
					the Atlantic were craftsmen or professionals; as African towns grew
					in size in the late eighteenth and nineteenth centuries, so too did
					the numbers of urban residents who were enslaved.</p>

					<p>For those eleven million African peoples who survived the
					Middle Passage, the majority would labor on plantation lands
					producing provisions and cash crops. As in Africa, ratios of men,
					women, and children working in the fields varied by crops and
					region, and the hungry months occurred before the year’s harvest.
					About 5.25 million African migrants worked in sugar cane, and
					perhaps 1.5 million toiled on tobacco, coffee, rice, indigo, cotton,
					and cacao estates. Another 1.5 million people worked in livestock
					pens, or on plantations producing millet, maize, wheat, cassava, or
					forestry products. An estimated one million enslaved Africans
					worked in silver and gold mining, but mostly before 1750. Brazilian
					gold, important particularly in 1690-1750, drew in perhaps 500,000
					African workers. Household work or ranching occupied the lives of
					750,000-1,000,000 African men, women and children.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-01.faces">Introduction</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-03.faces">Seasonal rainfall in the Atlantic slaving world</a>
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