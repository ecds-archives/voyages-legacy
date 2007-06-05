<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>

<s:expandableBoxSet expandedId="basic">

	<s:expandableBox text="#{res.slaves_query_title}" boxId="basic">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">
	
			<t:htmlTag value="table" style="border-collapse: collapse;">
			
			<t:htmlTag value="tr">
				
				<t:htmlTag value="td" styleClass="slaves-query-variable-title">
					<h:outputText value="#{res.slaves_query_slave_name}" />
				</t:htmlTag>
				
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
				
					<h:inputText
						style="width: 150px;"
						value="#{SlavesBean.workingQuery.slaveName}"
						onkeyup="SlavesSearch.searchFromBasicBox(true)" />
					
				</t:htmlTag>

			</t:htmlTag>
		
			<t:htmlTag value="tr">
			
				<t:htmlTag value="td" styleClass="slaves-query-variable-title">
					<h:outputText value="#{res.slaves_query_ship_name}" />
				</t:htmlTag>

				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
				
					<h:inputText
						style="width: 150px;"
						value="#{SlavesBean.workingQuery.shipName}"
						onkeyup="SlavesSearch.searchFromBasicBox(true)" />
					
				</t:htmlTag>

			</t:htmlTag>
		
			<t:htmlTag value="tr">
			
				<t:htmlTag value="td" styleClass="slaves-query-variable-title">
					<h:outputText value="#{res.slaves_query_voyage_id}" />
				</t:htmlTag>

				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
				
					<h:inputText
						style="width: 150px;"
						value="#{SlavesBean.workingQuery.voyageId}"
						onkeyup="SlavesSearch.searchFromBasicBox(true)" />
					
				</t:htmlTag>

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
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.yearFrom}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.yearTo}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
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
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.ageFrom}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.ageTo}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
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
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.heightFrom}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px 5px 0px 5px"><h:outputText value="-" /></t:htmlTag>
							<t:htmlTag value="td" style="padding: 0px;"><h:inputText style="width: 40px;" value="#{SlavesBean.workingQuery.heightTo}" onkeyup="SlavesSearch.searchFromBasicBox(true)"/></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.boys}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_boys}"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.girls}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_checkbox_girls}"/></t:htmlTag>
					</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			<t:htmlTag value="tr">
				<t:htmlTag value="td"></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
					<t:htmlTag value="table" style="border-collapse: collapse;">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.men}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_man}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.women}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.males}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px; width: 40px;"><h:outputText value="#{res.slaves_checkbox_males}" /></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.females}" onclick="SlavesSearch.searchFromBasicBox(false)"/></t:htmlTag>
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
		
		<t:htmlTag value="table" style="border-collapse: collapse;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 5px 0px 5px 0px;">
			
				<t:commandButton 
					forceId="true"
					id="submitBoxBasic"
					value="#{res.slaves_search_button}"
					action="#{SlavesBean.search}" />
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 5px;">
	
				<h:commandButton
					value="#{res.slaves_reset_button}"
					action="#{SlavesBean.reset}" />
		
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 10px;" forceId="true">
	
				<aa:zoneJSF id="totalBoxBasic">
					<h:outputText value="#{SlavesBean.numberOfExpectedResultsText}" />
				</aa:zoneJSF>
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px 0px 0px 5px; display: none;" id="totalUpdateIndicatorBoxBasic" forceId="true">
				<h:graphicImage url="../images/ajax-loader.gif" width="16" height="16" alt="" />
			</t:htmlTag>
		</t:htmlTag>
		</t:htmlTag>
	
	</s:expandableBox>
	
	<s:expandableBox text="#{res.slaves_query_country}" boxId="country">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">

			<s:lookupCheckboxList
				id="c"
				items="#{SlavesBean.countries}"
				selectedValues="#{SlavesBean.workingQuery.countries}" 
				onChange="function() {SlavesSearch.searchFromCountryBox(false)}" />
			
		</t:htmlTag>
	
		<t:htmlTag value="table" style="border-collapse: collapse;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 5px 0px 5px 0px;">
			
				<t:commandButton 
					forceId="true"
					id="submitBoxCountry"
					value="#{res.slaves_search_button}"
					action="#{SlavesBean.search}" />
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 5px;">
	
				<h:commandButton
					value="#{res.slaves_reset_button}"
					action="#{SlavesBean.reset}" />
		
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 10px;">
	
				<aa:zoneJSF id="totalBoxCountry">
					<h:outputText value="#{SlavesBean.numberOfExpectedResultsText}" />
				</aa:zoneJSF>
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px 0px 0px 5px; display: none;" id="totalUpdateIndicatorBoxCountry" forceId="true">
				<h:graphicImage url="../images/ajax-loader.gif" width="16" height="16" alt="" />
			</t:htmlTag>
		</t:htmlTag>
		</t:htmlTag>
		
	</s:expandableBox>
	
	<s:expandableBox text="#{res.slaves_query_place}" boxId="places">
	
		<t:htmlTag value="div" styleClass="slaves-query-section">

			<t:htmlTag value="div" style="margin-bottom: 5px;"><h:outputText value="#{res.slaves_query_embarkation}" /></t:htmlTag>
	
			<s:lookupCheckboxList
				id="r"
				items="#{SlavesBean.expPorts}"
				selectedValues="#{SlavesBean.workingQuery.embPorts}"
				expandedValues="#{SlavesBean.expandedEmbPorts}"
				onChange="function() {SlavesSearch.searchFromPlacesBox(false)}" />
				
		</t:htmlTag>
	
		<t:htmlTag value="div" styleClass="slaves-query-section">
	
			<t:htmlTag value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" styleClass="slaves-query-variable-title"><h:outputText value="#{res.slaves_query_captured}" /></t:htmlTag>
				<t:htmlTag value="td" styleClass="slaves-query-variable-controls">
					<t:htmlTag value="table" style="border-collapse: collapse;">
					<t:htmlTag value="tr">
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.disembSierraLeone}" onclick="SlavesSearch.searchFromPlacesBox(false)"/></t:htmlTag>
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
						<t:htmlTag value="td" style="padding: 0px;"><h:selectBooleanCheckbox value="#{SlavesBean.workingQuery.disembHavana}" onclick="SlavesSearch.searchFromPlacesBox(false)"/></t:htmlTag>
						<t:htmlTag value="td" style="padding: 0px;"><h:outputText value="#{res.slaves_captured_havana}" /></t:htmlTag>
					</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			</t:htmlTag>
		
		</t:htmlTag>

		<t:htmlTag value="table" style="border-collapse: collapse;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 5px 0px 5px 0px;">
			
				<t:commandButton 
					forceId="true"
					id="submitBoxPlaces"
					value="#{res.slaves_search_button}"
					action="#{SlavesBean.search}" />
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 5px;">
	
				<h:commandButton
					value="#{res.slaves_reset_button}"
					action="#{SlavesBean.reset}" />
		
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px 5px 10px;">
	
				<aa:zoneJSF id="totalBoxPlaces">
					<h:outputText value="#{SlavesBean.numberOfExpectedResultsText}" />
				</aa:zoneJSF>
	
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px 0px 0px 5px; display: none;" id="totalUpdateIndicatorBoxPlaces" forceId="true">
				<h:graphicImage url="../images/ajax-loader.gif" width="16" height="16" alt="" />
			</t:htmlTag>
		</t:htmlTag>
		</t:htmlTag>

	</s:expandableBox>

</s:expandableBoxSet>

<br>

<s:expandableBox text="#{res.slaves_current_query}">

	<s:querySummary
		items="#{SlavesBean.querySummary}"
		noQueryText="#{res.slaves_current_no_query}" />
	
</s:expandableBox>