<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>FAQs</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-faq.css" rel="stylesheet" type="text/css">

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>
	
	<f:param value="faqs" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title">
		<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
		<tr>
			<td>
				<img src="../images/help-faqs-title.png" width="240" height="50" border="0" alt="FAQs">			
			</td>
			<td>
				<table border="0" cellspacing="0" cellpadding="0" style="margin-left: auto; margin-right: 20px;">
				<tr>
					<td style="padding-right: 5px;"><h:inputText styleClass="glossary-search-box" value="#{FAQBean.searchTerm}" /></td>
					<td><h:commandButton value="Search" /></td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
	</div>
	
	<s:faqList
		id="faq"
		faqList="#{FAQBean.FAQList}" />
	
	
</h:form>
</f:view>
</body>
</html>