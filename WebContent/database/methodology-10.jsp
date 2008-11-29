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
				<f:param value="methodology-10" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Imputed Voyage Dates</h2>

					<p>There are also imputed variables for both voyage dates and on numbers of captives. Because most slaving voyages lasted for
					many months or even years, and no voyages have complete information for all ten date variables, we have created three definitions
					of "year" in the full downloadable database from which users can choose for purposes of analysis: the year in which the voyage
					originated, the year of embarkation of slaves, or the year of arrival at point of disembarkation. In the Voyages Database only
					“YEARAM” or “year of arrival” is provided.  We created imputed year values when the sources did not record the years when voyages
					departed their homeport, or departed the African coast, or the year when vessels arrived in the Americas. If a London-based vessel
					arrived in Jamaica in September 1770, for example, the year 1770 becomes the imputed African departure year, and the year 1769
					becomes the imputed departure year from the homeport.  Further, years of arrival in the Americas are grouped into periods of
					five, twenty-five, fifty, and one hundred years. For the numbers of slaves carried and the numbers who perished during the
					voyage, as well as the age and gender categories, information is also frequently incomplete and additional imputed values
					are added, the creation of which is discussed more fully below. Researchers can of course make their own estimates and these,
					like the inferences on which alternative estimates are based, may well be different from what we regard as optimal. We would
					like to emphasize that in many cases the optimal solution is not obvious, and one researcher’s estimates (and inferences) may
					be different from, but as good as, another’s, despite the fact that all are working with the same data set. Anyone using the
					data, including ourselves, therefore needs to specify clearly the assumptions he or she is using.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-09.faces">Geographic Data</a>
						</td>
						<td class="method-next">
							<a href="methodology-11.faces">Classification as a Trans-Atlantic Slaving Voyage</a>
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