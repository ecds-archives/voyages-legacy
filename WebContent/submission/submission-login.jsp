
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

	<h1>Sign in</h1>
	
	<br>
	
	<div style="width: 400px;">
	In order to be able to access prortected contents, please sign in.
	</div>
	<div style="width: 400px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<table>
			<tr>
				<td>User name:</td>
				<td><h:inputText value="#{SubmissionUsersBean.userName}"/></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><h:inputSecret value="#{SubmissionUsersBean.password}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:commandButton value="Sign in >" action="#{SubmissionUsersBean.auth}"/></td>
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
</h:form>
</f:view>
</body>
</html>