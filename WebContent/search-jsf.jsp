<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Search</title>
	<link href="search-jsf.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<div class="main-title">Trans-Atlantic Slave Trade</div>
	<h:form id="form">

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td class="side-panel">

				<div style="margin: 10px 0px 5px 10px; color: White;">Add condition to query</div>

				<div style="margin: 0px 0px 5px 10px;">
					<h:selectOneMenu onchange="form.submit();" value="#{SearchBean.selectedGroupId}" style="border: 0px; width: 276px; padding: 2px;">
						<f:selectItems value="#{SearchBean.voyageAttributeGroups}" />
					</h:selectOneMenu>
				</div>

				<div style="margin: 0px 0px 5px 10px;"><h:commandButton action="#{SearchBean.listAttributes}" value="Show" /></div>

				<div style="margin: 0px 0px 5px 10px;"><h:selectOneMenu value="#{SearchBean.selectedAtttibuteId}" style="border: 0px; width: 276px; padding: 2px;">
					<f:selectItems value="#{SearchBean.voyageAttributes}" />
				</h:selectOneMenu></div>

				<div style="margin: 0px 0px 5px 10px;"><h:commandButton action="#{SearchBean.addQueryCondition}" value="Add" /></div>

				<div class="side-panel-section side-panel-section-query-builder">Current query</div>

				<s:queryBuilder query="#{SearchBean.workingQuery}" />

				<div style="margin: 10px 0px 5px 10px;"><h:commandButton action="#{SearchBean.search}" value="Search" /></div>

				<div class="side-panel-section side-panel-section-history">History</div>

				<s:historyList onDelete="#{SearchBean.historyItemDelete}" onRestore="#{SearchBean.historyItemRestore}"
					history="#{SearchBean.history}" /></td>
				<td class="main-panel">

				<div class="tab-bar-container">
					<s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
						<s:tab text="Results listing" tabId="table" />
						<s:tab text="Graphs" tabId="timeline" />
						<s:tab text="Advanced statistics" tabId="statistics" />
					</s:tabBar>
				</div>

				<% /* Table tab */ %>
				<t:htmlTag value="div" styleClass="data-container" rendered="#{SearchBean.tableVisible}">

					<% /* Table with results */ %>
					<s:tabletab rendered="#{TableResultTabBean.resultsMode}" query="#{SearchBean.currentConditions}"
						conditionsOut="#{TableResultTabBean.conditions}" results="#{TableResultTabBean.results}"
						populatedAttributes="#{TableResultTabBean.populatedAttributes}"
						componentVisible="#{TableResultTabBean.componentVisible}">
						<s:resultscroll resultFirst="#{TableResultTabBean.current}" 
										resultLast="#{TableResultTabBean.resultSize}"
										resultSize="#{TableResultTabBean.numberOfResults}">
							<h:commandButton id="prev_pack" value="Previous result set" action="#{TableResultTabBean.prev}" />
							<h:commandButton id="next_pack" value="Next result set" action="#{TableResultTabBean.next}" />
							<h:commandButton id="configureButton" value="Configure table" action="#{TableResultTabBean.configurationMode}" />
						</s:resultscroll>
					</s:tabletab>

					<% /* Configuration of table */ %>
					<t:htmlTag value="div" rendered="#{TableResultTabBean.configurationMode}">
					
						<% /* Groups */ %>
						<t:htmlTag styleClass="configDiv" style="width: 594px;" value="div">
							<h:outputText style="margin-left: 3px;" id="config_label1" value="Choose group of attributes:" />
							<t:htmlTag value="br" />
							<h:selectOneMenu style="margin: 0px 0px 5px 10px;width: 200px;" value="#{TableResultTabBean.selectedGroupSet}"
								id="configure_groupSetCombo" onchange="submit()">
								<f:selectItems value="#{TableResultTabBean.availableGroupSets}" />
							</h:selectOneMenu>
						</t:htmlTag>

						<% /* Configuration of visible attributes */ %>
						<h:panelGrid styleClass="configTable" id="configure_groupAttrs" columns="3">

							<% /* Available attributes in current group */ %>
							<h:panelGrid styleClass="config" columns="1" id="configure_groupAttrsPanel">
								<h:outputText id="config_label2" value="Available attributes:" />
								<h:selectOneListbox style="width: 200px" id="configure_availAttributes" size="10"
									value="#{TableResultTabBean.selectedAttributeToAdd}">
									<f:selectItems value="#{TableResultTabBean.availableAttributes}" />
								</h:selectOneListbox>
							</h:panelGrid>

							<% /* Buttons -> and -< */ %>
							<h:panelGroup id="configure_buttonsAddRem">
								<h:panelGrid id="cinfigure_buttonsAddRemGrid">
									<h:commandButton style="width: 75px" id="configure_AddAttrButton" value="->"
										action="#{TableResultTabBean.addSelectedAttributeToList}" />
									<h:commandButton style="width: 75px" id="configure_RemAttrButton" value="<-"
										action="#{TableResultTabBean.remSelectedAttributeFromList}" />
								</h:panelGrid>
							</h:panelGroup>

							<% /* Currently set attributes */ %>
							<h:panelGrid columns="2" id="configure_currentAttrsPanelMain">
								
								<% /* List of attributes */ %>
								<h:panelGrid columns="1" id="configure_currentAttrsPanel">
									<h:outputText id="config_label3" value="Current attributes in talbe:" />
									<h:selectOneListbox style="width: 200px" id="configure_visibleAttributes"
										value="#{TableResultTabBean.selectedAttributeAdded}" size="10">
										<f:selectItems value="#{TableResultTabBean.visibleAttributes}" />
									</h:selectOneListbox>
								</h:panelGrid>
								
								<% /* Buttons - move up/down */ %>
								<h:panelGrid id="configure_upDownPanel" columns="1">
									<h:commandButton style="width: 75px" id="configure_UpAttrButton" value="Move up"
										action="#{TableResultTabBean.moveAttrUp}" />
									<h:commandButton style="width: 75px" id="configure_DownAttrButton" value="Move down"
										action="#{TableResultTabBean.moveAttrDown}" />
								</h:panelGrid>
							</h:panelGrid>
						</h:panelGrid>
						
						<% /* Results per page config */ %>
						<t:htmlTag styleClass="configDiv" style="width: 594px; height: 25px;" value="div">							
							<h:outputText style="margin-left: 3px; margin-top: 3px;" value="Results per page: " />
							<h:inputText style="margin-top: 3px; width: 40px;" value="#{TableResultTabBean.step}" />
						</t:htmlTag>
						
						<% /* Apply button part */ %>
						<t:htmlTag styleClass="configDiv" style="width: 594px; height: 25px;" value="div">							
							<h:commandButton style="margin-top: 5px; margin-left: 4px;" id="configure_applyConfigButton"
								value="Apply configuration" action="#{TableResultTabBean.resultsMode}" />
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag> 
				
				<% /* Statistical tab */ %>
				<s:stattab rendered="#{SearchBean.timeLineVisible}" 
						query="#{SearchBean.currentConditions}"
						conditionsOut="#{TimeLineResultTabBean.conditions}" 
						componentVisible="#{TimeLineResultTabBean.componentVisible}"
						styleClass="data-container">
					<h:panelGrid columns="1">
					
						<% /* Configuration of graph */ %>
						<h:panelGroup>
							<h:outputText value="Y axis value: " />
							<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAggregate}" id="tLAggregates">
								<f:selectItems value="#{TimeLineResultTabBean.aggregateFunctions}" />
							</h:selectOneMenu>
							<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
								<f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}" />
							</h:selectOneMenu>
							<h:commandButton id="show_stat" value="Show" action="#{TimeLineResultTabBean.showTimeLine}" />
						</h:panelGroup>
						
						<% /* Graph data */ %>
						<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
						  <t:htmlTag value="div" style="overflow:auto; width: 640px; height: 480px">
							<h:graphicImage value="#{TimeLineResultTabBean.chartPath}"/>
						  </t:htmlTag>
						</h:panelGroup>
						
						<% /* Change size panel */ %>
						<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
							<h:outputText value="Width: "/>
							<h:inputText value="#{TimeLineResultTabBean.chartWidth}" style="width: 40px;"/>
							<h:outputText value="Height: "/>
							<h:inputText value="#{TimeLineResultTabBean.chartHeight}" style="width: 40px;"/>
							<h:commandButton id="enlarge" value="Change size" action="#{TimeLineResultTabBean.setNewView}"/>
						</h:panelGroup>
					</h:panelGrid>
				</s:stattab>
				
				<s:stattab styleClass="data-container"
						rendered="#{SearchBean.statisticsVisible}"
						query="#{SearchBean.currentConditions}"
						conditionsOut="#{AdvancedStatisticsTabBean.conditions}">
					<t:htmlTag value="div" style="background: red;" rendered="#{AdvancedStatisticsTabBean.errorPresent}">
						<h:panelGrid columns="3">
							<h:outputText value="#{AdvancedStatisticsTabBean.errorMessage}"/>	
							<h:commandButton id="fixError" value="#Fix error" action="#{AdvancedStatisticsTabBean.fixError}"/>
							<h:commandButton id="goBackOnError" value="Back" action="#{AdvancedStatisticsTabBean.rollback}"/>
						</h:panelGrid>
					</t:htmlTag>
					<h:panelGrid columns="2">
						<h:outputText value="Chart setup: "/>
						<t:htmlTag value="div">
							<h:outputText value="Current series: " rendered="#{AdvancedStatisticsTabBean.seriesAdded}"/>
						</t:htmlTag>
					    <t:htmlTag value="div" styleClass="advancedStatConfigLeftDiv">
					    	<h:selectOneRadio onchange="submit()" 
					    					  value="#{AdvancedStatisticsTabBean.selectedChart}">
								<f:selectItems value="#{AdvancedStatisticsTabBean.availableCharts}"/>
							</h:selectOneRadio>
							<t:htmlTag value="div">
								<h:panelGrid columns="3">
									<h:outputText value="X axis value: "/>
									 
									<h:selectOneMenu style="width: 150px;" value="#{AdvancedStatisticsTabBean.xaxis}" id="xaxis_select">
										<f:selectItems value="#{AdvancedStatisticsTabBean.voyageSelectedAttributes}"/>
									</h:selectOneMenu>
									
									<h:panelGrid columns="2">
										<h:selectBooleanCheckbox onclick="submit()" value="#{AdvancedStatisticsTabBean.aggregate}"/>
										<h:outputText value="Enable aggregate functions"/>
									</h:panelGrid>													
							
									<h:outputText value="Order: "/>
						
									<h:selectOneMenu style="width: 150px;" value="#{AdvancedStatisticsTabBean.order}" id="order_select">
										<f:selectItems value="#{AdvancedStatisticsTabBean.availableOrders}"/>
									</h:selectOneMenu>
						
									<t:htmlTag value="div">
									</t:htmlTag>
							
									<h:outputText value="Y axis value: "/>
									
									<t:htmlTag value="div">
										<h:selectOneMenu style="width: 50px;" disabled="#{AdvancedStatisticsTabBean.notAggregate}" value="#{AdvancedStatisticsTabBean.selectedAggregate}" id="aggregate_select">
											<f:selectItems value="#{AdvancedStatisticsTabBean.aggregateFunctions}"/>
										</h:selectOneMenu>
										<h:selectOneMenu style="width: 100px;" value="#{AdvancedStatisticsTabBean.yaxis}" id="yaxis_select">
											<f:selectItems value="#{AdvancedStatisticsTabBean.voyageNumericAttributes}"/>
										</h:selectOneMenu>
									</t:htmlTag>
									
									<h:commandButton id="addSeries" value="Add series" action="#{AdvancedStatisticsTabBean.addSeries}"/>
								</h:panelGrid>
							</t:htmlTag>	
						</t:htmlTag>
						<t:htmlTag value="div" styleClass="advancedStatConfigRightDiv">
							<t:htmlTag value="div" rendered="#{AdvancedStatisticsTabBean.seriesAdded}">
								<h:selectManyCheckbox id="to_remove_check" 
										layout="pageDirection"
										value="#{AdvancedStatisticsTabBean.toRemove}">
									<f:selectItems value="#{AdvancedStatisticsTabBean.series}"/>
								</h:selectManyCheckbox>
								<h:commandButton id="removeSeries" value="Remove selected" action="#{AdvancedStatisticsTabBean.removeSeries}"/>
							</t:htmlTag>
						</t:htmlTag>
						
						<t:htmlTag value="div">
							<h:commandButton style="margin: 3px;" id="showGraph" value="Show" action="#{AdvancedStatisticsTabBean.showGraph}"/>						
							<t:htmlTag value="div" 
										style="overflow:auto; width: 640px; height: 480px"
										rendered="#{AdvancedStatisticsTabBean.statReady}">
								<h:graphicImage value="#{AdvancedStatisticsTabBean.chartPath}"/>
							</t:htmlTag>
							
							<% /* Change size panel */ %>
							<h:panelGroup rendered="#{AdvancedStatisticsTabBean.statReady}">
								<h:outputText value="Width: "/>
								<h:inputText value="#{AdvancedStatisticsTabBean.chartWidth}" style="width: 40px;"/>
								<h:outputText value="Height: "/>
								<h:inputText value="#{AdvancedStatisticsTabBean.chartHeight}" style="width: 40px;"/>
								<h:commandButton id="enlargeStat" value="Change size" action="#{AdvancedStatisticsTabBean.setNewView}"/>
							</h:panelGroup>
						</t:htmlTag>
						
					</h:panelGrid>
				</s:stattab>
				
				</td>
			</tr>
		</table>

	</h:form>
</f:view>
</body>
</html>
