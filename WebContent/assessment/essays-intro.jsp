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
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>

					<h1>Essays</h1>
					
					<ul class="essays-list">
					
						<li>
							<div class="essay-title">Catherine Zimmermann-Mulgrave: A Message of Hope</div>
							<div class="essay-info">
								<span class="essay-author">Domingues da Silva, Daniel</span>,
								<span class="essay-location">(Emory University)</span>,
								<span class="essay-date">August 2007</span>
							</div>
						</li>
						
						<li>
							<div class="essay-title">Job Ben Solomon and the Curse of Slavery in the Atlantic World</div>
							<div class="essay-info">
								<span class="essay-author">Domingues da Silva, Daniel</span>,
								<span class="essay-location">(Emory University)</span>,
								<span class="essay-date">July 2007</span>
							</div>
						</li>
						
						<li>
							<div class="essay-title">A Brief Overview of the Transatlantic Slave Trade</div>
							<div class="essay-info">
								<span class="essay-author">David Eltis</span>,
								<span class="essay-location">(Emory University)</span>,
								<span class="essay-date">2007</span>
							</div>
						</li>
						
						<li>
							<div class="essay-title">Voyages and Applied History</div>
							<div class="essay-info">
								<span class="essay-author">Jelmer Vos</span>,
								<span class="essay-location">(Emory University)</span>,
								<span class="essay-date">2007</span>
							</div>
						</li>
					
					</ul>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>