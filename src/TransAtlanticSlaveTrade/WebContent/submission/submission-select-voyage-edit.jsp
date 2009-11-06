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
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">
	<%@ include file="../top-logo-bar.jsp" %>
	
	<br>

	<h1>Select voyage for editing</h1>
	
	<br>
	
	<div style="width: 400px;">
		Select the voyage using voyage ID you want to edit.
		Then you can use the Lookup button to verify that
		it is really the voyage you are looking for.
	</div>
	
	<div style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:inputText value="#{SubmissionBean.lookupVoyageId}" />
		<h:commandButton value="Lookup ..." action="#{SubmissionBean.lookupVoyage}" />
	</div>

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
			<h:commandButton value="Yes, this is correct, edit this voyage >" action="#{SubmissionBean.editVoyage}"/>
		</t:htmlTag>

	</t:htmlTag>

	<t:htmlTag value="div">
		<h:commandButton value="< Previous" action="back"/>
	</t:htmlTag>

</h:form>
</f:view>
</body>
</html>