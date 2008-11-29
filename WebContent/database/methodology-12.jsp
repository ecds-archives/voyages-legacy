<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Methodology</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tooltip.js" type="text/javascript" language="javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Understanding the Database" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="methodology-12" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</h1>
					
                    <div class="method-info">
						<span class="method-author"><b>David Eltis</b></span>
						<span class="method-location">(Emory University)</span>,
						<span class="method-date">2007</span>
					</div>
					
					<h2>Voyage Outcomes</h2>
					
					<p>Obviously, not all slave ships made it to the Americas, or even to Africa. Fortunately the data set is quite rich in
					information on the outcome of voyages. The data set allows for 171 different voyage outcomes. As with the geographical
					variables, some regrouping is required to make these more manageable. The first regrouping, “Outcome of voyage for slaves”
					(FATE2), takes the standpoint of the Africans on board, and asks where the ship disembarked its slaves. The majority were
					disembarked in the Americas, but about 12 percent in the present sample died during the voyage. In addition, some who left
					African ports actually disembarked in another part of Africa or on the island of St. Helena (about two percent of all slaves
					recorded). Most of this latter group were captured by British naval cruisers in the nineteenth century, though a very few,
					in the previous century, ended up in Europe. A second regrouping, “Outcome of voyage if ship captured” (FATE3), is concerned
					with the fate of the ship and who might have interfered with its voyage. Slaves rebelled, shore-based Africans or pirates
					attacked ships, and one European power would often try to seize ships flying the flag of other powers, especially in wartime.
					Finally, a third regrouping, “Outcome of voyage for owner” (FATE4), takes the standpoint of the owners, and groups voyages on
					the basis of whether the ships reached the Americas, and if not, whether it was human agency or natural hazard that was
					responsible.  As indicated, each of these three regroupings is represented by a different variable.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-11.faces">Classification as a Trans-Atlantic Slaving Voyage</a>
						</td>
						<td class="method-next">
							<a href="methodology-13.faces">Inferring Places of Trade</a>
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

<%@ include file="../footer.jsp" %>

</body>
</html>