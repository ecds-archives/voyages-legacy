<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="../../search.css" rel="stylesheet" type="text/css">
	<title>Insert title here</title>
</head>
<body>
<f:view>
	<h:form id="form">
		<s:picture-gallery pictures="#{PicturesBean.pictureGalery}" rows="4" columns="2"
			thumbnailWidth="100" thumbnailHeight="100" />
			
			
		<h:commandLink value="fsdfdsa" action="showinfo" />
	</h:form>
</f:view>
</body>
</html>