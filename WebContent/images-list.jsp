<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Images</title>

<style type="text/css">

body {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }

table.imagelist-table {
	border-collapse: collapse; }
	
table.imagelist-table td {
	padding-right: 10px;
	padding-bottom: 5px;
 	padding-top: 5px; }
	
img.imagelist-thumbnail {
	border: 2px solid #CCCCCC; }
	
div.imagelist-gallery-image {
	text-align: center;
	margin-right: 10px;
	margin-bottom: 10px;
	float: left; }

</style>
	
</head>
<body>
<f:view>
	<h:form id="form">
	
		<input type="hidden" name="scrollPosX">
		<input type="hidden" name="scrollPosY">
		
		<h:selectOneMenu value="#{ImagesBean.listStyle}">
			<f:selectItem itemLabel="Table" itemValue="table" />
			<f:selectItem itemLabel="List" itemValue="list" />
			<f:selectItem itemLabel="Gallery" itemValue="gallery" />
		</h:selectOneMenu>
		
		<h:commandButton value="Change" />
		
		<hr>
		
		<s:imagelist
			id="images"
			images="#{ImagesBean.allImages}"
			listStyle="#{ImagesBean.listStyle}"
			onImageSelected="#{ImagesBean.openImageListener}"
			action="#{ImagesBean.openImageAction}"
			thumbnailWidth="#{ImagesBean.listThumbnailWidth}" 
			thumbnailHeight="#{ImagesBean.listThumbnailHeight}" />

	</h:form>
</f:view>
</body>
</html>