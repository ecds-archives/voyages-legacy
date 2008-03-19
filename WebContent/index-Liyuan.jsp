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
<h:form id="main">

<div id="top-bar">
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><div class="top-bar-logo">
			<a href="index.faces"><img src="new-images/logo.png" border="0" width="300" height="127"></a>
		</div></td>
		<td><div class="top-bar-right">
	
				<div class="main-menu-container" border="4"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" />			
		 		<div class="main-menu-help"> <s:mainMenuBar menuItems="#{MainMenuBean.help}"/>				
	
		</div></td>
	</tr>
	</table>
</div>

<%/*	
<div id="content">
<table class="section-index-layout" border="0" cellspacing="0" cellpadding="0" >
	<tr>
 		<td class="index-middle-left">
 			<table class="welcome-map" border="0" cellspacing="0" cellpadding="0" >
 			<tr><td> 	
 				<img src="./new-images/map2.png" border="0" width="378" height="356">		
 			</td>
 			</tr>
 			<tr>
 			<td>
				<img src="./new-images/index-maptext-bg.png" border="0" width="378" height="300" >		
 			</td></tr>
 			</table>
 		<td>
 		<td class="section-index-right-column">					
			<br>
				<img src="./new-images/index-title.png" border="0" width="466" height="56">		
			<br>
				<img src="./new-images/index-intro.png" border="0" width="440" height="95">		
			<br>
		</td>
	</tr>
</table>
</div>
*/ %>

<table border="0" cellspacing="0" cellpadding="0">
	<tr>	   
		<td class="index-middle-right"> 
			<br>
				<img src="./new-images/index-title.png" border="0" width="466" height="56">		
			<br>
				<img src="./new-images/index-intro.png" border="0" width="440" height="95">		
			<div class="index-middle-text"> 
			 	<a href="./database/search.faces">Search the database ></a>
			 <br>
				<a href="./assessment/estimate.faces">View the estiamtes ></a>
			<br>
				<a href="./resources/slaves.faces">Explore the names database ></a>
			</div>
			<div class="index-bottom-right">
				<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><img src="./new-images/index-bottom-intromap.png"></td>
					<td class="index-bottom-text">
						<a href="./assessment/intromaps.faces">
							<h>Introdutory Maps</h>
							<p>Nine maps showing the geographic evolution of the slave trade ></p>
						</a>
					</td>
				</tr>
				<tr>
					<td><img src="./new-images/index-bottom-hismap.png"></td>
					<td class="index-bottom-text">
						<a href="./resources/images.faces">
							<h>Historial images</h>
							<p>of people, ships, places and documents ></p>
						</a>
					</td>
				</tr>
				</table>
			</div>
		</td>
		<td class="index-middle-left">
			<table class="welcome" border="0" cellspacing="0" cellpadding="0">			
			<tr><td>
			<s:welcomeMap
					id="welcomeMap"
					initialText="#{HomePageBean.welcomeMapInitialText}"
					imageUrl="#{HomePageBean.welcomeMapImage}"
					imageHeight="#{HomePageBean.welcomeMapHeight}"
					imageWidth="#{HomePageBean.welcomeMapWidth}"
					places="#{HomePageBean.welcomeMapPlaces}" />			
			</td></tr>
			</table>		
		</td>		
	</tr>
</table>

<% /*
	<div class="index-bottom">
	
		
		<div style="padding: 20px 0px 0px 20px;"><img src="./images/index-highlights.png" width="175" height="22" border="0" alt="Website Highlights"></div>
		
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
	

	<div class="index-footer">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="footer-text"> Sponsor</td>
			<td class="footer-image"><a href="http://www.emory.edu"><img src="./new-images/footer-emory.png"></a></td>
			<td class="footer-image"> <img src="./new-images/footer-NEH.png"></td>
			<td class="footer-text"> W.E.B. Du Bois Institute </td>	
			<td class="footer-text"> <img src="./new-images/index-footer-verticalline.png"></td>
			<td class="footer-text"> Project Partner </td>
			<td class="footer-text"><h><a href="http://web.library.emory.edu/"> Emory University Libraries </a></h></td>
			<td class="footer-text"> The University of Hull (UK) </td>
			<td class="footer-text"> Universidad Federal Do Rio de Jeneiro(Brazil) </td>
			<td class="footer-text"> Victorial University of Wellington (New Zealand) </td>
		</tr>
		</table>
	</div>
	
</h:form>
</f:view>
</body>
</html>