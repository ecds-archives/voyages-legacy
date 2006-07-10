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
<script src="tooltip.js" type="text/javascript" language="javascript"></script>
</head>
<body>
<f:view>
	<div class="main-title">Trans-Atlantic Slave Trade</div>
	<h:form id="form">

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td class="side-panel">

				<div style="margin: 10px 0px 5px 10px; color: White;">Add condition to query</div>

				<div style="margin: 0px 0px 5px 10px;"><h:selectOneMenu onchange="form.submit();"
					value="#{SearchBean.selectedGroupId}" style="border: 0px; width: 276px; padding: 2px;">
					<f:selectItems value="#{SearchBean.voyageAttributeGroups}" />
				</h:selectOneMenu></div>

				<div style="margin: 0px 0px 5px 10px;"><h:selectOneMenu value="#{SearchBean.selectedAtttibuteId}"
					style="border: 0px; width: 276px; padding: 2px;">
					<f:selectItems value="#{SearchBean.voyageAttributes}" />
				</h:selectOneMenu></div>

				<div style="margin: 0px 0px 5px 10px;"><h:commandButton action="#{SearchBean.addQueryCondition}" value="Add" /></div>

				<s:expandableBox text="Current query">
					<s:queryBuilder query="#{SearchBean.workingQuery}" />
				</s:expandableBox>

				<div style="margin: 10px 0px 5px 10px;"><h:commandButton action="#{SearchBean.search}" value="Search" /></div>

				<s:expandableBox text="History">
					<s:historyList onDelete="#{SearchBean.historyItemDelete}" onRestore="#{SearchBean.historyItemRestore}"
						onPermlink="#{SearchBean.historyItemPermlink}" history="#{SearchBean.history}" />
				</s:expandableBox></td>
				<td class="main-panel">

				<div class="tab-bar-container"><s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
					<s:tab text="Results listing" tabId="table" />
					<s:tab text="Graphs" tabId="timeline" />
					<s:tab text="Advanced statistics" tabId="statistics" />
				</s:tabBar></div>

				<div class="message-bar-container"><s:messageBar rendered="false" binding="#{SearchBean.messageBar}" /></div>

				<%/* Table tab */

					%> <t:htmlTag value="div" styleClass="data-container" rendered="#{SearchBean.tableVisible}">

					<%/* Table with results */

						%>
					<s:tabletab onclick="#{TableResultTabBean.showDetails}" rendered="#{TableResultTabBean.resultsMode}"
						query="#{SearchBean.searchParameters}" conditionsOut="#{TableResultTabBean.conditions}"
						data="#{TableResultTabBean.data}" componentVisible="#{TableResultTabBean.componentVisible}"
						sortChanged="#{TableResultTabBean.sortChanged}" style="overflow:auto;" />

					<t:htmlTag value="div" style="background-color: #DDDDDD; padding: 5px;"
						rendered="#{TableResultTabBean.resultsMode}">
						<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
							<t:htmlTag value="tr">
								<t:htmlTag value="td" style="padding: 0px;">
									<h:commandLink style="font-weight: bold; text-decoration: none;" value="< Previous page"
										action="#{TableResultTabBean.prev}" />
									<h:outputText value=" | " />
									<h:commandLink style="font-weight: bold; text-decoration: none;" value="Next page >"
										action="#{TableResultTabBean.next}" />
									<h:outputText value=" | " />
									<h:outputText value="Showing" />
									<h:outputText value="#{TableResultTabBean.firstDisplayed}" />
									<h:outputText value="-" />
									<h:outputText value="#{TableResultTabBean.lastDisplayed}" />
									<h:outputText value="out of" />
									<h:outputText value="#{TableResultTabBean.totalRows}" />
									<h:outputText value=" | Show " />
									<h:selectOneMenu onchange="submit()" value="#{TableResultTabBean.step}">
										<f:selectItem itemValue="10" itemLabel="10" />
										<f:selectItem itemValue="25" itemLabel="25" />
										<f:selectItem itemValue="50" itemLabel="50" />
										<f:selectItem itemValue="100" itemLabel="100" />
										<f:selectItem itemValue="all" itemLabel="all" />
									</h:selectOneMenu>
									<h:outputText value="  results per page." />
								</t:htmlTag>
								<t:htmlTag value="td"
									style="padding-left: 0px; padding-bottom: 0px; padding-top: 0px; padding-right: 5px; text-align: right">
									<h:commandLink value="Preferences" action="#{TableResultTabBean.configurationMode}" />
								</t:htmlTag>
							</t:htmlTag>
						</t:htmlTag>
					</t:htmlTag>

					<t:htmlTag value="div" rendered="#{TableResultTabBean.detailMode}">
						<h:commandLink id="back_res_upper" value="Back to results" action="#{TableResultTabBean.resultsMode}" />
						<s:voyageDetail data="#{TableResultTabBean.detailData}" />
						<h:commandLink id="back_res_lower" value="Back to results" action="#{TableResultTabBean.resultsMode}" />
					</t:htmlTag>

					<%/* Configuration of table */

						%>
					<t:htmlTag value="div" style="padding: 10px; background-color: #EEEEEE"
						rendered="#{TableResultTabBean.configurationMode}">

						<t:htmlTag value="div"
							style="padding-bottom: 5px; font-weight: bold; font-weight: bold; border-bottom: 2px solid #CCCCCC; margin-bottom: 10px;">
							<h:outputText value="Configure columns" />
						</t:htmlTag>

						<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
							<h:outputText value="Choose group of attributes" />
						</t:htmlTag>
						<t:htmlTag value="div" style="margin-bottom: 5px; padding-bottom: 5px;">
							<h:selectOneMenu style="width: 300px;" value="#{TableResultTabBean.selectedGroupSet}"
								id="configure_groupSetCombo" onchange="submit()">
								<f:selectItems value="#{TableResultTabBean.availableGroupSets}" />
							</h:selectOneMenu>
						</t:htmlTag>

						<t:htmlTag value="table" style="border-collapse: collapse;">
							<t:htmlTag value="tr">

								<t:htmlTag value="td" style="padding: 0px;">
									<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
										<h:outputText value="Available attributes" />
									</t:htmlTag>
									<h:selectManyListbox style="width: 300px" id="configure_availAttributes" size="10"
										value="#{TableResultTabBean.selectedAttributeToAdd}">
										<f:selectItems value="#{TableResultTabBean.availableAttributes}" />
									</h:selectManyListbox>
								</t:htmlTag>

								<t:htmlTag value="td" style="padding-left: 10px; padding-right: 10px; padding-top: 0px; padding-bottom: 0px;">
									<t:htmlTag value="div" style="margin-bottom: 5px;">
										<h:commandButton style="width: 30px" id="configure_AddAttrButton" value=">"
											action="#{TableResultTabBean.addSelectedAttributeToList}" />
									</t:htmlTag>
									<t:htmlTag value="div">
										<h:commandButton style="width: 30px" id="configure_RemAttrButton" value="<"
											action=" #{TableResultTabBean.remSelectedAttributeFromList}" />
									</t:htmlTag>
								</t:htmlTag>

								<t:htmlTag value="td" style="padding: 0px;">
									<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
										<h:outputText value="Selected columns" />
									</t:htmlTag>
									<h:selectManyListbox style="width: 300px" id="configure_visibleAttributes"
										value="#{TableResultTabBean.selectedAttributeAdded}" size="10">
										<f:selectItems value="#{TableResultTabBean.visibleAttributes}" />
									</h:selectManyListbox>
								</t:htmlTag>

								<t:htmlTag value="td" style="padding-left: 10px; padding-right: 0px; padding-top: 0px; padding-bottom: 0px;">
									<t:htmlTag value="div" style="margin-bottom: 5px;">
										<h:commandButton style="width: 80px" id="configure_UpAttrButton" value="Move up"
											action="#{TableResultTabBean.moveAttrUp}" />
									</t:htmlTag>
									<t:htmlTag value="div">
										<h:commandButton style="width: 80px" id="configure_DownAttrButton" value="Move down"
											action="#{TableResultTabBean.moveAttrDown}" />
									</t:htmlTag>
								</t:htmlTag>

							</t:htmlTag>
						</t:htmlTag>

						<%-- 
							<% /* Groups */ %>
							<t:htmlTag styleClass="configDiv" style="border: 3px solid Blue; width: 594px;" value="div">
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
									<h:selectManyListbox style="width: 200px" id="configure_availAttributes" size="10"
										value="#{TableResultTabBean.selectedAttributeToAdd}">
										<f:selectItems value="#{TableResultTabBean.availableAttributes}" />
									</h:selectManyListbox>
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
										<h:selectManyListbox style="width: 200px" id="configure_visibleAttributes"
											value="#{TableResultTabBean.selectedAttributeAdded}" size="10">
											<f:selectItems value="#{TableResultTabBean.visibleAttributes}" />
										</h:selectManyListbox>
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
							
							<% /* Auto attach attributes from search */ %>
							<t:htmlTag styleClass="configDiv" style="width: 594px; height: 25px;" value="div">			
								<h:selectBooleanCheckbox style="margin-top: 3px;" value="#{TableResultTabBean.attachSearchedParams}" />				
								<h:outputText style="margin-left: 3px; margin-top: 3px;" value=" Attach to the result attributes from the search query" />
							</t:htmlTag>
							
							<% /* Apply button part */ %>
							<t:htmlTag styleClass="configDiv" style="width: 594px; height: 25px;" value="div">							
								<h:commandButton style="margin-top: 5px; margin-left: 4px;" id="configure_applyConfigButton"
									value="Apply configuration" action="#{TableResultTabBean.resultsMode}" />
							</t:htmlTag>
							--%>

						<t:htmlTag value="div" style="margin-top: 10px;">
							<t:htmlTag value="table" style="border-collapse: collapse;">
								<t:htmlTag value="tr">
									<t:htmlTag value="td" style="padding: 0px;">
										<h:selectBooleanCheckbox value="#{TableResultTabBean.attachSearchedParams}" />
									</t:htmlTag>
									<t:htmlTag value="td" style="padding-left: 5px; padding-top: 0px; padding-right: 0px; padding-bottom: 0px;">
										<h:outputText value="Attach to the result attributes from the search query" />
									</t:htmlTag>
								</t:htmlTag>
							</t:htmlTag>
						</t:htmlTag>

						<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 2px solid #CCCCCC;">
							<h:commandButton id="configure_applyConfigButton" value="Apply configuration"
								action="#{TableResultTabBean.resultsMode}" />
						</t:htmlTag>

					</t:htmlTag>

				</t:htmlTag> 
				
					<%/* Statistical tab */%> 
					
					<s:stattab rendered="#{SearchBean.timeLineVisible}" 
						query="#{SearchBean.searchParameters}"
						conditionsOut="#{TimeLineResultTabBean.conditions}" 
						componentVisible="#{TimeLineResultTabBean.componentVisible}"
						styleClass="data-container">
					<h:panelGrid columns="1" style="width: 100%; background-color: #EEEEEE;">

						<%/* Configuration of graph */%>
						<h:panelGroup style="margin-bottom: 5px;">
							<h:outputText value="Y axis value: " />
							<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAggregate}" id="tLAggregates">
								<f:selectItems value="#{TimeLineResultTabBean.aggregateFunctions}" />
							</h:selectOneMenu>
							<h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
								<f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}"/>
							</h:selectOneMenu>
							<h:commandButton id="show_stat" value="Show" action="#{TimeLineResultTabBean.showTimeLine}" />
						</h:panelGroup>

						<%/* Graph data */%>
						<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
							<t:htmlTag value="div" style="margin-top: 5px;margin-bottom: 5px; border-top: 2px solid #CCCCCC;margin-top: 5px;">
								<t:htmlTag value="div" style="overflow:auto; width: 100%; height: 500px">
									<h:graphicImage value="#{TimeLineResultTabBean.chartPath}" />
								</t:htmlTag>
							</t:htmlTag>
						</h:panelGroup>

						<%/* Change size panel */

							%>
						<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
							<h:outputText value="Width: " />
							<h:inputText value="#{TimeLineResultTabBean.chartWidth}" style="width: 40px;" />
							<h:outputText value="Height: " />
							<h:inputText value="#{TimeLineResultTabBean.chartHeight}" style="width: 40px;" />
							<h:commandButton id="enlarge" value="Change size" action="#{TimeLineResultTabBean.setNewView}" />
						</h:panelGroup>
					</h:panelGrid>
				</s:stattab> 
				
				<s:stattab styleClass="data-container" 
					rendered="#{SearchBean.statisticsVisible}"
					query="#{SearchBean.searchParameters}" 
					conditionsOut="#{AdvancedStatisticsTabBean.conditions}">
					
					
							
					<t:htmlTag value="div" style="padding: 2px;" 
						rendered="#{AdvancedStatisticsTabBean.errorPresent || AdvancedStatisticsTabBean.warningPresent}">
						<t:htmlTag id="div_error" value="td" style="background: red;"
							rendered="#{AdvancedStatisticsTabBean.errorPresent}">
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
	
	
					<t:htmlTag id="div_table" value="div" style="padding: 2px;background-color: #EEEEEE;">
					<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
						<t:htmlTag id="tr_third" value="tr" style="background-color: #EEEEEE;">
							<t:htmlTag id="td_setup_label" value="th" >
								<h:outputText value="Chart setup"/>
							</t:htmlTag>

							<t:htmlTag value="th" style="border-left: 2px solid #CCCCCC; margin-bottom: 10px;">
								<h:outputText value="Current series" />
							</t:htmlTag>
						</t:htmlTag>
						
						
						<t:htmlTag id="tr_setup" value="tr">
							<t:htmlTag value="td" style="background-color: #EEEEEE; width: 520px; border-bottom: 4px solid #CCCCCC;">
								<h:selectOneRadio onchange="submit()" value="#{AdvancedStatisticsTabBean.selectedChart}"
									disabled="#{AdvancedStatisticsTabBean.errorPresent}">
									<f:selectItems value="#{AdvancedStatisticsTabBean.availableCharts}" />
								</h:selectOneRadio>
								<t:htmlTag value="div">
									<h:panelGrid columns="3">
										<h:outputText value="X axis value: " />

										<h:selectOneMenu style="width: 150px;" value="#{AdvancedStatisticsTabBean.xaxis}" id="xaxis_select">
											<f:selectItems value="#{AdvancedStatisticsTabBean.voyageSelectedAttributes}" />
										</h:selectOneMenu>

										<h:panelGrid columns="2">
											<h:selectBooleanCheckbox onclick="submit()" disabled="#{AdvancedStatisticsTabBean.errorPresent}"
												value="#{AdvancedStatisticsTabBean.aggregate}" />
											<h:outputText value="Enable aggregate functions" />
										</h:panelGrid>

										<h:outputText value="Order: " />

										<h:selectOneMenu style="width: 150px;" value="#{AdvancedStatisticsTabBean.order}" id="order_select">
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
											<h:selectOneMenu style="width: 100px;" value="#{AdvancedStatisticsTabBean.yaxis}" id="yaxis_select">
												<f:selectItems value="#{AdvancedStatisticsTabBean.voyageNumericAttributes}" />
											</h:selectOneMenu>
										</t:htmlTag>

										<h:commandButton id="addSeries" disabled="#{AdvancedStatisticsTabBean.errorPresent}" value="Add series"
											action="#{AdvancedStatisticsTabBean.addSeries}" />
									</h:panelGrid>
								</t:htmlTag>
							</t:htmlTag>
							<t:htmlTag value="td" style="width: 240px; background-color: #EEEEEE; border-left: 2px solid #CCCCCC; border-bottom: 4px solid #CCCCCC; margin-bottom: 10px;">
								<t:htmlTag value="div" rendered="#{AdvancedStatisticsTabBean.seriesAdded}">
									<h:selectManyCheckbox id="to_remove_check" layout="pageDirection" value="#{AdvancedStatisticsTabBean.toRemove}">
										<f:selectItems value="#{AdvancedStatisticsTabBean.series}" />
									</h:selectManyCheckbox>
									<h:commandButton id="removeSeries" disabled="#{AdvancedStatisticsTabBean.errorPresent}" value="Remove selected"
										action="#{AdvancedStatisticsTabBean.removeSeries}" style=""/>
								</t:htmlTag>
								<t:htmlTag value="div" rendered="#{!AdvancedStatisticsTabBean.seriesAdded}">
									<h:outputText value="No series added" />
								</t:htmlTag>
							</t:htmlTag>

						</t:htmlTag>
						</t:htmlTag>
						
						<t:htmlTag value="div" style="background-color: #EEEEEE;">
							<t:htmlTag value="div" style="border-bottom: 2px solid #CCCCCC;">
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
					
				</s:stattab>
				
				
				</td>
			</tr>
		</table>

	</h:form>
</f:view>
</body>
</html>
