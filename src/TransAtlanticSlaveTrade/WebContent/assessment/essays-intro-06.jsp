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
				<f:param value="essays-intro-06" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>A Brief Overview of the Trans-Atlantic Slave Trade</h1>

					<div class="essay-info">
						<span class="essay-author">David Eltis</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">2007</span>
					</div>

					<h2>The African Side of the Trade</h2>

					<p>On the African side, the sheer human and environmental
					diversity of the continent makes it difficult to examine the trade
					from Africa as a whole. The slave trade did not expand, nor,
					indeed, decline, in all areas of Africa at the same time. Rather, a
					series of marked expansions (and declines) in individual regions
					contributed to a more gradual composite trend for sub-Saharan
					Africa as a whole. Each region that exported slaves experienced a
					marked upswing in the amount of slaves it supplied for the
					trans-Atlantic trade and, from that point, the normal pattern was
					for a region to continue to export large numbers of slaves for a
					century or more. The three regions that provided the fewest slaves
					– Senegambia, Sierra Leone, the Windward Coast – reached these
					higher levels for much shorter periods.</p>

					<p>By the third quarter of the eighteenth century, all regions
					had undergone an intense expansion of slave exports. A cargo of
					slaves could be sought at particular points along the entire
					Western African coast. As the Brazilian coffee and sugar boom got
					under way near the end of the eighteenth century, slavers rounded
					the Cape of Good Hope and traveled as far as southeast Africa to
					fill their vessels’ holds. But while the slave trade pervaded much
					of the African coast, its focus was no less concentrated in
					particular African regions than it was among European carriers.
					West Central Africa, the long stretch of coast south of Cape Lopez
					and stretching to Benguela, sent more slaves than any other part of
					Africa every quarter century with the exception of a fifty-year
					period between 1676 and 1725. From 1751 to 1850, this region
					supplied nearly half of the entire African labor force in the
					Americas; in the half century after 1800, West Central Africa sent
					more slaves than all of the other African regions combined.
					Overall, the center of gravity of the volume of the trade was
					located in West Central Africa by 1600. It then shifted northward
					slowly until about 1730, before gradually returning to its starting
					point by the mid-nineteenth century.</p>

					<p>Further, slaves left from relatively few ports of
					embarkation within each African region, even though their origins
					and ethnicities could be highly diverse. Although Whydah, on the
					Slave Coast, was once considered the busiest African slaving port
					on the continent, it now appears that it was surpassed by Luanda,
					in West Central Africa, and by Bonny, in the Bight of Biafra.
					Luanda alone dispatched some 1.3 million slaves, and these three
					most active ports together accounted for 2.2 million slave
					departures. The trade from each of these ports assumed a unique
					character and followed very different temporal profiles. Luanda
					actively participated in the slave trade from as early as the
					1570s, when the Portuguese established a foothold there, through
					the nineteenth century. Whydah supplied slaves over a shorter
					period, for about two centuries, and was a dominant port for only
					thirty years prior to 1727. Bonny, probably the second largest
					point of embarkation in Africa, sent four out of every five of all
					the slaves it ever exported in just the eighty years between 1760
					and 1840. It is not surprising, therefore, that some systematic links
					between Africa and the Americas can be perceived. As research on
					the issue of trans-Atlantic connections has progressed, it has
					become clear that the distribution of Africans in the New World is
					no more random than the distribution of Europeans. Eighty percent
					of the slaves who went to southeast Brazil were taken from West
					Central Africa. Bahia traded in similar proportions with the Bight
					of Benin. Cuba represents the other extreme: no African region
					supplied more than 28 percent of the slave population in this
					region. Most American import regions fell between these examples,
					drawing on a mix of coastal regions that diversified as the trade
					from Africa grew to incorporate new peoples.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-05.faces">Empire and Slavery</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-07.faces">The Middle Passage</a>
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