<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Lessons</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/education.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-further-reading.css" rel="stylesheet" type="text/css">
		
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="lessons">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputText value="Educational Materials" />
	</s:siteHeader>
	
	<div id="content">
	
		<img src="../images/lessons-index-title.png" width="350" height="40" border="0" alt="Educational Material">

		<p>One of the goals of the Trans-Atlantic Slave Trade Database has been to bring the work 
		of historians to a primary and secondary school audience, allowing students to engage in the 
		process of historical inquiry. To that end, a variety of lesson plans spanning a range of grade 
		levels have been developed to offer teachers diverse approaches from which to teach students about 
		the largest forced migration in history. These lesson plans are tied to national standards in Social 
		Studies (National Council for the Social Studies), History (National Center for History in the Schools), 
		and Geography (National Council Geography Education). Relevant lesson plans and educational resources 
		located on other websites have also been identified, to help educators build a broad study of the 
		trans-Atlantic slave trade into their curriculum. </p>

		<h2><a href="lesson-plans.faces">Lesson Plans</a></h2>

		<p>In order to present the Trans-Atlantic Slave Trade Database to a broader audience, particularly 
		a 6-12 grade audience, a dedicated team of teachers and curriculum developers from around the United 
		States developed lesson plans that explore the database.  Utilizing the various resources of the website, 
		these lessons plans allow students to engage the history and legacy of the Atlantic slave trade in diverse 
		and meaningful ways.  The lessons are all aligned with national standards in history (National Center for 
		History in the Schools), social studies (National Council for the Social Studies), and geography (National 
		Council for Geography Education) and range in both their grade levels and activities.  The lessons also 
		suggest readings for more information about the Slave Trade.</p>

		<h2><a href="others.faces">Web Resources</a></h2>

		<p>The websites listed here provide a variety of resources, including but not limited to lesson 
		plans, interactive maps, and narratives of enslaved Africans.</p>
		
<% /*
		<h2><a href="further-reading.faces">Further Reading</a></h2>
		
			<ul id="further-reading">
						<li>
							<span class="author">Ira Berlin</span>,
							<span class="title">Generations of Captivity: A History of African-American Slaves</span>
							<span class="year">(2003)</span>
						</li>
						<li>
							<span class="author">Phillip Curtin,</span>
							<span class="title">The Rise and Fall of the Plantation Complex: Essays in Atlantic History</span>
							<span class="year">(1998)</span>
						</li>
						<li>
							<span class="author">David Eltis,</span>
							<span class="title">The Rise of African Slavery in the Americas</span>
							<span class="year">(2000)</span>
						</li>
						<li>
							<span class="author">Olaudah Equiano,</span>
							<span class="title">The Interesting Narrative</span>
							<span class="year">(1789)</span>
						</li>
						<li>
							<span class="author">James Horton &amp; Lois Horton,</span>
							<span class="title">Slavery and the Making of America</span>
							<span class="year">(2005)</span>
						</li>
						<li>
							<span class="author">Herbert Klein,</span>
							<span class="title">The Atlantic Slave Trade</span>
							<span class="year">(1999)</span>
						</li>
						<li>
							<span class="author">Peter Kolchin,</span>
							<span class="title">American Slavery: 1619-1877</span>
							<span class="year">(1995)</span>
						</li>
						<li>
							<span class="author">Phillip Morgan,</span>
							<span class="title">Slave Counterpoint: Black Culture in the Eighteenth-Century Chesapeake and Lowcountry</span>
							<span class="year">(1998)</span>
						</li>
						<li>
							<span class="author">David Northrup (ed),</span>
							<span class="title">The Atlantic Slave Trade</span>
							<span class="title">(2001)</span>
						</li>
			</ul>
	*/ %>	
		</div>

</h:form>
</f:view>
</body>
</html>