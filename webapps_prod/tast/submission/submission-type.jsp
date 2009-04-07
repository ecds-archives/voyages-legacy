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
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">
	
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-active-left">Type of contribution</td>
			<td class="step-indicator-middle">Your voyage information</td>
			<td class="step-indicator-middle">Your sources</td>
			<td class="step-indicator-middle">Summary</td>
			<td class="step-indicator-right">Finish</td>
		</tr>
	</table>

	<br>

	<h1>Choose type of submission</h1>
	
	<br>
	
	<div style="font-weight: bold; font-size: 12pt">New voyage</div>
	<div style="width: 400px;">
	Use this option to contribute a new voyage.
	</div>
	<div style="width: 400px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:commandButton value="New voyage >" action="#{SubmissionBean.selectTypeNew}"/>
	</div>
	
	<div style="font-weight: bold; font-size: 12pt">Edit an existing voyage</div>
	<div style="width: 400px;">
	Use this option to change information on a voyage in the current database.
	</div>
	<div style="width: 400px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:commandButton value="Edit voyage >" action="#{SubmissionBean.selectTypeEdit}"/>
	</div>
	
	<div style="font-weight: bold; font-size: 12pt">Merge existing voyages</div>
	<div style="width: 400px;">
	Use this option if you want provide a suggestion for merging existing voyages into one. In the next step you will be asked to
	select the voyages you want to merge.
	</div>
	<div style="width: 400px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:commandButton value="Merge voyages >" action="#{SubmissionBean.selectTypeMerge}"/>
	</div>

</h:form>
</f:view>
</body>
</html>