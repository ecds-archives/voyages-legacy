<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Variable List</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-variables.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="Variable list" />
	</s:siteHeader>
	
	<div id="content" style="padding-top: 15px;">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" class="left-column">

				<div class="left-menu-title">Understanding the Database</div>
				<ul class="left-menu">
					<li><a href="tutorial.faces">Tutorial</a></li>
					<li><a href="methodology.faces">Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</a></li>
					<li><a href="variable-list.faces">Variable list</a></li>
				</ul>
			
				<div class="left-menu-title" style="margin-top: 10px;">Sources</div>
				<ul class="left-menu">					
					<li><a href="sources.faces?type=documentary">Documentary Sources</a></li>
					<li><a href="sources.faces?type=newspapers">Newspapers</a></li>
					<li><a href="sources.faces?type=published">Published Sources</a></li>
					<li><a href="sources.faces?type=unpublished">Unpublished Secondary Sources</a></li>
					<li><a href="sources.faces?type=private">Private Notes and Collections</a></li>
				</ul>

			</td>			
			<td valign="top">

				<h1 style="margin-left: 15px; padding-top: 0px; margin-bottom: 0px;">Variable list</h1>

				<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top" style="padding-left: 15px;">
				
						<div id="variables-list">
							<s:simpleTable rows="#{VariableListBean.table}" />
						</div>
						
					</td>
					<td valign="top" style="padding-left: 20px;">
					
						<div style="font-weight: bold; font-size: 12pt; padding-top: 15px; border-bottom: 1px solid Black; padding-left: 0px; padding-bottom: 2px;">Commentary</div>

						<div style="width: 170px; background-color: White; border-bottom: 1px solid #DDCA9D; padding: 10px;">
				
						<p style="margin-top: 0px;">The variable list provides summary information on variables of the Trans-Atlantic Slave Trade database in the "Search the Database" section. The variables are displayed here in the form of short labels.  The third column gives their equivalents in the downloadable SPSS version of the database, where variable names take the form of acronyms.  These names are also used in downloads of data for listings, tables and graphics from the website.</p>							
						<p style="margin-top: 10px;">Variables without an asterisk are "data variables" -- information as found in a primary source documenting a slaving voyage.  Variables followed by an asterisk are "imputed variables."  They include information, such as the mortality rate on slave voyages, that is calculated from data variables as well as information which, although not directly documented, can be supplied on the basis of patterns observed in data variables.  							</p>
						<p style="margin: 0px;">Clicking on the row for any variable takes the user to paragraphs relevant to it in the essay on "Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods."  The entire essay can be accessed through the link in the left frame.  							</p>
						
						</div>

					</td>	
				</tr>
				</table>

			</td>
			
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>