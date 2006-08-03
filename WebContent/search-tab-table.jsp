<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%/* Table with results */%>
<s:tabletab id="tableResults" onclick="#{TableResultTabBean.showDetails}" rendered="#{TableResultTabBean.resultsMode}"
	data="#{TableResultTabBean.data}" componentVisible="#{TableResultTabBean.componentVisible}"
	sortChanged="#{TableResultTabBean.sortChanged}" style="overflow: auto;" />

<t:htmlTag value="div" styleClass="pager" rendered="#{TableResultTabBean.resultsMode}">
	<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px;">
				<h:commandLink style="font-weight: bold; text-decoration: none;" value="< Previous page"
					action="#{TableResultTabBean.prev}" />
				<h:outputText value=" | " />
				<h:commandLink style="font-weight: bold; text-decoration: none;" value="Next page >"
					action="#{TableResultTabBean.next}" />
				<h:outputText value=" | " />
				<h:outputText value="Showing " />
				<h:outputText value="#{TableResultTabBean.firstDisplayed}" />
				<h:outputText value="-" />
				<h:outputText value="#{TableResultTabBean.lastDisplayed}" />
				<h:outputText value=" out of " />
				<h:outputText value="#{TableResultTabBean.totalRows}" />
				<h:outputText value=" | Show " />
				<h:selectOneMenu onchange="submit()" value="#{TableResultTabBean.step}">
					<f:selectItem itemValue="10" itemLabel="10" />
					<f:selectItem itemValue="25" itemLabel="25" />
					<f:selectItem itemValue="50" itemLabel="50" />
					<f:selectItem itemValue="100" itemLabel="100" />
					<f:selectItem itemValue="all" itemLabel="all" />
				</h:selectOneMenu>
				<h:outputText value="  results per page." />
			</t:htmlTag>
			<t:htmlTag value="td"
				style="padding-left: 0px; padding-bottom: 0px; padding-top: 0px; padding-right: 5px; text-align: right">
				<h:commandLink value="Preferences" action="#{TableResultTabBean.configurationMode}" />
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="detailTab" rendered="#{TableResultTabBean.detailMode}">

	<h:panelGrid style="padding-left: 5px;" columns="2">
		<h:outputText value="Detail information about voyage" style="font-size: 150%;" />
		<t:htmlTag value="div" styleClass="section-inside-footer">
			<h:commandButton id="backFromDetailMap" value="Back to results" action="#{TableResultTabBean.resultsMode}" />
		</t:htmlTag>
	</h:panelGrid>

	<h:dataTable value="#{TableResultTabBean.detailVoyageInfo}" var="info" style="padding-left: 10px;">
		<h:column>
			<h:outputText value="#{info.attribute}:" />
		</h:column>
		<h:column>
			<h:outputText value="#{info.value==null? \"not set\" : info.value}" />
		</h:column>
	</h:dataTable>

	<s:sectionGroup id="detailPanelSection" backgroundStyle="dark" tabsStyle="middle" buttonsStyle="middle"
		selectedSectionId="listing">

		<s:section title="Voyage details" sectionId="listing">

			<s:voyageDetail data="#{TableResultTabBean.detailData}" />

		</s:section>

		<s:section title="Voyage map" sectionId="maps">

			<h:outputText value="&nbsp;" escape="false" />

			<t:htmlTag value="table" id="DetailMap" style="border-collapse: collapse; padding-left: 20px; margin-left: auto; margin-right: auto;">

				<t:htmlTag value="tr">
					<t:htmlTag value="td">
						<s:map mapFile="#{TableResultTabBean.mapPath}" pointsOfInterest="#{TableResultTabBean.pointsOfInterest}"
							serverBaseUrl="servlet/maptile" miniMap="true" />
						<h:outputText value="&nbsp;" escape="false" />
					</t:htmlTag>

					<t:htmlTag value="td" style="vertical-align: top;">
						<t:htmlTag value="div" id="mapDetailLegend" styleClass="map-legend-div">
							<h:outputText value="Legend:" style="font-weight: bold; padding-left: 5px;" />
							<h:dataTable id="map-detail-legend" styleClass="legend-table-main" value="#{TableResultTabBean.legend}" var="legendGroup">
								<h:column>
									<h:outputText value="#{legendGroup.title}" />
									<h:dataTable id="map-legendDetailDetail" styleClass="legend-table-detail" columnClasses="legend-table-detail-col1,legend-table-detail-col2" value="#{legendGroup.items}"
										var="legendItem">
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


		</s:section>

	</s:sectionGroup>

