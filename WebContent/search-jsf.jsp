<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Search</title>
	<link href="search-jsf.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<div class="main-title">Trans-American Slave Trade</div>
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td class="side-panel">
			
				<div style="margin: 10px 0px 5px 10px; color: White;">Add field to query</div>

				<div style="margin: 0px 0px 5px 10px;">
	     		    <h:selectOneMenu value="#{SearchBean.selectedAttribute}" style="width: 276px; padding: 2px;">
					<h:selectOneMenu
	     		    	value="#{SearchBean.selectedGroupId}"
	     		    	style="width: 276px; padding: 2px;">
			        	<f:selectItems value="#{SearchBean.voyageAttributeGroups}"/>
			        </h:selectOneMenu>
			    </div>

				<div style="margin: 0px 0px 5px 10px;">
			    	<h:commandButton action="#{SearchBean.listAttributes}" value="Show" />	
			    </div>

				<div style="margin: 0px 0px 5px 10px;">
	     		    <h:selectOneMenu
	     		    	value="#{SearchBean.selectedAtttibuteId}"
	     		    	style="width: 276px; padding: 2px;">
			        	<f:selectItems value="#{SearchBean.voyageAttributes}"/>
			        </h:selectOneMenu>
			    </div>

			    <div style="margin: 0px 0px 5px 10px;">
			    	<h:commandButton action="#{SearchBean.addQueryCondition}" value="Add" />	
			    </div>
			    
			    <div class="side-panel-section side-panel-section-query-builder">Current query</div>

				<s:queryBuilder
					query="#{SearchBean.workingQuery}" />
				
			    <div style="margin: 10px 0px 5px 10px;">
					<h:commandButton action="#{SearchBean.search}" value="Search" />
			    </div>

		        <div class="side-panel-section side-panel-section-history">History</div>

				<s:historyList
					onDelete="#{SearchBean.historyItemDelete}"
					onRestore="#{SearchBean.historyItemRestore}"
					history="#{SearchBean.history}" />
			
			</td>
			<td class="main-panel">

				<div class="tab-bar-container">
					<s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
						<s:tab text="Results listing" tabId="table" />
						<s:tab text="Graphs" tabId="timeline" />
					</s:tabBar>
				</div>
				
				<t:htmlTag value="div" styleClass="data-container" rendered="#{SearchBean.tableVisible}">
				
					<s:tabletab rendered="#{TableResultTabBean.resultsMode}"
						query="#{SearchBean.currentConditions}"
						conditionsOut="#{TableResultTabBean.conditions}"
						results="#{TableResultTabBean.results}"
						populatedAttributes="#{TableResultTabBean.populatedAttributes}"
						componentVisible="#{TableResultTabBean.componentVisible}">
						<s:resultscroll resultFirst="#{TableResultTabBean.current}" resultSize="#{TableResultTabBean.resultSize}">
							<h:commandButton id="prev_pack" value="Previous result set" action="#{TableResultTabBean.prev}"/>
							<h:commandButton id="next_pack" value="Next result set" action="#{TableResultTabBean.next}"/>
							<h:commandButton id="configureButton" value="Configure table" action="#{TableResultTabBean.configurationMode}"/>
						</s:resultscroll>
					</s:tabletab>
					
					<t:htmlTag value="div" rendered="#{TableResultTabBean.configurationMode}">
					 	<t:htmlTag styleClass="configDiv" style="width: 594px;" value="div">
					 		<h:outputText id="config_label1" value="Choose set of attributes:"/>
					 		<t:htmlTag value="br"/>
						 	<h:selectOneMenu style="margin: 0px 0px 5px 10px;width: 200px;"
						 					 value="#{TableResultTabBean.selectedGroupSet}" 
						 					 id="configure_groupSetCombo"
						 					 onchange="submit()">
								<f:selectItems value="#{TableResultTabBean.availableGroupSets}"/>
							</h:selectOneMenu>
						</t:htmlTag>
						
						<h:panelGrid styleClass="configTable" id="configure_groupAttrs" columns="3">
						
							<h:panelGrid styleClass="config" columns="1" id="configure_groupAttrsPanel">
								<h:outputText  id="config_label2" value="Available attributes:"/>
								<h:selectOneListbox style="width: 200px" id="configure_availAttributes" size="10" value="#{TableResultTabBean.selectedAttributeToAdd}">
									<f:selectItems value="#{TableResultTabBean.availableAttributes}"/>
								</h:selectOneListbox>
							</h:panelGrid>
							
							<h:panelGroup id="configure_buttonsAddRem">
								<h:panelGrid id="cinfigure_buttonsAddRemGrid">
									<h:commandButton style="width: 75px" id="configure_AddAttrButton" value="->" action="#{TableResultTabBean.addSelectedAttributeToList}"/>
									<h:commandButton style="width: 75px" id="configure_RemAttrButton" value="<-" action="#{TableResultTabBean.remSelectedAttributeFromList}"/>
								</h:panelGrid>
							</h:panelGroup>
							
							<h:panelGrid columns="2" id="configure_currentAttrsPanelMain">
								<h:panelGrid columns="1" id="configure_currentAttrsPanel">
									<h:outputText  id="config_label3" value="Current attributes in talbe:"/>
									<h:selectOneListbox style="width: 200px" id="configure_visibleAttributes" value="#{TableResultTabBean.selectedAttributeAdded}" size="4">
										<f:selectItems value="#{TableResultTabBean.visibleAttributes}"/>
									</h:selectOneListbox>
								</h:panelGrid>					
								<h:panelGrid id="configure_ipDownPanel" columns="1">								
									<h:commandButton style="width: 75px" id="configure_UpAttrButton" value="Move up" action="#{TableResultTabBean.moveAttrUp}"/>
									<h:commandButton style="width: 75px" id="configure_DownAttrButton" value="Move down" action="#{TableResultTabBean.moveAttrDown}"/>	
								</h:panelGrid>
							</h:panelGrid>
															
						</h:panelGrid>
						<t:htmlTag styleClass="configDiv" style="width: 594px; height: 25px;" value="div">
							<h:commandButton style="margin-top: 5px; margin-left: 4px;" id="configure_applyConfigButton" value="Apply configuration" action="#{TableResultTabBean.resultsMode}"/>
						</t:htmlTag>					 
					</t:htmlTag>
				</t:htmlTag>
		
		      <s:stattab 
					rendered="#{SearchBean.timeLineVisible}"
		      		query="#{SearchBean.currentConditions}" 
		      		conditionsOut="#{TimeLineResultTabBean.conditions}"
		      		componentVisible="#{TimeLineResultTabBean.componentVisible}">
		        <h:panelGrid columns="1">
			      	<h:panelGroup>
				        <h:outputText value="Y axis value: "/>
		     		    <h:selectOneMenu value="#{TimeLineResultTabBean.chosenAggregate}" id="tLAggregates">
				        	<f:selectItems value="#{TimeLineResultTabBean.aggregateFunctions}"/>
				        </h:selectOneMenu>
			    	    <h:selectOneMenu value="#{TimeLineResultTabBean.chosenAttribute}" id="tLAttributes">
					        <f:selectItems value="#{TimeLineResultTabBean.voyageNumericAttributes}"/>		     
				        </h:selectOneMenu>
			        	<h:commandButton id="show_stat" value="Show" action="#{TimeLineResultTabBean.showTimeLine}"/>
		        	</h:panelGroup>
		        	<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
						<h:graphicImage value="#{TimeLineResultTabBean.chartPath}&height=480&width=640" rendered="#{TimeLineResultTabBean.normalView}"/>
						<h:graphicImage value="#{TimeLineResultTabBean.chartPath}&height=1024&width=1280" rendered="#{TimeLineResultTabBean.largeView}"/>
					</h:panelGroup>
					<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
						<h:commandButton id="enlarge" value="Enlarge" action="#{TimeLineResultTabBean.setLargeView}" rendered="#{TimeLineResultTabBean.normalView}"/>
						<h:commandButton id="shrink" value="Shrink" action="#{TimeLineResultTabBean.setNormalView}" rendered="#{TimeLineResultTabBean.largeView}"/>
					</h:panelGroup>
				</h:panelGrid>
		      </s:stattab>
		      
			</td>
		</tr>
		</table>

	</h:form>
</f:view>
</body>
</html>