<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Images database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources.css" rel="stylesheet" type="text/css">
	<link href="../styles/images.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-expandable-box.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="resources" /></td>
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

				<img src="../images/resources-index-title.png" width="350" height="40" border="0" alt="Resources">
				
				<p class="section-index-intro">Etiam pharetra ligula vel eros. Donec sed magna. Maecenas venenatis pede mattis sapien. Fusce convallis, velit quis elementum consectetuer, dui ante tempor nunc, vitae posuere libero tellus eu metus. Pellentesque scelerisque velit in ligula. Cras placerat. Curabitur vestibulum turpis quis leo. Fusce consequat bibendum lorem. Sed facilisis magna vel ipsum. Integer volutpat, urna at cursus nonummy, erat leo fringilla sem, id faucibus nulla odio ac mi. Sed eget nibh. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris vestibulum est ac lectus. Maecenas quis augue. Vestibulum id massa in nulla interdum blandit. Aliquam dignissim, leo ut eleifend commodo, augue metus volutpat orci, sed ultrices risus nibh vel lectus. Duis suscipit erat ac magna. Phasellus sed ligula. Proin convallis urna.</p>

				<p class="section-index-intro">Vivamus erat sem, sodales eu, porta sit amet, interdum id, ante. Duis leo libero, vulputate sit amet, eleifend quis, malesuada ultrices, pede. Duis volutpat. In nec nunc id elit sodales aliquam. Aenean dictum sapien eget neque. Aliquam dapibus tellus eu lacus. Maecenas hendrerit semper ipsum. Cras varius leo. Nunc dolor ligula, tempus nec, ultrices in, viverra sodales, lorem. Vestibulum erat quam, vestibulum sed, tempor ac, tempus ut, ligula.</p>
				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Images</a></div>
				
					<p class="section-index-intro">Donec dolor erat, ultricies sed, luctus eu, fermentum non, eros. Nunc id nulla. Donec tortor massa, lacinia vel, commodo sit amet, adipiscing ut, eros. Aenean et odio vel nibh scelerisque gravida. Mauris semper, lacus sit amet laoreet pretium, eros justo porta quam, in dictum pede augue nec erat.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Names Database</a></div>
				
					<p class="section-index-intro">hasellus sed est. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse venenatis convallis nisl. Sed mi velit, volutpat et, dapibus vitae, accumsan id, risus. Ut lorem. Aenean pharetra, lectus ac bibendum tincidunt, est turpis blandit tortor, at sodales lectus nibh quis arcu. Mauris suscipit. Integer nisi justo, cursus a, bibendum eu, ultricies ac, ligula. Integer eget arcu. Donec vel mi eu orci lacinia consequat.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>