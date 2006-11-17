<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:sectionGroup
    title="Time frame"
	id="selectionYears"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle">

	<t:htmlTag value="table" style="border-collapse: collapse; margin-left: 10px; margin-bottom: 10px; margin-top: 10px;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px">
			<h:outputText value="From" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:inputText value="#{EstimatesSelectionBean.yearFrom}" style="width: 60px" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:outputText value="To" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px;">
			<h:inputText value="#{EstimatesSelectionBean.yearTo}" style="width: 60px" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
	
</s:sectionGroup>

<br>

<s:sectionGroup
	selectedSectionId="nations"
	id="selectionNationsAndRegions"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle">
	
	<s:section title="National carriers" sectionId="nations">
		<t:div style="padding: 5px;" styleClass="nation-checkboxes">
		<s:checkboxListExpandable
			id="nations"
			items="#{EstimatesSelectionBean.allNations}"
			selectedValues="#{EstimatesSelectionBean.checkedNations}" />
		</t:div>
	</s:section>
	
	<s:section title="Export regions" sectionId="africanRegions">
		<t:div style="padding: 5px;" styleClass="export-region-list">
		<s:checkboxListExpandable
			id="expReg"
			items="#{EstimatesSelectionBean.allExpRegions}" 
			selectedValues="#{EstimatesSelectionBean.checkedExpRegions}"
			expandedValues="#{EstimatesSelectionBean.expandedExpRegions}" />
		</t:div>
	</s:section>

	<s:section title="Import regions" sectionId="americanRegions">
		<t:div style="padding-top: 5px; padding-bottom: 5px;" styleClass="import-region-list">
		<s:checkboxListPopup
			id="impReg"
			items="#{EstimatesSelectionBean.allImpRegions}"
			selectedValues="#{EstimatesSelectionBean.checkedImpRegions}"/>
		</t:div>
	</s:section>

</s:sectionGroup>

<div style="margin-top: 2px; padding: 5px 5px 5px 5px; background-color: White;">
	<h:commandButton
		value="Change selection"
		action="#{EstimatesSelectionBean.changeSelection}" />
</div>

<br>

<s:sectionGroup
    title="Current query"
	id="currentQuery"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle">
	
	<t:div style="padding: 5px 5px 5px 5px; background-color: White;">
		<t:div>
			<t:div style="font-weight: bold;"><h:outputText value="Selected national carriers:"/></t:div>
			<h:outputText value="#{EstimatesSelectionBean.selectedNationsAsText}" escape="false" />
		</t:div>
		<t:div style="margin-top: 5px;">
			<t:div style="font-weight: bold;"><h:outputText value="Selected export regions:"/></t:div>
			<h:outputText value="#{EstimatesSelectionBean.selectedExpRegionsAsText}" escape="false" />
		</t:div>
		<t:div style="margin-top: 5px;">
			<t:div style="font-weight: bold;"><h:outputText value="Selected import regions:"/></t:div>
			<h:outputText value="#{EstimatesSelectionBean.selectedImpRegionsAsText}" escape="false" />
		</t:div>
	</t:div>

</s:sectionGroup>

