<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Resources</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-index.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
	<h:form id="main">

		<s:siteHeader activeSectionId="resources">
			<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
			<h:outputText value="Resources" />
		</s:siteHeader>
		
		<div id="index-intro-band">
		
			<div id="index-intro-title">
				<img src="../images/resources-index-title.png" width="140" height="40" border="0" alt="Resources">
			</div>
	
			<div id="index-intro-text">This section of the Voyages website provides resources for putting 
			information in the databases into historical context.  The materials presently consist of a 
			database of images contemporary to the slave trade and a database of names of Africans rescued 
			from slave vessels in the nineteenth century, during the period of suppression. </div>
	
		</div>
		
		<div id="section-titles">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="section-title"><a href="images.faces">Images</a></td>
				<td class="section-title"><a href="/tast/about/origins.faces">African Origins Project</a></td>
			</tr>
			</table>
		</div>
		
		<div id="section-descs">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>

				<td class="section-desc">Representations of the trans-Atlantic slave trade, 
				an activity that lasted for centuries, changed over time. With permission 
				from various archives and libraries, selected images enable one to picture 
				people, places, artifacts and vessels of the slave trade era.  Links are 
				provided to relevant voyages in the Voyages Database.</td>

				<td class="section-desc">African Origins contains information about the migration 
				histories of Africans forcibly carried on slave ships into the Atlantic. Using the 
				personal details of 91,491 Africans liberated by International Courts of Mixed 
				Commission and British Vice Admiralty Courts, this resource makes possible new 
				geographic, ethnic, and linguistic data on peoples captured in Africa and 
				pulled into the slave trade. Through contributions to this website by Africans, 
				members of the African Diaspora, and others, we hope to set in motion the rediscovery 
				of the backgrounds of the millions of Africans captured and sold into slavery during 
				suppression of transatlantic slave trading in the 19th century.</td>

			</tr>
			</table>
		</div>


</h:form>
</f:view>
</body>
</html>