<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top">
			
     		    <h:selectOneMenu value="#{SearchBean.selectedAtttibute}">
		        	<f:selectItems value="#{SearchBean.voyageAttributes}"/>
		        </h:selectOneMenu>
				<h:commandButton action="#{SearchBean.addQueryCondition}" value="Add" />
		        
		        <hr>

				<s:queryBuilder query="#{SearchBean.workingQuery}" />
				
		        <hr>

				<h:commandButton action="#{SearchBean.search}" value="Search" />

		        <hr>

				<s:historyList
					onDelete="#{SearchBean.historyItemDelete}"
					onRestore="#{SearchBean.historyItemRestore}"
					history="#{SearchBean.history}" />
			
			</td>
			<td valign="top">

				<s:tabBar id="bar" onTabChanged="#{SearchBean.moduleTabChanged}">
					<s:tab text="Results listing" tabId="table" />
					<s:tab text="Graphs" tabId="timeline" />
				</s:tabBar>
				
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