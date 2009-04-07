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
				<f:param value="methodology-11" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Classification as a Trans-Atlantic Slaving Voyage</h2>
					
					<p>One important problem slave-trade researchers need to address is whether vessels bound for “Africa” in the sources are slaving
					vessels.  As late as the end of the seventeenth century, slaves formed less than half of trade by value between the
					Atlantic world and Africa. Many captains sailed to  Africa to purchase gold, ivory, dyewoods, or spices., Numerous
					naval vessels, troop transports or storeships sailed from Lisbon to the Portuguese forts at Benguela or Luanda, and
					other European powers needed to supply their trading forts as well. For most of the French, Portuguese and Dutch
					voyages to Africa, researchers other than ourselves have made the decision on whether or not a ship was a slaver,
					though we have uncovered a few additional voyages from these nations where the object of the voyage remains unclear.
					It might be noted that records of ship departures have typically survived better in the historical record than
					records of ship arrivals.</p>
					
					<p>For many British and Portuguese voyages, however, we have had to make some hard decisions in determining whether vessels
					were slavers or non-slavers. Many British and North American voyages returned to the port of origin after an interval of
					time during which a slave voyage could have taken place, but no information survives of the places of trade in Africa or
					the Americas. For most of these ships, clearance was for "Africa and the Americas" and many of the remainder in this group
					are ships leaving British American ports for Africa. Before the nineteenth century, ships rarely went from the Americas to
					Africa for anything but slaves. In all these cases the ship is assumed to have been a slaver. For several hundred more
					voyages from Brazil, even less is known. In Bahia, the main source of information on these is licenses of ship departures
					which specify "Elmina" (in West Africa) as the permitted destination. Large rolls of local tobacco were the trade good for
					African-bound Bahian ships, and in the eighteenth century slave traders of all nations depended on this tobacco. And as with
					North American Africa-bound ships, there is no evidence of a significant produce trade between Brazil and Africa. Gold was
					important in the first half of the eighteenth century and alcohol became more important later in the century, but return
					cargoes were always human. After 1680 many Portuguese voyages from Brazil show up in British records from Cape Coast Castle
					as well as the Dutch records for Elmina, and in the nineteenth century there is very good overlap between these licences and
					the observations of British observers on the movements of slave ships. We have made the decision to include these Bahia–Africa
					voyages in the data set. Nevertheless, we do have a file of 1,400 voyages of Atlantic voyages that might have carried slaves,
					but for which we are awaiting additional evidence. The majority of these certainly sailed from Europe to the Caribbean and then
					back again without sailing anywhere near Africa, but we cannot be absolutely certain and we retain information on them for
					future use as necessary. These "doubtfuls" are troublesome, but their numbers, compared to the voyages about which we are
					quite certain, are not great. </p>
					
					<p>There remains the question of produce ships—defined as ships that sailed to Africa to trade for animal products, agricultural
					commodities or minerals. We have identified 1,450 voyages that departed Africa without obtaining slaves. In some cases they
					carried supplies for the European castles on the coast, but in the majority of instances they traded for African produce before
					returning directly to Europe. In addition, there were always a few "tenders" each year that went to the coast to supply slaves
					for a larger ship, but did not themselves carry slaves across the Atlantic. The great majority of these non-slaving ships were
					Dutch and British, the two nations that carried on the largest trades in African produce. We have identified produce (as opposed
					to slave) vessels sometimes on the basis of their voyage histories, sometimes on the known activities of their captains and
					sometimes on the basis of small crew-to-tonnage ratios, suggesting they were not vessels that required additional crew to control
					slaves.<span class="superscript">(9)</span> Both the produce traders and the doubtful traders are held in a separate file and in
					the former case will be used as the basis for separate work on the African produce traffic. </p>
					
					<p>Similarly, on the American side of the Atlantic, the editors often had to decide whether vessels carrying slaves
					were trans-Atlantic or inter-colonial slavers. For a few hundred ships arriving at ports in the Americas, doubts remain.
					Most of these voyages are to be found in Klein’s set of voyages to Havana, 1790 to 1820, taken from the Spanish archives.
					It is clear that many smaller vessels were inter-island slave traders. That is, they trans-shipped African slaves from
					colonies such as St. Croix or St. Thomas to major plantation frontiers, such as Cuba. To separate the inter-island from
					trans-Atlantic vessels arriving in Havana with slaves, we used a benchmark total of 140 slaves—the average number of
					slaves on vessels in the sample that can be identified as trans-Atlantic slavers. Other researchers will use different
					criteria for distinguishing inter-American from trans-Atlantic slave voyages. </p>
					
					<p>Finally, not all voyages that crossed the Atlantic from Africa carried slaves. Generally we have assumed that all
					such voyages were slaving voyages, and have included them in the data set, though there is a slight possibility that
					a few of these vessels traded at produce markets on the coast. In summary, about 5 percent of the voyages included in
					the data set lack information about their activities after the voyages began. We nevertheless feel fairly confident
					that these were slaving voyages, and, as noted, those about which we feel less confident we retain in a separate file.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-10.faces">Imputed Voyage Dates</a>
						</td>
						<td class="method-next">
							<a href="methodology-12.faces">Voyage Outcomes</a>
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