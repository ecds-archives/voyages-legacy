<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Assessing the Slave Trade</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-index.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	<h:form id="main">

		<s:siteHeader activeSectionId="assessment">
			<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
			<h:outputText value="Assessing the Slave Trade" /> 
		</s:siteHeader>
	
		<div id="index-intro-band">
		
			<div id="index-intro-title">
				<img src="../images/assessment-index-title.png" width="340" height="40" alt="Assessing the Slave Trade" border="0">
			</div>

			<div id="index-intro-text">This section of the Voyages website
			is organized around three ways historians and researchers have used
			the Trans-Atlantic Slave Trade Database to interpret the slave trade:
			essays, quantitative analysis, and maps. Interactive features of the
			“Estimates” sub-section enable users to raise their own questions
			about the full volume of the slave trade by year, national carrier,
			and routes of slaving expeditions, and obtain answers in the form of
			tables, a timeline, or maps.</div>

		</div>
	
		<div id="section-titles">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="section-title"><a href="essays-intro-01.faces">Essays</a></td>
				<td class="section-title"><a href="estimates.faces">Estimates</a></td>
				<td class="section-title"><a href="intro-maps.faces">Introductory Maps</a></td>
			</tr>
			</table>
		</div>
	
		<div id="section-descs">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>

				<td class="section-desc">Three essays offer an introduction to the 
				Voyages website in the form of a broad interpretation and overview 
				of the volume and structure of the trans-Atlantic slave trade from 
				inception to suppression, an examination of seasonality in the 
				trans-Atlantic slave trade, and a study of an African liberated 
				in Cuba from a slave ship captured by a British cruiser in 1826. 
				Vignettes and research notes focus on more specialized topics.  
				Other essays will be added as the website develops.  The 
				<a href="../about/contact.faces">editorial board</a> will consider 
				for publication in the “Essays” section any text that uses databases 
				on the website as a major source.</td>
				
				<td class="section-desc">Although the Trans-Atlantic Slave Trade 
				Database includes all slave voyages that have been documented up 
				to now, it cannot claim to be complete. Records of many voyages 
				have disappeared, in some cases irretrievably, while other 
				documents remain to be discovered in public and private archives. 
				The “Estimates,” on the other hand, provide an educated guess of 
				how large the slave trade actually was.  Altogether, the estimates 
				are about 25 percent higher than unadjusted numbers in the main 
				database. They raise the final totals to over 12,500,000 Africans 
				forced to undertake the Middle Passage and around 10,700,000 who 
				completed it, the largest forced migration in modern history.</td>
				
				<td class="section-desc">A third way of using the
				Trans-Atlantic Slave Trade Database to interpret the slave trade is
				to represent statistics derived from it on historical maps. A
				project is now underway to create an atlas of the slave trade
				showing cartographically patterns and trends documented in the
				database. Yale University Press, the publisher of the atlas, and
				Mapping Specialists, Ltd., have given the Voyages website permission
				to preview 9 of over 200 maps that will make up the atlas. They
				depict the origins of the Atlantic slave trade, its development over
				time, how wind and ocean currents shaped its routes, and how it
				affected regions and ports in Africa and the Americas.</td>

			</tr>
			</table>
		</div>
		
	</h:form>
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>