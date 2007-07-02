<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Select voyages</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of contribution</td>
			<td class="step-indicator-active-middle">Your voyage information</td>
			<td class="step-indicator-middle">Your sources</td>
			<td class="step-indicator-middle">Summary</td>
			<td class="step-indicator-right">Finish</td>
		</tr>
	</table>

	<br>

	<h1>Select voyages</h1>
	
	<br>
	
	<t:htmlTag value="div" rendered="#{SubmissionBean.errorSelectAtLeastTwo}" style="color: #AA0000; font-weight: bold; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; padding-bottom: 10px;">
		<h:outputText value="Please select at least two voyages for merging." />
	</t:htmlTag>

	<t:htmlTag value="div" rendered="#{SubmissionBean.someSelectedVoyagesForMerge}" style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; padding-bottom: 10px;">
	
		<t:htmlTag value="div" style="width: 400px;">
			<h:outputText value="So far, you have selected the following voyages:" />
		</t:htmlTag>
		
		<h:dataTable value="#{SubmissionBean.selectedVoyagesForMerge}" var="v">
		<h:column>
			<f:facet name="header">
				<h:outputText value="Voyage ID"/>
			</f:facet>
			<h:outputText value="#{v.voyageId}"/>		
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Ship"/>
			</f:facet>
			<h:outputText value="#{v.shipname}"/>		
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Captain"/>
			</f:facet>
			<h:outputText value="#{v.captain}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Year"/>
			</f:facet>
			<h:outputText value="#{v.year}"/>
		</h:column>
		<h:column>
			<h:commandLink action="#{SubmissionBean.removeSelectedVoyageForMerge}">
				<h:outputText value="Remove" />
				<t:updateActionListener property="#{SubmissionBean.toRemovedVoyageId}" value="#{v.voyageId}" />
			</h:commandLink>
		</h:column>
		</h:dataTable>
	
	</t:htmlTag>
	
	<div style="width: 400px;">
		Please provide voyage ID and click Lookup button.
	</div>
	
	
	<t:div rendered="#{SubmissionBean.lessThanFive}" style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:inputText value="#{SubmissionBean.lookupVoyageId}" />
		<h:commandButton value="Lookup ..." action="#{SubmissionBean.lookupVoyage}" />
	</t:div>

	<t:htmlTag value="div" rendered="#{SubmissionBean.showLookupFailed}" style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; padding-bottom: 10px;">
		<h:outputText value="No voyage with this Voyage ID found. Please try again." />
	</t:htmlTag>
	
	<t:htmlTag value="div" rendered="#{SubmissionBean.showLookedUpVoyage}" style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; padding-bottom: 10px;">

		<t:htmlTag value="table" style="border-collapse: collapse">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; background-color: #EEEEEE; padding: 5px;"><h:outputText value="Voyage ID" /></t:htmlTag>
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; font-weight: bold; padding: 5px;"><h:outputText value="#{SubmissionBean.lookedUpVoyage.voyageId}" /></t:htmlTag>
		</t:htmlTag>
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; background-color: #EEEEEE; padding: 5px;"><h:outputText value="Captain" /></t:htmlTag>
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; font-weight: bold; padding: 5px;"><h:outputText value="#{SubmissionBean.lookedUpVoyage.captain}" /></t:htmlTag>
		</t:htmlTag>
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; background-color: #EEEEEE; padding: 5px;"><h:outputText value="Ship" /></t:htmlTag>
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; font-weight: bold; padding: 5px;"><h:outputText value="#{SubmissionBean.lookedUpVoyage.shipname}" /></t:htmlTag>
		</t:htmlTag>
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; background-color: #EEEEEE; padding: 5px;"><h:outputText value="Year when arrived to America" /></t:htmlTag>
			<t:htmlTag value="td" style="border: 1px solid #CCCCCC; font-weight: bold; padding: 5px;"><h:outputText value="#{SubmissionBean.lookedUpVoyage.year}" /></t:htmlTag>
		</t:htmlTag>
		</t:htmlTag>
	
		<t:htmlTag value="div" style="margin-top: 10px;">
			<h:commandButton value="Yes, this is correct, add this voyage to the list" action="#{SubmissionBean.addVoyageForMerge}"/>
		</t:htmlTag>

	</t:htmlTag>

	<t:htmlTag value="div">
		<h:commandButton value="< Previous" action="back"/>
		<h:commandButton value="Next >" action="#{SubmissionBean.mergeVoyages}"/>
	</t:htmlTag>

</h:form>
</f:view>
</body>
</html>