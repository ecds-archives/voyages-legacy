<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="h1"><h:outputText value="Table" /></t:htmlTag>

<t:div styleClass="estimates-table-options">
	<t:htmlTag value="table" style="border-collapse: collapse;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
			<h:outputText value="#{res.estimates_table_rows}"/>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:selectOneMenu value="#{DatabaseTableBean.rowGrouping}">
				<f:selectItem itemLabel="Flag*" itemValue="flagStar"/>
				<f:selectItem itemLabel="#{res.database_tableview_expports}" itemValue="expPorts" />
				<f:selectItem itemLabel="#{res.database_tableview_impports}" itemValue="impPorts" />
				<f:selectItem itemLabel="#{res.estimates_table_expregions}" itemValue="expRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_impregions}" itemValue="impRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_impregionsbreakdown}" itemValue="impRegionBreakdowns" />
				<f:selectItem itemLabel="#{res.estimates_table_departureBroad}" itemValue="departureBroad" />
				<f:selectItem itemLabel="#{res.estimates_table_departureRegion}" itemValue="departureRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_departure}" itemValue="departure" />
				<f:selectItem itemLabel="#{res.estimates_table_individualyears}" itemValue="years1" />
				<f:selectItem itemLabel="#{res.estimates_table_5years}" itemValue="years5" />
				<f:selectItem itemLabel="#{res.estimates_table_10years}" itemValue="years10" />
				<f:selectItem itemLabel="#{res.estimates_table_25years}" itemValue="years25" />
				<f:selectItem itemLabel="#{res.estimates_table_50years}" itemValue="years50" />
				<f:selectItem itemLabel="#{res.estimates_table_100years}" itemValue="years100" />
			</h:selectOneMenu>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
			<h:outputText value="#{res.estimates_table_columns}"/>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:selectOneMenu value="#{DatabaseTableBean.colGrouping}">
				<f:selectItem itemLabel="Flag*" itemValue="flagStar"/>
				<f:selectItem itemLabel="#{res.database_tableview_expports}" itemValue="expPorts" />
				<f:selectItem itemLabel="#{res.database_tableview_impports}" itemValue="impPorts" />
				<f:selectItem itemLabel="#{res.estimates_table_expregions}" itemValue="expRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_impregions}" itemValue="impRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_impregionsbreakdown}" itemValue="impRegionBreakdowns" />
				<f:selectItem itemLabel="#{res.estimates_table_departureBroad}" itemValue="departureBroad" />
				<f:selectItem itemLabel="#{res.estimates_table_departureRegion}" itemValue="departureRegion" />
				<f:selectItem itemLabel="#{res.estimates_table_departure}" itemValue="departure" />
			</h:selectOneMenu>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
			<h:outputText value="#{res.estimates_table_show}"/>
		</t:htmlTag>
		<% /* 
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:selectOneMenu value="#{DatabaseTableBean.aggregateFunction}">
				<f:selectItem itemLabel="#{res.database_tableview_sum}" itemValue="sum" />
				<f:selectItem itemLabel="#{res.database_tableview_avg}" itemValue="avg" />
				<f:selectItem itemLabel="#{res.database_tableview_count}" itemValue="count" />
			</h:selectOneMenu>
		</t:htmlTag>*/ %>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:selectOneMenu value="#{DatabaseTableBean.showMode}">
				<f:selectItems value="#{DatabaseTableBean.availableAttributes}" />
			</h:selectOneMenu>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 5px 0px 5px 0px;">
			<h:selectBooleanCheckbox value="#{DatabaseTableBean.omitEmptyRowsAndColumns}" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding-right: 10px;">
			<h:outputText value="#{res.estimates_table_omitempty}"/>
		</t:htmlTag>
		<t:htmlTag value="td">
			<h:commandButton action="#{DatabaseTableBean.refreshTable}" value="#{res.estimates_table_show}" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
</t:div>

<t:htmlTag value="div" styleClass="estimates-table">
	<s:simpleTable rows="#{DatabaseTableBean.table}" />
</t:htmlTag>

<t:htmlTag value="div" style="margin-top: 5px;">
	<t:commandButton value="Save table data" action="#{DatabaseTableBean.getFileAllData}"
		styleClass="button-save"/>
</t:htmlTag>
