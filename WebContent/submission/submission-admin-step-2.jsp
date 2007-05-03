<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<script src="../scripts/grid-editor.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<div style="margin: 10px;">
	<h1>Request details</h1>
	
	<br>
	
	<h:dataTable var="info" value="#{AdminSubmissionBean.submissionInfo}" styleClass="image-detail-info">
		<h:column>
			<t:htmlTag value="div" styleClass="image-detail-info-label">
				<h:outputText value="#{info.name}" />
			</t:htmlTag>
		</h:column>
		<h:column>
			<h:outputText value="#{info.value}" />
		</h:column>
	</h:dataTable>
			
	<br>
	
	
	<h:commandButton action="back-to-admin-main" value="< Back"/>
	<h:commandButton action="back-to-admin-main" value="Continue >"/>
	</div>
</h:form>
</f:view>
</body>
</html>