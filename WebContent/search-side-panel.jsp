<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

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

<div style="margin: 10px 0px 5px 10px;"><h:commandButton action="#{SearchBean.search}" value="Search" /></div>

<s:expandableBox text="History">
	<s:historyList onDelete="#{SearchBean.historyItemDelete}" onRestore="#{SearchBean.historyItemRestore}"
		onPermlink="#{SearchBean.historyItemPermlink}" history="#{SearchBean.history}" />
</s:expandableBox>
