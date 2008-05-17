<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Map of regions and ports" /></t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 10px 0px;">
	
		<s:map 
			id="map"
			x1="#{MapBean.mapX1}"
			y1="#{MapBean.mapY1}"
			x2="#{MapBean.mapX2}"
			y2="#{MapBean.mapY2}"
			zoomLevels="#{MapBean.zoomLevels}"
			pointsOfInterest="#{MapBean.pointsOfInterest}"
			miniMap="true"
			miniMapZoomLevel="#{MapBean.miniMapZoomLevel}"
			miniMapHeight="100"
			miniMapWidth="100"
			miniMapPosition="bottom right" 
			pointsSelectId="map_places" />

	</t:htmlTag>

	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 10px 0px; border-left: 0px solid #895D03;">
	
		<s:legend
			id="mapLegend" 
			styleClass="map-legend-div"
			legend="#{MapBean.legend}"
			maps="#{MapBean.availableMaps}"
			chosenMap="#{MapBean.chosenMap}" />

		<t:htmlTag value="div" style="margin-left: 5px;">
			<h:commandButton value="Refresh" action="#{MapBean.refresh}" />
		</t:htmlTag>

		<t:htmlTag value="div" styleClass="map-legend-section-title" style="margin-top: 50px;">
			<h:outputText value="Find visible place" />
		</t:htmlTag>
		
		<t:htmlTag value="div" styleClass="map-legend-section">

			<t:htmlTag value="select" forceId="true" id="map_places" />
			
			<t:htmlTag value="div" style="margin-top: 5px;">
				<h:commandButton value="Zoom to all places" action="#{MapBean.zoomToAll}" />
			</t:htmlTag>
			
		</t:htmlTag>
			
	</t:htmlTag>

</t:htmlTag>
</t:htmlTag>