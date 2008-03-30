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
	<script src="./scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="./scripts/slideshow.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	
	<f:param value="-" binding="#{MainMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div style="background-color: #05768A; color: White; padding: 4px 0px 4px 10px;">
		You are now on the homepage of the Trans-Atlantic Slave Trade Database
	</div>
	
	<div class="welcome-band">
	
		<table border="0" cellspacing="0" cellpadding="0" class="welcome-band">
		<tr>
			<td valign="top">
				<div id="welcome-image-frame"><img id="welcome-image" src="./images/index/slideshow-solomon.png" width="320" height="180" border="0" /></div>
				<div class="welcome-image-caption-and-buttons">
					<table border="0" cellspacing="0" cellpadding="0" class="welcome-image-caption-and-buttons">
					<tr>
						<td valign="top">
							<div id="welcome-image-caption">
							Image of Job Ben Solomon, a well know slave
							who himself Royal African Company in Gambia
							</div>
						</td>
						<td align="right" valign="top">
							<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td style="padding-right: 5px;"><img class="welcome-image-nav-button" src="./images/index/prev-image.png" width="22" height="22" border="0" onclick="SlideshowGlobals.prevSlide('welcome-slideshow')" /></td>
								<td><img class="welcome-image-nav-button" src="./images/index/next-image.png" width="22" height="22" border="0" onclick="SlideshowGlobals.nextSlide('welcome-slideshow')" /></td>
							</tr>
							</table>
						</td>
					</tr>
					</table>
				</div>
			</td>
			<td valign="top">
				<div class="welcome-text-big"><img src="./images/index/welcome-text.png" width="560" height="70" border="0" /></div>
				<div class="welcome-text-small">
					forcibly transporting over 12 million Africans to the Americas.
					It offers descendants, researchers, students and the general public
					a chance to rediscover the reality of one of the largest movements
					of people in the world history.
				</div>
			</td>
		</tr>
		</table>
	
	</div>
	
	<script language="javascript" type="text/javascript">

	SlideshowGlobals.registerSlideshow(new Slideshow(
		"welcome-slideshow",
		"welcome-image-frame",
		"welcome-image",
		"welcome-image-caption",
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		true,
		[
			new SlideshowImage(
				"./images/index/slideshow-solomon.png",
				"Image of Job Ben Solomon, a well know slave who himself Royal African Company in Gambia."),
			new SlideshowImage(
				"./images/index/slideshow-map.png",
				"Some different image. Some different image. Some different image. Some different image."),
			new SlideshowImage(
				"./images/index/slideshow-ship.png",
				"Description of another image. Description of another image. Description of another image.")
		]
		));
	
	</script>
	
	<div class="featured-sections-title">
		<img src="./images/index/discover-the-site.png" width="210" height="40" border="0">
	</div>
	
	<table border="0" cellspacing="0" cellpadding="0" class="featured-sections">
	<tr>
		<td valign="top">
		
			<div class="featured-section-image">
			
			</div>
			<div class="featured-section-caption">
				Database of Voyages
			</div>
			<div class="featured-section-description">
				When you want to use a color that already exists in an
				object or document, you can sample the color to achieve
				an exact match. 
			</div>
			<ul class="featured-section-links">
				<li><a href="#">Browse and Search the Database</a></li>
				<li><a href="#">Introduction to the Database</a></li>
				<li><a href="#">How can I contribute</a></li>
			</ul>
		
		</td>
		<td valign="top">
		
			<div class="featured-section-image">
			
			</div>
			<div class="featured-section-caption">
				Slave Trade Estimates
			</div>
			<div class="featured-section-description">
				You may find that the colors displayed on your monitor don’t
				match the colors of a scanned image or of a printer’s
				output. 
			</div>
			<ul class="featured-section-links">
				<li><a href="#">Browse Estimates</a></li>
				<li><a href="#">How is this different from the database?</a></li>
			</ul>
		
		</td>
		<td valign="top">
		
			<div class="featured-section-image">
			
			</div>
			<div class="featured-section-caption">
				Database of Slaves
			</div>
			<div class="featured-section-description">
				Color management lets you reproduce colors accurately
				by using color profiles and by correcting colors for display.
				output.
			</div>
			<ul class="featured-section-links">
				<li><a href="#">Browse the Names Database</a></li>
				<li><a href="#">Introduction to the Names Database</a></li>
			</ul>
		
		</td>
		<td valign="top">
		
			<div class="featured-section-image">
			
			</div>
			<div class="featured-section-caption">
				Historical Images
			</div>
			<div class="featured-section-description">
				You may find that the colors displayed on your monitor don’t
				match the colors of a scanned image or of a printer’s
				output. 
			</div>
			<ul class="featured-section-links">
				<li><a href="#">Browse the Historical Images</a></li>
			</ul>
		
		</td>
	</tr>
	</table>
	
	<div class="footer">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="http://www.emory.edu" class="footer-link"><img src="./images/emory-logo.png" width="82" height="32" border="0" /></a></td>
			<td><a href="#" class="footer-link">Woodruff Library<br>Digital Programs (USA)</a></td>
			<td><a href="#" class="footer-link">The University<br>of Hull (UK)</a></td>
			<td><a href="#" class="footer-link">Universidad of Federal<br>do Rio de Janeiro (Brazil)</a></td>
			<td><a href="#" class="footer-link">Victoria University<br>of Wellington (New Zealand)</a></td>
		</tr>
		</table>
	</div>

</f:view>
</body>
</html>