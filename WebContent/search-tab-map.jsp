<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:outputText value="#{MapBean.mapPath}" />
<h:graphicImage value="#{MapBean.mapPath}" />
<%--
<h:dataTable value="#{MapBean.ports}" var="group">
	<h:column>
		<f:facet name="header">
			<h:outputText value="Name" />
		</f:facet>
		<h:outputText value="#{group.label}" />
	</h:column>

	<h:column>
		<f:facet name="header">
			<h:outputText value="X" />
		</f:facet>
		<h:outputText value="#{group.x}" />
	</h:column>

	<h:column>
		<f:facet name="header">
			<h:outputText value="Y" />
		</f:facet>
		<h:outputText value="#{group.y}" />
	</h:column>
	
		<h:column>
		<f:facet name="header">
			<h:outputText value="Type" />
		</f:facet>
		<h:outputText value="#{group.portType}" />
	</h:column>

</h:dataTable>
--%>