<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" style="padding: 2px;"
	rendered="#{AdvancedStatisticsTabBean.errorPresent || AdvancedStatisticsTabBean.warningPresent}">
	<t:htmlTag id="div_error" value="td" style="background: red;" rendered="#{AdvancedStatisticsTabBean.errorPresent}">
		<h:panelGrid columns="3">
			<h:outputText value="#{AdvancedStatisticsTabBean.errorMessage}" />
			<h:commandButton id="fixError" value="Fix error" action="#{AdvancedStatisticsTabBean.fixError}" />
			<h:commandButton id="goBackOnError" value="Back" action="#{AdvancedStatisticsTabBean.rollback}" />
		</h:panelGrid>
	</t:htmlTag>

	<t:htmlTag id="div_warning" value="td" style="background: green;"
		rendered="#{AdvancedStatisticsTabBean.warningPresent}">
		<h:outputText id="warning_text" value="#{AdvancedStatisticsTabBean.warningMessage}" />
	</t:htmlTag>
	<t:htmlTag id="div_warning_fillin" value="td"></t:htmlTag>
</t:htmlTag>


<t:htmlTag id="div_table" value="div" style="padding: 2px;">
	<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
		<t:htmlTag id="tr_third" value="tr">
			<t:htmlTag id="td_setup_label" value="th">
				<h:outputText value="Chart setup" />
			</t:htmlTag>

			<t:htmlTag value="th" style="border-left: 2px solid #F1E7C8; margin-bottom: 10px;">
				<h:outputText value="Current series" />
			</t:htmlTag>
		</t:htmlTag>


		<t:htmlTag id="tr_setup" value="tr">
			<t:htmlTag value="td" style="width: 520px; border-bottom: 2px solid #F1E7C8;">
				<h:selectOneRadio
					onchange="submit()"
					value="#{AdvancedStatisticsTabBean.selectedChart}"
					disabled="#{AdvancedStatisticsTabBean.errorPresent}">
					<f:selectItems value="#{AdvancedStatisticsTabBean.availableCharts}" />
				</h:selectOneRadio>
				<t:htmlTag value="div">
					<h:panelGrid columns="3">
						<h:outputText value="X axis value: " />

						<h:selectOneMenu style="width: 300px;" value="#{AdvancedStatisticsTabBean.xaxis}" id="xaxis_select">
							<f:selectItems value="#{AdvancedStatisticsTabBean.voyageSelectedAttributes}" />
						</h:selectOneMenu>

						<h:panelGrid columns="2">
							<h:selectBooleanCheckbox onclick="submit()" disabled="#{AdvancedStatisticsTabBean.errorPresent}"
								value="#{AdvancedStatisticsTabBean.aggregate}" />
							<h:outputText value="Enable aggregate functions" />
						</h:panelGrid>

						<h:outputText value="Order: " />

						<h:selectOneMenu style="width: 300px;" value="#{AdvancedStatisticsTabBean.order}" id="order_select">
							<f:selectItems value="#{AdvancedStatisticsTabBean.availableOrders}" />
						</h:selectOneMenu>

						<t:htmlTag value="div">
						</t:htmlTag>

						<h:outputText value="Y axis value: " />

						<t:htmlTag value="div">
							<h:selectOneMenu style="width: 50px;" disabled="#{AdvancedStatisticsTabBean.notAggregate}"
								value="#{AdvancedStatisticsTabBean.selectedAggregate}" id="aggregate_select">
								<f:selectItems value="#{AdvancedStatisticsTabBean.aggregateFunctions}" />
							</h:selectOneMenu>
							<h:selectOneMenu style="width: 250px;" value="#{AdvancedStatisticsTabBean.yaxis}" id="yaxis_select">
								<f:selectItems value="#{AdvancedStatisticsTabBean.voyageNumericAttributes}" />
							</h:selectOneMenu>
						</t:htmlTag>

						<h:commandButton id="addSeries" disabled="#{AdvancedStatisticsTabBean.errorPresent}" value="Add series"
							action="#{AdvancedStatisticsTabBean.addSeries}" />
					</h:panelGrid>
				</t:htmlTag>
			</t:htmlTag>
			<t:htmlTag value="td"
				style="width: 240px; border-left: 2px solid #F1E7C8; border-bottom: 2px solid #F1E7C8; margin-bottom: 10px;">
				<t:htmlTag value="div" rendered="#{AdvancedStatisticsTabBean.seriesAdded}">
					<h:selectManyCheckbox id="to_remove_check" layout="pageDirection" value="#{AdvancedStatisticsTabBean.toRemove}">
						<f:selectItems value="#{AdvancedStatisticsTabBean.series}" />
					</h:selectManyCheckbox>
					<h:commandButton id="removeSeries" disabled="#{AdvancedStatisticsTabBean.errorPresent}" value="Remove selected"
						action="#{AdvancedStatisticsTabBean.removeSeries}" style="" />
				</t:htmlTag>
				<t:htmlTag value="div" rendered="#{!AdvancedStatisticsTabBean.seriesAdded}">
					<h:outputText value="No series added" />
				</t:htmlTag>
			</t:htmlTag>

		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="div" style="padding-left: 10px; padding-bottom: 5px; padding-top: 5px;">
		<t:htmlTag value="div">
			<h:commandButton style="margin: 3px;" id="showGraph" value="Show"
				disabled="#{AdvancedStatisticsTabBean.errorPresent}" action="#{AdvancedStatisticsTabBean.showGraph}" />
		</t:htmlTag>
		<t:htmlTag value="div" style="margin-top: 4px; overflow:auto; width: 100%; height: 500px"
			rendered="#{AdvancedStatisticsTabBean.statReady}">
			<h:graphicImage value="#{AdvancedStatisticsTabBean.chartPath}" />
		</t:htmlTag>
		<%/* Change size panel */%>
		<h:panelGroup rendered="#{AdvancedStatisticsTabBean.statReady}">
			<h:outputText value="Width: " />
			<h:inputText value="#{AdvancedStatisticsTabBean.chartWidth}" style="width: 40px;" />
			<h:outputText value="Height: " />
			<h:inputText value="#{AdvancedStatisticsTabBean.chartHeight}" style="width: 40px;" />
			<h:commandButton id="enlargeStat" value="Change size" action="#{AdvancedStatisticsTabBean.setNewView}" />
		</h:panelGroup>
	</t:htmlTag>


</t:htmlTag>
