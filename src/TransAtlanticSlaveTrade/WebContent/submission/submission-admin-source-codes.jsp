<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Source Code Details</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript">
		function confirmDelete() {
			var confirm_check = confirm('Are you sure you want to delete this source?');
			if(confirm_check == true) {
				return true;
			}
			else {
				return false;
			}
		}
	</script>
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<h1>Source Code Details</h1>
	
	<br>
	
	<div style="width: 550px; border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<table>
			<tr>
				<td>Source Short Code:</td>
				<td><h:outputText value="#{SubmissionSourceCodesBean.checkedSourceId}"/></td>
			</tr>
			<tr>
				<td>Source Description:</td>
				<td><h:inputText value="#{SubmissionSourceCodesBean.checkedName}"/></td>
			</tr>
			<tr>
				<td>Source Type:</td>
				<td><h:inputText value="#{SubmissionSourceCodesBean.checkedType}"/></td>
			</tr>
			
			<tr>
				<td></td>
				<td style="color:red"><h:outputText rendered="#{SubmissionSourceCodesBean.checkedSourceErrorMessage != null}" value="#{SubmissionSourceCodesBean.checkedSourceErrorMessage}"/></td>
			</tr>
			<tr>
				<td></td>
				<td><h:commandButton style="width:120px;" value="< Back" action="main-menu"/> &nbsp; &nbsp;
				<h:commandButton value="Delete Source" action="#{SubmissionSourceCodesBean.deleteSource}" onclick=" return confirmDelete();"/> &nbsp; &nbsp;
				<h:commandButton style="width:120px;" value="Save Source >" action="#{SubmissionSourceCodesBean.updateSource}"/></td>
			</tr>			
		</table>
	</div>
</h:form>
</f:view>
</body>
</html>