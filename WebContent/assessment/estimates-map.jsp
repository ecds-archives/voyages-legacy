<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
<t:htmlTag value="tr">
	<t:htmlTag value="td">
		<t:htmlTag value="h1"><h:outputText value="Map" /></t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="td" style="text-align: right;">
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<h:outputText value="&nbsp;" escape="false" />

<t:htmlTag id="estimates-map" value="table" 
	style="border-collapse: collapse; padding-left: 20px; margin-left: auto; margin-right: auto;">

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<s:map id="estimates-map-compo"
				mapFile="#{EstimatesMapBean.mapPath}"
				pointsOfInterest="#{EstimatesMapBean.pointsOfInterest}"
				miniMap="true"
				miniMapFile="#{EstimatesMapBean.miniMapFile}"
				miniMapWidth="100"
				miniMapHeight="100"
				serverBaseUrl="servlet/maptile" />
				
			<h:outputText value="&nbsp;" escape="false" />
			<%/*
			<h:outputText value="View by time period:" /> 
			<s:timeline
				id="tl-mainMap"
				markers="#{EstimatesMapBean.markers}"
				leftExtent="#{EstimatesMapBean.yearBegin}"		
				rightExtent="#{EstimatesMapBean.yearEnd}"
				markerWidth="40" />*/%>
		</t:htmlTag>

 		<t:htmlTag value="td" style="vertical-align: top;">
			<s:legend id="mapLegend-component" 
				styleClass="map-legend-div"
				legend="#{EstimatesMapBean.legend}"
				layers="#{EstimatesMapBean.layers}"
				refreshAction="#{EstimatesMapBean.refresh}" /> 
		</t:htmlTag>
		
	</t:htmlTag>

</t:htmlTag>

 
<h:outputText value="&nbsp;" escape="false" />