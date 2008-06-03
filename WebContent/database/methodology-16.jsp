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
		<h:outputText value="Understanding the database" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="methodology-16" binding="#{EssaysBean.paramActiveMenuId}" />			
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
					
					<h2>Age and Gender Ratios</h2>

					
					<p>As already noted, the demographic structure of the trade—the age and gender composition of the Africans carried
					off as slaves—is well represented in the data grouping of variables. Some modest inferences, however, are required
					to present an accurate picture of any age and gender pattern that requires computations of two or more voyages.
					Ships left the African coast with varying numbers of men, women and children on board. It makes little sense to
					combine, say, the Merced, taken into Sierra Leone with only one man slave on board, and the Alerta, which landed
					69 men among 606 slaves disembarked in Havana in September 1818. The ratio of men in the first voyage was 100
					percent, the ratio in the second case was 11 percent. Averaging without any further adjustment produces a ratio
					of men of 56 percent, which, given the different numbers of people on board, misrepresents historical reality.
					With large numbers of cases, this problem will tend to be unimportant, but if users select a small number of
					cases, they should employ a simple weighting technique to correct for the differences in the number of people
					being counted. Thus, in the above example, the weighted average of men on the two ships is very much closer to
					the 11 percent on the Alerta than the 100 percent on the Merced. Alternatively, users might disregard our
					voyage-based age and gender ratios and simply divide the total of males (or females) by the total number of
					slaves in the sample they select. As the above discussion suggests, the ratios for age and sex made available
					in the Voyages Database are calculated without weighting. Thus, for example, “Percentage male*” (MALRAT7) is
					computed by averaging the ratios computed for each voyage. Thus a mean of, say, 70 percent male for a group of
					years or a region is the unweighted average of male ratios for individual voyages in the selected group. If users
					wish to group all males in the selection (or all children) and divide by all slaves, they will most likely
					retrieve a different result from that provided in the search interface, but they will have to first download
					the database to make that calculation.  </p>
					
					<p>Users should also note that age and sex information was recorded on some vessels at the beginning of the
					voyage and others at the end of the voyage. We have created composite male and child ratio variables,
					“Percentage male*” (MALRAT7) and “Percentage children*” (CHILRAT7) that lump together information from
					both ends depending on availability, and where information has survived on both we gave precedence to
					the ratios at the point of disembarkation. This procedure was justified by the fact that shipboard
					mortality was only modestly age and sex specific, and those users who wish to eliminate these modest
					effects should download that database first<span class="superscript">(12)</span>.</p>
					
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-15.faces">Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</a>
						</td>
						<td class="method-next">
							<a href="methodology-17.faces">National Carriers</a>
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