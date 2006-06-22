<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Group</title>
	<script src="select-and-order.js" type="text/javascript" language="javascript"></script>
</head>
<body>
<f:view>
	<h:form>

		<b>Name</b><br>
		<h:inputText value="#{GroupsBean.groupName}" /><br>

		<b>User label</b><br>
		<h:inputText value="#{GroupsBean.groupUserLabel}" /><br>

		<b>Description</b><br>
		<h:inputTextarea value="#{GroupsBean.groupDescription}" /><br>

		<b>Attributes</b><br>
		<s:selectAndOrder
			sortable="false"
			availableItems="#{GroupsBean.availableAttributes}"
			selectedItems="#{GroupsBean.groupAttributes}" /><br>
			
		<b>Compound attributes</b><br>
		<s:selectAndOrder
			sortable="false"
			availableItems="#{GroupsBean.availableCompoundAttributes}"
			selectedItems="#{GroupsBean.groupCompoundAttributes}" /><br>

		<h:commandButton value="Save" action="#{GroupsBean.saveGroup}" />
		<h:commandButton value="Back" action="#{GroupsBean.cancelEdit}" />
		<h:outputText value="#{GroupsBean.errorText}" />
		
	</h:form>
</f:view>
</body>
</html>