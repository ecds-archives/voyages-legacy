<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Map of regions" /></t:htmlTag>

<t:htmlTag id="estimates-map" value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">
		<s:map id="estimates-map-compo"
			mapFile="#{EstimatesMapBean.mapPath}"
			pointsOfInterest="#{EstimatesMapBean.pointsOfInterest}"
			miniMap="true"
			miniMapFile="#{EstimatesMapBean.miniMapFile}"
			miniMapWidth="100"
			miniMapHeight="100"
			serverBaseUrl="../servlet/maptile" />
	</t:htmlTag>
	
	<t:htmlTag value="td" style="padding: 0px 0px 0px 10px; vertical-align: top;">
		<s:legend id="mapLegend-component" 
			styleClass="map-legend-div"
			legend="#{EstimatesMapBean.legend}"
			layers="#{EstimatesMapBean.layers}"
			refreshAction="#{EstimatesMapBean.refresh}" /> 
	</t:htmlTag>
		
</t:htmlTag>
</t:htmlTag>