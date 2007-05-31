<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>User detals</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>User details</h1>
	
	<br>
	
	<div style="width: 400px;">
	In order to be able to access prortected contents, please sign in.
	</div>
	<div style="width: 400px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<table>
			<tr>
				<td>User name:</td>
				<td><h:outputText value="#{SubmissionUsersBean.checkedUserName}"/></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><h:inputText value="#{SubmissionUsersBean.checkedPassword}"/></td>
			</tr>
			<tr>
				<td>First name:</td>
				<td><h:inputText value="#{SubmissionUsersBean.checkedFirstName}"/></td>
			</tr>
			<tr>
				<td>Last name:</td>
				<td><h:inputText value="#{SubmissionUsersBean.checkedLastName}"/></td>
			</tr>
			<tr>
				<td>Institution:</td>
				<td><h:inputText value="#{SubmissionUsersBean.checkedInstitution}"/></td>
			</tr>
			<tr>
				<td>Email address:</td>
				<td><h:inputText value="#{SubmissionUsersBean.checkedEmail}"/></td>
			</tr>
			<tr>
				<td>Date created:</td>
				<td><h:outputText value="#{SubmissionUsersBean.checkedDate}"/></td>
			</tr>
			<tr>
				<td>User active:</td>
				<td><h:selectBooleanCheckbox value="#{SubmissionUsersBean.checkedActive}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:outputText rendered="#{SubmissionUsersBean.checkedUserErrorMessage != null}" value="#{SubmissionUsersBean.checkedUserErrorMessage}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:commandButton value="< Back" action="main-menu"/>
				<h:commandButton value="Delete user" action="#{SubmissionUsersBean.deleteUser}"/>
				<h:commandButton value="Save changes >" action="#{SubmissionUsersBean.updateUser}"/></td>
			</tr>			
		</table>
	</div>
</h:form>
</f:view>
</body>
</html>