<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Compound attribute</title>
	<script src="select-and-order.js" type="text/javascript" language="javascript"></script>
</head>
<body>
<f:view>
	<h:form>

		<b>Name</b><br>
		<h:inputText value="#{CompoundAttributesBean.attributeName}" /><br>

		<b>User label</b><br>
		<h:inputText value="#{CompoundAttributesBean.attributeUserLabel}" /><br>

		<b>Description</b><br>
		<h:inputTextarea value="#{CompoundAttributesBean.attributeDescription}" /><br>

		<b>Attributes</b><br>
		<s:selectAndOrder
			sortable="false"
			availableItems="#{CompoundAttributesBean.availableAttributes}"
			selectedItems="#{CompoundAttributesBean.attributeAttributes}" /><br>
			
		<h:commandButton value="Save" action="#{CompoundAttributesBean.saveAttribute}" />
		<h:commandButton value="Back" action="#{CompoundAttributesBean.cancelEdit}" />
		<h:outputText value="#{CompoundAttributesBean.errorText}" />
		
	</h:form>
</f:view>
</body>
</html>