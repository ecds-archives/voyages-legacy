<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Groups</title>
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
		
		<h:commandButton action="#{GroupsBean.newGroup}" value="New" />
		
		<br>
		
		<h:dataTable value="#{GroupsBean.groups}" var="group" border="1">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Name" />
				</f:facet>
				<h:commandLink actionListener="#{GroupsBean.editGroup}" action="edit">
					<f:param id="groupId" name="groupId" value="#{group.id}" />
					<h:outputText value="#{group.name}" />
				</h:commandLink>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Label" />
				</f:facet>
				<h:outputText value="#{group.userLabel}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="List type" />
				</f:facet>
				<h:outputText value="#{group.description}" />
			</h:column>
		</h:dataTable>

	</h:form>
</f:view>
</body>
</html>