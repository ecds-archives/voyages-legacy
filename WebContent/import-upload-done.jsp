<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Import</title>
	<link href="import.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form id="form">

		The data file(s) was(were) succesfully uploaded.
		Click OK to go the progress window of the import.
		<h:commandButton value="OK" actionListener="#{ImportLog.openDetail}" action="detail">
			<f:param id="importDir" name="importDir" value="#{param.importDir}" />
		</h:commandButton>
		
		<h:commandButton value="X" action="#{ImportLog.xxx}" />
		<h:inputText value="#{ImportLog.abc}" />
		
		<h:inputText value="#{param.importDir}" />
		
	</h:form>
</f:view>
</body>
</html>