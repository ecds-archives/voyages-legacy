<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Further reading</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/education.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-further-reading.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="lessons">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Educational Materials" /></h:outputLink>
		<h:outputText value="Other resources" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="education-layout">
		<tr>
			<td id="education-left-column">
				<%@ include file="education-menu.jsp" %>
			</td>
			<td id="education-right-column">
				<s:simpleBox>
				
					<h1>Other Resources</h1>
					<p>The websites listed here provide a variety of resources, including but not limited to lesson 
					plans, interactive maps, and narratives of enslaved Africans. To help give additional context to 
					the Atlantic slave trade, a list of scholarly books are also provided to users.  These titles are 
					just a few of the numerous works related to the forced migration of millions of Africans.</p>

					
				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>