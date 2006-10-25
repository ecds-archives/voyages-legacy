<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<div style="font-weight: bold; padding: 5px; background-color: #CA4223; color: White;">Nations</div>
<div style="padding: 0px; background-color: White;">
	<div style="height: 150px; overflow: scroll; padding: 5px;">
	<s:checkboxList
		id="nations"
		items="#{EstimatesSelectionBean.allNations}"
		selectedValues="#{EstimatesSelectionBean.checkedNations}" />
	</div>
</div>

<div style="font-weight: bold; margin-top: 5px; padding: 5px; background-color: #CA4223; color: White;">African regions</div>
<div style="padding: 0px; background-color: White;">
	<div style="height: 150px; overflow: scroll; padding: 5px;">
	<s:checkboxList
		id="expRegions"
		items="#{EstimatesSelectionBean.allExpRegions}" 
		selectedValues="#{EstimatesSelectionBean.checkedExpRegions}" />
	</div>
</div>

<div style="font-weight: bold; margin-top: 5px; padding: 5px; background-color: #CA4223; color: White;">American regions</div>
<div style="padding: 0px; background-color: White;">
	<div style="height: 150px; overflow: scroll; padding: 5px;">
	<s:checkboxList
		id="impRegions"
		items="#{EstimatesSelectionBean.allImpRegions}"
		selectedValues="#{EstimatesSelectionBean.checkedImpRegions}" />
	</div>
</div>
	
<div style="padding: 0px 0px 5px 5px;">
	<h:commandButton
		value="Change selection"
		action="#{EstimatesSelectionBean.changeSelection}" />
</div>