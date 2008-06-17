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
				<f:param value="methodology-06" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Dates</h2>
					
					<p>Dates of slave voyage sailings and arrivals are useful categories for sorting voyages in the data set. We therefore
					decided to broaden the definition of "departure" dates and we estimated years of arrival in the Americas for select voyages.
					For the non-British slave voyages, dates of departure generally refer to the date the vessels sailed from port, although in
					the records of massive departures from Bahia de Todos os Santos in Brazil, the date of the issue of the license to export
					tobacco (the only data available for hundreds of voyages) is sometimes more than a month different from the date of permission
					to clear port when this source is also available. Similar variance appears in the large British trade. We assumed that the date
					crew entered pay (listed on Bristol or Liverpool muster rolls) was the date of departure (or close to it); and, for many voyages,
					this assumption is confirmed by analysis of other sources. Other "departure" dates included in the new slave trade data set are:
					the date vessels cleared customs; the dates Mediterranean passes were issued; the dates bonds were given; the dates sailors’
					pension monies were lodged; and the dates of vessel registration. These events usually occurred within 1-2 months of departure.
					London slave vessels cleared customs at Gravesend and often sailed from the "Downs," the shallows off the Kent coast. During
					contrary south-westerly winds, departures from the Downs were often delayed for several weeks. We included these dates of sail
					from the Downs as London "departure" dates. A separate variable defines these various "departure" dates. Reports of slave vessel
					arrivals in the Americas generally reached Europe within six to ten weeks. Without other documentary support we assumed the year
					or (in select cases) month of arrival, when the evidence was clear from the timing of gazette listings. Bristol and Liverpool
					muster rolls frequently record the dates the crew deserted ship or were discharged from pay in the Americas. For many voyages
					we assumed that these were arrival dates. When multiple dates were reported in the sources, the editors chose the latest dates
					for departures and the earliest dates for arrivals.<span class="superscript">(7)</span> </p>

					<p>The new data set includes variables that report the place and year of construction for most vessels in the British slave
					trade. Merchants often purchased ships from other trades and converted them to slavers. The British slaving fleet also included
					prizes captured from the French, Spanish, Dutch, or Americans during the many wars of the eighteenth century. Shipping sources,
					such as Lloyd’s Registers of Shipping, often do not distinguish whether a vessel was "French-built" or a "French prize." Similarly,
					the year of construction in the documents may refer to the year the vessel was captured, made free or rebuilt. As it is unlikely
					that many British merchants purchased vessels built in France in an open market, the user should assume that a "French-built"
					vessel was likely a war prize. Vessels reported as "French prizes," on the other hand, may not have been built in France. These
					ships could in fact have been built in Britain and subsequently captured and renamed by French merchants.</p>
				
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-05.faces">Data Variables</a>
						</td>
						<td class="method-next">
							<a href="methodology-07.faces">Names</a>
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