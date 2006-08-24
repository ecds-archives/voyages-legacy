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

body {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }

</style>
	
</head>
<body>
<body>
<f:view>
	<h:form id="form" enctype="multipart/form-data">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top">

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
					
			</td>
			<td valign="top">
			
				<table border="0" cellspacing="5" cellpadding="0">
				<tr>
					<td>Name</td>
					<td><h:inputText value="#{ImagesBean.image.name}" /></td>
				</tr>
				<tr>
					<td>Description</td>
					<td><h:inputText style="width: 300px;" value="#{ImagesBean.image.description}" /></td>
				</tr>
				<tr>
					<td></td>
					<td><h:commandButton value="Save" action="#{ImagesBean.saveImage}" /></td>
				</tr>
				</table>
			
			</td>
		</tr>
		</table>
		
		<!-- 
		<div class="section">Regions</div>
		
		<s:selectAndOrder
			sortable="false"
			availableItems="#{ImagesBean.availableRegions}"
			selectedItems="#{ImagesBean.selectedRegions}" />
	
		<div class="section">Ports</div>

		<s:selectAndOrder
			sortable="false"
			availableItems="#{ImagesBean.availablePorts}"
			selectedItems="#{ImagesBean.selectedPorts}" />
			
		 -->

		<div class="section">People</div>
		
		<s:lookupSelect
			sourceId="#{ImagesBean.peopleLookupSourceId}"
			selectedValues="#{ImagesBean.selectedPeopleIds}" />

	</h:form>
</f:view>
</body>
</html>