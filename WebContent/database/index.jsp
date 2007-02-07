<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>The Database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="database" /></td>
		</tr>
		</table>
	</div>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
			
				<s:expandableBox text="Quick Start">
					<h:outputText value="Nulla libero massa, condimentum quis, varius eu, volutpat vitae, massa. Pellentesque pellentesque libero sed nisi. Integer vitae elit. Suspendisse sollicitudin eros quis odio." />
				</s:expandableBox>
				
				<br>
				
				<s:expandableBox text="Sample Queries">
					<f:verbatim escape="false">
						<ul class="box">
							<li>What was the first country to get slaves from Africa?</li>
							<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
							<li>Why did European interest in Africa turn from the slave trade to colonization?</li>
							<li>Where were they taken to?</li>
							<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
							<li>Who sold the slaves to the European slavers?</li>
							<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
							<li>Why were the slaves taken from Africa?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>Which country brought more slaves from Africa than any other country?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>show me more queries...</li>
						</ul>
					</f:verbatim>
				</s:expandableBox>
				
			</td>
			<td class="section-index-right-column">

				<img src="../images/database-index-title.png" width="350" height="40" border="0" alt="The Database">
				
				<p class="section-index-intro">Etiam a metus. Curabitur lectus. Ut a neque. Cras non nisl. Nam eget risus. Nunc blandit, pede sed aliquet tincidunt, dolor felis mollis ipsum, id condimentum leo felis a nisl. Praesent ac nisl. Ut tincidunt tincidunt justo. Nullam est ipsum, molestie varius, sollicitudin vel, pretium eu, est. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nam ornare, tellus eu varius molestie, tellus libero imperdiet nibh, eget lacinia orci ante at tellus. Mauris suscipit, enim ac varius convallis, pede nisi tincidunt purus, ac vestibulum diam libero eu mauris. Duis lacus mi, elementum a, aliquam in, ultricies consectetuer, enim. Nam porta, massa et commodo egestas, risus elit volutpat risus, ac dignissim sapien arcu in mi. Ut fringilla. Etiam nulla libero, fringilla sed, condimentum vel, lacinia eu, ligula. Quisque imperdiet tincidunt erat.</p>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Search the Database</a></div>
				
					<p class="section-index-intro">Suspendisse sollicitudin eros quis odio. Nulla libero massa, condimentum quis, varius eu, volutpat vitae, massa. Pellentesque pellentesque libero sed nisi. Integer vitae elit.</p>
					
					<p class="section-index-intro">Mauris eu quam nec sem euismod semper. Phasellus laoreet leo quis leo. In massa tortor, sollicitudin id, tincidunt in, aliquam a, turpis. Duis vitae nulla. Nam vehicula, felis sit amet eleifend sagittis, libero elit molestie augue, a commodo dui nisi ultricies quam.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Download the Database</a></div>
				
					<p class="section-index-intro">Nullam posuere. Aliquam malesuada mollis leo. Nam mauris elit, rutrum consequat, cursus non, sodales id, erat. Phasellus bibendum gravida nulla. Praesent elit erat, viverra quis, aliquet sit amet, vestibulum tristique, dolor. Vivamus fringilla malesuada nisi. Suspendisse aliquam nunc non lacus egestas sodales.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Methodology</a></div>
				
					<p class="section-index-intro">Praesent at eros id augue feugiat rutrum. Aenean vitae nulla. Sed ultrices, diam quis ultricies dapibus, est ipsum consequat ipsum, quis adipiscing lorem leo sed felis. Aenean venenatis sollicitudin tellus. Maecenas ut erat. Fusce quis est. In porta nulla vel velit. Proin lobortis, sem ac lacinia dignissim, urna massa interdum elit, eu euismod eros eros eu leo. Suspendisse vel pede. Vivamus in sem sit amet nunc porttitor placerat.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>