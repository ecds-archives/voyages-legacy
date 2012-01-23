<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>African Origins project</title>

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
		<h:outputText value="African Origins Project" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>African Origins Project</h1>

					<p>In January 2009 the directors of the project to create <em>Voyages:
					The Trans-Atlantic Slave Trade Database</em> began the African Origins
					Project, a scholar-public collaborative endeavor to trace the
					geographic origins of Africans transported in the transatlantic
					slave trade. Starting with the detailed descriptions found in the <em>Voyages</em>
					African Names Database, this project seeks the help of members of
					the African diaspora, Africans, scholars, and others to identify
					the likely origins of these liberated Africans and thus begin to
					trace the migration histories of other Africans transported across
					the Atlantic during the 19<sup>th</sup> century suppression of the
					slave trade. Those with knowledge of African languages, cultural
					naming practices, and ethnic groups will assist in identifying
					these Africans’ origins by drawing on their own expertise to
					identify the likely ethno-linguistic origin of an individual’s
					name.</p>

					<p>The African Origins Project arose directly from the work of
					G. Ugo Nwokeji and David Eltis, who in 2002 used audio recordings
					of names found in Courts of Mixed Commission records for Havana,
					Cuba, and Freetown, Sierra Leone, to identify likely
					ethno-linguistic origins. The names in these recordings were
					pronounced by speakers of the same language and accent that the
					Courts of Mixed Commission registrars would likely have had (e.g.,
					if the name was written in a Havana register, Eltis and Nwokeji had
					the name pronounced by a Spanish speaker with a Havana accent).
					This helped connect the sound of the name to its spelling and thus
					enabled a more accurate assessment of the name’s possible ethnic
					origins than provided by its written counterpart alone.</p>

					<p>Eltis and Nwokeji played these recordings to informants in
					Nigeria, Sierra Leone, and Angola and to members of the African
					diaspora in parts of North America, who were able to identify
					through these pronunciations the likely ethnic group from which the
					name derived. Such one-on-one research with informants, though
					successful, proved highly time consuming and yielded little more
					than two identifications for each African in their dataset, and led
					to the pursuit of an online method of broadly soliciting volunteers
					to assist with this project.</p>

					<p>In addition to the African Names Database, the <em>Voyages</em>
					website contains digitized images of the first pages of the
					<a href="http://www.slavevoyages.org/tast/resources/images.faces">Sierra
					Leone and Cuba Courts of Mixed Commission registers</a>, from which the
					information on these Africans was drawn. Information on liberated
					Africans and registers of the Courts of Mixed Commission may also
					be found in the Glossary. For more information about the African
					Origins Project and opportunities to participate, please visit 
					<a href="http://www.african-origins.org">http://www.african-origins.org</a>.</p>  

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