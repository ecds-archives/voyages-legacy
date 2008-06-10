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
				<f:param value="methodology-09" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Geographic Data</h2>
					
					<p>The most straightforward of the imputed variables are geographic. The 786 locations in the data set where slave ships were
					built, registered, cleared for a slaving voyage, embarked or disembarked slaves are grouped into seven broad regions and 90
					specific regions. A full listing of these places is contained in the code book, but space constraints prevent all these from
					being represented in the maps. We have adopted a convention that a geographical placename, such as London or Luanda or Charleston,
					needs to be mentioned five times in the historical sources in order to appear on the online maps. Those wishing to use alternative
					groupings of ports and places into regions and broad regions may use the geographic appendix to the SPSS code book. For Europe and
					the Americas, the groupings are self- explanatory. For Africa eight regional definitions are employed, following conventions in the
					literature. Senegambia is anywhere north of the Rio Nunez. Sierra Leone comprises the Rio Nunez to just west of Cape Mount inclusive.
					The Windward Coast is defined as Cape Mount up to and including the Assini River. The Gold Coast runs east of here up to and including
					the Volta River. The Bight of Benin covers the Rio Volta to Rio Nun, and the Bight of Biafra, east of the Nun to Cape Lopez inclusive.
					West-central Africa is defined as the rest of the western coast of the continent south of this point, and South-east Africa anywhere
					east of the Cape of Good Hope. A few locations mentioned in the documents, for example Casnasonis, and Touau-Toro cannot be identified,
					and one other major designation, a definition of the Windward Coast associated with no less than seventy voyages, straddles the
					definition of Windward Coast and Sierra Leone adopted here and is excluded from the African regional groupings.</p>

					
					<p>On maps accompanying the “Search the database” interface, the default is “Broad Regions” at the lowest zoom level (20).
					“Specific regions,” labelled in large font, are best viewed at mid-range zoom levels (3 and 6). “Port” locations are shown
					by black dots and labels at the highest zoom level (1).  To change zoom level, use icons at the top of the main map area.
					Drop-down menus to the right of the map area offer a choice among geophysical and historical map backgrounds co-ordinated
					with broad regions, specific regions, and ports.  Red and yellow dots are data points activated by a mouse rollover.  Maps
					center in the middle of the Atlantic ocean.  To view detail, either use the positioning map at the bottom right to move the
					map display over a land or island area facing the ocean or center the map on an exact location by means of the drop-down list
					under “Find a visible place.” Estimates maps have a similar structure except that no data are provided for ports, and broad
					and specific regions have definitions that differ slightly from those in the main database interface. </p>
					
					<p>In the query frame to the left, broad and specific regional groupings of ports each become a new variable that appears in
					the selection box, as the user chooses, for example, “First place of slave purchase” or “First place of slave landing.”
					Finally, the variable “Place where voyage began” has an important imputed value added. We have assumed, if a ship brought
					slaves into a Brazilian port south of Amazonia, but left no record of where it began its voyage, that the voyage originated
					in the same place that it ended. The justification for this is the very strong bilateral nature of the Brazilian slave trade.
					Ninety-five percent of all voyages that carried slaves into Brazilian ports for which information does survive on port of
					departure left from the same port into which they carried slaves. Users who do not wish to use this imputed variable can
					find the equivalent data variable in the downloadable version of the database. </p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-08.faces">Imputed Variables</a>
						</td>
						<td class="method-next">
							<a href="methodology-10.faces">Imputed Voyage Dates</a>
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