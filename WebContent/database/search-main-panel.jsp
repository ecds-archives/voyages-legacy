<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:sectionGroup
	id="mainPanelSection"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle"
	selectedSectionId="#{SearchBean.mainSectionId}">
	
	<s:section title="Result listing" sectionId="listing">
		<%@ include file="search-tab-table.jsp" %>
	</s:section>
	
	<s:section title="Timeline" sectionId="basic-graph">
		<s:stattab
			styleClass="data-container">
		<%@ include file="search-tab-graph.jsp" %>
		</s:stattab>
	</s:section>

	<s:section title="Custom graphs" sectionId="custom-graphs">
		<s:stattab
			styleClass="data-container">
		<%@ include file="search-tab-stat.jsp" %>
		</s:stattab>
	</s:section>

	<s:section title="Summary statistics" sectionId="basic-statistics">
		<s:stattab>
		<%@ include file="search-tab-basic-stats.jsp" %>
		</s:stattab>
	</s:section>
	
	<s:section title="Maps" sectionId="map-ports">
		<s:stattab>
		<%@ include file="search-tab-map.jsp" %>
		</s:stattab>
	</s:section>
	
</s:sectionGroup>
