<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Contact Us</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-team.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="Contributors of data" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Contributors of data</h1>					
			

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>