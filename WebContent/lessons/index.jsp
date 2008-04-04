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
	<link href="../styles/lessons.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons-expandable-box.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="lessons">
		<h:outputLink value="../index.faces"><h:outputText value="Home page" /></h:outputLink>
		<h:outputText value="Educational Materials" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
			
				<s:expandableBox text="Quick Start">
					<h:outputText value="Suspendisse sollicitudin eros quis odio. Nulla libero massa, condimentum quis, varius eu, volutpat vitae, massa. Pellentesque pellentesque libero sed nisi. Integer vitae elit." />
				</s:expandableBox>
			</td>
			<td class="section-index-right-column">

				<img src="../images/lessons-index-title.png" width="350" height="40" border="0" alt="Educational Material">
				
				<p class="section-index-intro">
				One of the goals of the Trans Atlantic Slave Trade Online Database has been to bring the work of historians to a K-12 audience and allow students to engage in the process of historical inquiry.  A variety of lesson plans spanning a range of grade levels have been developed to offer teachers diverse approaches from which to teach students about the largest forced migration in history.  The lesson plans are tied to national standards in Social Studies (National Council for the Social Studies), History (National Center for History in the Schools), and Geography (National Council Geography Education), and when applicable Economics (National Council on Economic Education) and State standards.</p>
				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Lesson Plan</a></div>
				
					<p class="section-index-intro">Here you will find a variety standards-based lesson plans for use in your classroom.</p>
					<p>&nbsp;</p>
				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Lesson Maps</a></div>
				
					<p class="section-index-intro">Here you will find maps for teaching and testing geographical understanding of the slave trade. [This may be a feature we add to later versions of the database. We are including it here as a placeholder and to see what suggestions test users have for adding additional K-12 tools and resources.]</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Glossary</a></div>
				
					<p class="section-index-intro">A guide to important terms and concepts related to the Trans Atlantic Slave Trade.</p>
					<p>&nbsp;</p>
				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>