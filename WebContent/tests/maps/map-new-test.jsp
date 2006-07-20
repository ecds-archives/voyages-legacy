<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Map test</title>
	<script src="../../utils.js" type="text/javascript" language="javascript"></script>
	<script src="../../map.js" type="text/javascript" language="javascript"></script>
</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:map
			mapFile="test.map"
			serverBaseUrl="../../servlet/maptile" />
			
		<h:commandButton value="OK" />
	
	</h:form>
</f:view>
</body>
</html>