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
				<f:param value="essays-intro-02" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>New Products for Trade in the Americas</h2>

					<p>What can explain this extraordinary migration organized
					initially on a continent where the institution of slavery had
					declined or totally disappeared in the centuries prior to Columbian
					contact, and where, even when it had existed, slavery had never
					been confined to one group of people? To pose the question
					differently, why slavery, and why were the slaves carried across
					the Atlantic exclusively African? The short answer to the first of
					these two questions is that European expansion to the Americas was
					to mainly tropical and semi-tropical areas. Several products that
					were either unknown to Europeans (like tobacco), or occupied a
					luxury niche in pre-expansion European tastes (like gold or sugar),
					now fell within the capacity of Europeans to produce more
					abundantly. But while Europeans could control the production of
					such exotic goods, it became apparent in the first two centuries
					after Columbian contact that they chose not to supply the labor
					that would make such output possible. Free European migrants and
					indentured servants never traveled across the Atlantic in
					sufficient numbers to meet the labor needs of expanding
					plantations. Convicts and prisoners – the only Europeans that were
					ever forced to migrate – were much fewer in numbers again. Slavery,
					or some form of coerced labor was the only possible option if
					European consumers were to gain access to more tropical produce and
					precious metals.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-01.faces">Introduction</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-03.faces">The Enslavement of Africans</a>
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