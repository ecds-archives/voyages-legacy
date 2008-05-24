<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Download</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-index.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Downloads" />
	</s:siteHeader>
	
	<div id="content">		
		
			<h1>DOWNLOADS</h1> 
			
			<h2>The Trans-Atlantic Slave Trade SPSS Database (expanded version)</h2>
				
			<p>The 2008 version of the Trans-Atlantic Slave Trade Database contains 8,374 voyages 
			added since the CD-Rom was published in 1999 and additional information on 19,320 voyages.  
			The complete data set has 276 variables, compared with 99 in the Voyages version.  Users 
			interested in working with this larger dataset can download it in a file formatted for use 
			with SPSS software, selecting the edition year from the drop-down menu below.  A variable 
			in this dataset (EVGREEN) identifies voyages on the CD-ROM version published in 1999.  
			Recognizing that some users will find it useful to view data as it existed in 1999, 
			the CD version can also be selected for download.  A codebook describing all variable names, 
			variable labels, and values of the expanded dataset can be downloaded as a separate PDF document.  
			With only a few exceptions, it retains variable names in the original CD-ROM version.</p>
		
			<h3>Downloads:</h3>
				<ul>
					<li><a href="http://www.slavevoyages.org/downloads/tastdb-exp-2008.sav">tastdb-exp-2008.sav</a></li>
					<li><a href="http://www.slavevoyages.org/downloads/tastdb-1999.sav">tastdb-1999.sav</a></li>
					<li><a href="#">SPSS codebook</a></li>
				</ul>		
					
			
			<h2>The Trans-Atlantic Slave Trade Database (Voyages version)</h2>
			
			<p>This version of the Trans-Atlantic Slave Trade Database contains the 99 variables available 
			through the Voyages website database.  It is made available in three formats: SPSS (.sav), comma 
			delimited (.csv), and dBase (.dbf).  Users interested in working with this dataset with their 
			preferred software may select an edition year and file format below. </p>
			
			<h3>Downloads:</h3>
				<ul>
					<li><a href="http://www.slavevoyages.org/downloads/tastdb-2008.sav">tastdb-2008.sav</a></li>
					<li><a href="http://www.slavevoyages.org/downloads/tastdb-2008.csv">tastdb-2008.cvs</a></li>
					<li><a href="http://www.slavevoyages.org/downloads/tastdb-2008.dbf">tastdb-2008.dbf</a></li>
				</ul>
				
			<h2>Estimates spreadsheet</h2>
			
			<p>The Estimates section uses data from the expanded Trans-Atlantic Slave Trade Database dataset, 
			aggregated by regions of embarkation and disembarkation, but also incorporates published series 
			of embarkations and disembarkations for particular regions when these data provide more accurate 
			estimates than the database itself. Users interested in exploring the complex methodology involved 
			in generating the estimates, or in testing the extent to which alternative assumptions lower or 
			augment estimates of the full volume of the slave trade (in its entirety or for a national carrier 
			or period of time), can download an Excel-formatted copy of the spreadsheet below. </p>

			<h3>Download:</h3>
				<ul>
					<li><a href="#">Estimate.xls</a></li>
				</ul>
				
			<br>	
			<h3>Questions about downloading or using these datasets? Please contact 
			<a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">
			&#118;&#111;&#121;&#97;&#103;&#101;s&#64;&#101;mo&#114;y&#46;&#101;&#100;&#117;</a>.</h3>
			
	</div>

</h:form>
</f:view>
</body>
</html>