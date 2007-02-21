<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Map of regions and ports" /></t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 10px 0px;">
	
		<s:map
			mapFile="#{MapBean.mapPath}"
			pointsOfInterest="#{MapBean.pointsOfInterest}"
			miniMap="true"
			miniMapFile="#{MapBean.miniMapFile}"
			miniMapWidth="100"
			miniMapHeight="100"
			serverBaseUrl="../servlet/maptile" />

	</t:htmlTag>

	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 10px 0px; border-left: 0px solid #895D03;">
		
		<s:legend id="mapLegend-component" 
			styleClass="map-legend-div"
			legend="#{MapBean.legend}"
			layers="#{MapBean.layers}"
			refreshAction="#{MapBean.refresh}" 
			maps="#{MapBean.availableMaps}"
			chosenMap="#{MapBean.chosenMap}"  />
			
	</t:htmlTag>
	
</t:htmlTag>
</t:htmlTag>