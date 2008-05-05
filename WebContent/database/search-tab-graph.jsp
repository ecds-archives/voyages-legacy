<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<t:htmlTag value="div" style="padding: 2px;" rendered="#{GraphsBean.errorPresent || GraphsBean.warningPresent}">
	<t:htmlTag value="div" id="div_error" style="background: red;" rendered="#{GraphsBean.errorPresent}">
		<h:panelGrid columns="3">
			<h:outputText value="#{GraphsBean.errorMessage}" />
			<h:commandButton id="fixError" value="#{res.database_search_fixerror}" action="#{GraphsBean.fixError}" />
			<h:commandButton id="goBackOnError" value="#{res.database_search_back}" action="#{GraphsBean.rollback}" />
		</h:panelGrid>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">
	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 0px 0px;">
	
		<t:htmlTag value="div" style="margin-bottom: 5px; padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold;">
			<h:outputText value="#{res.database_search_chartsetup}" />
		</t:htmlTag>
		
		<t:htmlTag value="table" style="border-collapse: collapse;">
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_graph_type}" />
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 0px">
				<h:selectOneMenu onchange="submit()" style="width: 300px;" value="#{GraphsBean.selectedChart}" disabled="#{GraphsBean.errorPresent}">
					<f:selectItems value="#{GraphsBean.availableCharts}" />
				</h:selectOneMenu>
			</t:htmlTag>

		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_search_xaxisval}" />
			</t:htmlTag>
			
			<t:htmlTag value="td" style="padding: 0px;">
				<h:selectOneMenu style="width: 300px;" value="#{GraphsBean.xaxis}" id="xaxis_select">
					<f:selectItems value="#{GraphsBean.voyageSelectedAttributes}" />
				</h:selectOneMenu>
			</t:htmlTag>
		
		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_search_order}" />
			</t:htmlTag>
		
			<t:htmlTag value="td" style="padding: 0px;">
				<h:selectOneMenu id="stat-select-y" style="width: 300px;" value="#{GraphsBean.order}" id="order_select">
					<f:selectItems value="#{GraphsBean.availableOrders}" />
				</h:selectOneMenu>
			</t:htmlTag>

		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_search_yaxisval}" />
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 0px;">
				<t:htmlTag value="div">
					<h:selectOneMenu style="width: 50px;" disabled="#{GraphsBean.notAggregate}"
						value="#{GraphsBean.selectedAggregate}" id="aggregate_select">
						<f:selectItems value="#{GraphsBean.aggregateFunctions}" />
					</h:selectOneMenu>
					<h:selectOneMenu style="width: 250px;" value="#{GraphsBean.yaxis}" id="yaxis_select">
						<f:selectItems value="#{GraphsBean.voyageNumericAttributes}" />
					</h:selectOneMenu>
				</t:htmlTag>
			</t:htmlTag>

		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td">
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 5px 10px 0px 0px;">

				<h:commandButton
					id="addSeries"
					disabled="#{GraphsBean.errorPresent}"
					value="#{res.database_search_addseries}"
					action="#{GraphsBean.addSeries}" />
					
				<h:outputText value=" " />
					
				<h:commandButton
					id="showGraph"
					value="#{res.database_search_show}"
					disabled="#{GraphsBean.errorPresent}"
					action="#{GraphsBean.showGraph}" />
					
			</t:htmlTag>

		</t:htmlTag>
		</t:htmlTag>
		
	</t:htmlTag>
	<t:htmlTag value="td" style="vertical-align: top;">

		<t:htmlTag value="div" style="padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold;">
			<h:outputText value="#{res.database_search_currentseries}" />
		</t:htmlTag>
	
		<t:htmlTag value="div" rendered="#{GraphsBean.seriesAdded}">
			<h:selectManyCheckbox id="to_remove_check" layout="pageDirection" value="#{GraphsBean.toRemove}">
				<f:selectItems value="#{GraphsBean.series}" />
			</h:selectManyCheckbox>
			<h:commandButton id="removeSeries"
				disabled="#{GraphsBean.errorPresent}"
				value="#{res.database_search_remseries}"
				action="#{GraphsBean.removeSeries}" />
		</t:htmlTag>
		
		<t:htmlTag value="div" rendered="#{!GraphsBean.seriesAdded}">
			<h:outputText value="#{res.database_search_noseriesmsg}" />
		</t:htmlTag>
		
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" style="padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold; margin-top: 10px;">
	<h:outputText value="Graph" />
</t:htmlTag>

<t:htmlTag value="div"
	id="div_warning"
	style="margin: 3px; background: #E2873B; length: 640px;"
	rendered="#{GraphsBean.warningPresent}">
	<h:outputText id="warning_text" value="#{GraphsBean.warningMessage}" />
</t:htmlTag>

<t:htmlTag value="div"
	style="margin-top: 4px; width: 640px; height: 480px; overflow:auto; width: 100%; height: 500px"
	rendered="#{GraphsBean.statReady}">
	<h:graphicImage value="#{GraphsBean.chartPath}" />
</t:htmlTag>

<h:panelGroup rendered="#{GraphsBean.statReady}">
	<h:outputText value="#{res.database_search_width} " />
	<h:inputText value="#{GraphsBean.chartWidth}" style="width: 40px;" />
	<h:outputText value="#{res.database_search_height} " />
	<h:inputText value="#{GraphsBean.chartHeight}" style="width: 40px;" />
	<h:commandButton id="enlargeStat" value="#{res.database_search_changesize}" action="#{GraphsBean.setNewView}" />
</h:panelGroup>

<t:htmlTag rendered="#{GraphsBean.statReady}" value="div">
	<s:simpleButton id="chart-back-button-id" action="#{GraphsBean.prev}" styleClass="chart-back-button"/>
	<s:simpleButton id="chart-forward-button-id" action="#{GraphsBean.next}" styleClass="chart-forward-button"/>
</t:htmlTag>
