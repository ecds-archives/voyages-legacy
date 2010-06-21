<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Submission</title>
<link href="../styles/main.css" rel="stylesheet" type="text/css">
<link href="../styles/submission-grid.css" rel="stylesheet"
	type="text/css">
<script src="../scripts/utils.js" language="javascript"
	type="text/javascript"></script>
<script src="../scripts/grid-editor.js" language="javascript"
	type="text/javascript"></script>
<link href="../admin/main.css" rel="stylesheet" type="text/css">
<script src="../scripts/tooltip.js" type="text/javascript"
	language="javascript"></script>
</head>
<body>
<f:view>
	<h:form id="main">
		<I><h:outputText rendered="#{!AdminSubmissionBean.isChiefEditor}" value="Click &quot;Copy&quot; link to copy over existing information or adding new information to the field."/></I>
		<h2>1. Request details</h2>

		<s:gridEditor rows="#{AdminSubmissionBean.rows}"
			columns="#{AdminSubmissionBean.columns}"
			values="#{AdminSubmissionBean.values}"
			fieldTypes="#{AdminSubmissionBean.fieldTypes}"
			rowGroups="#{AdminSubmissionBean.rowGroups}"
			onColumnAction="#{AdminSubmissionBean.columnAction}" />
		<br>

		<s:gridEditor id="grid-slave" rows="#{AdminSubmissionBean.rowsSlave}"
			columns="#{AdminSubmissionBean.columnsSlave}"
			values="#{AdminSubmissionBean.valuesSlave}"
			rowGroups="#{AdminSubmissionBean.rowGroupsSlave}"
			fieldTypes="#{AdminSubmissionBean.fieldTypesSlave}" />

		<br>
		
		<t:htmlTag value="div" rendered="#{AdminSubmissionBean.isAdmin}">
		<s:gridEditor id="grid-slave3" rows="#{AdminSubmissionBean.rowsSlave3}"
			columns="#{AdminSubmissionBean.columnsSlave3}"
			values="#{AdminSubmissionBean.valuesSlave3}"
			rowGroups="#{AdminSubmissionBean.rowGroupsSlave3}"
			fieldTypes="#{AdminSubmissionBean.fieldTypesSlave3}" />
		</t:htmlTag>
		<br>

		<t:htmlTag value="div"
			rendered="#{AdminSubmissionBean.isSourceDetailsVisible}"
			style="border: 2px solid #CCCCCC; width: 500px; padding: 5px;">
			<h:outputText
				style="padding-left: 4px; font-weight: bold; font-style: italic;"
				value="Source details" />
			<t:dataTable value="#{AdminSubmissionBean.sourceData}" var="data"
				style="padding-top: 10px;">
				<h:column>
					<t:div style="width: 200px;">
						<h:outputText style="font-weight: bold;" value="#{data.name}" />
					</t:div>
				</h:column>
				<h:column>
					<h:outputText value="#{data.value}" />
				</h:column>
			</t:dataTable>
			<h:commandButton value="Close"
				action="#{AdminSubmissionBean.closeDetails}" />
		</t:htmlTag>

		<h2>2. Submitted sources</h2>

		<s:grid id="sources" columns="#{AdminSubmissionBean.sourcesColumns}"
			rows="#{AdminSubmissionBean.sourcesRows}"
			onOpenRow="#{AdminSubmissionBean.openSourcesRow}" />
		<br>
		<br>

		<t:htmlTag value="div"
			rendered="#{!AdminSubmissionBean.isChiefEditor}">
			<h2>3. Request status</h2>
			<table>
				<tr>
					<td><h:selectBooleanCheckbox
						value="#{AdminSubmissionBean.finished}" /></td>
					<td><h:outputText
						value="I am done with this submission and it does not require my further attention" />
					</td>
				</tr>
			</table>
		</t:htmlTag>

		<br>
		<br>
		

		<h:commandButton value="Add reviewer"
			rendered="#{AdminSubmissionBean.isChiefEditor && !AdminSubmissionBean.addingEditor && !AdminSubmissionBean.accepted}"
			action="#{AdminSubmissionBean.addEditor}" />
		<t:htmlTag value="div" rendered="#{AdminSubmissionBean.addingEditor&&AdminSubmissionBean.isChiefEditor}"
			style="border: 2px solid #CCCCCC; width: 400px; padding: 5px;">
			<h:selectOneMenu value="#{AdminSubmissionBean.newEditorUser}">
				<f:selectItems value="#{SubmissionUsersBean.editorUsers}" />
			</h:selectOneMenu>
			<h:commandButton value="Add reviewer"
				action="#{AdminSubmissionBean.applyAddEditor}" />
			<h:commandButton value="Cancel"
				action="#{AdminSubmissionBean.cancelAddEditor}" />
		</t:htmlTag>

		<br>
		<br>
		<h:commandButton value="< Back" action="#{AdminSubmissionBean.back}" />
		<h:commandButton value="Impute" action="#{AdminSubmissionBean.impute}"
			rendered="#{AdminSubmissionBean.isAdmin}" />
		<h:commandButton value="Save" action="#{AdminSubmissionBean.save}"
			rendered="#{AdminSubmissionBean.rejectAvailable}" />
		<h:commandButton value="Logout without saving"
			action="#{AdminSubmissionBean.logoutOnly}"
			rendered="#{AdminSubmissionBean.rejectAvailable}" />
		<h:commandButton value="Accept request"
			action="#{AdminSubmissionBean.submit}"
			rendered="#{AdminSubmissionBean.isAdmin}" />
		<h:commandButton value="Reject request"
			action="#{AdminSubmissionBean.rejectSubmission}"
			rendered="#{AdminSubmissionBean.isAdmin}" />
		<h:commandButton value="Delete request"
			action="#{AdminSubmissionBean.deleteSubmission}"
			rendered="#{AdminSubmissionBean.isAdmin}" />		
		<h:commandButton value="Delete voyage"
			action="#{AdminSubmissionBean.deleteVoyage}"
			rendered="#{AdminSubmissionBean.isAdmin && !AdminSubmissionBean.isNew}" />
		<br>
		
	</h:form>
</f:view>
</body>
</html>