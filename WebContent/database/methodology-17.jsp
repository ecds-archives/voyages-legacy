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
				<f:param value="methodology-17" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>National Carriers</h2>
					
					<p>We have also made some assumptions in order to project a fuller picture of national carriers in the trade.
					The set identifies the national affiliations of 25,570 or 73 percent of the voyages in the data set.
					The variable that carries this information is “Flag” (NATIONAL). For a further 7,705 ventures, the context of the
					voyage and the name of the ship owner or captain make inferences about place of registration possible, and thus we
					created an imputed variable of national affiliation that contains affiliations for 33,275 voyages. This is “Flag*”
					(NATINIMP). For some ships, no method enables one to impute the national registration. From 1839, the British allowed
					their cruisers to take slave ships flying the Portuguese flag into British Vice-Admiralty Courts for confiscation
					under British law. In response to this (and to similar legislation in 1845 that extended the provision to the
					Brazilian flag), many slave ships abandoned ship registration papers altogether. In addition, there are undoubtedly
					some voyages that registered in one country but that belonged to nationals of another, and others that sailed under
					false papers. Some British ships sailing under the French flag in the late eighteenth century are examples of the
					first; both British and United States owners sailing with Portuguese and Spanish papers after 1807—sometimes fraudulent,
					sometimes not—are examples of the second. Overall, these cases probably account for less than 1 percent of the ships
					included in the data set. It is also difficult to separate voyages made by ships owned in Britain from those owned in
					the British Americas and later in the United States. Some vessels identified as "British" were likely registered in
					the British Americas, and a similar problem of geographic specificity arises with the Portuguese and Brazilian ships
					in the nineteenth century. A frequency count of this imputed flag variable is nevertheless of interest and indicates
					that 12,014 voyages were British and that a further 2,175 were registered in the British Americas. Of the other principal
					nations, 11,366 are identified as Portuguese or Brazilian; 4,181 were French; 1,574 were Dutch; 1,551 were Spanish or
					Uruguayan; and 402 flew the flags of various Baltic states (mainly Danish), including Brandenburg-Prussia. Many of
					the voyages of unknown nationality were likely Portuguese, but even if they all were, Portuguese voyages are still
					somewhat underrepresented in the Voyages Database.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-16.faces">Age and Gender Ratios</a>
						</td>
						<td class="method-next">
							<a href="methodology-18.faces">Tonnage</a>
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