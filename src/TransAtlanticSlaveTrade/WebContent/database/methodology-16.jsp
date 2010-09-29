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
				<f:param value="methodology-16" binding="#{EssaysBean.paramActiveMenuId}" />			
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</h1>
					
                    <div class="method-info">
						<span class="method-author"><b>David Eltis</b></span>
						<span class="method-location">(Emory University)</span>,
						<span class="method-date">2010</span>
					</div>
					
					<h2>Age and Gender Ratios</h2>

					
					<p>As noted above in description of data variables for “Age categories,” they do provide enough 
					information to describe in gross terms the demographic structure of the trade—the age and gender 
					composition of the Africans carried off as slaves.  For 93 voyages, age and sex data is available 
					for up to three places of embarkation and two places of landing of slaves, and for 437 voyages 
					information exists on the age and gender of slaves who died on the crossing.  When available, 
					the quality of the data varies considerably.  The most precise data is for 3,404 voyages with 
					records that report  the number of “men,” “women,” boys,” and ”girls,” as well as in some cases 
					“children” and “infants.”  For another 811 voyages, the number of “adults” and “children” are 
					distinguished without indication of their gender; while 484 voyages reported the number of “males” 
					and “females” they carried without specifying how many were adults and children.</p>
					
					<p>Several age and gender variables were imputed from the data variables.  First, information 
					from voyages with more than one place of embarkation and/or disembarkation of slaves was aggregated 
					to produce one set of four variables – adults, children, males, and females – for places of departure 
					and another for places of arrival.  An additional set of the same variables was calculated for deaths on 
					the Middle Passage.  From these variables, we calculated two ratios: the proportion of children for all 
					voyages with information on adults and children, and the proportion of males for all voyages with information 
					on males and females. The proportion of adults and females are the reciprocals of the variables that were 
					computed. The ratios are to be understood as the proportion of all slaves for whom a characteristic can 
					be determined.  The child ratio is the percentage of slaves identified as either children or adults, 
					and the male ratio is the percentage of slaves identified as males or females.  For the smaller 
					number of voyages with information on both age and gender, we also calculated the ratios of men, 
					women, boys, and girls.  Ratios were imputed only for voyages with at least 20 slaves whose 
					age and/or gender is documented.</p>
					
					<p>Each ratio was calculated for places of departure, arrival, and deaths; but to further simplify 
					the information for inclusion in the set of variables available on the Voyages website, one set of 
					ratios is provided for each voyage with age and sex information.  It is the same as the proportion 
					at arrival when that is documented; otherwise it is the proportion at departure.  The age and sex of 
					captives was recorded almost two times more often at places of landing (3,731 voyages) than it was at 
					places of embarkation (1,970 voyages).  Information on the demographic composition of slave cargoes at 
					both embarkation and disembarkation exists for only 609 voyages.</p>
					
					<p>Caution is required in inferring age and gender patterns from the ratios.  
					Ships left the African coast with varying numbers of men, women and children 
					on board. It makes little sense to combine, say, the <i>Merced</i>, taken into Sierra 
					Leone with only one man slave on board, and the <i>Alerta</i>, which landed 69 men among 
					606 slaves disembarked in Havana in September 1818. The ratio of men in the first 
					voyage was 100 percent, the ratio in the second case was 11 percent. Averaging 
					without any further adjustment produces a ratio of men of 56 percent, which, given 
					the different numbers of people on board, misrepresents historical reality. With 
					large enough numbers of cases, this problem diminishes to the point of becoming 
					negligible; but if users select a small number of cases, they should employ a 
					simple weighting technique to correct for the differences in the number of people 
					being counted. Thus, in the above example, the weighted average of men on the two 
					ships is very much closer to the 11 percent on the <i>Alerta</i> than the 100 percent on 
					the <i>Merced</i>. Alternatively, users might disregard our voyage-based age and gender ratios 
					and simply divide the total of males (or females) by the total number of slaves in the 
					sample they select.</p>
					
					<p>As the above discussion suggests, the ratios for age and sex made available in the 
					Voyages Database are calculated without weighting. For example, “Percentage male*” (MALRAT7) 
					is computed by averaging the ratios computed for each voyage. Thus a mean of, say, 70 percent 
					male for a group of years or a region is the unweighted average of male ratios for individual 
					voyages in the selected group. If users wish to group all males in the selection (or all 
					children) and divide by all slaves, they may obtain somewhat different results from those 
					provided in the search interface, but they will have to first download the database to 
					make that calculation.</p>
					
					<p>Users should also remember that age and sex information was recorded on some vessels 
					at the beginning of the voyage and on others at the end of the voyage. We have created 
					composite male and child ratio variables, “Percentage male*” (MALRAT7) and “Percentage 
					children*” (CHILRAT7) that lump together information from both ends depending on availability 
					of data, and where information has survived on both we gave precedence to the ratios at the point 
					of disembarkation. This procedure is justified by the finding that shipboard mortality was only 
					modestly age and sex specific.  Those users who wish to eliminate these modest effects should 
					download that database first.(12)</p>
					
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-15.faces">Regions of Embarkation and Disembarkation</a>
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

<%@ include file="../footer.jsp" %>

</body>
</html>