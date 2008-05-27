<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>About Us</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-index.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputText value="About the Project" />
	</s:siteHeader>
	
	<div id="content">
	
		<h1 style="color: #606060;font-size:18pt;">About the Project</h1>

		<p>The Trans-Atlantic Slave Trade Database is the culmination of several decades of 
		independent and collaborative research by scholars drawing upon data in libraries and 
		archives around the Atlantic world.  The Voyages website itself is the product of two 
		years of development by a multi-disciplinary team of historians, librarians, curriculum 
		specialists, cartographers, computer programmers, and web designers, in consultation with 
		scholars of the slave trade from universities in Europe, Africa, South America, and North 
		America.  With principal sponsorship by the National Endowment for the Humanities, is the 
		principal sponsor of this project, which was also  Emory University Libraries.  </p>
	
		<h2><a href="history.faces">History</a></h2>
		
		<p>A brief account of the origins of a single multi-source dataset of the trans-Atlantic 
		slave trade and its realization first as a CD-ROM published by Cambridge University Press 
		in 1999 and now, in an expanded version, on the Voyages website.</p>
		
		<h2><a href="team.faces">Project Team</a></h2>
		
		<p>Names of the principal investigators, of members of the project development team, and 
		of individuals serving on its steering committee and advisory board.</p>
		
		<h2><a href="data.faces">Contributors of data</a></h2>
		
		<p>Names of scholars and researchers whose findings on the trans-Atlantic slave trade 
		have been incorporated into the Voyages Database.</p>
		
		<h2><a href="acknowledgements.faces">Acknowledgements</a></h2>
		
		<p>Major sponsors and institutional partners of the Voyages website, as well as other 
		organizations and individuals who have assisted the work of the project team.</p>
		
		<h2><a href="contact.faces">Contact us</a></h2>
		
		<p>Members of the Voyages editorial board and the email address for contacting the website.</p>
		
		<h2><a href="legal.faces">Legal</a></h2>
		
		<p>Copyright restrictions and permission to reproduce or use materials found on the website.</p>
	</div>

</h:form>
</f:view>
</body>
</html>