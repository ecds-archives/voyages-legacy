<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top">
			
				<s:queryBuilder query="#{SearchBean.workingQuery}" />
				
				<h:commandButton action="#{SearchBean.search}" />

				<s:historyList
					ondelete="#{SearchBean.historyItemDelete}"
					onrestore="#{SearchBean.historyItemRestore}"
					history="#{SearchBean.history}" />
			
			</td>
			<td valign="top">

				<s:tabBar id="bar">
					<s:tab text="Abc" tabId="abc" />
					<s:tab text="Xyz" tabId="xyz" />
				</s:tabBar>
			
			</td>
		</tr>
		</table>

	</h:form>
</f:view>
</body>
</html>