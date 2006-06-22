<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Groups</title>
	<link href="schema.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form>
	
		<jsp:include page="schema-edit-header.jsp"/>
		
		<div class="content">
		
			<h1><h:outputText value="#{Switcher.pageTitle}" />
			<span class="new">(<h:commandLink action="#{GroupsBean.newGroup}" value="Create new" />)</span></h1>

			<h:dataTable value="#{GroupsBean.groups}" var="group" border="0" cellpadding="0" cellspacing="0" styleClass="grid">
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
						<h:outputText value="Description" />
					</f:facet>
					<h:outputText value="#{group.description}" />
				</h:column>
			</h:dataTable>
			
		</div>

	</h:form>
</f:view>
</body>
</html>