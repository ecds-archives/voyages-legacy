<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Images</title>
<script type="text/javascript" src="utils.js"></script>
<script type="text/javascript" src="select-and-order.js"></script>
<script type="text/javascript" src="lookup-select.js"></script>
	
<style type="text/css">

body, input, select {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }
	
div.section {
	font-weight: bold;
	background-color: #DDDDDD;
	padding: 5px;
	margin-bottom: 5px; }

</style>
	
</head>
<body>
<body>
<f:view>
	<h:form id="form" enctype="multipart/form-data">
	
		<div style="font-size: 12pt; font-weight: bold; padding: 0px 5px 5px 5px; font-family: Arial, sans-serif;">
		Trans-Atlantic Trade Slave / Image database
		</div>
		
		<div class="section">Image</div>
	
		<h:graphicImage 
			url="#{ImagesBean.imageUrl}"
			width="#{ImagesBean.image.width}"
			height="#{ImagesBean.image.height}" />
			
		<br>
		
		<h:outputText value="#{ImagesBean.imageInfo}" />
		
		<br>
			
		<h:commandButton
			value="Upload new image"
			action="#{ImagesBean.showUploadBox}"
			rendered="#{!ImagesBean.uploadBoxShown}" />
		
		<h:panelGroup rendered="#{ImagesBean.uploadBoxShown}">
			
			<t:inputFileUpload
				storage="file"
				value="#{ImagesBean.uploadedImage}" />
				
			<h:commandButton
				action="#{ImagesBean.uploadNewImage}"
				value="Upload" />
			
			<h:commandButton
				action="#{ImagesBean.hideUploadBox}"
				value="Cancel" />
				
		</h:panelGroup>
		
		<br>
		<br>

		<div class="section">Image metadata</div>
			
		<table border="0" cellspacing="5" cellpadding="0">
		<tr>
			<td>Name</td>
			<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.name}" /></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.description}" /></td>
		</tr>
		<tr>
			<td>Source</td>
			<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.source}" /></td>
		</tr>
		<tr>
			<td>Date created</td>
			<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.dateCreated}" /></td>
		</tr>
		<tr>
			<td>Painter</td>
			<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.painter}" /></td>
		</tr>
		</table>
			
		<br>

		<div class="section">Database connections</div>
		
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" style="padding-right: 10px;">
			
				<div class="section">Regions</div>

				<s:lookupSelect
					sourceId="#{ImagesBean.regionsLookupSourceId}"
					selectedValues="#{ImagesBean.selectedRegionsIds}" />
			
			</td>
			<td valign="top" style="padding-right: 10px;">
			
				<div class="section">Ports</div>
			
				<s:lookupSelect
					sourceId="#{ImagesBean.portsLookupSourceId}"
					selectedValues="#{ImagesBean.selectedPortsIds}" />
			
			</td>
			<td>
			
				<div class="section">People</div>
				
				<s:lookupSelect
					sourceId="#{ImagesBean.peopleLookupSourceId}"
					selectedValues="#{ImagesBean.selectedPeopleIds}" />

			</td>
		</tr>
		</table>
		
		<div style="margin-top: 10px; border-bottom: 2px solid #CCCCCC; margin-bottom: 10px;"></div>
		
		<h:commandButton value="Save" action="#{ImagesBean.saveImage}" />
		<h:commandButton value="Cancel" action="#{ImagesBean.cancelEdit}" />

	</h:form>
</f:view>
</body>
</html>