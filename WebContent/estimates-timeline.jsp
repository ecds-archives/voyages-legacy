<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div style="background-color: White; height: 500px;">
	<s:eventLine
		id="eventLine"
		graphHeight="400"
		zoomLevel="0"
		offset="1600"
		selectorOffset="1500"
		viewportHeight="#{EstimatesTimelineBean.viewportHeight}"
		graphs="#{EstimatesTimelineBean.graphs}"
		events="#{EstimatesTimelineBean.events}" 
		zoomLevels="#{EstimatesTimelineBean.zoomLevels}"
		verticalLabels="#{EstimatesTimelineBean.verticalLabels}" />
</t:div>