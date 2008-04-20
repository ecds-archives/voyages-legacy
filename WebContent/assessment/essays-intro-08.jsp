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
				<f:param value="essays-intro-08" binding="#{EssaysBean.paramActiveMenuId}" />
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

					<h2>The Ending of the Slave Trade</h2>

					<p>When the transatlantic slave trade came to an end, it did so
					rather suddenly. When the Brazilian authorities began arresting
					slave ships at the end of 1850, the volume of the traffic of the
					traffic slipped back to levels not seen for two centuries, and the
					last transatlantic slave expedition – to Cuba and probably from the
					Congo River – completed its voyage in 1867. For the last two
					decades of the traffic, only the Bight of Benin and the Congo
					region were heavily engaged in the trade. Nevertheless over the
					whole period of the trade some 12.5 million slaves had been shipped
					from Africa and 10.7 million had arrived in the Americas, likely
					the most costly in human life of all of long-distance global
					migrations. Why the rather sudden end to a business which despite
					its high morbidity and mortality, had been seen as no different
					from any other until the late eighteenth century? This is a very
					large question which it would be presumptuous to attempt to answer
					here given the massive literature on the topic. One point is clear,
					the traffic did not fade away; rather it was suppressed at a time
					when the prices of slaves were rising to levels that had never
					previously attained. The economic imperatives clearly pointed to a
					continuation of the trade and without attempts to suppress it, the
					majority of the millions of people who crossed the Atlantic between
					1820 and 1920 might well have been African rather than European,
					and enslaved rather than free. As it was by the 1850s, for most in
					the Atlantic world, the slave trade had become a despised and
					illegal traffic. By the 1840s, the British had committed ten
					percent of their naval resources to suppressing the trade; a scant
					half century earlier they were the leading slave trading nation.</p>

					<p>One contributing factor to this shift is an extension of an
					argument made earlier in this essay. In one sense abolition was a
					shift in conceptions of who was eligible for enslavement. The
					definition of eligibility had certainly included other Europeans
					prior to the thirteenth century as a thriving slave trade within
					Europe saw people from the North captured by other Europeans and
					carried for sale in the South, many, ultimately, to the prosperous
					Islamic areas. This situation was little different from what
					existed in Africa, but, as already noted, by the time of Columbian
					contact, eligibility had come to exclude other Europeans. Africa
					was a much larger land mass and home to human populations of more
					diversity than could be found in any other area of similar size on
					the globe. It is not surprising that Africans did not have a
					continent-wide conception of insidership – that is peoples whom one
					could not enslave. In one sense, the massive and unprecedented flow
					of racially exclusive coerced labor across the Atlantic is perhaps
					the result of the differential pace in the evolution of a cultural
					pan-Europeanness on the one hand, and a pan-Africanism on the
					other. An interlude of two or three centuries between the former
					and the latter provided a window of opportunity in which the slave
					trade rose and fell dramatically. For four centuries from the
					mid-fifteenth century to 1867, Europeans were not prepared to
					enslave each other, but were prepared to buy Africans and keep them
					and their descendants enslaved. Given that “Africa“ scarcely
					existed as a concept for Africans in any sense before the
					nineteenth century, most people living in the sub-continent south
					of the Sahara (as in Europe) were prepared to enslave others from
					adjacent or distant societies. The corollary of this is that all
					peoples in history – even the most energetic of slave traders -
					have had strict definitions of eligibility – and thus
					ineligibility. “Ineligibility” implies that some basis for
					abolition has always existed. Between the fifteenth and nineteenth
					centuries, Europe and Africa simply had different conceptions of
					the peoples for whom slavery (and the slave trade) were
					inappropriate.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-07.faces">The Middle Passage</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-09.faces">The Trade’s Influence on Ethnic and Racial Identity</a>
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