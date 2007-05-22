<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<t:htmlTag value="h1"><h:outputText value="Summary statistics" /></t:htmlTag>

<s:simpleTable
	id="stat-table"
	rows="#{StatisticBean.statisticElements}"/>

<t:htmlTag value="div" style="margin-top: 5px;">
	<t:commandButton value="Save table data" action="#{StatisticBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>