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
		<div style="margin: 20px;">
			<table>
				<tr>
					<td> 
						<h:outputText value="Choose database revision: "/>
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
		
		
	</div>

</h:form>
</f:view>
</body>
</html>