<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1" style="margin: 0px 0px 10px 0px;"><h:outputText value="#{res.slaves_listing_title}" /></t:htmlTag>

<s:table id="listingEstimates" 
	data="#{SlavesBean.tableData}"
	sortChanged="#{SlavesBean.sortChanged}" 
	onclick="#{SlavesBean.showDetails}"/>

<t:htmlTag value="div" styleClass="pager">
	<t:htmlTag id="listingEstimates-bottom-bar" value="table" style="border-collapse: collapse; width: 100%;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px; text-align: left">
				<h:outputText value="#{res.estimates_list_showing} " />
				<h:outputText value="#{SlavesBean.firstDisplayed}" />
				<h:outputText value="-" />
				<h:outputText value="#{SlavesBean.lastDisplayed}" />
				<h:outputText value=" #{res.estimates_list_outof} " />
				<h:outputText value="#{SlavesBean.totalRows}" />
				<h:outputText value=" | #{res.estimates_list_show} " />
				<h:selectOneMenu onchange="submit()" value="#{SlavesBean.step}">
					<f:selectItem itemValue="10" itemLabel="10" />
					<f:selectItem itemValue="20" itemLabel="20" />
					<f:selectItem itemValue="50" itemLabel="50" />
					<f:selectItem itemValue="100" itemLabel="100" />
					<f:selectItem itemValue="200" itemLabel="200" />
				</h:selectOneMenu>
				<h:outputText value="  #{res.estimates_list_resperpage}" />
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px;">
				<s:tablelinks manager="#{SlavesBean.tableManager}"/>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" style="margin-top: 5px;">
	<t:commandButton value="Save results into csv file (only current view)" action="#{SlavesBean.getFileCurrentData}"
		styleClass="button-save"/>
	<t:outputText escape="false" value="&nbsp;&nbsp;"/>
	<t:commandButton value="Save results into csv file (all results)" action="#{SlavesBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>
