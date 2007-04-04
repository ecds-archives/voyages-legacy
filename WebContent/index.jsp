<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Trans-Atlantic Slave Trade</title>
	<link href="./styles/main.css" rel="stylesheet" type="text/css">
	<link href="./styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="./styles/index.css" rel="stylesheet" type="text/css">
	<script src="./scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="./scripts/welcome-map.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	
	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="index.faces"><img src="./images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" /></td>
		</tr>
		</table>
	</div>
	
	<div class="index-middle">
		<div class="index-middle-img">
		
			<div style="margin-left: 0px;">
				<s:welcomeMap
					id="welcomeMap"
					initialText="#{HomePageBean.welcomeMapInitialText}"
					imageUrl="#{HomePageBean.welcomeMapImage}"
					imageHeight="#{HomePageBean.welcomeMapHeight}"
					imageWidth="#{HomePageBean.welcomeMapWidth}"
					places="#{HomePageBean.welcomeMapPlaces}" />
			</div>
	
		</div>
	</div>
	
	<div class="index-bottom">
	
		<% /*
		
		<div style="padding: 20px 0px 0px 20px;"><img src="./images/index-highlights.png" width="175" height="22 border="0" alt="Website Highlights"></div>
		
		<div class="index-highlight">
			<div class="index-highlight-title"><a href="#">Database of voyages</a></div>
			<div class="index-highlight-text">
				Duis consequat tellus nec leo. Sed vestibulum tincidunt mauris. Nulla scelerisque.
				Sed non lorem. Integer vitae orci in purus fringilla tempus. Duis consequat tellus nec leo.</div>
		</div>
		
		<div class="index-highlight">
			<div class="index-highlight-title"><a href="#">Slave trade assesment</a></div>
			<div class="index-highlight-text">
				Sed non lorem. Duis consequat tellus nec leo. Sed vestibulum tincidunt mauris. Nulla scelerisque.
				Sed non lorem. Duis consequat tellus nec leo. Sed vestibulum tincidunt mauris. Nulla scelerisque.</div>
		</div>

		<div class="index-highlight">
			<div class="index-highlight-title"><a href="#">Database of images</a></div>
			<div class="index-highlight-text">
				Sed vestibulum tincidunt mauris. Nulla scelerisque. Sed non lorem. Maecenas hendrerit, sem at ornare semper,
				tellus ante tempor est, vel viverra nunc orci eget lectus. Maecenas hendrerit.</div>
		</div>

		<div class="index-highlight">
			<div class="index-highlight-title"><a href="#">Database of slaves</a></div>
			<div class="index-highlight-text">
				Maecenas hendrerit, sem at ornare semper, tellus ante tempor est, vel viverra nunc orci eget lectus.
				Morbi sapien felis, semper at, blandit non, fermentum in, eros.  Duis consequat tellus nec leo.</div>
		</div>
		
		*/ %>

	</div>

</f:view>
</body>
</html>