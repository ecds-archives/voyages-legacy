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
				miniMapWidth="100"
				miniMapHeight="100"
				serverBaseUrl="servlet/maptile" />
		</t:htmlTag>

		<t:htmlTag value="td" style="vertical-align: top;">
			<t:htmlTag value="div" id="mapLegend" styleClass="map-legend-div">
				<h:outputText value="Legend:" style="font-weight: bold; padding-left: 5px;" />
				<h:dataTable id="map-legend" styleClass="legend-table-main" value="#{MapBean.legend}" var="legendGroup">
					<h:column>
						<h:outputText value="#{legendGroup.title}" />
						<h:dataTable id="map-legendDetail" columnClasses="legend-table-detail-col1,legend-table-detail-col2" styleClass="legend-table-detail" value="#{legendGroup.items}" var="legendItem">
							<h:column>
								<h:graphicImage value="#{legendItem.imagePath}" />
							</h:column>
							<h:column>
								<h:outputText value="#{legendItem.legendString}" />
							</h:column>
						</h:dataTable>
					</h:column>
				</h:dataTable>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<h:outputText value="&nbsp;" escape="false" />
