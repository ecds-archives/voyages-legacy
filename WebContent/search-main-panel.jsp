<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<div class="tab-bar-container"><s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
	<s:tab text="Results listing" tabId="table" />
	<s:tab text="Graphs" tabId="timeline" />
	<s:tab text="Advanced statistics" tabId="statistics" />
</s:tabBar></div>

<t:htmlTag value="div" styleClass="message-bar-container">
	<s:messageBar rendered="false" binding="#{SearchBean.messageBar}" />
</t:htmlTag>


<%/* Table tab */%>
<t:htmlTag value="div" styleClass="data-container" rendered="#{SearchBean.tableVisible}">
	<%@ include file="search-tab-table.jsp" %>
</t:htmlTag>

<%/* Statistical tab */%>
<s:stattab rendered="#{SearchBean.timeLineVisible}" query="#{SearchBean.searchParameters}"
		conditionsOut="#{TimeLineResultTabBean.conditions}" componentVisible="#{TimeLineResultTabBean.componentVisible}"
		styleClass="data-container">
	<%@ include file="search-tab-graph.jsp" %>
</s:stattab>

<%/* Advanced stats tab */%>
<s:stattab styleClass="data-container" rendered="#{SearchBean.statisticsVisible}" query="#{SearchBean.searchParameters}"
		conditionsOut="#{AdvancedStatisticsTabBean.conditions}">
	<%@ include file="search-tab-stat.jsp" %>
</s:stattab>
