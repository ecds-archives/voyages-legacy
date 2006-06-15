<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu/tabs" prefix="d" %>

<html>
 <head>
    <title>Show Custom Component</title>
    <link href="common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    <f:view>
    <h:form>
   
      <d:tabletab results="#{TableResultTabBean.results}" populatedAttributes="#{TableResultTabBean.populatedAttributes}">
		<d:resultscroll resultFirst="#{TableResultTabBean.current}" resultSize="#{TableResultTabBean.resultSize}">
			<h:commandLink id="prev_pack" value="Previous result set" action="#{TableResultTabBean.prev}"/>
			<h:commandLink id="next_pack" value="Next result set" action="#{TableResultTabBean.next}"/>
		</d:resultscroll>
      </d:tabletab>

      <d:stattab>
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
				<h:graphicImage value="#{TimeLineResultTabBean.chartPath}" height="500" width="700" rendered="#{TimeLineResultTabBean.normalView}"/>
				<h:graphicImage value="#{TimeLineResultTabBean.chartPath}" height="600" width="800" rendered="#{TimeLineResultTabBean.largeView}"/>
			</h:panelGroup>
			<h:panelGroup rendered="#{TimeLineResultTabBean.chartReady}">
				<h:commandButton id="enlarge" value="Enlarge" action="#{TimeLineResultTabBean.setLargeView}" rendered="#{TimeLineResultTabBean.normalView}"/>
				<h:commandButton id="shrink" value="Shrink" action="#{TimeLineResultTabBean.setNormalView}" rendered="#{TimeLineResultTabBean.largeView}"/>
			</h:panelGroup>
		</h:panelGrid>
      </d:stattab>

      <h:commandButton id="submit" action="success" value="Submit"/>
    </h:form>
    </f:view>
  </body>
</html>