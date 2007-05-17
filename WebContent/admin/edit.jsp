<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Submission</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/grid-editor.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:gridEditor
		rows="#{AdminVoyageBean.rows}"
		columns="#{AdminVoyageBean.columns}"
		values="#{AdminVoyageBean.values}"
		fieldTypes="#{AdminVoyageBean.fieldTypes}"
		rowGroups="#{AdminVoyageBean.rowGroups}"/>

	<br>
	
	<h:commandButton value="< Back" action="back"/>
	<h:commandButton value="Save voyage" action="#{AdminVoyageBean.save}" rendered="#{AdminSubmissionBean.isAdmin}"/>
	<h:commandButton value="Delete voyage" action="delete" rendered="#{AdminSubmissionBean.isAdmin}"/>
</h:form>
</f:view>
</body>
</html>