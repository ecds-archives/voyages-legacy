<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Contribute</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Contribute" />
	</s:siteHeader>
	
	<div id="content">
	
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<%@ include file="submission-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
			
				<h1>Contribute Voyage Information</h1>
				
				<p>The Contribute section contains data entry forms by which users of the website can 
				supply new information or revise existing information in the Voyages Database. A team 
				of slave trade scholars will review each contribution, as part of the peer-review 
				process. Once confirmed, the new or revised information will be incorporated in the 
				next revision of the online Voyages Database. We anticipate releasing a new version 
				every three years.</p>
				
				<p>The Contribution entry forms take advantage of possibilities offered by the internet 
				to continuously improve the store of knowledge, whether in the traditional form of written 
				documents or in the new form of collections of machine-readable data. The Voyages Database 
				is itself the product of extensive revisions since the publication of the CD-ROM version of 
				the dataset in 1999.  The Contribute section will allow the database to evolve in a way that 
				respects the high standards of scholarship governing the construction of the original database.</p>
				
				<p>Step-by-step instructions on how to use the data entry forms are provided in the Voyages 
				Guide, a PDF file accessible in <a href="../database/guide.faces">“Understanding the Database”</a>. In addition, 
				we ask you to read our <a href="guidelines.faces">“Guidelines for Contributors.”</a></p> 

				<p>For purposes of managing contributions to the website, contributors are asked to register, 
				choose a user name and password, and provide an e-mail address for communication. Please send 
				a message to <a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">
							&#118;&#111;&#121;&#97;&#103;&#101;s&#64;&#101;mo&#114;y&#46;&#101;&#100;&#117;</a>. 
				When your registration is confirmed, signing in below will enable you to access the data entry forms.</p>
				
	
				<h2>Sign in</h2>
	
				<p>In order to be able to access protected contents, please sign in.</p>
				
				<div style="padding: 5px; background-color: #FFF8CE; border-top: 1px solid #DDCA9D; border-bottom: 1px solid #DDCA9D;">
				
					<table cellpadding="0" cellspacing="5" border="0" align="center">
					<tr>
						<td>User name</td>
						<td><h:inputText style="width: 200px;" value="#{SubmissionUsersBean.userName}"/></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><h:inputSecret style="width: 200px;" value="#{SubmissionUsersBean.password}"/></td>
					</tr>					
					<tr>
						<td></td>
						<td>
							<h:commandButton value="Sign in" action="#{SubmissionUsersBean.auth}"/>&nbsp;&nbsp;
							<h:outputText style="font-weight: bold;" value="#{SubmissionUsersBean.errorMessage}"/>
						</td>
					</tr>
					</table>
				
				</div>
				
				<p>If you don't have an account, <a href="new-user.faces">click here</a>.</p>
		
				</s:simpleBox>
			</td>
		</tr>
	</table>
	
	</div>

</h:form>
</f:view>
</body>
</html>
