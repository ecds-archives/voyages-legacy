<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="gallery.css" rel="stylesheet" type="text/css">
	<script src="gallery.js" language="javascript" type="text/javascript"></script>
	<title>Images gallery</title>
</head>

<body>
<f:view>


	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>

	<div class="header-links">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><div class="header-link"><a href="estimates.faces">Estimates</a></div></td>
			<td><div class="header-link"><a href="search.faces">Database search</a></div></td>
			<td><div class="header-link-active"><a href="galleryp.faces?obj=1&id=1&set=1&pict=0">Images database</a></div></td>
		</tr>
		</table>
	</div>

	<h:form id="form">

		<t:htmlTag value="div">
			<h:outputText escape="false" value="&nbsp"/>
			<%/*
			<t:div id="gallsery-div-back-to-list" style="text-align: center;">
				<h:outputText value="#{PicturesBean.currentPath}"/>
				<t:htmlTag value="br"/>
				<h:outputLink value="gallery.faces?gal=#{PicturesBean.lastGalleryName}">
					<h:outputText value="Back to gallery list"/>
				</h:outputLink>
			</t:div>
			*/%>
			<s:picture-gallery pictures="#{PicturesBean.pictureGalery}" rows="5" columns="1"
				thumbnailWidth="150" thumbnailHeight="100" galleryParams="#{GalleryRequestBean.galleryParams}" />
		 </t:htmlTag>

	
	</h:form>
	
</f:view>
</body>
</html>
