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
					
					<h2>African Agency and Resistance</h2>

					<p>If demand for slave-grown produce, social identity, and the
					Atlantic environment were three key factors shaping the traffic,
					the agency of Africans comprised a fourth major influence, but one
					which has received less attention from historians. The merchants
					who traded slaves on the coast to European ship captains – for
					example the Vili traders north of the Congo, the Efik in the Bight
					of Biafra - and behind them the groups that supplied the slaves,
					such as the Kingdom of Dahomey, the Aro network and further south,
					the Imbangala, all had strict conceptions of what made an
					individual eligible for enslavement. Among such criteria were
					constructions of gender, definitions of criminal behavior, and
					conventions for dealing with prisoners of war. The make up of
					slaves purchased on the Atlantic coast thus reflected whom Africans
					were prepared to sell as much as whom Euro-American plantation
					owners wanted to buy. But the victims of the slave trade also had a
					major impact on the trade. Probably about one in ten slaving
					voyages experienced major rebellions, the attempts to control which
					increased the costs of a slave voyage to the point where far fewer
					slaves entered the traffic than would have been the case without
					resistance. In addition, vessels from some regions on the coast
					appear to have been more prone to experience slave uprisings than
					those from other regions. The rebellion-prone areas were precisely
					those regions, broadly comprising Upper Guinea (Senegambia, Sierra
					Leone, and the Windward Coast) which had the least participation in
					the slave trade. The strong inference is that European slave
					traders avoided this part of the African coast except in those
					years when demand for slaves, and their prices were particularly
					high.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-04.faces">Currents Driving the Trade</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-06.faces">Early Slaving Voyages</a>
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