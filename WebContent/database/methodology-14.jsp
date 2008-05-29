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
					
					<h2>Imputing Numbers of Slaves</h2>
					
					<p>A second set of inferences is suggested by the data on numbers of slaves leaving Africa and arriving in the New (and
					in some cases, the Old) World. Although 25,288 voyages in the data set arrived with slaves, and a further 7,500 could
					have done so, the sources provide the actual number on board at arrival for only 18,184 voyages. On the African side,
					the data are much weaker, with only 8,207 yielding information on the number of slaves leaving Africa, out of 28,962
					voyages that left with slaves, and a further 4,443 that could have done so. Because most of those studying the slave
					trade are interested in the captives rather than the ships, some inference would seem appropriate for those ships
					that traded without leaving anything in the historical record about the slaves they carried. The first step in making
					reasonable inferences is to draw on the number of captives who might have been reported for the same voyage at an
					earlier or later stage of its itinerary.</p>
					
					<p>Only 5,803 voyages in the revised database contain numbers of both captives embarked and captives disembarked,
					but for a further 2,404 we have the figure for departures alone and for 12,279 numbers arrived alone. Imputed totals
					for the missing information may be made from the large subset of voyages that provide information on deaths during
					the passage. The Voyages Database contains 6,332 voyages for which a ratio of deaths to slaves embarked may be
					calculated). Deaths as a proportion of those embarked differed markedly by African region of embarkation. Table 2
					shows breakdowns of shipboard mortality as a percentage of those slaves taken on board.</p>

					<center>
					
					<p style="font-weight:bold">Table 2. Slaves died on board ships reaching the Americas as a percentage of those embarked, by African region
					of embarkation, 1527-1866 </p>					
					
					<table border="1" cellpadding="2" class="methodborder">
					<tr>
						<td></td>
						<td class="number">Deaths/Embarked(%)</td>
						<td class="number">Standard Deviation</td>
						<td class="number">Number of Voyages</td>

					</tr>
					<tr>
						<td>Senegambia</td>
						<td class="number">10.7%</td>
						<td class="number">13.5%</td>
						<td class="number">413</td>
					</tr>

					<tr>
						<td>Sierra Leone</td>
						<td class="number">9.5%</td>
						<td class="number">15.6%</td>
						<td class="number">226</td>
					</tr>
					<tr>

						<td>Windward Coast</td>
						<td class="number">9.3%</td>
						<td class="number">11.9%</td>
						<td class="number">108</td>
					</tr>
					<tr>
						<td>Gold Coast</td>

						<td class="number">11.8%</td>
						<td class="number">12.8%</td>
						<td class="number">640</td>
					</tr>
					<tr>
						<td>Bight of Benin</td>
						<td class="number">11.7%</td>
						<td class="number">14.3%</td>
						<td class="number">1,194</td>
					</tr>
					<tr>
						<td>Bight of Biafra</td>
						<td class="number">19.0%</td>
						<td class="number">18.7%</td>
						<td class="number">637</td>
					</tr>
					<tr>
						<td>West-central Africa</td>
						<td class="number">9.1%</td>
						<td class="number">11.7%</td>
						<td class="number">2,442</td>
					</tr>
					<tr>
						<td>South-east Africa</td>
						<td class="number">18.9%</td>
						<td class="number">16.3%</td>
						<td class="number">334</td>
					</tr>
					<tr>
						<td>Region cannot be identified</td>
						<td class="number">17.4%</td>
						<td class="number">17.7%</td>
						<td class="number">338</td>
					</tr>
					<tr>
						<td>All Africa</td>
						<td class="number">11.9%</td>
						<td class="number">14.2%</td>
						<td class="number">6,332</td>
					</tr>						
					</table>	
					</center>
					
					<p>The breakdown of mortality ratios by African region is used here as the basis for imputing numbers arrived
					in the Americas where totals leaving Africa exist, and for numbers leaving Africa where the numbers on board
					at arrival in the Americas are known.</p>

					
					<p>There remain 12,819 voyages with no information about the numbers of slaves on board ship.. Indeed, we do
					not even know if some of these ships carried slaves. The CD-ROM database divided such unknowns into just two
					groups, one sailing from North American ports and the other departing from all other ports in the Atlantic.
					Behind such categorization was the recognition that the slave vessels from North America were substantially
					smaller than the average. Separate means were calculated for the two groups, adjusted in the case of the
					larger group for region of embarkation in Africa where this information was available. In the Voyages
					Database a more refined strategy is adopted. A more complete data set now allows us to focus on the type
					of vessel as well as the route the voyage followed in forming estimates of captives transported where such
					information is missing. The number of captives on board was very much a function of the type of ship as well
					as place of trade in Africa, and to a lesser extent in the Americas. Moreover, the size of the type of vessel
					as reflected in its rig changed over time. The Appendix table attempts to take into account these factors by
					showing average number of captives both embarked and disembarked for 142 separate combinations of first, rig
					of vessel and time period; and second, where these were not available, place of trade in Africa; and third,
					a separate grouping of 18 types of vessels – smaller than those from the rest of the Atlantic World before
					1800 - built in North America. A small group of vessels have no information on either rig or place of trade
					and estimates of captives for these are classed as “No rig” in the Appendix table. The means for these 142
					categories were added to voyage records, as appropriate, whenever data on slaves could not be extracted from
					the sources. Users should note that in all cases these averages might be termed “running” in the sense that
					that as we added data tothe database we recalculated the statistics reported in the Appendix table. And as
					augmentation of the dataset is set to continue then the imputed values will vary as data are added in the
					future as well. Users should thus not expect to find that the imputed values assigned to any given ship
					type are always the same or, indeed, will remain the same. </p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-13.faces">Inferring Places of Trade</a>
						</td>
						<td class="method-next">
							<a href="methodology-15.faces">Regions of Embarkation and Disembarkation</a>
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