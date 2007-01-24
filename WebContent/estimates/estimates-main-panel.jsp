<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:sectionGroup
	selectedSectionId="table"
	id="mainPanelSection"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle">
	
	<s:section title="Table" sectionId="table">
		<%@ include file="estimates-table.jsp" %>
	</s:section>
	
	<s:section title="Listing" sectionId="listing">
		<%@ include file="estimates-listing.jsp" %>
	</s:section>

	<s:section title="Map" sectionId="map">
		<%@ include file="estimates-map.jsp" %>
	</s:section>

	<s:section title="Timeline" sectionId="timeline">
		<%@ include file="estimates-timeline.jsp" %>
	</s:section>

</s:sectionGroup>