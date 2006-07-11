<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Attributes</title>
	<script src="scrolling.js" type="text/javascript" language="javascript"></script>
	<link href="schema.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form id="form">
	
		<input type="hidden" name="scrollPosX">
		<input type="hidden" name="scrollPosY">
		<script type="text/javascript" language="javascript">
		<h:outputText value="#{AttributesBean.scrollToJavaScript}" />
		</script>
	
		<%@ include file="schema-edit-header.jsp" %>
	
		<div class="content">
		
			<h1><h:outputText value="#{Switcher.pageTitle}" /></h1>
				
			<h:dataTable value="#{AttributesBean.attributes}" var="attribute" border="0" cellpadding="0" cellspacing="0" styleClass="grid">
				<h:column>
					<f:facet name="header">
						<h:outputText value="Visible" />
					</f:facet>
					<h:outputText value="#{attribute.visibleText}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Category" />
					</f:facet>
					<h:outputText value="#{attribute.categoryText}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<b><h:outputText value="Name" /></b>
					</f:facet>
					<h:commandLink onclick="saveScrolling('form');" actionListener="#{AttributesBean.editAttribute}" action="edit">
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
					<h:outputText value="#{attribute.typeDisplayName}" />
				</h:column>
				<%--
				<h:column>
					<f:facet name="header">
						<h:outputText value="Description" />
					</f:facet>
					<h:outputText styleClass="description" value="#{attribute.description}" />
				</h:column>
				--%>
				<h:column>
					<f:facet name="header">
						<h:outputText value="In groups" />
					</f:facet>
					<h:outputText escape="false" value="#{attribute.groupsHTML}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Proxied groups" />
					</f:facet>
					<h:outputText escape="false" value="#{attribute.proxiedGroupsHTML}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="In compound attributes" />
					</f:facet>
					<h:outputText escape="false" value="#{attribute.compoundAttributesHTML}" />
				</h:column>
			</h:dataTable>
			
		</div>		

	</h:form>
</f:view>
</body>
</html>