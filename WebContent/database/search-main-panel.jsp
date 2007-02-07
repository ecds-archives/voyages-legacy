<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:panelTabSet id="mainPanelSection" selectedSectionId="#{SearchBean.mainSectionId}">

	<s:panelTab title="#{res.database_search_listing}" sectionId="listing">
		<%@ include file="search-tab-table.jsp" %>
	</s:panelTab>
	
	<s:panelTab title="#{res.database_search_timeline}" sectionId="basic-graph">
		<s:stattab
			styleClass="data-container">
		<%@ include file="search-tab-graph.jsp" %>
		</s:stattab>
	</s:panelTab>

	<s:panelTab title="#{res.database_search_customgraphs}" sectionId="custom-graphs">
		<s:stattab
			styleClass="data-container">
		<%@ include file="search-tab-stat.jsp" %>
		</s:stattab>
	</s:panelTab>

	<s:panelTab title="#{res.database_search_summarystats}" sectionId="basic-statistics">
		<s:stattab>
		<%@ include file="search-tab-basic-stats.jsp" %>
		</s:stattab>
	</s:panelTab>
	
	<s:panelTab title="#{res.database_search_maps}" sectionId="map-ports">
		<s:stattab>
		<%@ include file="search-tab-map.jsp" %>
		</s:stattab>
	</s:panelTab>

</s:panelTabSet>
