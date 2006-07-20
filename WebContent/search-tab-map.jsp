<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:outputText value="#{MapBean.mapPath}" />


<t:htmlTag value="div" id="map-control" style="position: absolute;">

	<t:htmlTag value="div" id="map-frm" style="position: absolute;"></t:htmlTag>

	<t:htmlTag value="div" id="map-tools-top"
		style="height:30px; position: absolute; background-color: #DDDDDD; border-bottom: 1px solid #999999;">
		<t:htmlTag value="table">
			<t:htmlTag value="tr">
				<t:htmlTag value="td">
					<h:commandButton onclick="map_mouse_mode = PAN;return false;" value="Realtime pan" />
				</t:htmlTag>
				<t:htmlTag value="td">
					<h:commandButton type="button" onclick="map_mouse_mode = ZOOM;return false;" value="Realtime zoom" />
				</t:htmlTag>
				<t:htmlTag value="td">
					<h:commandButton type="button" onclick="zoom_plus();return false;" value="+" />
				</t:htmlTag>
				<t:htmlTag value="td">
					<h:commandButton type="button" onclick="zoom_minus();return false;" value="-" />
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>

</t:htmlTag>

<%--
<h:dataTable value="#{MapBean.ports}" var="group">
	<h:column>
		<f:facet name="header">
			<h:outputText value="Name" />
		</f:facet>
		<h:outputText value="#{group.label}" />
	</h:column>

	<h:column>
		<f:facet name="header">
			<h:outputText value="X" />
		</f:facet>
		<h:outputText value="#{group.x}" />
	</h:column>

	<h:column>
		<f:facet name="header">
			<h:outputText value="Y" />
		</f:facet>
		<h:outputText value="#{group.y}" />
	</h:column>
	
		<h:column>
		<f:facet name="header">
			<h:outputText value="Type" />
		</f:facet>
		<h:outputText value="#{group.portType}" />
	</h:column>

</h:dataTable>
--%>
