<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style type="text/css">

body {
	font-size: 8pt;
	font-family: Verdana; }
	
table.event-line-left-label {
	-background-color: #AAFFFF; }

table.event-line-top-label {
	-background-color: #AAFFFF; }

div.event-line-graph {
	-background-color: #EEFFFF; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:eventLine
			graphs="test02.png, test01.png"
			items=""
			width="500" xMin="0" xMax="500" xSubdiv="50"
			height="200" yMin="0" yMax="200" ySubdiv="50" />

	</h:form>
</f:view>
</body>
</html>