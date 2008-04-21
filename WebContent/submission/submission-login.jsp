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
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
		<h:outputText value="Contribute" />
	</s:siteHeader>
	
	<div id="content">
	
	<h1>Sign in</h1>
	
	<br>
	
	<div style="width: 400px;">
	In order to be able to access prortected contents, please sign in.
	</div>
	
	<div style="width: 400px; border-bottom: 1px solid #CC9B1C; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td style="padding-right: 20px;">User name:</td>
				<td><h:inputText value="#{SubmissionUsersBean.userName}"/></td>
			</tr>
			<tr>
				<td style="padding-right: 20px;">Password:</td>
				<td><h:inputSecret value="#{SubmissionUsersBean.password}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:commandButton value="Sign in &gt;" action="#{SubmissionUsersBean.auth}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:outputText value="#{SubmissionUsersBean.errorMessage}"/></td>
			</tr>			
		</table>
	</div>
	
	<div style="width: 400px;">
	If you don't have an account, <a href="new-user.faces">click here</a>.
	</div>
		
	</div>

</h:form>
</f:view>
</body>
</html>
