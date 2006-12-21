<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyages</title>
	<link href="main.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>
	<h:form id="form">
	
		<h1>Voyages</h1>
		
		<div style="padding: 5px 10px 5px 10px; border: 1px solid #CCCCCC; background-color: #EEEEEE">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-right: 5px">From</td>
				<td style="padding-right: 10px"><h:selectOneMenu value="#{AdminVoyagesListBean.nationId}"><f:selectItems value="#{AdminVoyagesListBean.nations}"/></h:selectOneMenu></td>
				<td style="padding-right: 5px">From</td>
				<td style="padding-right: 10px"><h:inputText value="#{AdminVoyagesListBean.yearFrom}" style="width: 40px;" /></td>
				<td style="padding-right: 5px">To</td>
				<td style="padding-right: 10px"><h:inputText value="#{AdminVoyagesListBean.yearTo}" style="width: 40px;" /></td>
				<td style="padding-right: 5px"><h:commandButton value="Show" /></td>
				<td><h:commandButton value="Default" action="#{AdminVoyagesListBean.restoreDefaultOptions}" /></td>
			</tr>
			</table>
		</div>		
		
		<br>
	
		<s:grid id="voyges" 
			columns="#{AdminVoyagesListBean.columns}"
			rows="#{AdminVoyagesListBean.rows}"
			action="#{AdminVoyagesListBean.openVoyage}"
			onOpenRow="#{AdminVoyageBean.openVoyage}" />
			
		<br>
	
		<s:pager id="voyagesPager"
			maxShownPages="15"
			currentPage="#{AdminVoyagesListBean.currentPage}"
			firstRecord="#{AdminVoyagesListBean.firstRecordIndex}"
			lastRecord="#{AdminVoyagesListBean.lastRecordIndex}"
			pageSize="#{AdminVoyagesListBean.pageSize}" />

	</h:form>
</f:view>
</body>
</html>