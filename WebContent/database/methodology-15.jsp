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
					
					<h2>Regions of Embarkation and Disembarkation</h2>
					
					<p>The above procedures generate imputed figures, where necessary, for the data used to produce series of
					slave departures and arrivals for eight different regions in Africa and sixty-four different regions of
					disembarkation. The variables “Total slaves embarked*” (SLAXIMP) and “Total slaves disembarked*” (SLAMIMP)
					incorporate these imputed data. “Total slaves embarked” is derived first from the data variable, “Total slaves
					embarked” (TSLAVESD). When values for this are not available, then a SLAXIMP value is derived from the sum of
					values available for “Slaves carried from first port of purchase” (NCAR13), “Slaves carried from second port
					of purchase” (NCAR15), and “Slaves carried from third port of purchase” (NCAR17)--in other words, the slaves
					embarked at up to three ports in Africa where these are available. If these data are incomplete or missing
					then SLAXIMP draws from the data variable “Number of slaves arriving at first place of landing” (SLAARRIV) a
					djusted for voyage mortality (imputed, if necessary), and finally, if this is not available then “Total slaves
					purchased” (TSLAVESP) – a variable available only in the downloadable database is used. Only if none of these
					data variables can provide adequate information, does SLAXIMP incorporate one of the imputed values in the
					Appendix table. Similarly SLAMIMP is derived first from SLAARRIV, then, if necessary, from the sum of “Number
					of slaves disembarked at first place of landing “ (SLAS32), “Number of slaves disembarked at second place of
					landing “ (SLAS36), and “Number of slaves disembarked at third place of landing “ (SLAS39) - in other words
					the slaves disembarked at up to three ports at the completion of the Middle Passage. All four of these are
					data variables. If these are blank, then SLAMIMP draws on TSLAVESD when available, adjusted for shipboard
					mortality (again, imputed, as necessary). As with SLAXIMP, SLAMIMP resorts to the imputed values in the Appendix
					table only when sources do not document the number of Africans who were disembarked.</p>
					
					<p>Most users will probably prefer to use “Total slaves embarked*” and “Total slaves disembarked*” in their
					analyses, but they should be aware that captive numbers are to be found in four different variables each at
					embarkation (PLAC1TRA, PLAC2TRA, PLAC3TRA, MJBYPTIMP) and disembarkation (SLA1PORT, ADPSALE1, ADPSALE2, MJSLPTIMP).
					Eighty-seven percent of the slaving voyages documented at specific African ports embarked  slaves at only one
					location. This bulk loading pattern was particularly pronounced in regions east of the Bight of Benin. The captains
					who traded at more than one African location usually did so along the coastline from Senegal to Ouidah (Whydah).
					Single-port slave-trading occurred even more often in the Americas. Ninety-five percent of the slaving voyages that
					anchored at specific New World markets discharged their human cargoes at one port. Nevertheless, users should note
					that information on partial cargoes and full cargoes are located in four different data variables, one containing
					the total arrived or departed and the other three reflecting purchases (or sales) in possibly three different places.
					The imputed variables “Total slaves embarked*” and “Total slaves disembarked*” are based only on the principal place
					of trade in Africa or principal place of disembarkation. A more refined assessment of numbers of captives boarded or
					discharged requires a search of all eight variables (four for the embarked, and four for disembarked).</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-14.faces">Imputing Numbers of Slaves</a>
						</td>
						<td class="method-next">
							<a href="methodology-16.faces">Age and Gender Ratios</a>
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