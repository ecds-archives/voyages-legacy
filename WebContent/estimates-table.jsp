<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="table" style="border-collapse: collapse; margin-top: 10px;">
<t:htmlTag value="tr">
	<t:htmlTag value="td" style="padding: 5px 10px 5px 10px;">
		<h:outputText value="In rows show"/>
	</t:htmlTag>
	<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
		<h:selectOneMenu value="#{EstimatesTableBean.rowGrouping}">
			<f:selectItem itemLabel="Nations" itemValue="nation" />
			<f:selectItem itemLabel="Regions in Africa" itemValue="expRegion" />
			<f:selectItem itemLabel="Regions in America" itemValue="impRegion" />
			<f:selectItem itemLabel="Individual years" itemValue="years1" />
			<f:selectItem itemLabel="5 year period" itemValue="years5" />
			<f:selectItem itemLabel="10 year period" itemValue="years10" />
			<f:selectItem itemLabel="25 year period" itemValue="years25" />
			<f:selectItem itemLabel="50 year period" itemValue="years50" />
			<f:selectItem itemLabel="100 year period" itemValue="years100" />
		</h:selectOneMenu>
	</t:htmlTag>
	<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
		<h:outputText value="In columns show"/>
	</t:htmlTag>
	<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
		<h:selectOneMenu value="#{EstimatesTableBean.colGrouping}">
			<f:selectItem itemLabel="Nations" itemValue="nation" />
			<f:selectItem itemLabel="Regions in Africa" itemValue="expRegion" />
			<f:selectItem itemLabel="Regions in America" itemValue="impRegion" />
		</h:selectOneMenu>
	</t:htmlTag>
	<t:htmlTag value="td">
		<h:selectBooleanCheckbox value="#{EstimatesTableBean.omitEmptyRowsAndColumns}" />
	</t:htmlTag>
	<t:htmlTag value="td" style="padding-right: 10px;">
		<h:outputText value="Omit empty rows and columns"/>
	</t:htmlTag>
	<t:htmlTag value="td">
		<h:commandButton action="#{EstimatesTableBean.refreshTable}" value="Show" />
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="estimates-table" style="padding: 10px;">
	<s:simpleTable
		rows="#{EstimatesTableBean.table}" />
</t:htmlTag>