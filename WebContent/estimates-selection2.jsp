<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:sectionGroup
	selectedSectionId="nations"
	id="selection"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle">
	
	<s:section title="Nations" sectionId="nations">
		<t:div style="padding: 5px;" styleClass="nation-checkboxes">
		<s:checkboxList
			id="nations"
			items="#{EstimatesSelectionBean.allNations}"
			selectedValues="#{EstimatesSelectionBean.checkedNations}" />
		</t:div>
	</s:section>
	
	<s:section title="Africa" sectionId="africanRegions">
		<t:div style="padding: 5px;" styleClass="african-region-list">
		<s:checkboxList
			id="expRegions"
			items="#{EstimatesSelectionBean.allExpRegions}" 
			selectedValues="#{EstimatesSelectionBean.checkedExpRegions}"
			expandedValues="#{EstimatesSelectionBean.expandedExpRegions}" />
		</t:div>
	</s:section>

	<s:section title="America" sectionId="americanRegions">
		<t:div style="padding: 5px;" styleClass="american-region-list">
		<s:checkboxList
			id="impRegions"
			items="#{EstimatesSelectionBean.allImpRegions}"
			selectedValues="#{EstimatesSelectionBean.checkedImpRegions}"
			expandedValues="#{EstimatesSelectionBean.expandedImpRegions}" />
		</t:div>
	</s:section>

</s:sectionGroup>
	
<div style="margin-top: 2px; padding: 5px 5px 5px 5px; background-color: White;">
	<h:commandButton
		value="Change selection"
		action="#{EstimatesSelectionBean.changeSelection}" />
</div>