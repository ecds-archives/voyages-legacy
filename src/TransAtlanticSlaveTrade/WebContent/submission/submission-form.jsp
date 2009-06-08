<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Edit voyage information</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" type="text/javascript" langiage="javascript"></script>
	<script src="../scripts/tooltip.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/grid-editor.js" type="text/javascript" langiage="javascript"></script>
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of contribution</td>
			<td class="step-indicator-active-middle">Your voyage information</td>
			<td class="step-indicator-middle">Your sources</td>
			<td class="step-indicator-middle">Summary</td>
			<td class="step-indicator-right">Finish</td>
		</tr>
	</table>

	<br>

	<h1>Edit voyage information</h1>
	
	<br>
	
	<s:gridEditor 
		id="grid"
		rows="#{SubmissionBean.rows}"
		columns="#{SubmissionBean.columns}"
		values="#{SubmissionBean.values}"
		fieldTypes="#{SubmissionBean.fieldTypes}"
		rowGroups="#{SubmissionBean.rowGroups}"
		expandedGroups="#{SubmissionBean.expandedGridRows}" />
	
	<br>
		
	<s:gridEditor 
		id="grid-slave"
		rows="#{SubmissionBean.rowsSlave}"
		columns="#{SubmissionBean.columnsSlave}"
		values="#{SubmissionBean.valuesSlave}"
		rowGroups="#{SubmissionBean.rowGroupsSlave}"
		fieldTypes="#{SubmissionBean.fieldTypesSlave}"
		expandedGroups="#{SubmissionBean.expandedGridRowsSlave}" />

	<div style="margin-top: 10px; margin-bottom: 10px;">
		<h:commandButton value="< Previous" action="#{SubmissionBean.goBackFromForm}"/>
		<h:commandButton value="Save & logout" action="#{SubmissionBean.saveStateSubmission}"/>
		<h:commandButton value="Logout Without Saving" action="#{SubmissionBean.logoutOnly}"/>
		<h:commandButton value="Next > " action="#{SubmissionBean.toSources}"/>
	</div>

</h:form>
</f:view>
</body>
</html>