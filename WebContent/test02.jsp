<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Test</title>
</head>
<body>
<f:view>
	<h:form id="form">
		<h:inputText value="#{TestBean.a}" /> +
		<h:inputText value="#{TestBean.b}" /> =
		<h:inputText value="#{TestBean.c}" />
		<h:commandButton value="Add" action="#{TestBean.add}" />
	</h:form>
</f:view>
</body>
</html>