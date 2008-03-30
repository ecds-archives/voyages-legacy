<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>The Database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<f:param value="database" binding="#{MainMenuBean.activeSectionParam}" />
	<%@ include file="../top-bar.jsp" %>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
			
				<s:expandableBox text="Quick Start">
					<f:verbatim escape="false">
						<a href="../../tast/database/search.faces">Click here</a> to go directly to the query page for searching information in the main database.
					</f:verbatim>
				</s:expandableBox>
				
				<br>
				<% /* 
				<s:expandableBox text="Sample Queries">
					<f:verbatim escape="false">
						<ul class="box">
							<li>What was the first country to get slaves from Africa?</li>
							<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
							<li>Why did European interest in Africa turn from the slave trade to colonization?</li>
							<li>Where were they taken to?</li>
							<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
							<li>Who sold the slaves to the European slavers?</li>
							<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
							<li>Why were the slaves taken from Africa?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>Which country brought more slaves from Africa than any other country?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>show me more queries...</li>
						</ul>
					</f:verbatim>
				</s:expandableBox>
				*/ %>
			</td>
			<td class="section-index-right-column">

					<h1>New user registration</h1>
	
	<br>
	
	<table>
	<tr>
		<td>User name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserName}"/></td>
	</tr>
	<tr>
		<td>Password: </td><td><h:inputSecret value="#{SubmissionUsersBean.newUserPassword}"/></td>
	</tr>
	<tr>
		<td>Retype password: </td><td><h:inputSecret value="#{SubmissionUsersBean.newUserPasswordAgain}"/></td>
	</tr>
	<tr>
		<td>First name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserFirstName}"/></td>
	</tr>
	<tr>
		<td>Last name: </td><td><h:inputText value="#{SubmissionUsersBean.newUserLastName}"/></td>
	</tr>
	<tr>
		<td>Institution: </td><td><h:inputText value="#{SubmissionUsersBean.newUserInstitution}"/></td>
	</tr>
	<tr>
		<td>E-mail address: </td><td><h:inputText value="#{SubmissionUsersBean.newUserEmail}"/></td>
	</tr>
	<tr>
		<td>Verification word: </td><td><h:outputText value="#{SubmissionUsersBean.verificationString}"/></td>
	</tr>
	<tr>
		<td>Type in verification word: </td><td><h:inputText value="#{SubmissionUsersBean.newUserVerificationString}"/></td>
	</tr>
	<tr>
		<td></td><td><h:outputText rendered="#{SubmissionUsersBean.newUserErrorMessage != null}" value="#{SubmissionUsersBean.newUserErrorMessage}"/></td>
	</tr>	
	<tr>
		<td></td><td><h:commandButton value="Register" action="#{SubmissionUsersBean.createNewUser}"/></td>
	</tr>
	</table>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>