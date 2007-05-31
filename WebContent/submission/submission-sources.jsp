
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
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of submission</td>
			<td class="step-indicator-middle">Submission content</td>
			<td class="step-indicator-active-middle">Sources</td>
			<td class="step-indicator-right">Finish</td>
		</tr>
	</table>

	<h1>Specify sources for your submission</h1>
	
	<br>
	
	<table>
	<tr>
		<td>
			<h:commandButton style="width: 180px;" value="Provide primary source" action="#{SubmissionBean.primarySource}"/>
		</td>
	</tr>
	<tr>
		<td>
			<h:commandButton style="width: 180px;" value="Provide article description" action="#{SubmissionBean.articleSource}"/>
		</td>
	</tr>
	<tr>
		<td>		
			<h:commandButton style="width: 180px;" value="Provide book description" action="#{SubmissionBean.bookSource}"/>
		</td>
	</tr>
	<tr>
		<td>		
			<h:commandButton style="width: 180px;" value="Provide description" action="#{SubmissionBean.otherSource}"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<h2>Submitted sources</h2>
			...
		</td>
	</tr>
	</table>
	<h:commandButton value="< Previous" action="back"/>
	<h:commandButton value="Submit" action="#{SubmissionBean.submit}"/>
</h:form>
</f:view>
</body>
</html>