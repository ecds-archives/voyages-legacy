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
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
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
					
					<p>It is difficult to believe in the first decade of the twenty-first century that just over two centuries 
					ago, for those Europeans who thought about the issue, the shipping of enslaved Africans across the Atlantic 
					was morally indistinguishable from shipping textiles, wheat, or even sugar. Our reconstruction of a major 
					part of this migration experience covers an era in which there was massive technological change (steamers 
					were among the last slave ships), as well as very dramatic shifts in perceptions of good and evil. Just as 
					important perhaps were the relations between the Western and non-Western worlds that the trade both reflected 
					and encapsulated. Slaves constituted the most important reason for contact between Europeans and Africans for 
					nearly two centuries. The shipment of slaves from Africa was related to the demographic disaster consequent to 
					the meeting of Europeans and Amerindians, which greatly reduced the numbers of Amerindian laborers and raised 
					the demand for labor drawn from elsewhere, particularly Africa. As Europeans colonized the Americas, a steady 
					stream of European peoples migrated to the Americas between 1492 and the early nineteenth century. But what is 
					often overlooked is that, before 1820, perhaps three times as many enslaved Africans crossed the Atlantic as 
					Europeans. This was the largest transoceanic migration of a people until that day, and it provided the Americas 
					with a crucial labor force for their own economic development. The slave trade is thus a vital part of the history 
					of some millions of Africans and their descendants who helped shape the modern Americas culturally as well as in 
					the material sense.</p>
					
					<p>The genesis and history of Voyages database is laid out on a separate page. In this essay we wish to alert users 
					to its structure and to its limitations as well as its strengths. The data set contains thousands of names of ship 
					owners and ship captains, but it contains no names of the millions of slaves carried to the Americas. On the other 
					hand, this web site does provide the African names of and personal information about 67,004 captives who were found 
					on board slave vessels detained by naval cruisers attempting to suppress the slave trade in the nineteenth century. 
					These people can be searched and analyzed using the names interface. Although of limited utility for persons seeking 
					their own family histories, our data set does provide an extraordinary source for historical reconstruction of the 
					history of the African peoples in America. The details of the 34,941 voyages presented here greatly facilitate the 
					study of cultural, demographic, and economic change in the Atlantic world from the late sixteenth to the mid-nineteenth 
					centuries. Trends and cycles in the flow of African captives from specific coastal outlets should provide scholars with 
					new, basic information useful in examining the relationships among slaving, warfare—in both Africa and Europe—political 
					instability, and climatic and ecological change, among other forces. The data set in its earlier manifestations has already 
					provided new impetus to assessments of the volume and demographic structure of the trans-Atlantic slave trade, and, when the 
					names database is properly interpreted, it will contribute as well to our understanding of slaving routes from the interior 
					to the coast. </p>
					
					<p>For European societies located on either side of the Atlantic, the data set contains new information on ship 
					construction and registration and relatively extensive records of owners’ and captains’ names. It will now be 
					easier to pursue connections between the slave trade and other sectors of European and American economies. 
					Researchers should be able to unravel trends in long-distance shipping activities, particularly important 
					because no comparable body of data exists for other transoceanic trades. Data on crew mortality are abundant. 
					The implications for new assessments of the social as well as the economic role of the slave trade in the regions 
					where the slave voyage originated are obvious. In short, the major aim of this Emory supported Voyages web site is 
					to facilitate and stimulate new research on the slave trade, the implications of which reach far beyond the slave 
					trade itself.</p>
					
					<p>One immediate question is how complete are the data? It is probable that our data set now includes more than 
					95 percent of all voyages that left British ports—and the British were the second largest of the national slave 
					trader groups. The data on the eighteenth-century French and Dutch slave trades are also largely complete. The 
					reasons for such comprehensive coverage are fairly obvious. Compared with other slave traders, northwestern 
					European trading nations conducted the great bulk of their business relatively late in the slave trade era when 
					everyone kept better records. Surviving sources in these countries are therefore abundant. Casual inspection of 
					the relevant variables in the data set shows that almost all the voyages leaving ports in these countries have 
					more than one source of information, and some have as many as eighteen. Yet the data on the Iberian and Brazilian 
					trades after 1750 are also relatively complete, and information on the earlier period for these regions is vastly 
					greater than it was ten years ago. For a country by country assessment of the completeness of the data, readers are 
					referred to Chapter 1 of <span onmouseover=tooltipShow() onmouseout=tooltipHide(event,"")>
					Extending the Frontiers</span> and the spreadsheets downloadable from this web site that underpin 
					our estimates of the overall size of the slave trade. Our estimate of the total volume of slaves carried off from 
					Africa to the Americas is 12.5 million, and that the total number of voyages that set out to obtain captives was 
					43,600. New information will certainly emerge from the archives, but we think it unlikely that future scholars will 
					dramatically increase the  size or scale of the trans-Atlantic slave trade. </p>

					<p>The 34,941 trans-Atlantic voyages contained in the databaseallows us to infer the total number of voyages 
					carrying slaves from Africa. The “Estimates” page suggests that 12 ½  million captives (12,520,000) departed 
					Africa for the Americas. Dividing this total by the average number of people embarked per voyage, 304 individuals, 
					yields 41,190 voyages. Similarly, the “Estimates” pages suggests that 10,7 million enslaved Africans disembarked, 
					mainly in the Americas. Given the average number disembarked per voyage,265 people, yields an estimated 40,380 
					voyages arriving. Not all 34,941 voyages in the database carried slaves from Africa. A total of 1,536 ships (4.4%) 
					never reached the African coast because they were lost at sea, captured or suffered some other misfortune. After 
					removing these voyages, the database contains some trace of 81 percent of voyages that embarked captives. The 
					database also contains records of 32,788 voyages that disembarked slaves, or could have done so (in other words, 
					for some of these we do not know the outcome of the voyage). A total of 763 of these disembarked their slaves in 
					the Old World. The latter group comprised mainly ships captured in the nineteenth century which were taken to Sierra 
					Leone and St. Helena as part of the attempt to suppress the trade. A further 167 sank after leaving Africa with the 
					loss of their slaves. In all, the database contains some record of almost 80 percent of vessels disembarking captives. 
					Of course, there are other estimates of the volume of the trans-Atlantic slave trade. If we take a higher estimate of, 
					say, 15.4 million departures,  then the Voyages Database documents two-thirds of all slaving voyages that sailed 
					between 1514 and 1866.</p>
					
					<h2><a href="sources.faces">NATURE OF SOURCES</a></h2>					
					<p>Much of what is new in this data set lies in the sources, which call for some discussion. The published data 
					draw on a wide range of published and archival information. Postma, Mettas, and Richardson used new material in 
					the preparation of their published catalogues of voyages. Our data set does not reproduce all the sources that 
					previous authors used and listed. Thus, voyages drawn from published sources are represented here by that single 
					reference to them alone. Anyone wishing to consult their archival references will have to look them up in the 
					hard copy of these published volumes. We edited individual voyage details in these collections only when we found 
					new documentary evidence to support a change; consequently, we added these new references to the source record. 
					Pulling together the results of work carried out in separate national archives was particularly fruitful because 
					trans-Atlantic slave vessels could clear from one jurisdiction and arrive in the Americas in another. Specifically, 
					the international nature of the slave trade meant that a voyage that might appear primarily in one national body of 
					records had a very good chance of showing up, in addition, in the records of other countries. Thus, voyages organized 
					by London merchants operating independently of the Royal African Company (RAC) in the 1670s and 1680s obtained their 
					slaves in South-east Africa, outside the RAC’s English monopoly and where the English East India Company had little 
					physical presence. Because almost all these ships called at the Cape before beginning their trans-Atlantic journeys, 
					several of them appear in the Dutch Cape of Good Hope archives.  Portuguese ships leaving Bahia in Brazil for the "Mina" 
					coast appear in English Cape Coast Castle material at the Public Record Office. Slave ships of every nationality appear 
					in Lloyd’s Lists, and of course the hundreds of slave ships captured in the many European wars are often carefully 
					documented in the archives of the captors, as well as in the records of the nations to which the ships belonged. Indeed, 
					the South Atlantic Portuguese trade  has fewer sources per voyage, precisely because this branch of the traffic operated 
					independently of the others. Winds and ocean currents kept the South Atlantic trade out of the non-Portuguese archives, 
					as well as keeping northern Europeans out of South Atlantic ports.</p>
					
					<p>Of the 34,941 voyages in the data set, 12,452 have only one source listed. In fact, more than half of these apparently 
					single-source records are taken from already published material where, in nearly all cases, additional primary references 
					are to be found. Furthermore, other publications on which the present data set draws, such as Coughtry’s listing of Rhode 
					Island voyages, are based on a range of primary documents which are not listed by voyage in the publication itself. After 
					allowing for these multi-sourced single references, it would appear that perhaps only one in six of the 34,941 voyages are 
					based on a single historical record. Sixty percent of the voyages in the set have three or more separate sources each. 
					Researchers should no longer need to depend on data collected on the basis of a single source. Abbreviated reference to 
					the sources for any particular voyage can be viewed in the results section of the database by clicking on “configure columns” 
					and transferring “sources” from left box to the right box in the resulting window. The full reference may be viewed by rolling 
					the cursor over the abbreviated source or by clicking on the row of a voyage to view voyage details. Alphabetic listings of 
					references and full descriptions of locations are to be found in <a href="sources.faces">Home> Voyages Database> Understanding 
					the database> Sources </a>.</p>
					
					<p>While the sources are relatively rich, diversity brings a new set of problems. We can hardly expect that reports on voyages 
					made several thousand miles—as well as several months—apart, often in different languages and under different bureaucracies, 
					each with a separate set of official procedures to follow, to always generate perfectly consistent information. For example, 
					216 voyages in the data set apparently arrived in the Americas with more slaves on board than when they left Africa. Others 
					left port more than once on the same voyage, and some ships reportedly changed tonnage and even rig in the course of the voyage. 
					The same ship occasionally appears under more than one name on the same voyage. Those used to working with a single source per 
					voyage and generating data sets without any conflicting information should be warned that the editors have not attempted to 
					correct all these problems. The data set offered here is by no means "clean" in the sense of being entirely internally consistent. 
					We have pursued and eliminated many of the inconsistencies, but to eliminate all would have imposed an order on the historical 
					record that anyone who has visited the archives (or indeed examined published sources such as Mettas or Richardson) knows does 
					not exist. The editors always entered only one value per variable when faced with alternative information. In making such choices, 
					we followed certain rules that researchers can change after going back to the sources. If users elect to do this, however, they, 
					too, will have a set which is both not "clean" and not necessarily reflective of the historical records from which it is drawn.</p>
					
					<h2>DATA VARIABLES</h2>					
					<p>The variables include information that, for convenience, have been grouped into eight categories: (1) vessel characteristics 
					(name, tonnage, rig, guns, place and year of construction, owners); (2) the outcome of the voyage (3) the itinerary of the voyage; 
					(4) the dates at which the vessel left or arrived; (5) the captain and crew of the vessel; (6) the numbers of captives; (7) the 
					characteristics of the captives and their experience of mortality; and (8) the sources for the record. The Variable List in the 
					“Understanding the database” section presents a complete listing of the data variables as well as the imputed variables in the 
					data set. Imputed variables are always marked with an asterisk. No voyage, however, includes information for all data or imputed 
					variables. –. Table 1 provides a summary of the coverage for some of the more important data variables. </p>
					
					<p>Generally, we attempted to preserve the written documentary record in adding to the data variables. Numeric variables, such 
					as vessel tonnage, numbers of crew, and numbers of slaves, demanded a ranking of sources, particularly for the well-documented 
					British trade.(5) Sources</p>
					
					<p>Table 1. Select Summary of Information Contained in the Trans-Atlantic Slave Voyage Data Set</p>
					<table>
						<tr><td>34,941</td>
							<td>Number of slave voyages in the data set</td></tr>
						<tr><td>33,337</td>
							<td>Voyages with name of vessel</td></tr>
						<tr><td>30,895</td>
							<td>Voyages with name of captain(s)</td></tr>
						<tr><td>21,024</td>
							<td>Voyages with name of at least one ship owner</td></tr>
						<tr><td>17,591</td>
							<td>Tonnage of ship available</td></tr>
						<tr><td>28,443</td>
							<td>Place of ship departure given</td></tr>
						<tr><td>22,193</td>
							<td>Date of ship departure given</td></tr>
						<tr><td>21,121</td>
							<td>Place(s) of embarkation on the African coast available</td></tr>
						<tr><td>8,207</td>
							<td>Numbers of Africans embarked reported</td></tr>
						<tr><td>4,426</td>
							<td>Voyages with age or gender of Africans reported</td></tr>
						<tr><td>24,849</td>
							<td>Place(s) of disembarkation available</td></tr>
						<tr><td>17,445</td>
							<td>Dates of arrival at place of disembarkation available</td></tr>
						<tr><td>18,184</td>
							<td>Numbers of Africans disembarked reported</td></tr>
						<tr><td>6,332</td>
							<td>Voyages reporting number of Africans died on board</td></tr>
						<tr><td>8,881</td>	
							<td>Voyages with place of ship construction reported</td></tr>
						<tr><td>10,100</td>
							<td>Date of return to Europe or end of voyage given</td></tr>
						<tr><td>31,554</td>
							<td>Some indication of outcome of voyage indicated</td></tr>
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