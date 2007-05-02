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
</head>
<body>
<f:view>
<h:form id="main">

	<h1>New submission - step 2</h1>
	
	<s:gridEditor
		rows="#{SubmissionBean.rows}"
		columns="#{SubmissionBean.columns}"
		values="#{SubmissionBean.values}"
		fieldTypes="#{SubmissionBean.fieldTypes}"/>

	<h:commandButton value="Next >" action="#{SubmissionBean.submitVoyage}"/>

</h:form>
</f:view>
</body>
</html>