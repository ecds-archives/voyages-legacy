<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Slave voyages - Administration panel</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
	<link href="../admin/tabs.css" rel="stylesheet" type="text/css">
	<link href="../admin/edit.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Slave voyages - <h:outputText value="Administration panel" rendered = "#{AdminSubmissionBean.isAdmin}"/> 
	<h:outputText value="Reviewer panel" rendered = "#{!AdminSubmissionBean.isAdmin}"/> </h1>
	<br> 
	<h:commandButton value="Download" rendered = "#{AdminSubmissionBean.isAdmin}" action="#{AdminSubmissionBean.getFileAllData}" />
	<t:outputText escape="false" value="&nbsp;"/>
	<h:commandButton value="Logout" action="#{AdminSubmissionBean.logout}"/>	
	<br>
	<br>
	
	<t:htmlTag rendered="#{!AdminSubmissionBean.isAdmin}" value="div">
		<s:tabBar id="bar" selectedTabId="#{AdminSubmissionBean.selectedTab}" onTabChanged="#{AdminSubmissionBean.onTabChanged}">

			<s:tab text="Requests list" tabId="requests" />			
		</s:tabBar>
	</t:htmlTag>
	
	<t:htmlTag rendered="#{AdminSubmissionBean.isAdmin}" value="div">
	<tr>
		<s:tabBar id="bar" selectedTabId="#{AdminSubmissionBean.selectedTab}" onTabChanged="#{AdminSubmissionBean.onTabChanged}">
			<s:tab text="Voyages list" tabId="voyages" />
			<s:tab text="Requests list" tabId="requests" />
			<s:tab text="Users list" tabId="users" />
			<s:tab text="Publish new database revision" tabId="publish" />						
		</s:tabBar>	
		</tr>
	</t:htmlTag>	
	
	<h:panelGroup rendered="#{AdminSubmissionBean.voyagesListSelected}">
	
		
		<f:verbatim>

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
	
		<f:verbatim>
		<div value="div" style="margin-top: 10px; padding: 5px 10px 5px 10px; border: 1px solid #CCCCCC; background-color: #EEEEEE">
			
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>

				<td style="padding-right: 5px">Type of request</td>
				<td style="padding-right: 10px">					
					<f:verbatim>
						<h:selectOneMenu value="#{AdminSubmissionBean.requestType}"><f:selectItems value="#{AdminSubmissionBean.requestTypes}"/></h:selectOneMenu>
					</f:verbatim>
				</td>
				<td style="padding-right: 5px"></td>
				
				<td style="padding-right: 5px">Status of request</td>
				<td style="padding-right: 10px"><f:verbatim>
						<h:selectOneMenu value="#{AdminSubmissionBean.requestStatus}">
							<f:selectItem itemLabel="All requests" itemValue="1"/>
							<f:selectItem itemLabel="Under review" itemValue="2"/>
							<f:selectItem itemLabel="Not reviewed, not solved" itemValue="3"/>
							<f:selectItem itemLabel="Solved" itemValue="4"/>
						</h:selectOneMenu></f:verbatim></td>
				<td style="padding-right: 5px"><f:verbatim><h:commandButton value="Show" /></f:verbatim></td>
				
			</tr>
			</table>
		</div>
		<br>
		</f:verbatim>
		<div style="height: 500px; overflow: auto;">
		
		
		<s:grid id="voyges" 
			columns="#{AdminSubmissionBean.requestColumns}"
			rows="#{AdminSubmissionBean.requestRows}" 
			onOpenRow="#{AdminSubmissionBean.newRequestId}"
			action="#{AdminSubmissionBean.resolveRequest}" />
		</div>		
		<f:verbatim>

		</f:verbatim>
	
	</h:panelGroup>

	<h:panelGroup rendered="#{AdminSubmissionBean.usersListSelected}">
	
		<f:verbatim>
		<div value="div" style="margin-top: 10px; padding: 5px 10px 5px 10px; border: 1px solid #CCCCCC; background-color: #EEEEEE">
			
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-right: 10px">Show: </td>
				<td style="padding-right: 5px"><f:verbatim><h:selectOneMenu value="#{SubmissionUsersBean.accountType}"><f:selectItems value="#{SubmissionUsersBean.accountTypes}"/></h:selectOneMenu></f:verbatim></td>
				<td><f:verbatim><h:commandButton value="Refresh" action="#{SubmissionUsersBean.refresh}" /></f:verbatim></td>
			</tr>
			</table>
		</div>
		</f:verbatim>
		<div style="height: 500px; overflow: auto; padding-top: 10px;">
		
		
		<s:grid id="users"
			columns="#{SubmissionUsersBean.userColumns}"
			rows="#{SubmissionUsersBean.userRows}" 
			action="#{SubmissionUsersBean.enterEditUser}"
			onOpenRow="#{SubmissionUsersBean.editUser}"
			onColumnClick="#{SubmissionUsersBean.onGridColumnClick}" />
		</div>
		<f:verbatim>
		
		</f:verbatim>
	
	</h:panelGroup>
	
	<h:panelGroup rendered="#{AdminSubmissionBean.publishSelected}">
		<t:htmlTag value="br"/>
		<f:verbatim>
			<h2>Warning!</h2>
			After after applying changes, new database revision will immediately be published.<br>
			The process can take few minutes. Please do not press the button twice or do not
			refresh the page.<br><br>
		</f:verbatim>
		<h:outputText value="Revision name: "/>
		<h:inputText value="#{AdminSubmissionBean.revisionName}"/>
		<t:htmlTag value="br"></t:htmlTag>
		<h:outputText value="#{AdminSubmissionBean.message}" rendered="#{AdminSubmissionBean.message != null}"/>
		<t:htmlTag value="br"></t:htmlTag>
		<h:commandButton value="Publish new revision" onclick="if (confirm('Are you sure you want to publish new database revision?')) return true; return false;" action="#{AdminSubmissionBean.publish}"/>
	</h:panelGroup>
	
	
</h:form>
</f:view>
</body>
</html>