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
			<h:inputText  value="#{EstimatesSelectionBean.yearTo}" style="width: 40px" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="div" style="padding: 5px 0px 0px 0px; font-style: italic;">
		<h:outputText escape="false" value="#{EstimatesSelectionBean.timeFrameExtentHint}" />
		<h:outputText escape="false" value=" (" />
		<h:commandLink action="#{EstimatesSelectionBean.restoreDefaultTimeFrameExtent}">
			<h:outputText value="restore it" />
		</h:commandLink>			
		<h:outputText escape="false" value=")." />
	</t:htmlTag>

	<t:htmlTag value="div" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #9EDEE0;">

		<h:commandButton
			value="#{res.estimates_left_changeselection}"
			action="#{EstimatesSelectionBean.changeSelection}" />

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
				action="#{EstimatesSelectionBean.changeSelection}" />
	
			<h:outputText value=" " />
	
			<h:commandButton
				value="#{res.estimates_left_resetselection}"
				action="#{EstimatesSelectionBean.resetSelection}" />
	
		</t:htmlTag>
		
	</t:div>
</s:expandableBox>
	
<s:expandableBox boxId="regions" text="#{res.estimates_left_regions}">
	<t:div styleClass="region-list">
	
		<t:div styleClass="region-title">
			<h:outputText value="Embarkation regions" />
		</t:div>
	
		<s:checkboxListPopup
			id="expReg"
			items="#{EstimatesSelectionBean.allExpRegions}" 
			selectedValues="#{EstimatesSelectionBean.checkedExpRegions}" />
			
	</t:div>
	<t:div styleClass="region-list">
	
		<t:div styleClass="region-title">
			<h:outputText value="Disembarkation regions" />
		</t:div>

		<s:checkboxListPopup
			id="impReg"
			items="#{EstimatesSelectionBean.allImpRegions}"
			selectedValues="#{EstimatesSelectionBean.checkedImpRegions}"/>
		
	</t:div>
	<t:htmlTag value="div">

		<h:commandButton
			value="#{res.estimates_left_changeselection}"
			action="#{EstimatesSelectionBean.changeSelection}" />
			
		<h:outputText value=" " />

		<h:commandButton
			value="#{res.estimates_left_resetselection}"
			action="#{EstimatesSelectionBean.resetSelection}" />

	</t:htmlTag>
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
	
	<t:div style="margin-top: 5px;">

		<h:commandButton
			id="buttonPermlink"
			action="#{EstimatesSelectionBean.createPermlink}"
			value="#{res.estimates_main_permlink_button}" />
			
		<s:popup binding="#{EstimatesSelectionBean.permlinkPopup}" width="420" height="200">
			<t:htmlTag value="div" styleClass="permlink-info">
				<t:div styleClass="permlink-title">
					<t:outputText value="#{res.estimates_main_permlink_title}" />
				</t:div>
				<t:div styleClass="permlink-desc">
					<t:outputText value="#{res.estimates_main_permlink_desc}" />
				</t:div>
				<t:div styleClass="permlink-link">
					<t:outputText escape="false" value="<textarea rows='4' wrap='virtual'>" />
					<t:outputText value="#{EstimatesSelectionBean.permLink}" />
					<t:outputText escape="false" value="</textarea>" />
				</t:div>
				<t:div styleClass="permlink-close">
					<h:commandButton value="#{res.estimates_main_permlink_close}" styleClass="permlink-close-button" />
					<h:outputText value=" "/>
					<s:copyToClipboardButton text="#{res.estimates_main_permlink_copy}" data="#{estimatesselectionbean.permLink}" />
				</t:div>
			</t:htmlTag>
		</s:popup>

	</t:div>
	
</s:expandableBox>

<p style="font-style: italic;margin: 10px 10px 0px 10px;">Note: For purposes of calculation, estimates of 
embarked and disembarked slaves in tables, the timeline, and maps have not been rounded.  When users cite 
any number, they are advised to round it to the nearest hundred.</p>
