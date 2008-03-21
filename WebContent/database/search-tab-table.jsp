<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" rendered="#{TableResultTabBean.resultsMode}">
	<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<t:htmlTag value="h1"><h:outputText value="List of voyages" /></t:htmlTag>
		</t:htmlTag>
		<t:htmlTag value="td" style="text-align: right;">
			<h:commandLink value="Configure columns of the table>" action="#{TableResultTabBean.configurationMode}" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<s:tabletab
	id="tableResults"
	onclick="#{TableResultTabBean.showDetails}"
	rendered="#{TableResultTabBean.resultsMode}"
	data="#{TableResultTabBean.data}"
	componentVisible="#{TableResultTabBean.componentVisible}"
	sortChanged="#{TableResultTabBean.sortChanged}"
	style="overflow: auto;" />

<t:htmlTag value="div" styleClass="table-bottom-tools" rendered="#{TableResultTabBean.resultsMode}">
	<t:htmlTag value="table" style="border-collapse: collapse; width: 100%;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px; text-align: left">
				<h:outputText value="#{res.database_search_showing} " />
				<h:outputText value="#{TableResultTabBean.firstDisplayed}" />
				<h:outputText value="-" />
				<h:outputText value="#{TableResultTabBean.lastDisplayed}" />
				<h:outputText value=" #{res.database_search_outof} " />
				<h:outputText value="#{TableResultTabBean.totalRows}" />
				<h:outputText value="#{res.database_search_showandline}" />
				<h:selectOneMenu onchange="submit()" value="#{TableResultTabBean.tableManager.step}">
					<f:selectItem itemValue="10" itemLabel="10" />
					<f:selectItem itemValue="15" itemLabel="15" />
					<f:selectItem itemValue="20" itemLabel="20" />
					<f:selectItem itemValue="30" itemLabel="30" />
					<f:selectItem itemValue="50" itemLabel="50" />
					<f:selectItem itemValue="100" itemLabel="100" />
					<f:selectItem itemValue="200" itemLabel="200" />
				</h:selectOneMenu>
				<h:outputText value=" #{res.database_search_resperpage}" />
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px;">
				<s:tablelinks manager="#{TableResultTabBean.tableManager}"/>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" style="margin-top: 5px;" rendered="#{TableResultTabBean.resultsMode}">
	<t:commandButton value="Download current view" action="#{TableResultTabBean.getFileCurrentData}"
		styleClass="button-save"/>
	<t:outputText escape="false" value="&nbsp;&nbsp;"/>
	<t:commandButton value="Download all results" action="#{TableResultTabBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>


<t:htmlTag value="h1" styleClass="with-subtitle" rendered="#{TableResultTabBean.configurationMode}"><h:outputText value="List of voyages" /></t:htmlTag>
<t:htmlTag value="div" styleClass="subtitle" rendered="#{TableResultTabBean.configurationMode}"><h:outputText value="Configure columns" /></t:htmlTag>

<t:htmlTag value="div" rendered="#{TableResultTabBean.configurationMode}">

	<t:htmlTag value="div" styleClass="database-configure-columns-pane">

		<t:htmlTag value="div" style="font-weight: normal; margin-bottom: 5px;">
			<h:outputText value="#{res.database_search_choosegroupofvariables}" />
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
						<h:outputText value="#{res.database_search_availablevariables}" />
					</t:htmlTag>
					<h:selectManyListbox style="width: 300px" id="configure_availAttributes" size="10"
						value="#{TableResultTabBean.selectedAttributeToAdd}">
						<f:selectItems value="#{TableResultTabBean.availableAttributes}" />
					</h:selectManyListbox>
				</t:htmlTag>

				<t:htmlTag value="td" style="padding-left: 10px; padding-right: 10px; padding-top: 0px; padding-bottom: 0px;">
					<t:htmlTag value="div" style="margin-bottom: 5px;">
						<h:commandButton style="width: 30px" id="configure_AddAttrButton" value=">"
							action="#{TableResultTabBean.addSelectedAttributeToList}" />
					</t:htmlTag>
					<t:htmlTag value="div">
						<h:commandButton style="width: 30px" id="configure_RemAttrButton" value="<"
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
						<h:commandButton style="width: 80px" id="configure_UpAttrButton" value="#{res.database_search_movep}"
							action="#{TableResultTabBean.moveAttrUp}" />
					</t:htmlTag>
					<t:htmlTag value="div">
						<h:commandButton style="width: 80px" id="configure_DownAttrButton" value="#{res.database_search_movedown}"
							action="#{TableResultTabBean.moveAttrDown}" />
					</t:htmlTag>
				</t:htmlTag>

			</t:htmlTag>
		</t:htmlTag>

		<% /* 
		<t:htmlTag value="div" style="margin-top: 10px;">
			<t:htmlTag value="table" style="border-collapse: collapse;">
				<t:htmlTag value="tr">
					<t:htmlTag value="td" style="padding: 0px;">
						<h:selectBooleanCheckbox onchange="submit();" value="#{TableResultTabBean.attachSearchedParams}" />
					</t:htmlTag>
					<t:htmlTag value="td" style="padding-left: 5px; padding-top: 0px; padding-right: 0px; padding-bottom: 0px;">
						<h:outputText value="#{res.database_search_qattachresultstoattrs}" />
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
		*/ %>

	</t:htmlTag>

	<t:htmlTag value="div" styleClass="database-configure-columns-buttons">
		<h:commandButton id="configureApplyConfigButton" value="#{res.database_search_applyconfig}"
			action="#{TableResultTabBean.resultsMode}" />
		<h:outputText value=" " />
		<h:commandButton id="configureCancel" value="#{res.database_search_cancel}" action="#{TableResultTabBean.cancelConfiguration}" />
	</t:htmlTag>

</t:htmlTag>

