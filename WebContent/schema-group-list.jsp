<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Groups</title>
</head>
<body>
<f:view>
	<h:dataTable value="#{GroupListBean.groups}" var="group">
		<h:column>
			<f:facet name="header">
				<h:outputText value="Name" />
			</f:facet>
				<h:commandLink value="#{group.name}" />
			<h:outputText value="#{group.name}" />
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Label" />
			</f:facet>
			<h:outputText value="#{group.userLabel}" />
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Description" />
			</f:facet>
			<h:outputText value="#{group.description}" />
		</h:column>
	</h:dataTable>
</f:view>
</body>
</html>