<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>

<head>
	<title>Reports</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="search.css" rel="stylesheet" type="text/css">
	<link href="sections.css" rel="stylesheet" type="text/css">
	<link href="menu.css" rel="stylesheet" type="text/css">
	<link href="search-query.css" rel="stylesheet" type="text/css">
	<link href="search-table.css" rel="stylesheet" type="text/css">
	<link href="map.css" rel="stylesheet" type="text/css">
	<link href="timeline.css" rel="stylesheet" type="text/css">
	<script src="tooltip.js" type="text/javascript" language="javascript"></script>
	<script src="jslib/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="jslib/scriptaculous.js" type="text/javascript" language="javascript"></script>
	<script src="jslib/aa.js" type="text/javascript" language="javascript"></script>
	<script src="map.js" language="javascript" type="text/javascript"></script>
	<script src="utils.js" language="javascript" type="text/javascript"></script>
	<script src="graphics.js" language="javascript" type="text/javascript"></script>
	<script src="graphics-svg.js" language="javascript" type="text/javascript"></script>
	<script src="graphics-vml.js" language="javascript" type="text/javascript"></script>
	<script src="query-builder.js" language="javascript" type="text/javascript"></script>
	<script src="tools_map_adds.js" language="javascript" type="text/javascript"></script>
	<script src="timeline.js" language="javascript" type="text/javascript"></script>
</head>

<body>
<f:view>

	<h:form id="form">

		<div align="center">
		
		 <table>
		  <td>
			<s:map
				mapFile="#{MapBean.mapPath}"
				pointsOfInterest="#{MapBean.pointsOfInterest}"
				miniMap="true"
				miniMapFile="#{MapBean.miniMapFile}"
				miniMapWidth="100"
				miniMapHeight="100"
				serverBaseUrl="servlet/maptile" />
				
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText value="View by time period:" />
			
			<s:timeline
				id="tl-mainMap"
				markers="#{MapBean.markers}"
				leftExtent="#{MapBean.yearBegin}"		
				rightExtent="#{MapBean.yearEnd}"
				markerWidth="40" />
		  </td>

 		  <td style="vertical-align: top;">
			<s:legend id="mapLegend-component" 
				styleClass="map-legend-div"
				legend="#{MapBean.legend}"
				layers="#{MapBean.layers}"
				refreshAction="#{MapBean.refresh}" />
		  </td>
		 </table>
		</div>

	
	</h:form>
	
</f:view>
</body>
</html>
