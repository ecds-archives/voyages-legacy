<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission - step 1</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">
	<%@ include file="../top-logo-bar.jsp" %>
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-active-middle">Type of contribution</td>
			<td class="step-indicator-left">Your voyage information</td>
			<td class="step-indicator-left">Your sources</td>
			<td class="step-indicator-middle">Summary</td>		
		</tr>
	</table>
	<br>

	<h1>Select voyage for editing or deletion</h1>
	
	<br>
	
	<div style="width: 400px;">
		Using the voyage id select the voyage you want to edit or delete, then use the lookup button to verify that this is the voyage you are looking for.
	</div>
	
	<div style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:inputText value="#{SubmissionBean.lookupVoyageId}" />
		<h:commandButton value="Lookup ..." action="#{SubmissionBean.lookupVoyage}" />
	</div>

	<t:htmlTag value="div" rendered="#{SubmissionBean.showLookupFailed}" style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; padding-bottom: 10px;">
		<h:outputText value="#{SubmissionBean.errorMsg}" />
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
			<h:commandButton value="Yes, this is correct; edit or delete this voyage >" action="#{SubmissionBean.editVoyage}"/>
		</t:htmlTag>

	</t:htmlTag>

	<t:htmlTag value="div">
		<h:commandButton value="< Previous" action="back"/>
	</t:htmlTag>

</h:form>
</f:view>
</body>
</html>