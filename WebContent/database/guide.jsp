<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Tutorial</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

	<script language="javascript" type="text/javascript">
	function popitup(url) {
		newwindow=window.open(url,'name','height=768,width=1024,resizable, scrollbars, location=0');
		if (window.focus) {newwindow.focus()}
		return false;
	}
	</script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="Guide" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
			
				<s:simpleBox>
					<h1>A Guide to Understanding and Using the Online Database and Website</h1>
					
					<p>This 40-page guide, prepared by Liz Milewicz and Nafees Khan, provides both an 
					introduction and tutorial to how to use the four databases on the Voyages website: 
					the Voyages Database, the Estimates Database, the Images Database, and the African 
					Names Database.  It also contains a section on how to add new information to the Voyages  
					Database through the data submission form in Contribute.</p>
					
					<p>The guide is provided as a PDF file to facilitate reference to it in a separate window 
					or downloading to print in its entirety.</p>
					
					<ul>
						<li><a href="guide/VoyagesGuide.pdf" onclick="return popitup('guide/VoyagesGuide.pdf')">VoyagesGuide.pdf</a></li>
					</ul>
					
				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>