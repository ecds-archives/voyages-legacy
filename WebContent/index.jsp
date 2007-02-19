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

			<div style="padding: 20px 0px 0px 300px;">

			<s:welcomeMap
				id="welcomeMap"
				imageUrl="#{HomePageBean.welcomeMapImage}"
				imageHeight="#{HomePageBean.welcomeMapHeight}"
				imageWidth="#{HomePageBean.welcomeMapWidth}"
				places="#{HomePageBean.welcomeMapPlaces}" />
				
			</div>
		
		</div>
	</div>

</f:view>
</body>
</html>
