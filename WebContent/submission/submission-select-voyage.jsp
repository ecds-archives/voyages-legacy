<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Submission - step 1</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Submission - select voyage</h1>
	
	<br>
	
	<div style="width: 400px;">
		Select the voyage using voyage ID you want to edit.
		Then you can use the Lookup button to verify that
		it is really the voyage you are looking for.
	</div>
	
	<div style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<h:inputText value="#{SubmissionBean.voyageId}" />
		<h:commandButton value="Lookup ..."/>
	</div>
	
	<div>
		<h:commandButton value="Next >" action="#{SubmissionBean.loadEdit}"/>
	</div>

</h:form>
</f:view>
</body>
</html>