<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:outputText value="&nbsp;" escape="false" />

<t:htmlTag value="table" style="border-collapse: collapse; padding-left: 20px; margin-left: auto; margin-right: auto;">

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<s:map
				mapFile="#{MapBean.mapPath}"
				pointsOfInterest="#{MapBean.pointsOfInterest}"
				miniMap="true"
				miniMapFile="#{MapBean.miniMapFile}"
				miniMapWidth="100"
				miniMapHeight="100"
				serverBaseUrl="servlet/maptile" />
				
			<% /* 
				
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText value="View by time period:" />
			
			<s:timeline
				id="tl-mainMap"
				markers="#{MapBean.markers}"
				leftExtent="#{MapBean.yearBegin}"		
				rightExtent="#{MapBean.yearEnd}"
				markerWidth="40" />
				
				*/ %>

		</t:htmlTag>

 		<t:htmlTag value="td" style="vertical-align: top;">
			<s:legend id="mapLegend-component" 
				styleClass="map-legend-div"
				legend="#{MapBean.legend}"
				layers="#{MapBean.layers}"
				refreshAction="#{MapBean.refresh}" 
				maps="#{MapBean.availableMaps}"
				chosenMap="#{MapBean.chosenMap}" 
				 />
		</t:htmlTag>
	</t:htmlTag>

</t:htmlTag>

 
<h:outputText value="&nbsp;" escape="false" />
