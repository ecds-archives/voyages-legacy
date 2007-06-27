<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Image</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/resources-images-expanded.css" rel="stylesheet" type="text/css">
	<link href="../styles/images-commons.css" rel="stylesheet" type="text/css">
</head>
<body id="image-expanded">
<f:view>
<h:form id="main">

	<div class="image-expanded-title">
		<h:outputText value="#{ImagesBean.imageTitle}" />
	</div>

	<div class="image-expanded-container">
		<h:graphicImage url="#{ImagesBean.imageExpandedURL}" />
	</div>

</h:form>
</f:view>
</body>
</html>