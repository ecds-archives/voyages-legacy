<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:panelGrid columns="1" style="width: 100%; padding-top: 5px;">

	<%/* Configuration of graph */%>
	<h:panelGroup>
		<t:htmlTag value="div" styleClass="section-inside-group">
			<h:outputText value="Y axis value: " />
			<%/* 
			<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAggregate}" id="tLAggregates">
				<f:selectItems value="#{TimeLineResultTabBean.aggregateFunctions}" />
			</h:selectOneMenu>
			 */%>
			<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
				<f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}" />
			</h:selectOneMenu>
			<h:commandButton id="showStat" value="Show" action="#{TimeLineResultTabBean.showTimeLine}" />
		</t:htmlTag>
	</h:panelGroup>

	<h:panelGroup rendered="true" style="background: white;">
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

	</h:panelGroup>	
	<%/* Graph data */%>
	<% /*
	<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
		<t:htmlTag value="div" styleClass="section-inside-group">
			<t:htmlTag value="div" style="overflow:auto; width: 100%; height: 500px">
				<h:graphicImage value="#{TimeLineResultTabBean.chartPath}" />
			</t:htmlTag>
		</t:htmlTag>
	</h:panelGroup>

	<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
		<t:htmlTag value="div" styleClass="section-inside-footer">
			<h:outputText value="Width: " />
			<h:inputText value="#{TimeLineResultTabBean.chartWidth}" style="width: 40px;" />
			<h:outputText value="Height: " />
			<h:inputText value="#{TimeLineResultTabBean.chartHeight}" style="width: 40px;" />
			<h:commandButton id="enlarge" value="Change size" action="#{TimeLineResultTabBean.setNewView}" />
		</t:htmlTag>
	</h:panelGroup>
	*/ %>
</h:panelGrid>
