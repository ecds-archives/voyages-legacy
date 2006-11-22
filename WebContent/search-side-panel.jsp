<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>

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
			<h:inputText value="#{SearchBean.yearFrom}" style="width: 60px" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
			<h:outputText value="To" />
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px;">
			<h:inputText value="#{SearchBean.yearTo}" style="width: 60px" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
	
</s:sectionGroup>

<div class="sections-sepatator"></div>

<s:sectionGroup
	id="selectAttributeSectionByPopupMenu"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle"
	selectedSectionId="#{SearchBean.selectedCategory}">

	<s:section title="Beginner" sectionId="beginners">
		<s:menuPopup
			customSubmitFunction="animateAttribute"
			id="popupMenuAttributesBeginner"
			items="#{SearchBean.menuAttributesBeginners}"
			onMenuSelected="#{SearchBean.addConditionFromMenu}" />
	</s:section>

	<s:section title="General" sectionId="general">
		<s:menuPopup
			customSubmitFunction="animateAttribute"
			id="popupMenuAttributesGeneral"
			items="#{SearchBean.menuAttributesGeneral}"
			onMenuSelected="#{SearchBean.addConditionFromMenu}" />
	</s:section>

</s:sectionGroup>

<div class="sections-sepatator"></div>

<script type="text/javascript" language="javascript">

function animateAttribute(menuItem, submitFunction)
{

	var dest = document.getElementById("animateAttributeDest");
	
	var x1 = ElementUtils.getOffsetLeft(menuItem) + 1;
	var y1 = ElementUtils.getOffsetTop(menuItem) + 1;
	var w1 = ElementUtils.getOffsetWidth(menuItem) - 2;
	var h1 = ElementUtils.getOffsetHeight(menuItem) - 2;
	
	var x2 = ElementUtils.getOffsetLeft(dest);
	var y2 = ElementUtils.getOffsetTop(dest);
	var w2 = ElementUtils.getOffsetWidth(dest) - 2;
	var h2 = ElementUtils.getOffsetHeight(dest) - 2;

	var a = document.createElement("div");
	a.style.position = "absolute";
	a.style.left = x1 + "px";
	a.style.top = y1 + "px";
	a.style.width = w1 + "px";
	a.style.height = h1 + "px";
	a.style.border = "1px solid Black";
	a.style.zIndex = "1";
	
	document.body.appendChild(a);
	
	var anim = new Animation(a, 20, 300, submitFunction);
	anim.setPositions(x1, y1, x2, y2);
	anim.setSizes(w1, h1, w2, h2);
	anim.setOpacities(1, 0.1);
	anim.start();

}

</script>

<%--
<s:sectionGroup
	id="selectAttributeSectionBySliderMenu"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle"
	selectedSectionId="beginner">

	<s:section title="Beginner" sectionId="beginner">
		<s:menuSlider
			id="sliderMenuAttributesBeginner"
			items="#{SearchBean.menuAttributesBeginner}"
			onMenuSelected="#{SearchBean.addConditionFromMenu}" />
	</s:section>

	<s:section title="General" sectionId="general">
		<s:menuSlider
			id="sliderMenuAttributesGeneral"
			items="#{SearchBean.menuAttributesGeneral}"
			onMenuSelected="#{SearchBean.addConditionFromMenu}" />
	</s:section>

</s:sectionGroup>

<div class="sections-sepatator"></div>
--%>

<%--
<s:sectionGroup
	id="selectAttributeSection"
	backgroundStyle="dark"
	tabsStyle="middle"
	buttonsStyle="middle"
	selectedSectionId="#{SearchBean.selectedCategory}">
	
	<s:section title="Beginner" sectionId="beginner">

		<h:selectOneMenu
			id="selectBeginnerGroupId"
			value="#{SearchBean.selectedBeginnerGroupId}"
			onchange="form.submit();"
			styleClass="attributes-and-groups">
			<f:selectItems value="#{SearchBean.voyageAttributeBeginnerGroups}" />
		</h:selectOneMenu>

		<h:selectOneMenu
			id="selectBeginnerAttributeId"
			value="#{SearchBean.selectedBeginnerAtttibuteId}"
			styleClass="attributes-and-groups">
			<f:selectItems value="#{SearchBean.voyageBeginnerAttributes}" />
		</h:selectOneMenu>
	
		<h:commandButton
			id="buttonAddQueryConditionBeginner"
			styleClass="main-box-button"
			action="#{SearchBean.addQueryConditionBeginner}"
			value="Add" />
	
	</s:section>
	
	<s:section title="General" sectionId="general">

		<h:selectOneMenu
			id="selectGeneralGroupId"
			value="#{SearchBean.selectedGeneralGroupId}"
			onchange="form.submit();"
			styleClass="attributes-and-groups">
			<f:selectItems value="#{SearchBean.voyageAttributeGeneralGroups}" />
		</h:selectOneMenu>
	
		<h:selectOneMenu
			id="selectGeneralAttributeId"
			value="#{SearchBean.selectedGeneralAtttibuteId}"
			styleClass="attributes-and-groups">
			<f:selectItems value="#{SearchBean.voyageGeneralAttributes}" />
		</h:selectOneMenu>
	
		<h:commandButton
			id="buttonAddQueryConditionGeneral"
			styleClass="main-box-button"
			action="#{SearchBean.addQueryConditionGeneral}"
			value="Add" />

	</s:section>

</s:sectionGroup>

<div class="sections-sepatator"></div>
--%>

<div id="animateAttributeDest">
<s:sectionGroup
	id="queryBuilderSection"
	title="Current query"
	backgroundStyle="middle"
	buttonsStyle="dark">

	<s:queryBuilder
		id="queryBuilder"
		query="#{SearchBean.workingQuery}"
		onUpdateTotal="#{SearchBean.updateTotal}" />

	<t:htmlTag value="table" style="border-collapse: collapse;">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px;">

			<h:commandButton
				id="buttonSearch"
				styleClass="main-box-button"
				action="#{SearchBean.search}"
				value="Search" />
	
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 0px 0px 5px;">

			<aa:zoneJSF id="total">
				<h:outputText value="#{SearchBean.numberOfResultsText}" />
			</aa:zoneJSF>

		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px 0px 0px 5px; display: none;" id="totalUpdateIndicator" forceId="true">
			<h:graphicImage url="ajax-loader.gif" width="16" height="16" alt="" />
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>

</s:sectionGroup>
</div>

<script type="text/javascript" language="javascript">

AjaxAnywhere.prototype.showLoadingMessage = function()
{
	document.getElementById("totalUpdateIndicator").style.display = "";
}

AjaxAnywhere.prototype.hideLoadingMessage = function()
{
	document.getElementById("totalUpdateIndicator").style.display = "none";
}

AjaxAnywhere.prototype.handlePrevousRequestAborted = function()
{
}

</script>


<div class="sections-sepatator"></div>

<s:sectionGroup
	id="historySection"
	title="History"
	backgroundStyle="middle"
	buttonsStyle="dark">
	
	<s:historyList
		id="history"
		onDelete="#{SearchBean.historyItemDelete}"
		onRestore="#{SearchBean.historyItemRestore}"
		onPermlink="#{SearchBean.historyItemPermlink}"
		history="#{SearchBean.history}" />

</s:sectionGroup>