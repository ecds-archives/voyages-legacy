<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:sectionGroup
	id="mainPanelSection"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle"
	selectedSectionId="listing">
	
	<s:section title="Result listing" sectionId="listing">
		<%@ include file="search-tab-table.jsp" %>
	</s:section>
	
	<s:section title="Basic graph" sectionId="basic-graph">
		<s:stattab
			styleClass="data-container"
			query="#{SearchBean.searchParameters}"
			conditionsOut="#{TimeLineResultTabBean.conditions}">
		<%@ include file="search-tab-graph.jsp" %>
		</s:stattab>
	</s:section>

	<s:section title="Custom graphs" sectionId="custom-graphs">
		<s:stattab
			styleClass="data-container"
			query="#{SearchBean.searchParameters}"
			conditionsOut="#{AdvancedStatisticsTabBean.conditions}">
		<%@ include file="search-tab-stat.jsp" %>
		</s:stattab>
	</s:section>
	
	<s:section title="Map of ports" sectionId="map-ports">
		<s:stattab
			query="#{SearchBean.searchParameters}"
			conditionsOut="#{MapBean.conditions}">
		<%@ include file="search-tab-map.jsp" %>
		</s:stattab>
	</s:section>
	
</s:sectionGroup>

<%--
<div class="tab-bar-container">
	<s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
		<s:tab text="Results listing" tabId="table" />
		<s:tab text="Graphs" tabId="timeline" />
		<s:tab text="Advanced statistics" tabId="statistics" />
	</s:tabBar>
</div>

<t:htmlTag value="div"
	styleClass="message-bar-container">
	<s:messageBar rendered="false" binding="#{SearchBean.messageBar}" />
</t:htmlTag>

<%/* Table tab */%>
<t:htmlTag value="div"
	styleClass="data-container"
	rendered="#{SearchBean.tableVisible}">
	<%@ include file="search-tab-table.jsp" %>
</t:htmlTag>

<%/* Statistical tab */%>
<s:stattab
		rendered="#{SearchBean.timeLineVisible}"
		query="#{SearchBean.searchParameters}"
		conditionsOut="#{TimeLineResultTabBean.conditions}"
		componentVisible="#{TimeLineResultTabBean.componentVisible}"
		styleClass="data-container">
	<%@ include file="search-tab-graph.jsp" %>
</s:stattab>

<%/* Advanced stats tab */%>
<s:stattab
		styleClass="data-container"
		rendered="#{SearchBean.statisticsVisible}"
		query="#{SearchBean.searchParameters}"
		conditionsOut="#{AdvancedStatisticsTabBean.conditions}">
	<%@ include file="search-tab-stat.jsp" %>
</s:stattab>
--%>
