<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Glossary</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-glossary.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/utils.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/glossary-list.js" type="text/javascript" language="javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>
	
	<f:param value="glossary" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title"><img src="../images/help-glossary-title.png" width="240" height="50" border="0" alt="Demos"></div>

	<div class="glossary-letters-and-search">
		<table border="0" cellspacing="0" cellpadding="0" class="glossary-letters-and-search">
		<tr>
			<td>
				<s:glossaryLetters
					id="letters"
					letters="#{GlossaryBean.letters}"
					glossaryListId="form:glossary" />
			</td>
			<td style="padding-top: 3px; padding-bottom: 3px;">
			
				<table border="0" cellspacing="0" cellpadding="0" style="margin-left: auto; margin-right: 10px;">
				<tr>
					<td style="padding-right: 5px;"><h:inputText styleClass="glossary-search-box" value="#{GlossaryBean.searchTerm}" /></td>
					<td><h:commandButton value="Search" /></td>
				</tr>
				</table>
				
			</td>
		</tr>
		</table>
	</div>
	
	<div class="glossary-terms-cont">
	
		<s:glossaryList
			id="glossary"
			terms="#{GlossaryBean.terms}" />
			
		<%@ include file="../footer.jsp" %>
	
	</div>

</h:form>
</f:view>

</body>
</html>