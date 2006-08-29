<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="gallery.css" rel="stylesheet" type="text/css">
	<title>Pictures gallery</title>
</head>

<body>
<f:view>

	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>
	<div class="header-path">
		<a href="search.faces">Database search</a> |
		<a href="reports.faces">Precompiled reports</a> |
		<a href="gallery.faces">Images database</a>
	</div>

	<h:form>

		<s:picture-gallery pictures="#{PicturesBean.pictureGalery}" rows="4" columns="2"
			thumbnailWidth="100" thumbnailHeight="100" action="#{PicturesBean.showinfo}"
			searchCondition="#{PicturesBean.searchCondition}" />

	
	</h:form>
	
</f:view>
</body>
</html>
