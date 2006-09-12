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

body, input, select {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }

a {
	color: #1177DD; }

table.imagelist-table {
	border-collapse: collapse; }
	
td.imagelist-table-header {
	font-weight: bold; }

table.imagelist-table td {
	padding-right: 10px;
	padding-bottom: 5px;
 	padding-top: 5px;
 	padding-left: 5px;
 	border-bottom: 2px solid #EEEEEE; }
	
img.imagelist-thumbnail {
	border: 2px solid #CCCCCC; }
	
div.imagelist-gallery {
	margin: 5px; }
	
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
		
		<div style="font-size: 12pt; font-weight: bold; padding: 0px 5px 5px 5px; font-family: Arial, sans-serif;">
		Trans-Atlantic Trade Slave / Image database
		</div>
		
		<div style="padding: 5px; background-color: #EEEEEE; border: 2px solid #CCCCCC;">
		
		<h:selectOneMenu value="#{ImagesBean.listStyle}">
			<f:selectItem itemLabel="Table" itemValue="table" />
			<f:selectItem itemLabel="List" itemValue="list" />
			<f:selectItem itemLabel="Gallery" itemValue="gallery" />
		</h:selectOneMenu>
		
		<h:selectOneMenu value="#{ImagesBean.thumbnailSize}">
			<f:selectItem itemLabel="Small" itemValue="32x32" />
			<f:selectItem itemLabel="Medium" itemValue="48x48" />
			<f:selectItem itemLabel="Big" itemValue="64x64" />
			<f:selectItem itemLabel="Huge" itemValue="128x128" />
		</h:selectOneMenu>

		<h:inputText value="#{ImagesBean.searchInListFor}" />
		<h:commandButton value="OK" />
		
		|
		
		<h:commandButton action="#{ImagesBean.newImage}" value="New image" />
		
		</div>
		
		<s:imageList
			id="images"
			columns="Size, Image type, Date, Creator, Source"
			images="#{ImagesBean.allImages}"
			listStyle="#{ImagesBean.listStyle}"
			selectedImageId="#{ImagesBean.selectedImageId}"
			action="#{ImagesBean.openImage}" />

	</h:form>
</f:view>
</body>
</html>