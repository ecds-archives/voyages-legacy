<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Images database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-expandable-box.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="resources">
		<h:outputLink value="../index.faces"><h:outputText value="Home page" /></h:outputLink>
		<h:outputText value="Resources" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
				
				<s:expandableBox text="Quick Start">
					<h:outputText value="" />
				</s:expandableBox>
			</td>
			<td class="section-index-right-column">

				<img src="../images/resources-index-title.png" width="350" height="40" border="0" alt="Resources">
				
				<p class="section-index-intro">The Voyages website provides resources that offer some context to the information available in the database of transatlantic slave trading voyages. The slave trade was an activity that involved peoples, places, tools as well as practices that today are unfamiliar to most, or no longer exist. In this page, you will find resources that will help you visualize or imagine these features of the transatlantic slave trade era. The materials are divided into a database of images contemporary to the slave trade and a database of names of Africans rescued from slave vessels in the nineteenth century, during the period of suppression. With permission from various archives and libraries, the images displayed here can be used to observe details of people, places, artifacts and vessels of this time and make connections with the relevant voyages in the main database. The names database was built with documents from the Foreign Office, Series 84 and 313, held by the British National Archives. The details of the vessels from which the liberated Africans were rescued are listed in the voyages database. </p>

				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Images</a></div>
				
					<p class="section-index-intro">The transatlantic slave trade was an activity that lasted for centuries. As a consequence, the ways it became represented changed through time. Here, you will find images contemporary to the transatlantic slave trade representing places, vessels and artifacts as well as of slavers and slaves.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Names Database</a></div>
				
					<p class="section-index-intro">The transatlantic slave trade culminated in the age of abolition. In the nineteenth century, mixed-commissioned courts were created around the Atlantic basins to adjudicate vessels illegally engaged in the slave trade. The names database offers a compilation of lists of liberated Africans from slave vessels captured by British cruisers between 1819 and 1845, and taken for adjudication in the courts established in Freetown, Sierra Leone, and in Havana, Cuba.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>