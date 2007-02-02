<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>About Us</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-expandable-box.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="about" /></td>
		</tr>
		</table>
	</div>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
			
				<s:expandableBox text="Quick Start">
					<h:outputText value="Suspendisse sollicitudin eros quis odio. Nulla libero massa, condimentum quis, varius eu, volutpat vitae, massa. Pellentesque pellentesque libero sed nisi. Integer vitae elit. Nulla libero massa, condimentum quis, varius eu, volutpat vitae, massa. Pellentesque pellentesque libero sed nisi. Integer vitae elit." />
				</s:expandableBox>				

				<br>		

				<s:expandableBox text="Contact Us">
					<f:verbatim escape="false">
					<b>Emory University 2005</b><br>
					Robert W. Woodruff Library, 540 Asbury Circle<br>
					Atlanta, Georgia 30322
					</f:verbatim>
				</s:expandableBox>				

			</td>
			<td class="section-index-right-column">

				<img src="../images/about-index-title.png" width="350" height="40" border="0" alt="About Us">
				
				<p class="section-index-intro">Donec venenatis metus id velit. Vivamus purus. Mauris in dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Phasellus ornare volutpat mi. Sed velit. Nulla facilisi. Nunc eget libero. Etiam non purus a ante tincidunt volutpat. Donec dignissim, elit in consequat pharetra, nibh ligula aliquam lectus, sit amet lobortis ipsum purus non orci. Nunc posuere sapien eu arcu. Curabitur ullamcorper urna facilisis tellus.</p>

				<p class="section-index-intro">Donec nisi dolor, molestie nec, tempus et, aliquam sed, ante. Nullam nunc quam, faucibus ac, mollis eu, dapibus et, magna. Sed dui. Morbi molestie auctor arcu. Maecenas nisi justo, placerat ut, ornare in, porta eget, libero. Vivamus semper.</p>
				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Bios of Researchers</a></div>
				
					<p class="section-index-intro">Phasellus sed est. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse venenatis convallis nisl. Sed mi velit, volutpat et, dapibus vitae, accumsan id, risus. Ut lorem. Aenean pharetra, lectus ac bibendum tincidunt, est turpis blandit tortor, at sodales lectus nibh quis arcu. Mauris suscipit.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">About the Grand and Partners</a></div>
				
					<p class="section-index-intro">Aenean dictum sapien eget neque. Aliquam dapibus tellus eu lacus. Maecenas hendrerit semper ipsum. Cras varius leo. Nunc dolor ligula, tempus nec, ultrices in, viverra sodales, lorem. Vestibulum erat quam, vestibulum sed, tempor ac, tempus ut, ligula.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Contact Us</a></div>
				
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