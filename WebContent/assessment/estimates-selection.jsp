<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<div id="debug"></div>


<s:expandableBoxSet expandedId="time-frame">
<s:expandableBox boxId="time-frame" text="#{res.estimates_left_timeframe}">

	<t:htmlTag value="table" style="border-collapse: collapse;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px">
			<h:outputText value="#{res.estimates_left_from}" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:inputText value="#{EstimatesSelectionBean.yearFrom}" style="width: 40px" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:outputText value="#{res.estimates_left_to}" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px;">
			<h:inputText value="#{EstimatesSelectionBean.yearTo}" style="width: 40px" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #9EDEE0;">

		<h:commandButton
			value="#{res.estimates_left_changeselection}"
			action="#{EstimatesSelectionBean.changeTimeFrameSelection}" />

		<h:outputText value=" " />
			
		<h:commandButton
			value="#{res.estimates_left_resetselection}"
			action="#{EstimatesSelectionBean.resetSelection}" />

	</t:htmlTag>

</s:expandableBox>

<s:expandableBox boxId="nations" text="#{res.estimates_left_natcarriers}">
	<t:div styleClass="nation-checkboxes">
	<s:checkboxListExpandable
		id="nations"
		items="#{EstimatesSelectionBean.allNations}"
		selectedValues="#{EstimatesSelectionBean.checkedNations}" />
		
	<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #9EDEE0;">

		<h:commandButton
			value="#{res.estimates_left_changeselection}"
			action="#{EstimatesSelectionBean.changeGeographicSelection}" />

		<h:outputText value=" " />

		<h:commandButton
			value="#{res.estimates_left_resetselection}"
			action="#{EstimatesSelectionBean.resetSelection}" />

	</t:htmlTag>
		
	</t:div>
</s:expandableBox>
	
<s:expandableBox boxId="exp-regions" text="#{res.estimates_left_expregions}">
	<t:div styleClass="export-region-list">
	<s:checkboxListExpandable
		id="expReg"
		items="#{EstimatesSelectionBean.allExpRegions}" 
		selectedValues="#{EstimatesSelectionBean.checkedExpRegions}"
		expandedValues="#{EstimatesSelectionBean.expandedExpRegions}" />
		
	<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #9EDEE0;">

		<h:commandButton
				value="#{res.estimates_left_changeselection}"
				action="#{EstimatesSelectionBean.changeGeographicSelection}" />
				
		<h:outputText value=" " />

		<h:commandButton
				value="#{res.estimates_left_resetselection}"
				action="#{EstimatesSelectionBean.resetSelection}" />

	</t:htmlTag>		

	</t:div>
</s:expandableBox>

<s:expandableBox boxId="imp-regions" text="#{res.estimates_left_impregions}">
	<t:div styleClass="import-region-list">
	<s:checkboxListPopup
		id="impReg"
		items="#{EstimatesSelectionBean.allImpRegions}"
		selectedValues="#{EstimatesSelectionBean.checkedImpRegions}"/>
		
		<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #9EDEE0;">

			<h:commandButton
				value="#{res.estimates_left_changeselection}"
				action="#{EstimatesSelectionBean.changeGeographicSelection}" />
				
			<h:outputText value=" " />

			<h:commandButton
				value="#{res.estimates_left_resetselection}"
				action="#{EstimatesSelectionBean.resetSelection}" />

		</t:htmlTag>
	</t:div>
</s:expandableBox>

</s:expandableBoxSet>

<br>

<s:expandableBox text="#{res.estimates_left_currentquery}">
	<t:div>
		<t:div style="font-weight: bold;"><h:outputText value="#{res.estimates_left_selectednations}"/></t:div>
		<h:outputText value="#{EstimatesSelectionBean.selectedNationsAsText}" escape="false" />
	</t:div>
	<t:div style="margin-top: 5px;">
		<t:div style="font-weight: bold;"><h:outputText value="#{res.estimates_left_selectedexport}"/></t:div>
		<h:outputText value="#{EstimatesSelectionBean.selectedExpRegionsAsText}" escape="false" />
	</t:div>
	<t:div style="margin-top: 5px;">
		<t:div style="font-weight: bold;"><h:outputText value="#{res.estimates_left_selectedimport}"/></t:div>
		<h:outputText value="#{EstimatesSelectionBean.selectedImpRegionsAsText}" escape="false" />
	</t:div>
</s:expandableBox>
