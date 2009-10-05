<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Sitemap</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-sitemap.css" rel="stylesheet" type="text/css">
	
	<script language="javascript" type="text/javascript">
	
	function openPage(url)
	{
		if (window.opener)
			window.opener.location.href = url;
		else
			window.open(url);
	}
	
	</script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>
	
	<f:param value="sitemap" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title"><img src="../images/help-sitemap-title.png" width="240" height="50" border="0" alt="Demos"></div>
	
	<div id="sitemap">
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="sitemap-col-1">
			<s:simpleList
				listStyle="ul"
				elements="#{SitemapBean.sitemapCol1}" />
		</td>
		<td class="sitemap-col-2">
			<s:simpleList
				listStyle="ul"
				elements="#{SitemapBean.sitemapCol2}" />
		</td>
	</tr>
	</table>
	
</h:form>
</f:view>

<%@ include file="../google-analytics.jsp" %>

</body>
</html>