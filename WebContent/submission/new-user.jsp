<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission - step 1</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

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
	

</h:form>
</f:view>
</body>
</html>