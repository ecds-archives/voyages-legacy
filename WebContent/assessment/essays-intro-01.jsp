<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-intro-01" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>A Brief Overview of the Trans-Atlantic Slave Trade</h1>
					
					<div class="essay-info">
						<span class="essay-author">David Eltis</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">2007</span>
					</div>
					
					<p>The transatlantic slave trade was the largest long-distance
					coerced movement of people in history and, prior to the
					mid-nineteenth century, formed the major demographic well-spring
					for the re-peopling of the Americas following the collapse of the
					Amerindian population. Cumulatively, as late as 1820, nearly four
					Africans had crossed the Atlantic for every European, and, given
					the differences in the sex ratios between European and African
					migrant streams, about four out of every five females that
					traversed the Atlantic were from Africa. From the late fifteenth
					century the Atlantic Ocean, once a formidable barrier that
					prevented regular interaction between those peoples inhabiting the
					four continents it touched, became a commercial highway that
					integrated the histories of Africa, Europe, and the Americas for
					the first time. As the above figures suggest, slavery and the slave
					trade were the linchpins of this process. With the decline of the
					Amerindian population labor from Africa formed the basis of the
					exploitation of the gold and agricultural resources of the export
					sectors of the Americas, with sugar plantations absorbing well over
					two thirds of slaves carried across the Atlantic by the major
					European and Euro-American powers. For several centuries slaves
					were the most important reason for contact between Europeans and
					Africans.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
						</td>
						<td class="essay-next">
							<a href="essays-intro-02.faces">New Products for Trade in the Americas</a>
						</td>
					</tr>
					</table>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>