<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyages</title>
	<link href="main.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:grid id="voyges"
			columns="#{AdminVoyagesListBean.columns}"
			columns="#{AdminVoyagesListBean.rows}" />
	
		<s:pager id="voyagesPager"
			currentPage="#{AdminVoyagesListBean.currentPage}"
			firstRecord="#{AdminVoyagesListBean.firstRecordIndex}"
			lastRecord="#{AdminVoyagesListBean.lastRecordIndex}"
			pageSize="#{AdminVoyagesListBean.pageSize}" />

	</h:form>
</f:view>
</body>
</html>