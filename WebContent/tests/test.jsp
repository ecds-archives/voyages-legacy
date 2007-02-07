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
	
	<h:form id="form">
	
	<s:panelTabSet 
		id="mainPanelSection"
		selectedSectionId="#{SearchBean.mainSectionId}">
	
		<s:panelTab title="tab 1" sectionId="listing">
			<h:outputText value="tab 1 contents"/>
		</s:panelTab>

		<s:panelTab title="tab 2" sectionId="maps">
			<h:outputText value="tab 2 contents"/>
		</s:panelTab>
		
	</s:panelTabSet>
	
	</h:form>
	
</f:view>
</body>
</html>