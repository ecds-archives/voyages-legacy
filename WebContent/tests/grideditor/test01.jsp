<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Test</title>
	<script src="../../scripts/grid-editor.js" language="javascript" type="text/javascript"></script>
	<link href="../../scripts/grid-editor.css" rel="stylesheet" type="text/css">
	
	<style type="text/css">
	
	body, input, select, textarea {
		font-size: 8pt;
		font-family: Verdana, sanf-serif; }
		
	table.grid-editor {
		border-collapse: collapse; }
	
	table.grid-editor td,
	table.grid-editor th {
		padding: 3px 5px 3px 5px;
		border: 1px solid #CCCCCC; }
		
	table.grid-editor th {
		text-align: left;
		background-color: #EEEEEE; }

	table.grid-editor-list td {
		padding: 0px;
		border-style: none;
		border-width: 0px; }
		
	input.grid-editor-name,
	input.grid-editor-phone {
		width: 100px; }

	textarea.grid-editor-desc {
		width: 200px; }
		
	input.record-gridEditor-date-day,
	input.record-gridEditor-date-month {
		width: 20px; }
		
	input.record-gridEditor-date-year {
		width: 40px; }
		
	div.grid-editor-past-note {
		background-color: #FFFFDD;
		border: 1px solid #EEDD66;
		margin-top: 3px;
		margin-bottom: 0px;
		padding: 3px; }

	</style>
	
</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:gridEditor
			id="grid"
			rows="#{GridEditorTestBean.rows}"
			rowGroups="#{GridEditorTestBean.rowGroups}"
			columns="#{GridEditorTestBean.columns}"
			values="#{GridEditorTestBean.values}"
			fieldTypes="#{GridEditorTestBean.fieldTypes}" />
			
		<h:commandButton value="Test submit" />
		<h:commandButton value="Test error" action="#{GridEditorTestBean.testError}" />

	</h:form>
</f:view>
</body>
</html>