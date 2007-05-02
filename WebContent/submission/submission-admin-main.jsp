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
	
	<h:commandButton value="Apply changes" action="#{AdminSubmissionBean.submit}"/>
	
	<br>
	
	<div style="width: 400px;">
		Select the voyage using voyage ID you want to edit.
		Then you can use the Lookup button to verify that
		it is really the voyage you are looking for.
	</div>
	
	<div style="border-bottom: 1px solid #CCCCCC; margin-bottom: 10px; margin-top: 5px; padding-bottom: 10px;">
		<table cellpadding="0">
			<tr>
				<th>New voyage requests</th>
				<th>Edit requests</th>
				<th>Merge requests</th>
			</tr>
			<tr>
				<td>
					<h:selectOneListbox value="#{AdminSubmissionBean.newRequest}" style="width: 200px; height: 200px;">
						<f:selectItems value="#{AdminSubmissionBean.newRequests}"/>
					</h:selectOneListbox>
				</td>
				<td>
					<h:selectOneListbox value="#{AdminSubmissionBean.editRequest}" style="width: 200px; height: 200px;">
						<f:selectItems value="#{AdminSubmissionBean.editRequests}"/>
					</h:selectOneListbox>
				</td>
				<td>
					<h:selectOneListbox value="#{AdminSubmissionBean.mergeRequest}" style="width: 200px; height: 200px;">
						<f:selectItems value="#{AdminSubmissionBean.mergeRequests}"/>
					</h:selectOneListbox>
				</td>
			</tr>
			<tr>
				<td>
					<h:commandButton id="b1" value="Resolve selected request >" action="#{AdminSubmissionBean.resolveNew}"/>
				</td>
				<td>
					<h:commandButton id="b2" value="Resolve selected request >" action="#{AdminSubmissionBean.resolveEdit}"/>
				</td>
				<td>
					<h:commandButton id="b3" value="Resolve selected request >" action="#{AdminSubmissionBean.resolveMerge}"/>
				</td>
			</tr>
		</table>
		
	</div>
	
	<div>
	</div>

</h:form>
</f:view>
</body>
</html>