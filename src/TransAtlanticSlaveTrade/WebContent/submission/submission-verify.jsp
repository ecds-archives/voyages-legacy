<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Edit voyage information</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" type="text/javascript" langiage="javascript"></script>
	<script src="../scripts/grid-editor.js" type="text/javascript" langiage="javascript"></script>
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">
	<%@ include file="../top-logo-bar.jsp" %>	
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of contribution</td>
			<td class="step-indicator-middle">Your voyage information</td>
			<td class="step-indicator-middle">Your sources</td>
			<td class="step-indicator-active-middle">Summary</td>		
		</tr>
</table>
	<br>
	
	<h1>Verify submitted information</h1>
	<I>To cancel this contribution, please click "Cancel Contribution" button at "Your voyage information" page</I>	
	<br>

	<s:gridEditor 
		id="grid"
		rows="#{SubmissionVerifyBean.verifyRows}"
		columns="#{SubmissionVerifyBean.verifyColumns}"
		values="#{SubmissionVerifyBean.verifyValues}"
		fieldTypes="#{SubmissionVerifyBean.fieldTypes}"
		rowGroups="#{SubmissionVerifyBean.verifyRowGroups}"
		expandedGroups="#{SubmissionVerifyBean.verifyExpandedGridRows}" />
	
	<br>	
	
	<s:gridEditor 
		id="grid-slave"
		rows="#{SubmissionVerifyBean.verifyRowsSlave}"
		columns="#{SubmissionVerifyBean.verifyColumnsSlave}"
		values="#{SubmissionVerifyBean.verifyValuesSlave}"
		rowGroups="#{SubmissionVerifyBean.verifyRowGroupsSlave}"
		fieldTypes="#{SubmissionVerifyBean.fieldTypesSlave}"
		expandedGroups="#{SubmissionVerifyBean.verifyExpandedGridRowsSlave}" />

	<br>
	<br>
	
	<h1>Submitted sources</h1>
	
	<br>
	
	<s:grid id="sources" 
					columns="#{SourcesBean.columns}"
					rows="#{SourcesBean.rows}" />
					
	<div style="margin-top: 10px; margin-bottom: 10px;">
		<h:commandButton value="< Correct information" action="correct"/>
		<h:commandButton value="Submit" action="#{SubmissionBean.submit}"/>
	</div>

</h:form>
</f:view>
</body>
</html>