<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>New Source Code</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>New Source Code</h1>
	
	<br>
	<div style="width: 550px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
	
	<table>
		<tr>
			<td>Source Short Code:</td>
			<td><h:inputText value="#{SubmissionSourceCodesBean.newSourceId}"/></td>
		</tr>
		<tr>
			<td>Source Description:</td>
			<td><h:inputText value="#{SubmissionSourceCodesBean.newName}"/></td>
		</tr>
		<tr>
			<td>Source Type:</td>
			<td><h:inputText value="#{SubmissionSourceCodesBean.newType}"/></td>
		</tr>

	<tr>
		<td></td><td><h:outputText rendered="#{SubmissionSourceCodesBean.newSourceErrorMessage != null}" value="#{SubmissionSourceCodesBean.newSourceErrorMessage}"/></td>
	</tr>	
	<tr>
		<td></td>
		<td><h:commandButton style="width:120px;" value="< Back" action="main-menu"/> &nbsp; &nbsp;
		<h:commandButton style="width:120px;" value="Create Source" action="#{SubmissionSourceCodesBean.createNewSource}"/></td>
	</tr>
		</table>
	</div>
</h:form>
</f:view>
</body>
</html>