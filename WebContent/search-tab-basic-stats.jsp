<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<t:htmlTag value="div" style="padding: 10px; width: 100%;">
	<s:simpleTable id="stat-table" rows="#{StatisticBean.statisticElements}"/>

<%/*	<h:dataTable var="statEl" value="#{StatisticBean.statisticElements}" 
					styleClass="basic-stat-tab"
					headerClass="search-simple-stat-h_c1,search-simple-stat-h_c2,search-simple-stat-h_c3,search-simple-stat-h_c4"
					columnClasses="search-simple-stat-r_c1,search-simple-stat-r_c2,search-simple-stat-r_c3,search-simple-stat-r_c4">
		<h:column>		
			<f:facet name="header">
				<f:attribute name="class" value="fjsdklfjlds"/>
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
	*/%>
</t:htmlTag>