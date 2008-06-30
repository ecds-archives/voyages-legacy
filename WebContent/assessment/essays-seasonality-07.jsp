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

	<script language="javascript" type="text/javascript">
	function popitup(url) {
		newwindow=window.open(url,'name','height=486,width=500,resizable, scrollbars, location=0');
		if (window.focus) {newwindow.focus()}
		return false;
	}
	</script>
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
				<f:param value="essays-seasonality-07" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Slave-trading seasonality: case studies</h2>

					<p>To spotlight seasonality in the trans-Atlantic slave trade,
					consider slaving voyages that departed from New Calabar, Bight of
					Biafra (Elem Kalabari, Nigeria), and those that arrived in
					Barbados. New Calabar was a major embarkation point for enslaved
					Africans from the Bight of Biafra; in 1650-1700 one-third of all
					Africans shipped from the region passed through the village,
					located on the
					<a href="../resources/images-detail.faces?image=new-calabar-and-bonny">New Calabar River</a>.
					In the 1630s and 1640s, the Dutch were the first Europeans to
					challenge Portuguese dominance in New Calabar; by the late 1670s,
					the London-based Royal African Company (RAC) outfitted the majority
					of slaving ships to this African trading site. Regarding Barbados,
					the RAC stationed agents in its main port, Bridgetown, and in the
					late 1600s the island-colony produced more high-quality sugar per
					acre than any region in the Atlantic world.</p>

					<p>Between 1654 and 1851, the <a
						href="../database/search.faces">Voyages Database</a> documents 315
					slaving trips that departed New Calabar for the Americas. Of these,
					one can estimate months of departure for 257 voyages, and plot
					departure months against the estimated number of slaves embarked.
					Results indicate that slave exports from New Calabar dropped during
					the period of yam planting and weeding (March-June) in the
					hinterland, and then exports rose sharply in August as workers
					harvested yams, peaking during the main harvest in October. They
					then decreased by February-March, a period that coincides with
					declining yam stocks (Figure 1). In 1677-78, Arthur Doegood captained
					one of the RAC slaving voyages to New Calabar <a
						href="../database/voyage.faces?voyageId=9990">VoyageID 9990</a>
					and his <a
						href="http://www.nationalarchives.gov.uk/slavery/pdf/Arthur_Translation.pdf">logbook</a>
					survives in the National Archives in London. Doegood anchored at
					New Calabar in mid-February 1678, after the optimal fall
					provisioning-slaving season. Within a week, his supercargo, George
					Hingston, complained that he was not “free to deale in many
					[slaves]” because we “have noe provitions for them,” “findeing
					yames very scarse.” By April many of the yams he bought were
					“rotten” and he was forced to buy unripe “green plantins.”</p>

					<center>
					<a href="essays-seasonality-fig-1.png" onclick="return popitup('essays-seasonality-fig-1.png')">
						<img src="essays-seasonality-fig-1.png" width="500"/></a>
					</center>
					
					<p>Two months after departing New Calabar, Doegood arrived in
					Carlisle Bay, Barbados when slaves were producing the last barrels
					of sugar. Agent Hingston’s journal entry on 30-31 May 1678
					indicates that he had arrived at the beginning of the out-of-crop
					rainy season: “the next day rainy weather were not many buyers on
					board.” The rains would last through early November, followed by
					drier weather and winter-spring grain and sugar harvests when
					planters demanded greater numbers of newly enslaved African
					workers. Information contained in the <a
						href="../database/search.faces">Voyages Database</a> indicates
					that slave imports into Barbados began increasing towards the
					beginning of the “in crop” provisions and sugar season, and then
					began declining in March after provisions’ harvests and as less and
					less sugar needed to be cut and processed (Figure 2).</p>
					
					<center>
					<a href="essays-seasonality-fig-2.png" onclick="return popitup('essays-seasonality-fig-2.png')">
						<img src="essays-seasonality-fig-2.png" width="500"/></a>
					</center>
					<br><br>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-06.faces">Provisioning-slaving seasons</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-08.faces">Trans-Atlantic pathways and harvest cycles</a>
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