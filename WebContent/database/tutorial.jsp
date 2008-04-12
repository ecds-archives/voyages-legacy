<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Tutorial</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="Tutorial" />
	</s:siteHeader>
	
	<div id="content">
	
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

				<h1>Tutorial</h1>

			</td>
			
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>