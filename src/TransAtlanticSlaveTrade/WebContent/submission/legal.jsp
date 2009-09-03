<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Legal</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">
	
</head>
<body>
<f:view>
	
<%@ include file="../top-logo-bar.jsp" %>
	
<h:form id="form">	
	<div class="main-content">
		
		<br><br><br><br>		
		<table align="center" border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td valign="middle" id="main-content">
				<s:simpleBox>
			
					<h1>Legal</h1>
					
					<p>I warrant that I have the right to contribute the following data to the Voyages Database and its inclusion
					 in the Voyages Database will not infringe anyone's intellectual property rights. I also agree that this data 
					 will become part of the Voyages: The Trans-Atlantic Slave Trade Database website and will be governed by any 
					 applicable licenses.</p> 
					
				</s:simpleBox>
				<br><br>				
			</td>
		</tr>
		<tr>
			<td><h:commandButton value="Accept" action="accept"/>
				<h:commandButton value="Decline" action="decline"/>
			</td>
		</tr>
		</table>
				
	</div>

</h:form>
	
</f:view>

</body>
</html>