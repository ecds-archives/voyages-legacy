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

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="lessons" /></td>
		</tr>
		</table>
	</div>
	
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
				Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Vestibulum molestie
				nulla in sapien. Pellentesque id mi. Aliquam luctus nisi at ipsum. Praesent non
				risus. Aliquam purus pede, egestas eget, fringilla id, imperdiet sed, dui.
				Integer nec neque. Cras condimentum iaculis turpis. Suspendisse id velit nec
				urna suscipit dignissim. Ut porttitor. Fusce urna tortor, convallis ac, ultricies et,
				scelerisque vel, nisl. Aliquam at diam in urna posuere vulputate. Ut eget odio.
				Quisque nibh tellus, faucibus nec, porta et, dignissim vitae, tellus. Sed eros dolor,
				suscipit non, pharetra sed, molestie in, velit. Proin quam elit, consectetuer varius,
				volutpat eu, vehicula vel, sapien.</p>

				<p class="section-index-intro">
				Cras condimentum iaculis turpis. Suspendisse id velit nec urna suscipit dignissim.
				Ut porttitor. Fusce urna tortor, convallis ac, ultricies et, scelerisque vel, nisl.
				Aliquam at diam in urna posuere vulputate. Ut eget odio. Quisque nibh tellus,
				faucibus nec, porta et, dignissim vitae, tellus. Sed eros dolor, suscipit non,
				pharetra sed, molestie in, velit. Proin quam elit, consectetuer varius, volutpat eu,
				vehicula vel, sapien.</p>
				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Lesson Plan</a></div>
				
					<p class="section-index-intro">Phasellus sed est. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse venenatis convallis nisl. Sed mi velit, volutpat et, dapibus vitae, accumsan id, risus. Ut lorem. Aenean pharetra, lectus ac bibendum tincidunt, est turpis blandit tortor, at sodales lectus nibh quis arcu. Mauris suscipit.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Lesson Maps</a></div>
				
					<p class="section-index-intro">Aenean dictum sapien eget neque. Aliquam dapibus tellus eu lacus. Maecenas hendrerit semper ipsum. Cras varius leo. Nunc dolor ligula, tempus nec, ultrices in, viverra sodales, lorem. Vestibulum erat quam, vestibulum sed, tempor ac, tempus ut, ligula.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Glossary</a></div>
				
					<p class="section-index-intro">Sed sem mi, placerat quis, condimentum vel, malesuada ut, ipsum. Maecenas condimentum libero id purus. Etiam at lectus. Morbi non turpis. Proin placerat velit. Fusce ac orci. Duis pede est, tincidunt in, euismod ac, posuere in, lorem. Nam lacinia, mi at fermentum adipiscing, ligula libero tincidunt eros, sed aliquet augue nunc sit amet elit.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>