</t:htmlTag>

<%/* Configuration of table */

			%>
<t:htmlTag value="div" rendered="#{TableResultTabBean.configurationMode}">

	<t:htmlTag value="div" styleClass="section-inside-title">
		<h:outputText value="Configure columns" />
	</t:htmlTag>

	<t:htmlTag value="div" styleClass="section-inside-group">

		<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
			<h:outputText value="Choose group of attributes" />
		</t:htmlTag>

		<t:htmlTag value="div" style="margin-bottom: 5px; padding-bottom: 5px;">
			<h:selectOneMenu style="width: 300px;" value="#{TableResultTabBean.selectedGroupSet}" id="configure_groupSetCombo"
				onchange="submit()">
				<f:selectItems value="#{TableResultTabBean.availableGroupSets}" />
			</h:selectOneMenu>
		</t:htmlTag>

		<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">

				<t:htmlTag value="td" style="padding: 0px;">
					<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
						<h:outputText value="Available attributes" />
					</t:htmlTag>
					<h:selectManyListbox style="width: 300px" id="configure_availAttributes" size="10"
						value="#{TableResultTabBean.selectedAttributeToAdd}">
						<f:selectItems value="#{TableResultTabBean.availableAttributes}" />
					</h:selectManyListbox>
				</t:htmlTag>

				<t:htmlTag value="td" style="padding-left: 10px; padding-right: 10px; padding-top: 0px; padding-bottom: 0px;">
					<t:htmlTag value="div" style="margin-bottom: 5px;">
						<h:commandButton style="width: 30px" id="configure_AddAttrButton" value="xx"
							action="#{TableResultTabBean.addSelectedAttributeToList}" />
					</t:htmlTag>
					<t:htmlTag value="div">
						<h:commandButton style="width: 30px" id="configure_RemAttrButton" value="yy"
							action="#{TableResultTabBean.remSelectedAttributeFromList}" />
					</t:htmlTag>
				</t:htmlTag>

				<t:htmlTag value="td" style="padding: 0px;">
					<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
						<h:outputText value="Selected columns" />
					</t:htmlTag>
					<h:selectManyListbox style="width: 300px" id="configure_visibleAttributes"
						value="#{TableResultTabBean.selectedAttributeAdded}" size="10">
						<f:selectItems value="#{TableResultTabBean.visibleAttributes}" />
					</h:selectManyListbox>
				</t:htmlTag>

				<t:htmlTag value="td" style="padding-left: 10px; padding-right: 0px; padding-top: 0px; padding-bottom: 0px;">
					<t:htmlTag value="div" style="margin-bottom: 5px;">
						<h:commandButton style="width: 80px" id="configure_UpAttrButton" value="Move up"
							action="#{TableResultTabBean.moveAttrUp}" />
					</t:htmlTag>
					<t:htmlTag value="div">
						<h:commandButton style="width: 80px" id="configure_DownAttrButton" value="Move down"
							action="#{TableResultTabBean.moveAttrDown}" />
					</t:htmlTag>
				</t:htmlTag>

			</t:htmlTag>
		</t:htmlTag>

		<t:htmlTag value="div" style="margin-top: 10px;">
			<t:htmlTag value="table" style="border-collapse: collapse;">
				<t:htmlTag value="tr">
					<t:htmlTag value="td" style="padding: 0px;">
						<h:selectBooleanCheckbox value="#{TableResultTabBean.attachSearchedParams}" />
					</t:htmlTag>
					<t:htmlTag value="td" style="padding-left: 5px; padding-top: 0px; padding-right: 0px; padding-bottom: 0px;">
						<h:outputText value="Attach to the result attributes from the search query" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>

	</t:htmlTag>

	<t:htmlTag value="div" styleClass="section-inside-footer">
		<h:commandButton id="configureApplyConfigButton" value="Apply configuration"
			action="#{TableResultTabBean.resultsMode}" />
		<h:outputText value=" " />
		<h:commandButton id="configureCancel" value="Cancel" action="#{TableResultTabBean.cancelConfiguration}" />
	</t:htmlTag>

</t:htmlTag>
