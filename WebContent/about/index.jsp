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
	
		<img src="../images/about-index-title.png" width="350" height="40" border="0" alt="The Database">

		<p>Over 34,000 individual slaving expeditions between 1527 and 1866
		that have been identified and verified to have actually occurred make
		up the Trans-Atlantic Slave Trade Database. Records of the voyages
		have been found in multiple archival sources which are listed in a
		variable in the dataset. These records provide details about vessels,
		enslaved peoples, slave traders and owners, and trading routes. The
		database enables users to search for information about a particular
		voyage or group of voyages and it provides full interactive capability
		to analyze the data and report results in the form of statistical
		tables, graphs, maps, or on a timeline. In addition to information in
		the database itself, specific voyages are linked to images and to
		copies of primary sources in the “Resources” section, and “Educational
		materials” like lesson-plans are linked in turn to relevant voyages in
		the main database. Users are encouraged to compare findings from the
		main database with “Estimates” in the first section. The latter are
		somewhat higher because they represent an attempt to take into account
		the number of slaves on voyages for which information is lacking or
		not yet included in the main database.</p>
	
	</div>

</h:form>
</f:view>
</body>
</html>