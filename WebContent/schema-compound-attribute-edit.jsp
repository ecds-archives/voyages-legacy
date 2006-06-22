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
	<link href="schema.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form>
	
		<jsp:include page="schema-edit-header.jsp"/>

		<div class="content">

			<h1><h:outputText value="#{Switcher.pageTitle}" /></h1>

			<h:outputText value="#{CompoundAttributesBean.errorText}" rendered="#{CompoundAttributesBean.error}" styleClass="error" />
	
			<h2>Basic information</h2>

			<div class="label">Name</div>
			<div class="field"><h:inputText style="width: 200px;" value="#{CompoundAttributesBean.attributeName}" /></div>
	
			<div class="label">User label</div>
			<div class="field"><h:inputText style="width: 200px;" value="#{CompoundAttributesBean.attributeUserLabel}" /></div>
	
			<div class="label">Description</div>
			<div class="field"><h:inputTextarea style="width: 300px;" value="#{CompoundAttributesBean.attributeDescription}" /></div>
	
			<h2>Attributes</h2>

			<div class="field"><s:selectAndOrder
				sortable="false"
				availableItems="#{CompoundAttributesBean.availableAttributes}"
				selectedItems="#{CompoundAttributesBean.attributeAttributes}" /></div>
				
			<h:commandButton value="Save" action="#{CompoundAttributesBean.saveAttribute}" />
			<h:commandButton value="Back" action="#{CompoundAttributesBean.cancelEdit}" />
			
		</div>
		
	</h:form>
</f:view>
</body>
</html>