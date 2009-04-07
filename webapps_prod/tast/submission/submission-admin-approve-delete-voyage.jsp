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
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
	<link href="../admin/tabs.css" rel="stylesheet" type="text/css">
	<link href="../admin/edit.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Confirm operation</h1>
	<div style="width: 400px;">
	Approving this request will cause deletion of chosen voyage.<br>
	Are you sure you want to continue?
	</div>
	
	<h:commandButton value="< No, go back" action="back"/>
	<h:commandButton value="Yes, proceed >" action="#{AdminVoyageBean.delete}"/>

</h:form>
</f:view>
</body>
</html>