<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission - step 1</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<script src="../scripts/grid-editor.js" type="text/javascript" langiage="javascript"></script>
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>New submission - edit</h1>
	
	<br>
	
	<s:gridEditor
		rows="#{SubmissionBean.rows}"
		columns="#{SubmissionBean.columns}"
		values="#{SubmissionBean.values}"
		fieldTypes="#{SubmissionBean.fieldTypes}"
		rowGroups="#{SubmissionBean.rowGroups}" />

	<div style="margin-top: 10px; margin-bottom: 10px;">
		<h:commandButton value="< Previous" action="#{SubmissionBean.goBackFromForm}"/>
		<h:commandButton value="Submit" action="#{SubmissionBean.submit}"/>
	</div>

</h:form>
</f:view>
</body>
</html>