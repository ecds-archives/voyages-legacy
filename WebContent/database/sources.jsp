<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Sources (bibliography)</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-sources.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyage Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="#{SourcesListBean.pageTitle}" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
			
				<%@ include file="secondary-menu.jsp" %>
			
			</td>			
			<td valign="top" id="main-content">
			
				<s:simpleBox>

					<h1><h:outputText value="#{SourcesListBean.pageTitle}" /></h1>
					
					<div class="switch-layout">
						<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="switch-layout-first-link">
								<h:outputLink
									styleClass="#{SourcesListBean.sortByNameLinkCssClass}"
									value="#{SourcesListBean.sortByNameLink}">
									<h:outputText value="Sort by long reference" />
								</h:outputLink>
							</td>						
							<td class="switch-layout-second-link">
								<h:outputLink
									styleClass="#{SourcesListBean.sortByIdLinkCssClass}"
									value="#{SourcesListBean.sortByIdLink}">
									<h:outputText value="Sort by short reference" />
								</h:outputLink>
							</td>
						</table>
					</div>
					
					<div id="sources">
						<s:simpleTable rows="#{SourcesListBean.sources}" />
					</div>
				
				</s:simpleBox>

			</td>
			
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>