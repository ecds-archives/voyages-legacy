<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:panelTabSet
	selectedSectionId="table"
	id="mainPanelSection">
	
	<s:panelTab title="#{res.estimates_main_table}" sectionId="table">
		<t:htmlTag value="div" styleClass="results-panel">
			<%@ include file="estimates-tab-table.jsp" %>
		</t:htmlTag>
	</s:panelTab>
	
	<s:panelTab title="#{res.estimates_main_list}" sectionId="listing">
		<t:htmlTag value="div" styleClass="results-panel">
			<%@ include file="estimates-tab-listing.jsp" %>
		</t:htmlTag>
	</s:panelTab>

	<s:panelTab title="#{res.estimates_main_map}" sectionId="map">
		<t:htmlTag value="div" styleClass="results-panel">
			<%@ include file="estimates-tab-map.jsp" %>
		</t:htmlTag>
	</s:panelTab>

	<s:panelTab title="#{res.estimates_main_timeline}" sectionId="timeline">
		<t:htmlTag value="div" styleClass="results-panel">
			<%@ include file="estimates-tab-timeline.jsp" %>
		</t:htmlTag>
	</s:panelTab>

</s:panelTabSet>