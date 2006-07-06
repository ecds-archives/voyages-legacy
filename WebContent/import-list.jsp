<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Import</title>
</head>
<body>
<f:view>
	<h:form id="form">
	
		<h:dataTable value="#{ImportLog.importLogs}" var="log" border="1" cellpadding="0" cellspacing="0" styleClass="grid">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Started" />
				</f:facet>
				<h:commandLink actionListener="#{ImportLog.openDetail}" action="detail">
					<f:param id="itemId" name="itemId" value="#{log.id}" />
					<h:outputText value="#{log.started}" />
				</h:commandLink>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Finished" />
				</f:facet>
				<h:outputText value="#{log.finished}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Duration" />
				</f:facet>
				<h:outputText value="#{log.duration}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Outcome" />
				</f:facet>
				<h:outputText value="#{log.outcome}" />
			</h:column>
		</h:dataTable>
	
	</h:form>
</f:view>
</body>
</html>