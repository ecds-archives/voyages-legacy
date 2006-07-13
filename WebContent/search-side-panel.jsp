<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

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

<s:sectionGroup
	id="queryBuilderSection"
	title="Current query"
	backgroundStyle="middle"
	buttonsStyle="dark">

	<s:queryBuilder
		id="queryBuilder"
		query="#{SearchBean.workingQuery}" />
	
	<h:commandButton
		id="buttonSearch"
		styleClass="main-box-button"
		action="#{SearchBean.search}"
		value="Search" />

</s:sectionGroup>

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

<%--
<s:expandableBox text="Choose query mode">
	<t:htmlTag value="div" style="margin: 5px 0px 5px 10px;">
		<h:selectOneMenu style="border: 0px; width: 276px; padding: 2px;" onchange="form.submit();"
			value="#{SearchBean.category}">
			<f:selectItem itemLabel="Beginner" itemValue="0" />
			<f:selectItem itemLabel="General" itemValue="1" />
		</h:selectOneMenu>
	</t:htmlTag>
</s:expandableBox>

<s:expandableBox text="Add condition to query">

	<t:htmlTag value="div" style="margin: 5px 0px 5px 10px;">
		<h:selectOneMenu onchange="form.submit();" value="#{SearchBean.selectedGroupId}"
			style="border: 0px; width: 276px; padding: 2px;">
			<f:selectItems value="#{SearchBean.voyageAttributeGroups}" />
		</h:selectOneMenu>
	</t:htmlTag>

	<t:htmlTag value="div" style="margin: 0px 0px 5px 10px;">
		<h:selectOneMenu value="#{SearchBean.selectedAtttibuteId}" style="border: 0px; width: 276px; padding: 2px;">
			<f:selectItems value="#{SearchBean.voyageAttributes}" />
		</h:selectOneMenu>
	</t:htmlTag>

	<t:htmlTag value="div" style="margin: 0px 0px 5px 10px;">
		<h:commandButton action="#{SearchBean.addQueryCondition}" value="Add" />
	</t:htmlTag>

</s:expandableBox>

<s:expandableBox text="Current query">
	<s:queryBuilder query="#{SearchBean.workingQuery}" />
</s:expandableBox>

<div style="margin: 10px 0px 5px 10px;">
	<h:commandButton action="#{SearchBean.search}" value="Search" />
</div>

<s:expandableBox text="History">
	<s:historyList onDelete="#{SearchBean.historyItemDelete}" onRestore="#{SearchBean.historyItemRestore}"
		onPermlink="#{SearchBean.historyItemPermlink}" history="#{SearchBean.history}" />
</s:expandableBox>

--%>