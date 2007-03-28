<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Timeline" /></t:htmlTag>

<t:htmlTag value="div" styleClass="database-graph-options">
	<t:htmlTag value="table" style="border-collapse: collapse;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:outputText value="#{res.database_search_yaxisval} " />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
				<f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}" />
			</h:selectOneMenu>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:commandButton id="showStat" value="#{res.database_search_show}" action="#{TimeLineResultTabBean.showTimeLine}" />
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
	viewportHeight="#{TimeLineResultTabBean.viewportHeight}"
	graphs="#{TimeLineResultTabBean.graphs}"
	events="#{TimeLineResultTabBean.events}" 
	zoomLevels="#{TimeLineResultTabBean.zoomLevels}"
	verticalLabels="#{TimeLineResultTabBean.verticalLabels}" />
