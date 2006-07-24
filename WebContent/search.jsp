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
	<link href="sections.css" rel="stylesheet" type="text/css">
	<link href="menu.css" rel="stylesheet" type="text/css">
	<link href="search.css" rel="stylesheet" type="text/css">
	<link href="search-query.css" rel="stylesheet" type="text/css">
	<link href="search-table.css" rel="stylesheet" type="text/css">
	<link href="map.css" rel="stylesheet" type="text/css">
	<script src="tooltip.js" type="text/javascript" language="javascript"></script>
	<script src="jslib/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="jslib/scriptaculous.js" type="text/javascript" language="javascript"></script>
	<script language="javascript" type="text/javascript" src="map.js"></script>
	<script language="javascript" type="text/javascript" src="utils.js"></script>
</head>
<body>
<f:view>

	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>
	<div class="header-path">
		Homepage &gt; Database of voyages &gt; Search
	</div>
	
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
			<tr>
				<td class="side-panel">
					<%@ include file="search-side-panel.jsp" %>
				</td>
				<td class="main-panel">
					<s:messageBar rendered="false" binding="#{SearchBean.messageBar}" />
					<%@ include file="search-main-panel.jsp" %>
				</td>
			</tr>
		</table>
		
	</h:form>

</f:view>
</body>
</html>