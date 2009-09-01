<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Register</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">

	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">
	
	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="submission-login.faces"><h:outputText value="Contribute" /></h:outputLink>
		<h:outputText value="Register" />
	</s:siteHeader>

	<div id="content">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<%@ include file="submission-menu.jsp" %>
			</td>		
			
			<td valign="top" id="main-content">
				<s:simpleBox>

					<h1>New user registration</h1>
	
	<br>
	
	<table>
	<tr>
		<td>User name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserName}"/></td>
	</tr>
	<tr>
		<td>Password: </td><td><h:inputSecret value="#{SubmissionUsersBean.newUserPassword}"/></td>
	</tr>
	<tr>
		<td>Retype password: </td><td><h:inputSecret value="#{SubmissionUsersBean.newUserPasswordAgain}"/></td>
	</tr>
	<tr>
		<td>First name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserFirstName}"/></td>
	</tr>
	<tr>
		<td>Last name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserLastName}"/></td>
	</tr>
	<tr>
		<td>Institution: </td><td><h:inputText value="#{SubmissionUsersBean.newUserInstitution}"/></td>
	</tr>
	<tr>
		<td>E-mail address: </td><td><h:inputText value="#{SubmissionUsersBean.newUserEmail}"/></td>
	</tr>
	<tr>
		<td>Telephone number1: </td><td><h:inputText value="#{SubmissionUsersBean.newUserPhone1}"/></td>
	</tr>
	<tr>
		<td>Telephone number2: </td><td><h:inputText value="#{SubmissionUsersBean.newUserPhone2}"/></td>
	</tr>
	<tr>
		<td>Brief description of new material and sources: </td><td><h:inputText value="#{SubmissionUsersBean.newUserDescription}"/></td>
	</tr>
	<tr>
		<td>Verification word: </td><td><h:outputText value="#{SubmissionUsersBean.verificationString}"/></td>
	</tr>
	<tr>
		<td>Type in verification word: </td><td><h:inputText value="#{SubmissionUsersBean.newUserVerificationString}"/></td>
	</tr>
	<tr>
		<td></td><td><h:outputText rendered="#{SubmissionUsersBean.newUserErrorMessage != null}" value="#{SubmissionUsersBean.newUserErrorMessage}"/></td>
	</tr>	
	<tr>
		<td></td><td><h:commandButton value="Register" action="#{SubmissionUsersBean.createNewUser}"/></td>
	</tr>
	</table>
</s:simpleBox>
			</td>
		</tr>
		</table>
		

</div>
</h:form>
</f:view>
</body>
</html>