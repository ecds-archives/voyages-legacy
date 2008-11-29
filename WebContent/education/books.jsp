<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Books</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/education.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-info.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="lessons">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Educational Materials" /></h:outputLink>
		<h:outputText value="Books" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="education-layout">
		<tr>
			<td id="education-left-column">
				<%@ include file="education-menu.jsp" %>
			</td>
			<td id="education-right-column">
				<s:simpleBox>
				
					<h1>Books</h1>
					
					<div style="height: 400px;">TBD ...</div>

				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>