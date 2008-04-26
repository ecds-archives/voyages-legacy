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
		<h:outputText value="Contact Us" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Contact Us</h1>
					
					<ul id="team">

						<li><div class="group">VOYAGES EDITORIAL BOARD</div>
							<ul>
								<li>
									<span class="person"><a href="mailto:lachance1943@rogers.com">Paul Lachance</a></span> &ndash;
									<span class="function">Managing Editor</span>
									<span class="place">
										(Professor,
										Department of History,
										University of Ottawa,
										Canada)</span>
								</li>
								<li>
									<span class="person">Manuel Barcia Paz</span>
									<span class="place">
										(Lecturer,
										Department of Spanish, Portuguese and Latin American Studies,
										University of Leeds,
										UK)</span>
								</li>
								<li>
									<span class="person">Steve Behrendt</span>
									<span class="place">
										(Senior Lecturer,
										History Programme,
										Victoria University of Wellington,
										New Zealand)</span>
								</li>
								<li>
									<span class="person">David Eltis</span>
									<span class="place">
										(Professor, 
										Robert W. Woodruff Professor of History,
										Atlanta, Georgia, USA)</span>
								</li>
								<li>
									<span class="person">Manolo Florentino</span>
									<span class="place">
										(Professor,
										Departamento de História,
										Universidade Federal do Rio de Janeiro,
										Brazil)</span>
								</li>
								<li>
									<span class="person">Antonio Mendes</span>
									<span class="place">
										(MISSING INFORMATION)</span>
								</li>
								<li>
									<span class="person">David Richardson</span>
									<span class="place">
										(Director,
										Wilberforce Institute for the study of Slavery and Emancipation at The University of Hull,
										UK)</span>
								</li>
							</ul>
						</li>
						
						<li><div class="group">VOYAGES WEBSITE ADMINISTRATION</div>
							<ul>
								<li>
									<span class="person"><a href="mailto:jfenton@emory.edu">Kyle Fenton</a></span> &ndash;
									<span class="function">Web Development Team Leader</span>
								</li>
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