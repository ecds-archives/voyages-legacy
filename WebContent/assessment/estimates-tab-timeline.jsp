<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
<t:htmlTag value="tr">
	<t:htmlTag value="td">
		<t:htmlTag value="h1"><h:outputText value="Timeline" /></t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="td" style="text-align: right;">
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:div style="background-color: White;">
	<s:eventLine
		id="eventLine"
		graphHeight="200"
		zoomLevel="0"
		offset="1600"
		selectorOffset="1500"
		eventsColumns="2"
		viewportHeight="#{EstimatesTimelineBean.viewportHeight}"
		graphs="#{EstimatesTimelineBean.graphs}"
		events="#{EstimatesTimelineBean.events}" 
		zoomLevels="#{EstimatesTimelineBean.zoomLevels}"
		verticalLabels="#{EstimatesTimelineBean.verticalLabels}" />
</t:div>

<t:htmlTag value="div" style="margin-top: 5px;">
	<t:commandButton value="Save timeline data" action="#{EstimatesTimelineBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>
