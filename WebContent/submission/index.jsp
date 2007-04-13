<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
<h:form id="main">

	<s:gridEditor
		rows="#{SubmissionBean.rows}"
		columns="#{SubmissionBean.columns}"
		values="#{SubmissionBean.values}"/>

	<h:commandButton value="Send" action="#{SubmissionBean.submit}"/>

</h:form>
</f:view>
</body>
</html>