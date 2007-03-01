<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBox text="#{res.slaves_query_title}">

	<t:htmlTag value="table" style="border-collapse: collapse;">
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_slave_name}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.querySlaveName}" /></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_age}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="div" styleClass="slaves-query-variable-controls">
				<t:htmlTag value="table" style="border-collapse: collapse;">
				<t:htmlTag value="tr">
					<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.queryAgeFrom}" /></t:htmlTag>
					<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
					<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.queryAgeTo}" /></t:htmlTag>
				</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>
		
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_height}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="div" styleClass="slaves-query-variable-controls">
				<t:htmlTag value="table" style="border-collapse: collapse;">
				<t:htmlTag value="tr">
					<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.queryHeightFrom}" /></t:htmlTag>
					<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
					<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.queryHeightTo}" /></t:htmlTag>
				</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_sexage}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryBoy}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_boys}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryGirl}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_checkbox_girls}" /></t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td"></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryMan}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_man}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryWoman}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_checkbox_woman}" /></t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td"></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryMale}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_males}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryFemail}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px"><h:outputText value="#{res.slaves_checkbox_females}" /></t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer" style="height: 5px;"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer-line"></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-spacer-line"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer" style="height: 5px;"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_ship_name}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.queryShipName}" /></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_country}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.queryCountry}" /></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_exp_port}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.queryExpPort}" /></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_captured}" /></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.querySierraLeone}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_captured_sierra_leone}" /></t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td"></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.queryHavana}" /></t:htmlTag>
				<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_captured_havana}" /></t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer" style="height: 5px;"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer-line"></t:htmlTag>
		<t:htmlTag value="td" styleClass="slaves-query-spacer-line"></t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td" styleClass="slaves-query-spacer" style="height: 5px;"></t:htmlTag>
	</t:htmlTag>

	</t:htmlTag>
	
	<h:commandButton value="Search database"/>

</s:expandableBox>