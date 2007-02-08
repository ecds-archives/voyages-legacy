<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:panelTabSet id="mainPanelSection" selectedSectionId="#{SearchBean.mainSectionId}">

	<s:panelTab title="#{res.database_search_listing}" sectionId="listing">
		<t:htmlTag value="div" styleClass="search-panel">
			<%@ include file="search-tab-table.jsp" %>
		</t:htmlTag>
	</s:panelTab>
	
	<s:panelTab title="#{res.database_search_timeline}" sectionId="basic-graph">
		<t:htmlTag value="div" styleClass="search-panel">
			<%@ include file="search-tab-graph.jsp" %>
		</t:htmlTag>
	</s:panelTab>

	<s:panelTab title="#{res.database_search_customgraphs}" sectionId="custom-graphs">
		<t:htmlTag value="div" styleClass="search-panel">
			<%@ include file="search-tab-stat.jsp" %>
		</t:htmlTag>
	</s:panelTab>

	<s:panelTab title="#{res.database_search_summarystats}" sectionId="basic-statistics">
		<t:htmlTag value="div" styleClass="search-panel">
			<%@ include file="search-tab-basic-stats.jsp" %>
		</t:htmlTag>
	</s:panelTab>
	
	<s:panelTab title="#{res.database_search_maps}" sectionId="map-ports">
		<t:htmlTag value="div" styleClass="search-panel">
			<%@ include file="search-tab-map.jsp" %>
		</t:htmlTag>
	</s:panelTab>

</s:panelTabSet>