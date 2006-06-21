<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Attributes</title>
</head>
<body>
<f:view>
	<h:form>
	
		<a href="schema-group-list.faces">Groups</a> |
		<a href="schema-compound-attribute-list.faces">Compound attributes</a> |
		<a href="schema-attribute-list.faces">Attributes</a>
		
		<br>
	
		<h:commandLink action="#{AttributesBean.switchToVoyages}" value="Voyages"/> |
		<h:commandLink action="#{AttributesBean.switchToSlaves}" value="Slaves"/>
		
		<br>
		
		<h:dataTable value="#{AttributesBean.attributes}" var="attribute" border="1">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Name" />
				</f:facet>
				<h:commandLink actionListener="#{AttributesBean.editAttribute}" action="edit">
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
					<h:outputText value="Description" />
				</f:facet>
				<h:outputText value="#{attribute.description}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Type" />
				</f:facet>
				<h:outputText value="#{attribute.type}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Dictionary" />
				</f:facet>
				<h:outputText value="#{attribute.dictionary}" />
			</h:column>
		</h:dataTable>

	</h:form>
</f:view>
</body>
</html>