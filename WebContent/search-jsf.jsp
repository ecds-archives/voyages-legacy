<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>
<link href="search-jsf.css" rel="stylesheet" type="text/css">
<script src="tooltip.js" type="text/javascript" language="javascript"></script>
</head>
<body>
<f:view>
	<div class="main-title">Trans-Atlantic Slave Trade</div>
	<h:form id="form">

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td class="side-panel">
					<%@ include file="search-side-panel.jsp" %>
				</td>
				
				<td class="main-panel">
					<%@ include file="search-main-panel.jsp" %>												
				</td>
			</tr>
		</table>

	</h:form>
</f:view>
</body>
</html>
