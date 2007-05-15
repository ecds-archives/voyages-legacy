<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Voyage</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-voyage.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/map.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/lib/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/lib/scriptaculous.js" type="text/javascript" language="javascript"></script>
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/map.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">	

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	
	<div id="content">
	
			
		<t:htmlTag value="div" styleClass="detail-title">
			<h:outputText value="#{res.database_voyage_voyagedetail} #{VoyageDetailBean.detailVoyageInfo[0].value}"/> 
		</t:htmlTag>
			
		<t:htmlTag value="div" styleClass="detail-link-back">
			<h:commandLink value="< go back" action="#{VoyageDetailBean.back}" />
		</t:htmlTag>

		<s:voyageDetail data="#{VoyageDetailBean.detailData}" />
	
	</div>

</h:form>
</f:view>
</body>
</html>