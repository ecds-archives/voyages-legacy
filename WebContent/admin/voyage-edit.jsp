<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyage</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="tabs.css" rel="stylesheet" type="text/css">
	<link href="edit.css" rel="stylesheet" type="text/css">
	<script src="../record-editor.js" language="javascript" type="text/javascript"></script>
	<script src="../utils.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:tabBar id="bar" selectedTabId="#{AdminVoyageBean.selectedGroupId}">
			<s:tab text="Ship" tabId="ship" />
			<s:tab text="Outcome" tabId="outcome" />
			<s:tab text="Itinerary" tabId="itinerary" />
			<s:tab text="Dates" tabId="dates" />
			<s:tab text="Crew" tabId="crew" />
			<s:tab text="Slave (numbers)" tabId="slaveNumbers" />
			<s:tab text="Slave (characteritics)" tabId="slaveChars" />
			<s:tab text="Sources" tabId="sources" />
		</s:tabBar>
	
		<h:panelGroup rendered="#{AdminVoyageBean.groupShipSelected}">
			<t:htmlTag value="h2"><h:outputText value="Ship" /></t:htmlTag>
			<s:recordEditor id="ship"
				schema="#{AdminVoyageBean.shipSchema}"
				values="#{AdminVoyageBean.shipValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupOutcomeSelected}">
			<t:htmlTag value="h2"><h:outputText value="Outcome" /></t:htmlTag>
			<s:recordEditor id="ship"
				schema="#{AdminVoyageBean.outcomeSchema}"
				values="#{AdminVoyageBean.outcomeValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupItinerarySelected}">
			<t:htmlTag value="h2"><h:outputText value="Itinerary" /></t:htmlTag>
			<s:recordEditor id="itinerary"
				schema="#{AdminVoyageBean.itinerarySchema}"
				values="#{AdminVoyageBean.itineraryValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupDatesSelected}">
			<t:htmlTag value="h2"><h:outputText value="Dates" /></t:htmlTag>
			<s:recordEditor id="dates"
				schema="#{AdminVoyageBean.datesSchema}"
				values="#{AdminVoyageBean.datesValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupCrewSelected}">
			<t:htmlTag value="h2"><h:outputText value="Crew" /></t:htmlTag>
			<s:recordEditor id="crew"
				schema="#{AdminVoyageBean.crewSchema}"
				values="#{AdminVoyageBean.crewValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupSlaveNumsSelected}">
			<t:htmlTag value="h2"><h:outputText value="Slave (numbers)" /></t:htmlTag>
			<s:recordEditor id="slaveNums"
				schema="#{AdminVoyageBean.slaveNumsSchema}"
				values="#{AdminVoyageBean.slaveNumsValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupSlaveCharsSelected}">
			<t:htmlTag value="h2"><h:outputText value="Slave (characteritics)" /></t:htmlTag>
			<s:recordEditor id="slaveChars"
				schema="#{AdminVoyageBean.slaveCharsSchema}"
				values="#{AdminVoyageBean.slaveCharsValues}" />
		</h:panelGroup>

		<h:panelGroup rendered="#{AdminVoyageBean.groupSourcesSelected}">
			<t:htmlTag value="h2"><h:outputText value="Sources" /></t:htmlTag>
			<s:recordEditor id="sources"
				schema="#{AdminVoyageBean.sourcesSchema}"
				values="#{AdminVoyageBean.sourcesValues}" />
		</h:panelGroup>
		
		<br>

		<h:commandButton value="Save" action="#{AdminVoyageBean.saveVoyage}" />
	
		<h:commandButton value="Cancel" action="cancel" />

	</h:form>
</f:view>
</body>
</html>