<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Demos</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>
	
	<f:param value="demos" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<img src="../images/help-demos-title.png" width="140" height="50" border="0" alt="Demos">			
			</td>
			<td class="demo-links">
				<a href="demo-overview.faces">Voyages site overview</a>
				<a href="demo-search.faces" class="demo-link-active">Voyages Database search</a>
			</td>
		</tr>
		</table>
	</div>
	
	<div style="margin-left: auto; margin-right: auto; width: 640px; margin-top: 10px;">
	This video demonstrates a basic search for a specific slave vessel in the Database as
	well as the voyage details, Map display, and a related image.
	</div>

	<div style="margin-left: auto; margin-right: auto; padding-top: 19px; width: 640px; background-color: Black; margin-top: 10px;">
		<object classid="clsid:02bf25d5-8c17-4b23-bc80-d3488abddc6b" width="640" height="496" codebase="http://www.apple.com/qtactivex/qtplugin.cab">
			<param name="src" value="demo-search.mov">
			<param name="autoplay" value="false">
			<param name="controller" value="true">
			<embed src="demo-search.mov" width="640" height="496" autoplay="false" controller="true" pluginspage="http://www.apple.com/quicktime/download/">
			</embed>
		</object>
	</div>

	</h:form>
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>