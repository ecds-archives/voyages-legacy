<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Help</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>
	
	<f:param value="index" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title"><img src="../images/help-index-title.png" width="240" height="50" border="0" alt="Demos"></div>
	
</h:form>
</f:view>
</body>
</html>