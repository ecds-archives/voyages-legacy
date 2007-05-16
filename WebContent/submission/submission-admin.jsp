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
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
	<link href="../admin/tabs.css" rel="stylesheet" type="text/css">
	<link href="../admin/edit.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Administration panel</h1>
	
	<br>
	
	<s:tabBar id="bar" selectedTabId="#{AdminSubmissionBean.selectedTab}" onTabChanged="#{AdminSubmissionBean.onTabChanged}">
		<s:tab text="Voyages list" tabId="voyages" />
		<s:tab text="Requests list" tabId="requests" />
	</s:tabBar>
	
	<h:panelGroup rendered="#{AdminSubmissionBean.voyagesListSelected}">
	
		<t:htmlTag value="br"/>
		<t:htmlTag value="br"/>
		
		<f:verbatim>
		<div value="div" style="padding: 5px 10px 5px 10px; border: 1px solid #CCCCCC; background-color: #EEEEEE">
			
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-right: 5px">From</td>
				<td style="padding-right: 10px"></f:verbatim><h:selectOneMenu value="#{AdminVoyagesListBean.nationId}"><f:selectItems value="#{AdminVoyagesListBean.nations}"/></h:selectOneMenu><f:verbatim></td>
				
				<td style="padding-right: 5px">Yearam from</td>
				<td style="padding-right: 10px"></f:verbatim><h:inputText value="#{AdminVoyagesListBean.yearFrom}" style="width: 40px;" /><f:verbatim></td>
				<td style="padding-right: 5px">to</td>
				<td style="padding-right: 10px"></f:verbatim><h:inputText value="#{AdminVoyagesListBean.yearTo}" style="width: 40px;" /><f:verbatim></td>
				
				<td style="padding-right: 5px">Voyage ID from</td>
				<td style="padding-right: 10px"></f:verbatim><h:inputText value="#{AdminVoyagesListBean.voyageIdFrom}" style="width: 40px;" /><f:verbatim></td>
				<td style="padding-right: 5px">to</td>
				<td style="padding-right: 10px"></f:verbatim><h:inputText value="#{AdminVoyagesListBean.voyageIdTo}" style="width: 40px;" /><f:verbatim></td>
				
				<td style="padding-right: 5px"><h:commandButton value="Show" /></td>
				<td></f:verbatim><h:commandButton value="Restore default" action="#{AdminVoyagesListBean.restoreDefaultOptions}" /><f:verbatim></td>
			</tr>
			</table>
		</div>
		<br>
		</f:verbatim>
		<s:grid id="voyges" 
			columns="#{AdminVoyagesListBean.columns}"
			rows="#{AdminVoyagesListBean.rows}"
			action="#{AdminVoyageBean.openVoyageAction}"
			onOpenRow="#{AdminVoyageBean.openVoyage}" />
		<f:verbatim>	
		<br>
		</f:verbatim>
		<s:pager id="voyagesPager"
			maxShownPages="15"
			currentPage="#{AdminVoyagesListBean.currentPage}"
			firstRecord="#{AdminVoyagesListBean.firstRecordIndex}"
			lastRecord="#{AdminVoyagesListBean.lastRecordIndex}"
			pageSize="#{AdminVoyagesListBean.pageSize}" />
	
	
	</h:panelGroup>
	
	<h:panelGroup rendered="#{AdminSubmissionBean.requestsListSelected}">
	
		<t:htmlTag value="br"/>
		<t:htmlTag value="br"/>
		<f:verbatim>
		<div value="div" style="padding: 5px 10px 5px 10px; border: 1px solid #CCCCCC; background-color: #EEEEEE">
			
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-right: 5px">Type of request</td>
				<td style="padding-right: 10px"></f:verbatim><h:selectOneMenu value="#{AdminSubmissionBean.requestType}"><f:selectItems value="#{AdminSubmissionBean.requestTypes}"/></h:selectOneMenu><f:verbatim></td>
				<td style="padding-right: 5px">
				
				<td style="padding-right: 5px">Status of request</td>
				<td style="padding-right: 10px"></f:verbatim>
						<h:selectOneMenu value="#{AdminSubmissionBean.requestStatus}">
							<f:selectItem itemLabel="All requests" itemValue="1"/>
							<f:selectItem itemLabel="Reviewed, not solved" itemValue="2"/>
							<f:selectItem itemLabel="Not reviewed, not solved" itemValue="3"/>
							<f:selectItem itemLabel="Solved" itemValue="4"/>
						</h:selectOneMenu><f:verbatim></td>
				<td style="padding-right: 5px"></f:verbatim><h:commandButton value="Show" /><f:verbatim></td>
				
			</tr>
			</table>
		</div>
		<br>
		</f:verbatim>
	
		<f:verbatim>
		<div style="height: 500px; overflow: auto;">
		</f:verbatim>
		
		<s:grid id="voyges" 
			columns="#{AdminSubmissionBean.requestColumns}"
			rows="#{AdminSubmissionBean.requestRows}" 
			onOpenRow="#{AdminSubmissionBean.newRequestId}"
			action="#{AdminSubmissionBean.resolveRequest}" />
		
		<f:verbatim>
		</div>
		</f:verbatim>
	
	</h:panelGroup>

</h:form>
</f:view>
</body>
</html>