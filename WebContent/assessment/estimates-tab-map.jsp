<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>

<t:htmlTag value="h1"><h:outputText value="Map of regions" /></t:htmlTag>

<t:htmlTag id="estimates-map" value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">

		<s:map
			id="estimatesMap"
			x1="#{EstimatesMapBean.mapX1}"
			y1="#{EstimatesMapBean.mapY1}"
			x2="#{EstimatesMapBean.mapX2}"
			y2="#{EstimatesMapBean.mapY2}"
			zoomLevels="#{EstimatesMapBean.zoomLevels}"
			pointsOfInterest="#{EstimatesMapBean.pointsOfInterest}"
			miniMapZoomLevel="#{EstimatesMapBean.miniMapZoomLevel}"
			miniMap="true"
			miniMapWidth="100"
			miniMapHeight="100"
			miniMapPosition="bottom right"
			pointsSelectId="map_places" />
			
	</t:htmlTag>
	
	<t:htmlTag value="td" style="padding: 0px 0px 0px 10px; vertical-align: top;">
	
		<s:legend
			id="estimatesMapLegend" 
			styleClass="map-legend-div"
			legend="#{EstimatesMapBean.legend}"
			refreshAction="#{EstimatesMapBean.refresh}" 
			maps="#{EstimatesMapBean.availableMaps}"
			chosenMap="#{EstimatesMapBean.chosenMap}" />

		<t:htmlTag value="div" styleClass="map-legend-section-title" style="margin-top: 15px;">
			<h:outputText value="Find visible place" />
		</t:htmlTag>
		
		<t:htmlTag value="div" styleClass="map-legend-section">
			<t:htmlTag value="select" forceId="true" id="map_places" />
		</t:htmlTag>
			 
	</t:htmlTag>
		
</t:htmlTag>
</t:htmlTag>