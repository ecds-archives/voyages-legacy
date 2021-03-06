<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Edit voyage information</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/tooltip.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/grid-editor.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/disablekey.js" type="text/javascript" language="javascript"></script>
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">
	<%@ include file="../top-logo-bar.jsp" %>
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of contribution</td>
			<td class="step-indicator-active-middle">Your voyage information</td>
			<td class="step-indicator-middle">Your sources</td>
			<td class="step-indicator-middle">Summary</td>		
		</tr>
</table>
	<br>
	<table width="600">
	<tbody>
	<h1>Edit voyage information</h1>
	<tr><td>
		<I><h:outputText rendered="#{SubmissionBean.isEditType}" value="Click &quot;Copy&quot; to copy over existing information, or, to add new data, enter the new data directly to the field. In the special case where you wish to recommend deletion, type &quot;DELETE&quot; in the Name of Vessel row and click on add note to explain the reasons for your request and any relevant sources."/></I>
		<I><h:outputText rendered="#{SubmissionBean.isMergeType}" value="Click &quot;Copy&quot; link to copy over existing information or adding new information to the field."/></I>
		<br/>		
	</td></tr>
	<br>
	</tbody>
	</table>
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
		<h:commandButton value="Logout without saving" action="#{SubmissionBean.logoutOnly}"/>
		<h:commandButton value="Cancel contribution & logout" action="#{SubmissionBean.cancel}"/>
		<h:commandButton value="Save & next> " action="#{SubmissionBean.toSources}"/>
	</div>

</h:form>
<script type="text/javascript">insertAttrib(document.getElementById("main"), "autocomplete", "off")</script>
</f:view>
</body>
</html>