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
				<td class="section-title"><a href="/tast/resources/slaves.faces">African Names Database</a></td>
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

				<td class="section-desc">During the last 60 years of the trans-Atlantic slave trade, courts around the Atlantic 
basins condemned over two thousands vessels for engaging in the traffic and recorded the details of captives found on 
board including their African names. The African Names Database was  created from these records, now located in the 
Registers of Liberated Africans at the Sierra Leone National Archives, Freetown, as well as Series FO84, FO313, CO247 and 
CO267 held at the British National Archives in London. Links are provided to the ships in the Voyages Database from which 
the liberated Africans were rescued, as well as to the African Origins site where users can hear the names pronounced and 
help us identify the languages in which they think the names are used.</td>

			</tr>
			</table>
		</div>


</h:form>
</f:view>
</body>
</html>