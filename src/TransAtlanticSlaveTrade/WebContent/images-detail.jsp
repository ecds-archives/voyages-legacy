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
<script type="text/javascript" src="./scripts/utils.js"></script>
<script type="text/javascript" src="./scripts/select-and-order.js"></script>
<script type="text/javascript" src="./scripts/lookup-select.js"></script>
	
<style type="text/css">

body, input, select, textarea {
	font-family: Verdana, Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	font-size: 8pt; }
	
div.section {
	font-weight: bold;
	background-color: #DDDDDD;
	padding: 5px;
	margin-bottom: 5px; }
	
div.error {
	margin-bottom: 5px;
	padding: 5px;
	font-weight: bold;
	color: White;
	background-color: #FF5500; }
	
select.lookup-select {
	width: 400px; }

</style>
	
</head>
<body>
<body>
<f:view>
	<h:form id="form" enctype="multipart/form-data">
	
		<div style="font-size: 12pt; font-weight: bold; padding: 0px 5px 5px 5px; font-family: Arial, sans-serif;">
		Trans-Atlantic Slave Trade / Image database
		</div>
		
		<t:htmlTag value="div" styleClass="error" rendered="#{AdminImagesBean.errorText != null}">
			<h:outputText value="#{AdminImagesBean.errorText}" />
		</t:htmlTag>
		
		<div class="section">Image</div>
	
		<h:graphicImage 
			url="#{AdminImagesBean.imageUrl}"
			width="#{AdminImagesBean.imageWidth}"
			height="#{AdminImagesBean.imageHeight}" />
			
		<br>
		
		<h:outputText value="#{AdminImagesBean.imageInfo}" />
		
		<br>
			
		<h:commandButton
			value="Upload new image"
			action="#{AdminImagesBean.showUploadBox}"
			rendered="#{!AdminImagesBean.uploadBoxShown}" />
		
		<h:panelGroup rendered="#{AdminImagesBean.uploadBoxShown}">
			
			<t:inputFileUpload
				storage="file"
				value="#{AdminImagesBean.uploadedImage}" />
				
			<h:commandButton
				action="#{AdminImagesBean.uploadNewImage}"
				value="Upload" />
			
			<h:commandButton
				action="#{AdminImagesBean.hideUploadBox}"
				value="Cancel" />
				
		</h:panelGroup>
		
		<br>
		<br>

		<div class="section">External metadata</div>
			
		<table border="0" cellspacing="0" cellpadding="0"><tr><td valign="top">

			<table border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td style="width: 80px;">Title</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageTitle}" /></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><h:inputTextarea rows="5" style="width: 300px;" value="#{AdminImagesBean.imageDescription}" /></td>
			</tr>
			<tr>
				<td>ID for link</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageExternalId}" /></td>
			</tr>
			</table>
		
		</td><td style="width: 20px;"></td><td valign="top">
		
			<table border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td style="width: 100px;">Category</td>
				<td>
					<h:selectOneMenu value="#{AdminImagesBean.imageCategoryId}">
						<f:selectItems value="#{AdminImagesBean.detailCategories}" />
					</h:selectOneMenu>
				</td>
			</tr>
			<tr>
				<td>Source</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageSource}" /></td>
			</tr>
			<tr>
				<td>Date</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageDate}" /></td>
			</tr>
			<tr>
				<td>Creator</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageCreator}" /></td>
			</tr>
			<tr>
				<td>Language</td>
				<td>
					<h:selectOneMenu value="#{AdminImagesBean.imageLanguage}">
						<f:selectItems value="#{AdminImagesBean.languages}" />
					</h:selectOneMenu>
				</td>
			</tr>
			</table>

		</td></tr></table>
			
		<br>

		<div class="section">Internal metadata</div>

		<table border="0" cellspacing="0" cellpadding="0"><tr><td valign="top">

			<table border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td style="width: 80px;">Comments</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageComments}" /></td>
			</tr>
			<tr>
				<td>References</td>
				<td><h:inputTextarea rows="5" style="width: 300px;" value="#{AdminImagesBean.imageReferences}" /></td>
			</tr>
			</table>

		</td><td style="width: 20px;"></td><td valign="top">

			<table border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td style="width: 100px;">Is at Emory</td>
				<td><h:selectBooleanCheckbox value="#{AdminImagesBean.imageEmory}" /></td>
			</tr>
			<tr>
				<td>Emory location</td>
				<td><h:inputText style="width: 300px;" value="#{AdminImagesBean.imageEmoryLocation}" /></td>
			</tr>
			<tr>
				<td>Authorization</td>
				<td><h:selectOneMenu value="#{AdminImagesBean.imageAuthorizationStatus}">
					<f:selectItems value="#{AdminImagesBean.authorizationStatusItems}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td>Image status</td>
				<td><h:selectOneMenu value="#{AdminImagesBean.imageImageStatus}">
					<f:selectItems value="#{AdminImagesBean.imageStatusItems}" />
				</h:selectOneMenu></td>
			</tr>
			<tr>
				<td>Ready to go</td>
				<td><h:selectBooleanCheckbox value="#{AdminImagesBean.imageReadyToGo}" /></td>
			</tr>
			</table>

		</td></tr></table>

		<br>

		<div class="section">Database connections - voyages</div>
		
		<table border="0" cellspacing="0" cellpadding="0"><tr><td valign="top">
		
			<table border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td style="width: 80px;">Voyages</td>
				<td><h:inputTextarea style="width: 300px;" rows="10" value="#{AdminImagesBean.imageVoyageIds}" /></td>
			</tr>
			</table>
			
		</td><td style="width: 20px;"></td><td valign="top" style="padding-top: 10px; padding-left: 5px; font-style: italic; width: 300px;">
			
			Notes:
			Insert voyage IDs on separate lines.
			Voyage IDs are not validated agains the database.
			You can create links to voyages do not exist yet.

		</td></tr></table>
		
		<div class="section">Database connections - ports and regions</div>

		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" style="padding-left: 5px; padding-right: 10px;">
			
				<div style="margin-bottom: 5px; font-weight: bold;">Regions</div>

				<s:lookupSelect
					id="regions"
					sourceId="#{AdminImagesBean.regionsLookupSourceId}"
					selectedValues="#{AdminImagesBean.selectedRegionsIds}" />
			
			</td>
			<td valign="top" style="padding-right: 10px;">
			
				<div style="margin-bottom: 5px; font-weight: bold;">Ports</div>
			
				<s:lookupSelect
					id="ports"
					sourceId="#{AdminImagesBean.portsLookupSourceId}"
					selectedValues="#{AdminImagesBean.selectedPortsIds}" />
			
			</td>
		</tr>
		</table>
		
		<div style="margin-top: 10px; border-bottom: 2px solid #CCCCCC; margin-bottom: 10px;"></div>
		
		<h:commandButton value="Save" action="#{AdminImagesBean.saveImage}" />
		<h:commandButton value="Delete" onclick="if (!confirm('Are you sure?')) return false;" action="#{AdminImagesBean.deleteImage}" rendered="#{AdminImagesBean.editedImageId != 0}" />
		<h:commandButton value="Cancel" action="#{AdminImagesBean.cancelEdit}" />

	</h:form>
</f:view>
</body>
</html>