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
					
					<h2>Resistance and Price of Slaves</h2>
					
					<p>Finally, we consider two variables that will attract more attention than most. First, we use the data variable
					“African resistance” (RESISTANCE) to compute a “Rate of Resistance” variable which is available only on the time
					line and in Custom Graphs. This is simply the number of vessels experiencing some recorded act of resistance
					divided by the total number of vessels in a given year and expressed as a percentage. The second is the “Sterling
					cash price (of slaves) in Jamaica” variable (JAMCASPR), which may be used to track the price paid for slaves in
					the Americas as they were sold from the vessel. The Voyages Database contains prices for those on board 959 voyages.
					The full derivation of these data is described elsewhere, but a summary description is appropriate here.
					<span class="superscript">(23)</span> Prices for human beings in the Americas were subject to as many
					influences as were prices in any other market. Key factors included the characteristics of the person being
					sold, the distance between slave markets in the Americas and Africa and the price of the captive in Africa.
					This variable attempts to adjust for several of these factors so that the underlying price trends become
					apparent to the user of Voyages. In most cases the data are taken from the slave traders’ accounts and
					correspondence. Our first goal was to ensure that we recorded a single category of captive – what was
					frequently referred to at the time as “a prime male”.<span class="superscript">(24)</span> Second, we adjusted
					that price for the price differential between the market in which the slave was actually sold and the price in
					Jamaica. Thus, if the captive was sold in one of the eastern Caribbean islands we would make a small adjustment
					upwards to reflect the ten extra days extra sailing time it would take to reach Jamaica. Third, we converted all
					prices into pounds sterling. What we did not do was to express the price in constant pounds (adjusted for
					inflation) – in other words, in real terms. This variable is thus based on archival data, but the adjustments
					we have made in the interests of making it intelligible for users have the effect of converting this into an
					imputed variable. </p>

					
					<p>The above discussion is not exhaustive in the sense that we not have touched on and explained every single
					variable in either the Voyages interface or the two databases offered for downloading. Many of the variables
					need no more explanation than is available on the Variable List page and readers are referred to this page
					for any missing in the preceding pages. </p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-18.faces">Tonnage</a>
						</td>
						<td class="method-next">
							<a href="methodology-20.faces">Appendix</a>
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