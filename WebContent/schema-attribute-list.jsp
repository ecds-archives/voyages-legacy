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
	
		<h:commandLink
			action="#{Switcher.gotoVoyagesGroups}"
			value="Voyages - groups"/> |
		<h:commandLink
			action="#{Switcher.gotoVoyagesCompoundAttributes}"
			value="Voyages - compound atrributes"/> |
		<h:commandLink
			action="#{Switcher.gotoVoyagesAttributes}"
			value="Voyages - attributes"/> |
		<h:commandLink
			action="#{Switcher.gotoSlavesGroups}"
			value="Slaves - groups"/> |
		<h:commandLink
			action="#{Switcher.gotoSlavesCompoundAttributes}"
			value="Slaves - compound atrributes"/> |
		<h:commandLink
			action="#{Switcher.gotoSlavesAttributes}"
			value="Slaves - attributes"/>
	
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
				<h:outputText value="#{attribute.typeUserName}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="List type" />
				</f:facet>
				<h:outputText value="#{attribute.dictionary}" />
			</h:column>
		</h:dataTable>

	</h:form>
</f:view>
</body>
</html>