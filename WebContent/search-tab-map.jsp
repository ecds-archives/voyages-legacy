<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:outputText value="&nbsp;" escape="false" />

<s:map
	mapFile="#{MapBean.mapPath}"
	pointsOfInterest="#{MapBean.pointsOfInterest}"
	miniMap="true"
	miniMapWidth="100"
	miniMapHeight="100"
	serverBaseUrl="servlet/maptile"/>

<h:outputText value="&nbsp;" escape="false" />