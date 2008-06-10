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
				<f:param value="methodology-18" binding="#{EssaysBean.paramActiveMenuId}" />			
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
					
					<h2>Tonnage</h2>

					
					<p>Perhaps the most difficult of the imputed variables developed for the Voyages Database is “Standardized tonnage”
					(TONMOD) derived from the data variable “Tonnage.” This may be the least reliable of the imputed variables provided
					in the Voyages Database. It is offered here as a guide to ship size and to provide a crude basis for calculating
					indices of crowding on ships for both slaves and crew, as well as a basis for examining the efficiency with which
					the trade was conducted over time and between major ports and carriers. The reasons for the relative unreliability
					of this variable begin with the differences between deadweight tonnage, tons burden (for loose-packed cargo), and
					freight tons (for merchandise). But even within these types, jurisdictions often had their own methods of computation.
					<span class="superscript">(13)</span>
					Sometimes more than one method was used simultaneously, and in most countries the various methods changed over time.
					In England, for example, the Royal African Company, the Naval Office shipping returns, the Royal Navy, and Lloyd’s
					Registers of Shipping all appear to have computed tons differently until 1786, when the measured ton became standard
					by Parliamentary statute. The formula was changed in 1836. It is possible to ignore some of these (the Royal Navy,
					which did occasionally carry slaves to the Americas, appears to have used the same formula as the RAC) and develop
					an equivalency for some others. But some jurisdictions introduced political bias because tonnage might be tied to
					subsidies or figures might be altered to circumvent the efforts of another branch of officialdom to control the
					numbers of slaves carried per ton.</p>

					<p>The standard adopted here is the one established by the British--the largest of national traders when the slave
					trade was at its height. Beginning in 1773, British ships were required to use measured tons as well as registered
					tons in their official documentation; from 1786, measured tons alone became the standard.<span class="superscript">(14)</span>
					After 1807, slave ships were not usually of British origin, but reports of their activities originated from or were
					transmitted through British channels. Much of the data were converted into British tons in the process. Reports from
					the British Vice Admiralty Courts almost invariably list British tonnage, and in 1840 the Foreign Office instructed
					its overseas "observers" to give tonnages as provided in the ships’ papers where possible, as well as in British tons.
					<span class="superscript">(15)</span> Many tonnage data, however, are from non-British jurisdictions.
					Several independent contemporary observers suggested that the Portuguese (and Brazilian) ton was perhaps 5 percent
					smaller than its 1773–1835 British counterpart, and the Spanish ton 50 percent larger.<span class="superscript">(16)</span>

					The differences between Portuguese and British tonnage for the nineteenth century, at least, seem small enough to
					disregard. A regression equation is estimated for converting Spanish into British tons that suggests that the
					former was perhaps two-thirds larger, with the difference varying somewhat by size of ship. United States tonnages
					are taken to be the so-called "Custom House Measure" of 1789, which was modeled on the British formula. Although
					some differences existed in the application of this rule among American ports, no adjustment is made here.
					<span class="superscript">(17)</span></p>
	
					<p>For the period before 1786, a further regression equation is estimated for converting Royal African Company
					tonnages into the pre-1786 registered ton.<span class="superscript">(18)</span>  Also for this period,
					the Dutch ton, or last, is taken to be double the size of the British registered ton, and the French tonneau
					de mer is treated as equivalent to the British registered ton.<span class="superscript">(19)</span> In addition to
					these adjustments, it is, of course, necessary to convert all pre-1786 tonnages to the standard British measured
					ton adopted for the set. Once more the British registered-to-measured conversion formulae are called into service.
					There remain several tonnages for British ships between 1714 and 1786, the provenance of which we are not certain.
					We have used registered tons wherever we could, but much of the data were collected by others and it is not always
					clear which tonnage measurement is used. We have made the assumption that such tonnages were the same as registered
					tons down to 1786. As noted above, ships could use either the registered or the new measured ton in their papers
					between 1773 and 1786, but slavers sailing before and after 1773 appear not to have changed their tonnage. Finally,
					it should be noted that there are almost no Spanish and Portuguese tonnage observations in the set before 1773 and
					very few Dutch tonnage records after 1786. No conversion has been attempted for Scandinavian, Hanseatic League (or
					Brandenburg-Prussian), Sardinian, or Mexican tonnages, values for which thus do not appear in the “Standardized
					tonnage” variable.</p>
					
					<p>One last tonnage adjustment (not made here) is required for known bias. Tonnages of French slave ships
					between 1784 and 1792 were inflated (that is to say the size of the ton was deflated) substantially, as the
					French government based their subsidy of the slave trade on tonnages.<span class="superscript">(20)</span>

					The size of the bias is unknown and tonnages of French ships in this period are simply ignored in the conversion
					procedure. A second bias (also not made here) is apparent in Portuguese tonnages between 1815 and 1830. A
					Portuguese law of 1684, and clauses in the 1815 and 1817 Anglo-Portuguese slave trade treaties, limited
					Portuguese and later Brazilian ships to a ratio of between 2.5 and 3.5 slaves per ton, depending on the
					construction of the ship.<span class="superscript">(21)</span> Ratios were normally lower than this in
					every branch of the trade for which data survive, and the regulation must have had little practical
					impact. As pressure to suppress the trade mounted in the nineteenth century and conditions on board
					deteriorated, it is possible that these strictures began to have some application. In any event, British
					officials in Brazil between 1815 and 1830 (after which the complete Brazilian trade was illegal and such
					regulations became moot) became convinced that the Portuguese tonnage measurements were being inflated by
					60 percent on average so that more slaves could be confined on board.<span class="superscript">(22)</span>
					The issue cannot be resolved on the available evidence and no adjustment is made here, but users have been warned.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-17.faces">National Carriers</a>
						</td>
						<td class="method-next">
							<a href="methodology-19.faces">Resistance and Price of Slaves</a>
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