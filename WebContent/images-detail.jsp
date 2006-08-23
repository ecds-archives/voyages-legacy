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
					url="#{ImagesBean.image.imageUrl}"
					width="#{ImagesBean.image.width}"
					height="#{ImagesBean.image.height}" /><br>
					
				Upload new image:
				<t:inputFileUpload
					storage="file"
					value="#{ImagesBean.uploadedImage}" />
					
				<h:outputText value="#{ImagesBean.uploadedImage}" />
			
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
	
	</h:form>
</f:view>
</body>
</html>