<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Compound attributes</title>
	<script src="scrolling.js" type="text/javascript" language="javascript"></script>
	<link href="schema.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form>
	
		<%@ include file="schema-edit-header.jsp" %>
		
		<div class="content">
		
			<h1><h:outputText value="#{Switcher.pageTitle}" />
			<span class="new">(<h:commandLink action="#{CompoundAttributesBean.newAttribute}" value="Create new" />)</span></h1>
		
			<h:dataTable value="#{CompoundAttributesBean.attributes}" var="attribute" border="0" cellpadding="0" cellspacing="0" styleClass="grid">
				<h:column>
					<f:facet name="header">
						<h:outputText value="Name" />
					</f:facet>
					<h:commandLink actionListener="#{CompoundAttributesBean.editAttribute}" action="edit">
						<f:param id="attributeId" name="attributeId" value="#{attribute.id}" />
						<h:outputText value="#{attribute.name}" />
					</h:commandLink>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Label" />
					</f:facet>
					<h:outputText value="#{attribute.userLabel}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Type" />
					</f:facet>
					<h:outputText value="#{attribute.typeUserName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Attributes" />
					</f:facet>
					<h:outputText value="#{group.attributesCount}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Description" />
					</f:facet>
					<h:outputText styleClass="description" value="#{attribute.dictionary}" />
				</h:column>
			</h:dataTable>
	
		</div>

	</h:form>
</f:view>
</body>
</html>