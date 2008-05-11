<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Timeline" /></t:htmlTag>

<t:htmlTag value="div" styleClass="database-graph-options">
	<t:htmlTag value="table" style="border-collapse: collapse;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px 15px 0px 0px;">
			<h:outputText value="Display variable" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:selectOneMenu value="#{TimelineBean.chosenAttribute}" id="tLAttributes">
				<f:selectItems value="#{TimelineBean.voyageNumericAttributes}" />
			</h:selectOneMenu>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:commandButton id="showStat" value="#{res.database_search_show}" action="#{TimelineBean.showTimeLine}" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<s:eventLine
	id="eventLine_db"
	graphHeight="200"
	zoomLevel="0"
	offset="1600"
	selectorOffset="1500"
	viewportHeight="#{TimelineBean.viewportHeight}"
	graphs="#{TimelineBean.graphs}"
	events="#{TimelineBean.events}" 
	zoomLevels="#{TimelineBean.zoomLevels}"
	verticalLabels="#{TimelineBean.verticalLabels}" />
	
<t:htmlTag value="div" style="margin-top: 5px;">
	<t:commandButton value="Download timeline data" action="#{TimelineBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>
