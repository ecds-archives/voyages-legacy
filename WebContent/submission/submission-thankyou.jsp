<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission completed</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Submission completed</h1>
	
	<br>
	
	<div style="width: 400px;">
	The submission process has been completed. Your submission
	has been stored in the system, and it soon reviewed.
	</div>
	
	<h:commandLink value="Submit next request" action="#{SubmissionBean.submitNext}"/>
	<br>
	<h:commandLink value="Logout" action="#{SubmissionBean.logout}"/>

</h:form>
</f:view>
</body>
</html>