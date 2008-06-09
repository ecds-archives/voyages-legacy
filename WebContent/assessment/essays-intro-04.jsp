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
				<f:param value="essays-intro-04" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Early Slaving Voyages</h2>

					<p>With the key forces shaping the traffic briefly described,
					we can now turn to a short narrative of the slave trade. The first
					Africans forced to work in the New World left from Europe at the
					beginning of the sixteenth century, not from Africa. There were few
					vessels that carried only slaves on this early route, so that most
					would have crossed the Atlantic in smaller groups on vessels
					carrying many other commodities, rather than dedicated slave ships.
					Such a slave route was possible because an extensive traffic in
					African slaves from Africa to Europe and the Atlantic islands had
					existed for half a century before Columbian contact such that ten
					percent of the population of Lisbon was black in 1455,iii and black
					slaves were common on large estates in the Portuguese Algarve. The
					first slave voyage direct from Africa to the Americas probably
					sailed in 1526.iv Before mid-century, all trans-Atlantic slave ships
					sold their slaves in the Spanish Caribbean, with the gold mines in
					Cibao on Hispaniola emerging as a major purchaser. Cartagena, in
					modern Columbia, appears as the first mainland Spanish American
					destination for a slave vessel - in the year 1549.v On the African
					side the great majority of people entering the early slave trade
					came from the Upper Guinea coast, and moved through Portuguese
					factories initially in Arguim, and later the Cape Verde islands.
					Nevertheless, the 1526 voyage set out from the other major
					Portuguese factory in West Africa - Sao Tome in the Bight of Biafra
					– though the slaves almost certainly originated in the Congo.</p>

					<p>The slave traffic to Brazil, eventually accounting for about
					forty percent of the trade, got underway around 1560. Sugar drove
					this traffic, as Africans gradually replaced the Amerindian labor
					force on which the early sugar mills (called engenhos) had drawn
					over the period 1560 to 1620. By the time the Dutch invaded Brazil
					in 1630, Pernambuco, Bahia and Rio de Janeiro were supplying almost
					all of the sugar consumed in Europe and almost all the slaves
					producing it were African. Consistent with the earlier discussion
					of Atlantic wind and ocean currents, there were by 1640 two major
					branches of the trans-Atlantic slave trade operating, one to Brazil,
					and the other to the mainland Spanish Americas, but together they
					accounted for less 7,500 departures a year from the whole of
					sub-Saharan Africa, almost all of them by 1600 from west-central
					Africa. The sugar complex spread to the eastern Caribbean from the
					beginning of the 1640s. Sugar consumption steadily increased in
					Europe, and the slave system began two centuries of westward
					expansion across tropical and sub-tropical North America.vi At the
					end of the seventeenth century, gold discoveries in first, Minas
					Gerais and later in Goias and other parts of Brazil began a
					transformation of the slave trade which triggered further expansion
					of the business. In Africa, the Bights of Benin and Biafra became
					major sources of supply, in addition to Angola, and were joined
					later by the more marginal provenance zones of Sierra Leone, the
					Windward Coast and South-east Africa. The volume of slaves carried
					off reached thirty thousand per annum in the 1690s and eighty-five
					thousand a century later. More than eight out of ten Africans
					pulled into the traffic in the era of the slave trade made their
					journeys in the century and a half after 1700.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-03.faces">African Agency and Resistance</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-05.faces">Empire and Slavery</a>
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