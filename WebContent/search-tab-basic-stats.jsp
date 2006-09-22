<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<t:htmlTag value="div" style="padding: 10px; width: 100%;">
	<t:htmlTag value="b">
		<h:outputText value="Summary statistics"/>
	</t:htmlTag>
	<h:dataTable var="statEl" value="#{StatisticBean.statisticElements}" 
					styleClass="basic-stat-tab">
		<h:column>
			<f:facet name="header">
				<h:outputText value=""/>
			</f:facet>
			<t:htmlTag value="b">
				<h:outputText value="#{statEl.name}"/>
			</t:htmlTag>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Total"/>
			</f:facet>
			<h:outputText value="#{statEl.total}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Number of voyages in sample"/>
			</f:facet>
			<h:outputText value="#{statEl.sampleTotal}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Average"/>
			</f:facet>
			<h:outputText value="#{statEl.avrg}"/>
		</h:column>
	</h:dataTable>
</t:htmlTag>