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
		<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
		<h:outputText value="Download" />
	</s:siteHeader>
	
	<div id="content">		
		
			<h1>DOWNLOADS</h1> 
			
			<h2>The Trans-Atlantic Slave Trade SPSS Database (expanded version)<h2>
				
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
		
			<table>	
				<tr>
					<td> 
						<h:outputText value="Select edition year: "/>
					</td>
					<td>
						<h:selectOneListbox value="#{DownloadDatabaseBean.revision}">
							<f:selectItems value="#{DownloadDatabaseBean.revisions}"/>
						</h:selectOneListbox>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<h:selectBooleanCheckbox value="#{DownloadDatabaseBean.codes}">
							<h:outputText value="Resolve codes to names"/>
						</h:selectBooleanCheckbox>
					</td>
				</tr>
				<tr>
					<td> 
						<h:outputText value="To download the database, click "/>
						<h:commandLink action="#{DownloadDatabaseBean.getFileAllData}" value="here"/>
						<h:outputText value="."/>
					</td>
				</tr>
		
				<tr>
					<td>
					<a href="../TSTD67.sav">Download</a> static SPSS file.
					</td>
				</tr>
			
			</table>
		
		
		
	</div>

</h:form>
</f:view>
</body>
</html>