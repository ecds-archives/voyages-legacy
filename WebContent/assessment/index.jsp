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

				<td class="section-desc">This sub-section contains
				interpretations in traditional text format. An introductory essay
				provides a broad interpretation and overview of the volume and
				structure of the trans-Atlantic slave trade from inception to
				suppression. Several vignettes describe the personal experience of
				Africans carried to the Americas on slave ships. Finally, research
				notes focus on more specialized topics. Other “Essays” will be added
				as the website develops. Its <a href="../about/contact.faces">editorial
				board</a> will consider for publication in the “Essays” section any text
				that uses databases on the website as a major source.</td>
				
				<td class="section-desc">This link takes you to the page for
				querying the main database. You can search for information about a
				specific voyage, a small group of voyages, or a large subset of the
				data set such as all voyages under the Portuguese flag. There is a
				choice between a smaller “basic” set of 24 variables and a larger
				“general” set of 64 variables. Each set groups variables into the
				same eight categories. The interface of the query section is
				designed to be as intuitive as possible, but many of the FAQs are
				about how to use its features.</td>
				
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
</body>
</html>