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
		<h:outputText value="Understanding the Satabase" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="methodology-05" binding="#{EssaysBean.paramActiveMenuId}" />
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
									
					<h2>Data Variables</h2>					
					<p>The variables include information that, for convenience, have been grouped into eight categories: (1) vessel characteristics
					(name, tonnage, rig, guns, place and year of construction, owners); (2) the outcome of the voyage (3) the itinerary of the voyage;
					(4) the dates at which the vessel left or arrived; (5) the captain and crew of the vessel; (6) the numbers of captives; (7) the
					characteristics of the captives and their experience of mortality; and (8) the sources for the record. The Variable List in the
					“Understanding the Database” section presents a complete listing of the data variables as well as the imputed variables in the
					data set. Imputed variables are always marked with an asterisk. No voyage, however, includes information for all data or imputed
					variables. Table 1 provides a summary of the coverage for some of the more important data variables. </p>
					
					<p>Generally, we attempted to preserve the written documentary record in adding to the data variables. Numeric variables, such
					as vessel tonnage, numbers of crew, and numbers of slaves, demanded a ranking of sources, particularly for the well-documented
					British trade. <span class="superscript">(5)</span></p>
					
					<center>
					<p style="font-weight: bold;">Table 1. Select Summary of Information Contained in the Trans-Atlantic Slave Voyage Data Set: Version 2008 Compared to 2010</p>					
					
					<table border="1" cellspacing="0" cellpadding="3" class="methodborder">
						<tr>
							<td></td>
							<td>2008</td>
							<td>2010</td>
						</tr>
						<tr>
							<td>Number of slave voyages in the data set</td>
							<td class="number">34,940</td>
							<td class="number">34,948</td>
						</tr>
						<tr>
							<td>Voyages with name of vessel</td>
							<td class="number">33,337</td>
							<td class="number">33,353</td>
						</tr>
						<tr>
							<td>Voyages with name of captain(s)</td>
							<td class="number">30,895</td>
							<td class="number">30,894</td>
						</tr>
						<tr>
							<td>Voyages with name of at least one ship owner</td>
							<td class="number">21,024</td>
							<td class="number">21,064</td>
						</tr>
						<tr>
							<td>Tonnage of ship available</td>
							<td class="number">17,691</td>
							<td class="number">17,608</td>
						</tr>
						<tr>
							<td>Place of ship departure given</td>
							<td class="number">28,443</td>
							<td class="number">28,449</td>
						</tr>
						<tr>
							<td>Date of ship departure given</td>
							<td class="number">22,193</td>
							<td class="number">22,231</td>
						</tr>
						<tr>
							<td>Place(s) of embarkation on the African coast available</td>
							<td class="number">21,121</td>
							<td class="number">21,291</td>
						</tr>
						<tr>
							<td>Numbers of Africans embarked reported</td>
							<td class="number">8,207</td>
							<td class="number">8,272</td>
						</tr>
						<tr>
							<td>Voyages with age or gender of Africans reported</td>
							<td class="number">4,426</td>
							<td class="number">4,498</td>
							</tr>
						<tr>
							<td>Place(s) of disembarkation available</td>
							<td class="number">24,849</td>
							<td class="number">24,916</td>
						</tr>
						<tr>
							<td>Dates of arrival at place of disembarkation available</td>
							<td class="number">17,446</td>
							<td class="number">17,533</td>
							</tr>
						<tr>
							<td>Numbers of Africans disembarked reported</td>
							<td class="number">18,184</td>
							<td class="number">18,269</td>
						</tr>
						<tr>
							<td>Voyages reporting number of Africans died on board</td>
							<td class="number">6,332</td>
							<td class="number">6,438</td>
						</tr>
						<tr>
							<td>Voyages with place of ship construction reported</td>
							<td class="number">8,881</td>
							<td class="number">8,888</td>	
						</tr>
						<tr>
							<td>Date of return to Europe or end of voyage given</td>
							<td class="number">10,100</td>
							<td class="number">10,414</td>
							</tr>
						<tr>
							<td>Some indication of outcome of voyage indicated</td>
							<td class="number">31,554</td>
							<td class="number">31,612</td>
						</tr>
					</table>
					</center>
						
					<p>Sources often report different numbers of slaves embarked on or "taken on board" the coast of Africa or landed in the
					Americas. Furthermore, for some years there are inconsistencies in slave age or gender totals per voyage. Regarding
					slave exports, we were careful to distinguish between the number of slaves purchased and the number who in fact were
					shipped from the coast. We used slave departure totals, whether reported by slave traders, African merchants, or
					European captains, agents, or merchants. We included the slave departures reported in sources such as logs kept by
					the Dutch and English castles on the Gold Coast, even though the totals often were rounded numbers, such as 400 or
					500 slaves, and even though the totals occasionally were significantly less than the numbers of slaves who were disembarked
					in the Americas. Users should keep these biases in mind, not least for any calculations of mortality they may wish to try.
					For slave arrivals recorded in customs documents or shipping gazettes, we decided to use maximum totals where there was
					conflicting information, under the assumption that these differences might indicate deaths of slaves before slaves disembarked.</p>
					<h2>Age categories</h2>	
				
					<p>Age categories must also be used with care.<span class="superscript">(6)</span>

					The Voyages Database includes variables for adult males ("men"), adult
					females ("women"), male children ("boys"), female children ("girls"), adults, children, and infants (reported often as
					"infants at the breast"). Unfortunately, age and sex definitions changed over time and among carriers. Arrivals in the
					early Iberian Americas were assigned a ratio of what a prime male slave would cost—the latter being termed a pieça da
					Indes. A child would receive a rating of half a pieça, a woman 0.8, and so on. It has not proved possible to infer age
					and gender breakdowns from aggregated pieças. In the 1660–1730 period, the London-based Royal African Company (RAC) defined
					children as about ten years of age or younger. For most of the British and French slave trades, a height (about four feet
					four inches) and/or age (about puberty) criterion distinguished adults from children. In the nineteenth century, captured
					slave ships of all nations, but mainly Spanish and Brazilian, had their human cargoes recorded by a variety of courts, some
					British, some international. There is little doubt that the criterion used to separate out adults was sexual maturity as
					assessed by physical appearance, which for most Africans at this time would probably occur in the mid-teens, but could vary
					according to the diet prevalent in the areas from which Africans were drawn as well as according to the eye of the purchasers.
					Yet another categorization emerges from Cuban slave trade data (1790–1820) taken from the Seville archives, which adds "men-boys"
					and "women-girls" to the previous categories. These we included among men and women, respectively. All these measurements are of
					course imprecise, with even a clear age definition of "ten years and younger" hinging on casual inspection by Europeans, because
					many African cultures did not attach importance to knowledge of precise ages. In nineteenth-century court records, different
					officials often recorded slightly different distributions of the same group of slaves. However, the physiological correlation
					of height (specifically the teenage growth spurt) and sexual maturity means that there is broad similarity among most of these
					concepts, except for the RAC’s definition, which excluded individuals that other definitions would have included as children.
					As the RAC records form the bulk of the age and gender information for 1660–1710, the share of children for this period is
					biased downward.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-04.faces">Cases and Variables</a>
						</td>
						<td class="method-next">
							<a href="methodology-06.faces">Dates</a>
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