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
				<f:param value="essays-intro-05" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Empire and Slavery</h2>

					<p>In the second half of the eighteenth century six imperial
					systems straddled the Atlantic each one sustained by a slave trade.
					The English, French, Portuguese, Spanish, Dutch, and Danish all
					operated behind trade barriers (termed mercantilistic restrictions)
					and produced a range of plantation produce - sugar, rice, indigo,
					coffee, tobacco, alcohol, and some precious metals - though with
					sugar usually the most valuable. It is extraordinary that
					consumers’ pursuit of this limited range of exotic consumer goods,
					which collectively added so little to human welfare, could have
					generated for so long the horrors and misery of the Middle Passage
					and plantation slavery. Given the dominance of Portuguese and
					British slave traders, it is not surprising that Brazil and the
					British Americas received the most Africans, though both nations
					became adept at supplying foreign slave systems as well. Throughout
					the slave trade, more than seven out of every ten slaves went to
					these regions. The French Americas imported about half the slaves
					that the British did, with the majority going to Saint-Domingue.
					The Spanish flag, which dominated in the earliest phase of the
					trade before retreating in the face of competition, began to
					expand again in the late nineteenth century with the growth of the
					Cuban sugar economy.</p>

					<p>Yet, in the next century - between 1750 and 1850 – every one
					of these empires had either disappeared or become severely
					truncated. A massive shift to freer trade meant that instead of six
					plantation empires controlled from Europe, there were now only three
					plantation complexes, two of which—Brazil and the United
					States—were independent, and the third, Cuba, was far wealthier and
					more dynamic than its European owner. Extreme specialization now
					saw the United States producing most of the world’s cotton, Cuba
					most of the world’s sugar, and Brazil with a similar dominance in
					coffee. Slaves thus might disembark in six separate jurisdictions
					in the Americas in the eighteenth century, but by 1850 they went
					overwhelmingly to only two areas, Brazil and Cuba, given that
					American cotton planters drew on Africa for almost none of their
					labor needs, relying instead on natural population growth and a
					domestic slave trade. Indeed, overall the United States absorbed
					only 5 percent of the slaves arriving in the Americas. This massive
					reorganization of the traffic and the rapid natural growth of the
					US slave population had little immediate impact on the size of the
					slave trade. The British, Americans, Danish, and Dutch dropped out of the
					slave trade, but the decade 1821 to 1830 still saw over 80,000
					people a year leaving Africa in slave ships. Well over a million
					more – one tenth of the volume carried off in the slave trade era -
					followed in the next twenty years.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-04.faces">Early Slaving Voyages</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-06.faces">The African Side of the Trade</a>
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