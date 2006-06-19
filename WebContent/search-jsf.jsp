<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Search</title>
	<link href="search-jsp.css" rel="stylesheet" type="text/css">
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
	     		    <h:selectOneMenu value="#{SearchBean.selectedAtttibute}" style="width: 276px; padding: 2px;">
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
				
				<div class="data-container">
					<s:tabletab
						rendered="#{SearchBean.tableVisible}"
						query="#{SearchBean.currentConditions}"
						conditionsOut="#{TableResultTabBean.conditions}"
						results="#{TableResultTabBean.results}"
						populatedAttributes="#{TableResultTabBean.populatedAttributes}"
						componentVisible="#{TableResultTabBean.componentVisible}">
						<s:resultscroll resultFirst="#{TableResultTabBean.current}" resultSize="#{TableResultTabBean.resultSize}">
							<h:commandLink id="prev_pack" value="Previous result set" action="#{TableResultTabBean.prev}"/>
							<h:commandLink id="next_pack" value="Next result set" action="#{TableResultTabBean.next}"/>
						</s:resultscroll>
					</s:tabletab>
				</div>
		
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