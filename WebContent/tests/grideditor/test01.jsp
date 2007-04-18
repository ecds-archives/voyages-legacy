<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Test</title>
	<script src="../../scripts/grid-editor.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:gridEditor
			id="grid"
			rows="#{GridEditorTestBean.rows}"
			columns="#{GridEditorTestBean.columns}"
			values="#{GridEditorTestBean.values}"
			extensions="#{GridEditorTestBean.extensions}" />
			
		<h:commandButton value="Test submit" />

	</h:form>
</f:view>
</body>
</html>