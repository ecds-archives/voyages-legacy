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
	<link href="../styles/about-team.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="Legal" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Legal</h1>
					
					<p>The materials included in this website are intended to be used 
					for research, teaching, and private study. All items may be protected 
					by the U.S. Copyright Law (Title 17, U.S.C.). Transmission or 
					reproduction of protected items beyond that allowed by fair use requires 
					the written permission of the copyright owners. Some items may also be 
					subject to additional restrictions imposed by the copyright owner and/or 
					by Emory University. It is the researcher's obligation to determine and 
					satisfy copyright or other use restrictions when publishing or otherwise 
					distributing materials from this website, including an assessment of fair 
					use rights in light of intended use.</p>
					
					<p>Please contact 
					<a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">
					the Voyages website administrator</a>
					 for information about the copyright status and any restrictions on the use of particular 
					 images or texts. Requests for reproductions and a license to use the materials from this 
					 website should also be directed to the administrator. Permission to reproduce or license 
					 to use may need to be sought from the institution housing the original.</p>
					 
					
				</s:simpleBox>
			</td>
		</tr>
		</table>

		<br>
<a rel="license" href="http://creativecommons.org/licenses/by-nc/3.0/us/">
    <img alt="Creative Commons License" style="border-width:0" src="../images/ccl.png" />
</a>
<br />
<span xmlns:dc="http://purl.org/dc/elements/1.1/" href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title" rel="dc:type">Voyages: The Trans-Atlantic Slave Trade Database</span>
 by <a xmlns:cc="http://creativecommons.org/ns#" href="http://www.slavevoyages.org" property="cc:attributionName" rel="cc:attributionURL">Voyages: The Trans-Atlantic Slave Trade Database</a> is licensed under a 
 <a rel="license" href="http://creativecommons.org/licenses/by-nc/3.0/us/">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.
 <br />
 Permissions beyond the scope of this license may be available at <a xmlns:cc="http://creativecommons.org/ns#" href="http://www.slavevoyages.org/tast/about/legal.faces" rel="cc:morePermissions">http://www.slavevoyages.org/tast/about/legal.faces</a>.
		
		
	</div>

</h:form>
	
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>