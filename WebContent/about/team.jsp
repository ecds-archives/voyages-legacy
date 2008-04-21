<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Project development team</title>

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
		<h:outputText value="Project development team" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<% /* <f:param value="essays-solomon" binding="#{EssaysBean.paramActiveMenuId}" /> */ %>
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Project development team</h1>
					
					<p>Many people contributed to the creation and implementation of this site. They include the following:</p>
					
					<ul id="team">

						<li><div class="group">PRINCIPAL INVESTIGATORS</div>
							<ul>
								<li><span class="person">David Eltis</span> &ndash; <span class="function">Woodruff Professor of History</span> <span class="place">(Department of History, Emory University, Atlanta, Georgia USA)</span></li>
								<li><span class="person">Martin Halbert</span> &ndash; <span class="function">Digital Programs and Systems Division Chair</span> <span class="place">(Woodruff Library, Emory University, Atlanta, Georgia USA)</span></li>
							</ul>
						</li>
					
						<li><div class="group">PROJECT DEVELOPMENT TEAM</div>
							<ul>
								<li><span class="person">Liz Milewicz</span> &ndash; <span class="function">Project Manager</span></li>
								<li><span class="person">Erika Farr</span> &ndash; <span class="function">Digital Programs Team Leader</span></li>
								<li><span class="person">Kyle Fenton</span> &ndash; <span class="function">Unix Administration, Integration &amp Web Development Team Leader</span></li>
								<li><span class="person">Liyuan Han</span> &ndash; <span class="function">Programmer</span></li>
								<li><span class="person">Chris LaRosa</span> &ndash; <span class="function">Programmer</span></li>
								<li><span class="person">Pawel Jurczyk</span> &ndash; <span class="function">Programmer</span></li>
								<li><span class="person">Jan Zich</span> &ndash; <span class="function">Programmer</span></li>
								<li><span class="person">Paul Lachance</span> &ndash; <span class="function">Visiting Professor</span></li>
								<li><span class="person">Richard Arzillo</span> &ndash; <span class="function">Research Assistant</span></li>
								<li><span class="person">Angela Campbell</span> &ndash; <span class="function">SIRE Undergraduate Research Fellow</span></li>
								<li><span class="person">Nicholas J. Radburn</span> &ndash; <span class="function">Research Assistant</span> <span class="place">(Victorian University of Wellington, New Zealand)</span></li>
								<li><span class="person">Rik van Welie</span> &ndash; <span class="function">Research Fellow </span></li>
								<li><span class="person">Daniel B. Domingues da Silva</span> &ndash; <span class="function">Image Coordinator </span></li>
								<li><span class="person">Stacey Martin</span> &ndash; <span class="function">GIS Consultant</span></li>
								<li><span class="person">Jelmer Vos</span> &ndash; <span class="function">Visiting Professor</span></li>
								<li><span class="person">Nafees M. Khan</span> &ndash; <span class="function">Curriculum Development Advisor </span></li>
								<li><span class="person">Heather Dahl</span> &ndash; <span class="function">Curriculum Development Team Member</span></li>
								<li><span class="person">Brian Hamilton</span> &ndash; <span class="function">Curriculum Development Team Member</span></li>
								<li><span class="person">Kristine Leach</span> &ndash; <span class="function">Curriculum Development Team Member</span></li>
								<li><span class="person">Michael Poreda</span> &ndash; <span class="function">Curriculum Development Team Member</span></li>
								<li><span class="person">Katherine Skinner</span> &ndash; <span class="function">Digital Preservation Coordinator </span></li>
								<li><span class="person">Lea McLees</span> &ndash; <span class="function">Emory Libraries Director of Communication</span></li>
								<li><span class="person">Ginger Cain</span> &ndash; <span class="function">Emory Library Development Coordinator</span></li>
								<li><span class="person">Franky Abbott</span> &ndash; <span class="function">Digital Programs Publicity Coordinator </span></li>
								<li><span class="person">Kathleen Turaski</span> &ndash; <span class="function">Web Design Consultant</span></li>
							</ul>
						</li>

						<li><div class="group">STEERING COMMITTEE</div>
							<ul>
								<li><span class="person">Stephen D. Behrendt </span> &ndash; <span class="function">History Programme</span> <span class="place">(Victoria University of Wellington, New Zealand)</span></li>
								<li><span class="person">Manolo Florentino</span> &ndash; <span class="function">Web Design Consultant</span> <span class="place">(Departamento de Historia, Universidade Federal do Rio de Janeiro, Brazil)</span></li>
								<li><span class="person">David Richardson</span> &ndash; <span class="function">Web Design Consultant</span> <span class="place">(Department of History, The University of Hull, United Kingdom)</span></li>
							</ul>
						</li>

						<li><div class="group">ADVISORY BOARD</div>
							<ul>
								<li><span class="person">Carole Hahn</span> &ndash; <span class="function">Division of Educational Studies, Emory University</span>, <span class="place">(Atlanta, Georgia, USA)</span></li>
								<li><span class="person">Herbert S. Klein</span> &ndash; <span class="function">Department of History, Columbia University</span>, <span class="place">(New York, New York, USA)</span></li>
								<li><span class="person">Paul Lovejoy</span> &ndash; <span class="function">Department of History, York University</span>, <span class="place">(Toronto, Ontario, Canada)</span></li>
								<li><span class="person">Joseph Miller</span> &ndash; <span class="function">Department of History, University of Virginia</span>,<span class="place">(Charlottesville, Virginia USA)</span></li>
								<li><span class="person">G. Ugo Nwokeji</span> &ndash; <span class="function">Department of African American Studies, University of California at Berkeley</span> <span class="place">(Berkeley, California, USA)</span></li>
								<li><span class="person">Regina Werum</span> &ndash; <span class="function">Department of Sociology, Emory University</span>, <span class="place">(Atlanta, Georgia, USA)</span></li>
							</ul>
						</li>
					
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