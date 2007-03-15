<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBoxSet expandedId="basic">

	<s:expandableBox text="#{res.slaves_query_title}" boxId="basic">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">
	
			<t:htmlTag value="table" style="border-collapse: collapse;">
			
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_slave_name}" /></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.workingQuery.slaveName}" /></t:htmlTag>
			</t:htmlTag>
		
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_ship_name}" /></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls"><h:inputText style="width: 150px;;" value="#{SlavesTableBean.workingQuery.shipName}" /></t:htmlTag>
			</t:htmlTag>
		
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-spacer"></t:htmlTag>
			</t:htmlTag>
			
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_year}" /></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
					<t:htmlTag value="div" styleClass="slaves-query-variable-controls">
						<t:htmlTag value="table" style="border-collapse: collapse;">
						<t:htmlTag value="tr">
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.yearFrom}" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.yearTo}" /></t:htmlTag>
						</t:htmlTag>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
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
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.ageFrom}" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.ageTo}" /></t:htmlTag>
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
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.heightFrom}" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesTableBean.workingQuery.heightTo}" /></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.boys}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_boys}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.girls}" /></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.men}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_man}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.women}" /></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.males}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_males}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.females}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px"><h:outputText value="#{res.slaves_checkbox_females}" /></t:htmlTag>
					</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-spacer" style="height: 5px;"></t:htmlTag>
			</t:htmlTag>
		
			</t:htmlTag>
		
		</t:htmlTag>
		
		<h:commandButton value="#{res.slaves_search_button}" action="#{SlavesTableBean.search}" />
		<h:outputText value=" " />
		<h:commandButton value="#{res.slaves_reset_button}" action="#{SlavesTableBean.reset}" />
	
	</s:expandableBox>
	
	<s:expandableBox text="#{res.slaves_query_country}" boxId="origin">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">

			<s:lookupCheckboxList
				id="c"
				items="#{SlavesTableBean.countries}"
				selectedValues="#{SlavesTableBean.workingQuery.countries}" />
			
		</t:htmlTag>
	
		<h:commandButton value="Search database" action="#{SlavesTableBean.search}" />
		<h:outputText value=" " />
		<h:commandButton value="#{res.slaves_reset_button}" action="#{SlavesTableBean.reset}" />
		
	</s:expandableBox>
	
	<s:expandableBox text="#{res.slaves_query_place}" boxId="places">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">

			<t:htmlTag value="div" style="margin-bottom: 5px;"><h:outputText value="#{res.slaves_query_embarkation}" /></t:htmlTag>
	
			<s:lookupCheckboxList
				id="r"
				items="#{SlavesTableBean.expPorts}"
				selectedValues="#{SlavesTableBean.workingQuery.embPorts}"
				expandedValues="#{SlavesTableBean.expandedEmbPorts}" />
				
		</t:htmlTag>
	
		<t:htmlTag value="div" styleClass="slaves-query-section">
	
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_captured}" /></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
					<t:htmlTag value="table" style="border-collapse: collapse;">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.disembSierraLeone}" /></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesTableBean.workingQuery.disembHavana}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_captured_havana}" /></t:htmlTag>
					</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		
		</t:htmlTag>

		<h:commandButton value="Search database" action="#{SlavesTableBean.search}" />
		<h:outputText value=" " />
		<h:commandButton value="#{res.slaves_reset_button}" action="#{SlavesTableBean.reset}" />

	</s:expandableBox>

</s:expandableBoxSet>

<br>

<s:expandableBox text="#{res.slaves_current_query}">

	<s:querySummary
		items="#{SlavesTableBean.querySummary}"
		noQueryText="#{res.slaves_current_no_query}" />
	
</s:expandableBox>