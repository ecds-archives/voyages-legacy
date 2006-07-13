<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:panelGrid columns="1" style="width: 100%; background-color: #F1E7C8; padding-top: 5px;">

	<%/* Configuration of graph */%>
	<h:panelGroup style="margin-bottom: 5px; margin-top: 5px;">
		<h:outputText value="Y axis value: " />
		<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAggregate}" id="tLAggregates">
			<f:selectItems value="#{TimeLineResultTabBean.aggregateFunctions}" />
		</h:selectOneMenu>
		<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
			<f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}" />
		</h:selectOneMenu>
		<h:commandButton id="showStat" value="Show" action="#{TimeLineResultTabBean.showTimeLine}" />
	</h:panelGroup>

	<%/* Graph data */%>
	<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
		<t:htmlTag value="div" style="margin-top: 5px;margin-bottom: 5px; border-top: 2px solid #D6A51A;margin-top: 5px;">
			<t:htmlTag value="div" style="overflow:auto; width: 100%; height: 500px">
				<h:graphicImage value="#{TimeLineResultTabBean.chartPath}" />
			</t:htmlTag>
		</t:htmlTag>
	</h:panelGroup>

	<%/* Change size panel */%>
	<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
		<h:outputText value="Width: " />
		<h:inputText value="#{TimeLineResultTabBean.chartWidth}" style="width: 40px;" />
		<h:outputText value="Height: " />
		<h:inputText value="#{TimeLineResultTabBean.chartHeight}" style="width: 40px;" />
		<h:commandButton id="enlarge" value="Change size" action="#{TimeLineResultTabBean.setNewView}" />
	</h:panelGroup>
	
</h:panelGrid>
