<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Submission</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission-grid.css" rel="stylesheet" type="text/css">
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/grid-editor.js" language="javascript" type="text/javascript"></script>
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
	<script src="../scripts/tooltip.js" type="text/javascript" langiage="javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<h2>1. Request details</h2>

	<s:gridEditor
		rows="#{AdminSubmissionBean.rows}"
		columns="#{AdminSubmissionBean.columns}"
		values="#{AdminSubmissionBean.values}"
		fieldTypes="#{AdminSubmissionBean.fieldTypes}"
		rowGroups="#{AdminSubmissionBean.rowGroups}"
		onColumnAction="#{AdminSubmissionBean.columnAction}" />
	<br>
		
	<s:gridEditor 
		id="grid-slave"
		rows="#{AdminSubmissionBean.rowsSlave}"
		columns="#{AdminSubmissionBean.columnsSlave}"
		values="#{AdminSubmissionBean.valuesSlave}"
		rowGroups="#{AdminSubmissionBean.rowGroupsSlave}"
		fieldTypes="#{AdminSubmissionBean.fieldTypesSlave}" />
	
	
	<br>
	<br>
	
	<t:htmlTag value="div" 
	           rendered="#{AdminSubmissionBean.isSourceDetailsVisible}" 
	           style="border: 2px solid #CCCCCC; width: 500px; padding: 5px;" >
        <h:outputText style="padding-left: 4px; font-weight: bold; font-style: italic;" value="Source details"/>
		<t:dataTable value="#{AdminSubmissionBean.sourceData}" var="data" style="padding-top: 10px;">	
			<h:column>
				<t:div style="width: 200px;">
					<h:outputText style="font-weight: bold;" value="#{data.name}"/>
				</t:div>
    		</h:column>
    		<h:column>
        		<h:outputText value="#{data.value}"/>
    		</h:column>
		</t:dataTable>
		<h:commandButton value="Close" action="#{AdminSubmissionBean.closeDetails}"/>
	</t:htmlTag>
	
	<h2>2. Submitted sources</h2>
	
	<s:grid id="sources" 
			columns="#{AdminSubmissionBean.sourcesColumns}"
			rows="#{AdminSubmissionBean.sourcesRows}" 
			onOpenRow="#{AdminSubmissionBean.openSourcesRow}" />
	<br>
	<br>

	<h:commandButton value="Add reviewer" 
	                 rendered="#{AdminSubmissionBean.isChiefEditor && !AdminSubmissionBean.addingEditor}"
					 action = "#{AdminSubmissionBean.addEditor}"/>
	<t:htmlTag value="div" 
	           rendered="#{AdminSubmissionBean.addingEditor}" 
	           style="border: 2px solid #CCCCCC; width: 400px; padding: 5px;" >
		<h:selectOneMenu value="#{AdminSubmissionBean.newEditorUser}">
			<f:selectItems value="#{SubmissionUsersBean.editorUsers}"/>
		</h:selectOneMenu>
		<h:commandButton value="Add reviewer" action="#{AdminSubmissionBean.applyAddEditor}"/>
		<h:commandButton value="Cancel" action="#{AdminSubmissionBean.cancelAddEditor}"/>
	</t:htmlTag>
	
	<br>
	<br>
	<t:htmlTag value="div" rendered="#{!AdminSubmissionBean.isChiefEditor}">
		<f:verbatim><h2>3. Request status</h2>
		<table>
			<tr>
				<td><f:verbatim><h:selectBooleanCheckbox value="#{AdminSubmissionBean.finished}" /></f:verbatim>
				</td>
				<td><f:verbatim> <h:outputText value="I am done with this submission and it does not require my further attention"/></f:verbatim>
				</td>
			</tr>
		</table></f:verbatim>
	</t:htmlTag>
	<br>
	<br>
	<h:commandButton value="< Back" action="back"/>
	<h:commandButton value="Save" action="#{AdminSubmissionBean.save}" rendered="#{AdminSubmissionBean.rejectAvailable}"/>
	<h:commandButton value="Apply changes" action="#{AdminSubmissionBean.submit}" rendered="#{AdminSubmissionBean.isAdmin}"/>
	<h:commandButton value="Reject request" action="#{AdminSubmissionBean.rejectSubmission}" rendered="#{AdminSubmissionBean.isAdmin}"/>
	<br>
</h:form>
</f:view>
</body>
</html>