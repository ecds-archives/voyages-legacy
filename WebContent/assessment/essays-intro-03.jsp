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
				<f:param value="essays-intro-03" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>The Enslavement of Africans</h2>

					<p>But why were the slaves always African? One possible answer
					draws on the different values of societies around the Atlantic and,
					more particularly, the way groups of people involved in creating a
					transatlantic community saw themselves in relation to others – in
					short, how they defined their identity. Ocean-going technology
					brought Europeans into large scale face-to-face contact with
					peoples who were culturally and physically more different from
					themselves than any others with whom they had interacted in the
					previous millennium. In neither Africa, nor Asia could Europeans
					initially threaten territorial control - with the single and
					limited exception of western Angola. African capacity to resist
					Europeans ensured that sugar plantations were established in the
					Americas rather than in Africa. But if Africans, aided by tropical
					pathogens, were able to resist the potential invaders, some
					Africans were prepared to sell slaves to Europeans for use in the
					Americas. As this suggests, European domination of Amerindians was
					complete. Indeed, from the European perspective it was much too
					complete. The epidemiological impact of the Old World destroyed not
					only native American societies, but also a potential labor supply.</p>

					<p>Every society in history before 1900 provided at least an
					unthinking answer to the question of which groups are to be
					considered eligible for enslavement, and normally they did not
					recruit heavily from their own community. A revolution in
					ocean-going technology gave Europeans the ability to get continuous
					access to remote peoples and move them against their will over very
					long distances. Strikingly, it was much cheaper to obtain slaves in
					Europe than to send a vessel to an epidemiological hostile coast in
					Africa without proper harbors and remote from European political
					financial and military power. That this option was never seriously
					considered suggests a European inability to enslave other
					Europeans. Except for a few social deviants, neither Africans nor
					Europeans would enslave members of their own societies, but in the
					early modern period, Africans had a somewhat narrower conception of
					who was eligible for enslavement than had Europeans. It was this
					difference in definitions in eligibility for enslavement which
					explains the dramatic rise of the transatlantic slave trade.
					Slavery, which had disappeared from north-west Europe long before
					this point, exploded into a far greater significance and intensity
					than it had possessed at any point in human history. The major
					cause was a dissonance in African and European ideas of eligibility
					for enslavement at the root of which lies culture or societal
					norms, not easily tied to economics. Without this dissonance, there
					would have been no African slavery in the Americas. The slave trade
					was thus a product of differing constructions of social identity
					and the ocean-going technology that brought Atlantic societies into
					sudden contact with each other.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-02.faces">New Products for Trade in the Americas</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-04.faces">Currents Driving the Trade</a>
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