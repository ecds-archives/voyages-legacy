<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>The Database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	<h:form id="main">

		<s:siteHeader activeSectionId="database">
			<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
			<h:outputText value="Voyages Database" />
		</s:siteHeader>

		<div id="content">
		<h1 style="color: #6c4e28;font-size:18pt;">The Voyages Database</h1>

		<p>The Trans-Atlantic Slave Trade Database comprises nearly 35,000 
		individual slaving expeditions between 1514 and 1866. Records of the 
		voyages have been found in archives and libraries throughout the Atlantic 
		world.  They provide information about vessels, enslaved peoples, slave 
		traders and owners, and trading routes.  A variable (Source) cites the 
		records for each voyage in the database.  Other variables enable users 
		to search for information about a particular voyage or group of voyages.  
		The website provides full interactive capability to analyze the data and 
		report results in the form of statistical tables, graphs, maps, or on a timeline. </p>

		<h2><a href="guide.faces">Understanding the Database</a></h2>
		
		<p>This part of “The Database” contains a guide and a demo to help 
		newcomers get started in using the database.   An essay explains in 
		detail the sources and methods used in constructing the database and 
		creating the set of estimates based on it.  A list of the variables 
		in the version of the database on this website is provided.  Lastly, 
		the user will find a comprehensive bibliography of primary and 
		secondary sources consulted in creating the database.</p>

		<h2><a href="search.faces">Search the Voyages Database</a></h2>

		<p>This link takes you to the page for querying the main database. 
		You can search for information about a specific voyage, a small group 
		of voyages, or a large subset of the data set such as all voyages under 
		the Portuguese flag. There is a choice between a smaller “basic” set of 
		24 variables and a larger “general” set of 64 variables. Each set groups 
		variables into the same eight categories. The interface of the query 
		section is designed to be as intuitive as possible, but many of the 
		FAQs are about how to use its features. </p>

		<h2><a href="download.faces">Downloads</a></h2>

		<p class="section-index-intro">The variables accessible on-line 
		cover the full range of information available in the database.  
		Some users will want to work with variables that have not been 
		included in the selection for the website and use spreadsheets or 
		statistics software to analyze the data. This link takes you to a 
		page with buttons to download the full set of variables in the 
		Trans-Atlantic Slave Trade Database in SPSS format, a PDF copy of 
		the SPSS codebook, the website version of the database in three 
		formats (SPSS, csv, and dBase), and the Excel spreadsheet used in making “Estimates.”</p>

		<h2><a href="../submission/submission-login.faces">Contribute</a></h2>

		<p>The “Contribute” section consists of data entry forms by which 
		users of the website can supply new information or revise existing 
		information on trans-Atlantic slaving voyages.  Specialists on the 
		slave trade will review submissions of new or revised information.  
		Once confirmed, the new or revised information will be incorporated 
		in the next revision of the database appearing in the website.  
		We anticipate releasing a new revision every three years.</p>

		</div>

	</h:form>
</f:view>
</body>
</html>