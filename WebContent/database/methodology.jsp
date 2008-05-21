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
					referred to Chapter 1 of Extending the Frontiers <span onmouseover=tooltipShow() onmouseout=tooltipHide(event,"") 
					class="superscript">i</span> and the spreadsheets downloadable from this web site that underpin 
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
					say, 15.4 million departures,<span class="superscript">ii</span>  then the Voyages Database documents two-thirds of all slaving voyages that sailed 
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
					several of them appear in the Dutch Cape of Good Hope archives.<span class="superscript">iii
					</span> Portuguese ships leaving Bahia in Brazil for the "Mina" 
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
					
					<p>New material tends to raise the question of the appropriateness of the variables used. The selection offered here has changed 
					several times in the last six years and will no doubt change again in the future as interests shift. Each entry in our data set 
					is a single voyage, assigned a unique identification number as the first of piece of information (VOYAGEID). The question of 
					what voyages to include is also to some extent arbitrary. The term "trans-Atlantic" is less straightforward than it appears. 
					Omitting ships sailing to the Mascarene Islands was an easy decision, but several French ships in the late eighteenth century 
					began their slaving activities in the Indian Ocean, but then on the same voyage brought slaves to the Americas after selling 
					some Africans in Bourbon and the Cape of Good Hope. What to do with the British ships that carried hundreds of children from 
					the Upper Guinea coast to Lisbon in the mid-eighteenth century? These we included on the basis of length of voyage. Should 
					one include the Portuguese trade to o São Tomé in the Bight of Biafra—probably the most enduring branch of the Atlantic slave 
					trade? (excluded on the same basis). Then there were the more than 1,200 slave ships engaged in trans-Atlantic voyages, nearly 
					half with slaves on board, that the British captured and carried into Sierra Leone, the Cape of Good Hope, St. Helena, Fernando Po, 
					or Luanda, before they were able to reach their intended American destinations. These we included. Or even more confusing, the 
					1,060 slaves awaiting shipment in barracoons in Ambriz, Angola, in May 1842, but carried off in British cruisers to St. Helena 
					and Sierra Leone and never subjected to court proceedings of any kind, because they had never been on board a slave ship (excluded).
					<span class="superscript">iv</span>
					Limits had to be established, but the data set provides a basis for those who disagree with those limits to use our work to create their 
					own data sets. A total of 65 variables are made available on the search the database interface, general category, and 293 variables are 
					available in the downloadable version of the database. , Users should note, though, that the website set combines all sources into one 
					variable and the day, month, year values are also combined into one variable for each of the ten dates entered. The names of captives, 
					names of Caribbean agents, names of crew other than captain, the details of shipboard insurrection, and much other information are not 
					included in the present data set, but may be added fairly easily or linked with it via the unique voyage identification number.</p>
					
					<p>The database contains two broad types of variables: data variables and imputed variables. The largest group, 44 of those in the 
					search interface, are data variables. They incorporate information collected from the sources. Imputed variables, indicated by an 
					asterisk, are mainly imputed from knowledge of the relevant voyage or adjacent voyages, calculated directly from data encountered 
					in archival or published sources, or inferred from patterns observed in data variables when not documented directly in primary 
					sources.  An example of an imputed variable is the “Region of Slave Disembarkation Broadly Defined,” a variable that allows one to 
					group voyages to Jamaica or Cuba or St. Domingue into the broadly defined region “Caribbean”. In augmenting the number of voyages 
					on which analysis can be conducted, the imputed variables produce more statistically significant results in using the options in 
					the “Search the database” interface to create tables and custom graphs. They form the basis of the tables and graphs that users can build. </p>
					
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
					British trade <span class="superscript">v</span> Sources.</p>
					
					<p>Table 1. Select Summary of Information Contained in the Trans-Atlantic Slave Voyage Data Set</p>
					<table>
						<tr><td class="number">34,941</td>
							<td>Number of slave voyages in the data set</td></tr>
						<tr><td class="number">33,337</td>
							<td>Voyages with name of vessel</td></tr>
						<tr><td class="number">30,895</td>
							<td>Voyages with name of captain(s)</td></tr>
						<tr><td class="number">21,024</td>
							<td>Voyages with name of at least one ship owner</td></tr>
						<tr><td class="number">17,591</td>
							<td>Tonnage of ship available</td></tr>
						<tr><td class="number">28,443</td>
							<td>Place of ship departure given</td></tr>
						<tr><td class="number">22,193</td>
							<td>Date of ship departure given</td></tr>
						<tr><td class="number">21,121</td>
							<td>Place(s) of embarkation on the African coast available</td></tr>
						<tr><td class="number">8,207</td>
							<td>Numbers of Africans embarked reported</td></tr>
						<tr><td class="number">4,426</td>
							<td>Voyages with age or gender of Africans reported</td></tr>
						<tr><td class="number">24,849</td>
							<td>Place(s) of disembarkation available</td></tr>
						<tr><td class="number">17,445</td>
							<td>Dates of arrival at place of disembarkation available</td></tr>
						<tr><td class="number">18,184</td>
							<td>Numbers of Africans disembarked reported</td></tr>
						<tr><td class="number">6,332</td>
							<td>Voyages reporting number of Africans died on board</td></tr>
						<tr><td class="number">8,881</td>	
							<td>Voyages with place of ship construction reported</td></tr>
						<tr><td class="number">10,100</td>
							<td>Date of return to Europe or end of voyage given</td></tr>
						<tr><td class="number">31,554</td>
							<td>Some indication of outcome of voyage indicated</td></tr>
					</table>
						
					<p>often report different numbers of slaves embarked on or "taken on board" the coast of Africa or landed in the 
					Americas. Furthermore, for some years there are inconsistencies in slave age or gender totals per voyage. Regarding 
					slave exports, we were careful to distinguish between the number of slaves purchased and the number who in fact were 
					shipped from the coast. We used slave departure totals, whether reported by slave traders, African merchants, or 
					European captains, agents, or merchants. We included the slave departures reported in sources such as logs kept by 
					the Dutch and English castles on the Gold Coast, even though the totals often were rounded numbers, such as 400 or 
					500 slaves, and even though the totals occasionally were significantly less than the numbers of slaves who were disembarked 
					in the Americas. Users should keep these biases in mind, not least for any calculations of mortality they may wish to try. 
					For slave arrivals recorded in customs documents or shipping gazettes, we decided to use maximum totals where there was 
					conflicting information, under the assumption that these differences might indicate deaths of slaves before slaves disembarked.</p>
					
					<p>Age categories must also be used with care.<span class="superscript">vi</span> 
					The Voyages database includes variables for adult males ("men"), adult 
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
					for departures and the earliest dates for arrivals.<span class="superscript">vii</span> </p>
					
					<p>Orthography is also a major issue in any historical database. For most voyage entries in the new data set, we maintained 
					the spelling or wording of the names of vessels, captains, and merchants. Exceptions include corrections of obvious mistakes 
					arising from the fact that the recorder of the information was often less than fluent in the language of the nation to which 
					the vessel belonged. And in the Portuguese and Brazilian cases we took the more drastic step of standardized all entries according 
					to modern Portuguese conventions. Even without these problems, variations of spelling were, of course, common before the nineteenth 
					century and, as discussed below, we have standardized some spellings to facilitate sorting. We removed the definite article from 
					vessel names in all languages. Occasionally sources reported different names for the same vessel. The Pretty Betty is also identified, 
					for example, as the Pretty Peggy. In such cases, we separated the two names with "(a)" to indicate an alternate name/spelling, as in 
					Pretty Betty (a) Pretty Peggy. We attempted to maintain the consistency of captains’ and owners’ names throughout their voyage histories 
					to facilitate the user’s sorting of the file. Again, for some entries we placed alternate spellings after "(a)."</p>
					
					<p>We included three variables for captains in the data set. The ordering of these names indicates the order these men appeared, 
					chronologically, to be associated with the voyage. For some British and French voyages, sources list different captains during 
					the ship’s outfitting. A slave vessel may have cleared customs under the command of one captain but sailed to Africa under a 
					subsequent captain. Evidence from the British trade suggests that for some voyages the first captain, rather than leaving the 
					vessel, worked as a supercargo for the voyage. Therefore, we decided to keep the names and their ordering in the data set. The 
					user will not be able to determine which captains were in charge of the vessels on the Middle Passage for all voyages. Some 
					of the captains died before slaving on the coast; other captains’ listings include the man who commanded the vessels on the 
					homeward passages from the Americas. We kept all abbreviations in captains’ names, consistent with the documentary evidence. 
					From the Mettas-Daget catalog of French slave voyages, we attempted to maintain a consistent spelling of captains’ names as 
					indicated in the index to the two-volume French set. Double surnames and indicators of rank (Sieur, Chevalier, de, de la) pose 
					problems singular to the organization of the French subset. In short, the spelling of names is not fixed in the French language. 
					We followed the spellings preferred in the index, though we transcribed first-name abbreviations as indicated in the documentary 
					evidence. To facilitate sorting the Voyages database’ file by captains’ names, we maintained the ordering of surnames as indicated 
					in the published index. </p>
					
					<p>Similarly, we followed, as closely as possible, the spelling and ordering of ship owners’ names given in the documentary evidence. 
					The user will note some voyages "owned" by the RAC, Compagnie du Sénégal, or other monopoly trading groups. For these voyages, companies 
					hired the vessels from ship owners and a group of partners or shareholders invested in the trading cargoes. The names of these individuals 
					are not known. For most of the slave voyages in the data set, however, merchants owned fractional shares of the vessel and trading cargo. 
					The listing of merchants in the set probably reflects the size of each shareholder, though this fact can be confirmed only for a few 
					voyages. For some voyages we only know the principal owner "and Company." This is true particularly for many Bristol (England) voyages. 
					To indicate the fact that the voyage was owned and/or organized by additional owners, we placed an asterisk, *, at the end of the last 
					recorded merchant’s name, as in "Jones, Thomas*" (read: "Thomas Jones and Company"). For some other British voyages, father–son partnerships 
					are listed, as in "Richard Farr, Sons and Company." For such voyages, we included the second owner with surname "Farr" as "Farr (Son)" and 
					indicated that subsequent partners may be present by adding an asterisk after the third owner, "Farr (Son)*." Similarly, for the Dutch firm 
					Jan Swart & Zoon (son), we entered the second owner as "Swart (Zoon)."</p>
					
					<p>Ownership information contained in the French slave trade documents presents additional problems for the researcher. Unlike the 
					British trade, in which many records of extended partnerships survive, French documents usually list single armateurs who organized 
					slave voyages. According to Stein, an armateur was "the merchant who organized and usually financed a large part of the slaving 
					expedition."<span class="superscript">viii</span>  
					Other merchant-investors, therefore, are not recorded in the documents. In cases in which additional owners are 
					suggested by the words "company" (Compagnie or Cie.) or "associates" (consorts), we inserted an asterisk. Many French slave 
					voyages were organized by family members. French documents include these familial relationships: brother(s) (frères), father (père), 
					wife (épouse), widow (veuve or vve), eldest son (fils aîné), and son(s) (fils). These relationships are integral to the archival 
					record and have been maintained in the Voyages database. Because the French words frères and fils can imply multiple brothers and 
					sons, we inserted an asterisk in the second ownership column, as in "Portier (Frères)*." In some cases, the document may record 
					owners as "Brunaud Frères et Compagnie." For these few cases, we inserted a double asterisk as in "Brunaud (Frères)**." Some 
					documents report the names of the propriéteurs who hired out their vessels to the armateurs, the affreteurs who freighted the 
					slave ships, or the local agents who transacted business for absentee armateurs. We excluded these names from Voyages database. 
					French owners’ names often include complex double surnames and aristocratic titles. As in the case of French captains’ names, 
					we attempted to preserve the spelling in the original documents while following the Mettas-Daget index to standardize the basic 
					spelling and name ordering. We did this to allow the user to analyze ownership patterns easily through an A–Z owner–variable 
					sort. The user should refer to the index of volume 2 of Mettas-Daget’s Répertoire for a complete listing of the variant spellings 
					of French merchants’ names.</p>
					
					<p>The common multipleIberian names-- whether  vessels, captains or owners—causes particular problems for researchers. Spanish 
					and Portuguese names often incorporate the surnames of both the father and the mother. In the case of ship’s names, length 
					stemmed from the habit of introducing multiple saints’ names and objects of religious veneration into the name of the vessel - 
					at least before 1800. Length is a problem in this context because the official record of the vessel (and person), as well as 
					the common usage of names by sailors and owners often recorded and employed only fragments of the full name, but unfortunately 
					not always the same fragments. Users are consequently warned that it is often more difficult to track and eliminate double-counting 
					of Iberian vessels and people than it is of their non-Iberian counterparts. Upward bias from double-counting is thus more likely in 
					the records of the Iberian than of the non-Iberian slave trades. </p>
					
					<p>The new data set includes variables that report the place and year of construction for most vessels in the British slave 
					trade. Merchants often purchased ships from other trades and converted them to slavers. The British slaving fleet also included 
					prizes captured from the French, Spanish, Dutch, or Americans during the many wars of the eighteenth century. Shipping sources, 
					such as Lloyd’s Registers of Shipping, often do not distinguish whether a vessel was "French-built" or a "French prize." Similarly, 
					the year of construction in the documents may refer to the year the vessel was captured,made free or rebuilt. As it is unlikely 
					that many British merchants purchased vessels built in France in an open market, the user should assume that a "French-built" 
					vessel was likely a war prize. Vessels reported as "French prizes," on the other hand, may not have been built in France. These 
					ships could in fact have been built in Britain and subsequently captured and renamed by French merchants.</p>
					
					<h2>IMPUTED VARIABLES</h2>
					<p>The second group of variables are Imputed Variables.  In the “Search the Voyages Database” interface, users can choose 22 
					of these imputed variables to facilitate searches. The imputed variables are derived directly from the data set. The assumptions 
					underlying these derivations are based are explained more fully below, but mostly the derived variables are amalgamations or 
					regroupings of the historical data from the first group of variables. To make the imputed variables as transparent as possible 
					and to facilitate refinements, alternative assessments, and corrections that users might think necessary, we have included a 
					download page which makes the database available to users in various formats. Indeed, it provides with the data needed to create 
					their own imputed variables.</p>
					
					<p>The most straightforward of the imputed variables are geographic. The 786 locations in the data set where slave ships were 
					built, registered, cleared for a slaving voyage, embarked or disembarked slaves are grouped into seven broad regions and 90 
					specific regions. A full listing of these places is contained in the code book, but space constraints prevent all these from 
					being represented in the maps. We have adopted a convention that a geographical placename, such as London or Luanda or Charleston, 
					needs to be mentioned five times in the historical sources in order to appear on the online maps, Those wishing to use alternative 
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
					by black dots and labels at the highest zoom level (1).  To change zoom level, use icons at the top of the main area map.  
					Drop-down menus to the right of the map area offer a choice among geophysical and historical map backgrounds co-ordinated 
					with broad regions, specific regions, and ports.  Red and yellow dots are data points activated by a mouse rollover.  Maps 
					center in the middle of the Atlantic ocean.  To view detail, either use the positioning map at the bottom right to move the 
					map display over a land or island area facing the ocean or center the map on an exact location by means of the drop-down list 
					under “Find a visible place.” “Estimates” maps have a similar structure except that no data are provided for ports, and broad 
					and specific regions have definitions that differ slightly from those in the main database interface. </p>
					
					<p>In the query frame to the left, broad and specific regional groupings of ports each become a new variable that appears in 
					the selection box, as the user chooses, for example, “First place of slave purchase” or “First place of slave landing.” 
					Finally, the variable “Place where voyage began” has an important imputed value added. We have assumed, if a ship brought 
					slaves into a Brazilian port south of Amazonia, but left no record of where it began its voyage, that the voyage originated 
					in the same place that it ended. The justification for this is the very strong bilateral nature of the Brazilian slave trade. 
					Ninety-five percent of all voyages that carried slaves into Brazilian ports for which information does survive on port of 
					departure left from the same port into which they carried slaves. Users who do not wish to use this imputed variable can 
					find the equivalent data variable in the downloadable version of the database. </p>
					
					<p>There are also imputed variables for both voyage dates and on numbers of captives. Because most slaving voyages lasted for 
					many months or even years, and no voyages have complete information for all ten date variables, we have created three definitions 
					of "year" in the full downloadable database from which users can choose for purposes of analysis: the year in which the voyage 
					originated, the year of embarkation of slaves, or the year of arrival at point of disembarkation. In the Voyages database only 
					“YEARAM” or “year of arrival” is provided.  We created imputed year values when the sources did not record the years when voyages 
					departed their homeport, or departed the African coast, or the year when vessels arrived in the Americas. If a London-based vessel 
					arrived in Jamaica in September 1770, for example, the year 1770 becomes the imputed African departure year, and the year 1769 
					becomes the imputed departure year from the homeport.  Further, years of arrival in the Americas are grouped into periods of 
					five, twenty-five, fifty, and one hundred years. For the numbers of slaves carried and the numbers who perished during the 
					voyage, as well as the age and gender categories, information is also frequently incomplete and additional imputed values 
					are added, the creation of which is discussed more fully below. Researchers can of course make their own estimates and these, 
					like the inferences on which alternative estimates are based, may well be different from what we regard as optimal. We would 
					like to emphasize that in many cases the optimal solution is not obvious, and one researcher’s estimates (and inferences) may 
					be different from, but as good as, another’s, despite the fact that all are working with the same data base. Anyone using the 
					data, including ourselves, therefore needs to specify clearly the assumptions he or she is using.</p>
					
					<p>One important problem slave-trade researchers need to address is whether vessels bound for “Africa” are slaving 
					vessels.  As late as the end of the seventeenth century, slaves formed less than half of trade by value between the 
					Atlantic world and Africa. Many captains sailed to  Africa to purchase gold, ivory, dyewoods, or spices., Numerous 
					naval vessels, troop transports or storeships sailed from Lisbon to the Portuguese forts at Benguela or Luanda, and 
					other European powers needed to supply their trading forts as well.. For most of the French, Portuguese and Dutch 
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
					commodities or minerals. . We have identified 1,450 voyages that departed Africa without obtaining slaves. In some cases they 
					carried supplies for the European castles on the coast, but in the majority of instances they traded for African produce before 
					returning directly to Europe. In addition, there were always a few "tenders" each year that went to the coast to supply slaves 
					for a larger ship, but did not themselves carry slaves across the Atlantic. The great majority of these non-slaving ships were 
					Dutch and British, the two nations that carried on the largest trades in African produce. We have identified produce (as opposed 
					to slave) vessels sometimes on the basis of their voyage histories, sometimes on the known activities of their captains and 
					sometimes on the basis of small crew-to-tonnage ratios, suggesting they were not vessels that required additional crew to control 
					slaves.  Both the produce traders and the doubtful traders are held in a separate file and in the former case will be used as the 
					basis for separate work on the African produce traffic. </p>
					
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
					
					<p>Obviously, not all slave ships made it to the Americas, or even to Africa. Fortunately the data set is quite rich in 
					information on the outcome of voyages. The data set allows for 171 different voyage outcomes. As with the geographical 
					variables, some regrouping is required to make these more manageable. The first regrouping, “Outcome of voyage for slaves” 
					(FATE2), takes the standpoint of the Africans on board, and asks where the ship disembarked its slaves. The majority were 
					disembarked in the Americas, but about 12 percent in the present sample died during the voyage. In addition, some who left 
					African ports actually disembarked in another part of Africa or on the island of St. Helena (about two percent of all slaves 
					recorded). Most of this latter group were captured by British naval cruisers in the nineteenth century, though a very few, 
					in the previous century, ended up in Europe. A second regrouping, “Outcome of voyage if ship captured” (FATE3), is concerned 
					with the fate of the ship and who might have interfered with its voyage. Slaves rebelled, shore-based Africans or pirates 
					attacked ships, and one European power would often try to seize ships flying the flag of other powers, especially in wartime. 
					Finally, a third regrouping, “Outcome of voyage for owner” (FATE4), takes the standpoint of the owners, and groups voyages on 
					the basis of whether the ships reached the Americas, and if not, whether it was human agency or natural hazard that was 
					responsible.  As indicated, each of these three regroupings is represented by a different variable.</p>
					
					<p>Establishing the outcome of the voyage is an important prerequisite to inferring information about both places of trade 
					and numbers of people purchased. We have a good basis for imputing locations of slave trading as well as estimating the 
					numbers of slaves embarked and disembarked. We turn first to the geography of the traffic. For some voyages we know the 
					intended ports of trade on the African coast and in the Americas. Private correspondence, newspaper reports, and official 
					records of clearances from ports in Europe and the Americas frequently provide such information. Of the 34,941 voyages in 
					the data set, at least 1,536 did not embark slaves, usually on account of capture or natural hazard. Of those that did, 
					several hundred failed to complete the Middle Passage. The data set provides some information on African place of trade 
					for 20,826 voyages or about half of those that are likely to have obtained slaves. While this information surpasses 
					current knowledge of the geography of the slave trade, it is possible to glean yet more. For 4,084 voyages that left 
					Africa with slaves, or could have done so in the sense that the ship was not wrecked or captured prior to trade beginning, 
					we may not know the African place of embarkation, but we do know where the captain intended to buy slaves. If we assume 
					that he did in fact do what was intended, then after eliminating those locations that are not easy to group into regions 
					(for example the French designation Côte d’Or, which ranged from the Windward Coast to the Bight of Biafra), we are left 
					with 25,010 voyages that contain useful information on place of African trade—or about 60 percent of those vessels in 
					our sample that actually did or could have left Africa with slaves. Switching to the other side of the Atlantic, the 
					data set yields some information on ports of arrival for 24,844 voyages. Once more we have additional information on 
					where 5,444 voyages intended to trade their slaves even though we cannot be certain that they actually did so. If we 
					assume that captains completed the voyage according to plan, then the sample for places of disembarkation increases 
					from 24,844 to 30,288 voyages, or close to 75 percent of all those ventures disembarking captives. </p>
					
					<p>How valid are these assumptions on imputing places of trade from information on intended place of trade? Most slave 
					ships traded in the regions where owners declared they would trade. After eliminating captured ships that rarely 
					completed their voyages as intended, as well as those ships with very broadly defined destinations ("Americas" or 
					"British North America"), a Pearson product moment correlation run on ports of arrival in the Americas generated a 
					coefficient of 0.83 (n=9,541). A similar procedure for region of trade in Africa and intended region of trade produced 
					a Pearson product moment correlation of 0.714 (n=13,951). Ii should also be kept in mind that merchandise always had to 
					be loaded in Europe and the Americas for a specific African region and was often impossible to sell in another region. 
					It was unusual to find a specific manufactured good selling in more than one region.<span class="superscript">x</span> 
					Taken together, this evidence 
					appears sufficiently strong to allow some modest inferences for those voyages that we know purchased slaves in Africa, 
					or subsequently disembarked slaves in other parts of the Atlantic world, and for which the intended but not the actual 
					region of trade is known. </p>

					<p>In addition to these inferential issues, there are also known biases in the geographic data. The British signed 
					three treaties with the Portuguese between 1811 and 1817 that contained clauses limiting Portuguese slave traders to 
					regions of Africa south of the equator, and the last two of the treaties allowed British cruisers to capture Portuguese 
					ships that did not adhere to these provisions. Brazil assumed these treaties when the country became independent in 1822. 
					From 1815, slave ships arriving in Bahia, which had strong trading relations with the Bight of Benin or Slave Coast (north 
					of the equator), usually reported their African port of departure as Cabinda or Malemba, ports just north of the Congo. 
					British officials in Bahia, as well as naval officers patrolling the African coast, were convinced that all Bahian ships 
					nevertheless continued to trade on the Slave Coast.<span class="superscript">xi</span></p>
					
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
					
					<p>Table 2. Slaves died on board ships reaching the Americas as a percentage of those embarked, by African region 
					of embarkation, 1527-1866 </p>
					
					<table>
					<tr>
						<td></td>
						<td class="number">Deaths/Embarked(%)</td>
						<td class="number">Standard Deviation</td>
						<td class="number">Number of Voyages</td>
					</tr>
					<tr>
						<td>Senegambia</td>
						<td class="number">10.7%</td>
						<td class="number">13.5</td>
						<td class="number">413</td>
					</tr>
					<tr>
						<td>Sierra Leone</td>
						<td class="number">9.5%</td>
						<td class="number">15.6</td>
						<td class="number">226</td>
					</tr>
					<tr>
						<td>Windward Coast</td>
						<td class="number">9.3%</td>
						<td class="number">11.9</td>
						<td class="number">108</td>
					</tr>
					<tr>
						<td>Gold Coast</td>
						<td class="number">11.8%</td>
						<td class="number">12.8</td>
						<td class="number">640</td>
					</tr>
					<tr>
						<td>Bight of Benin</td>
						<td class="number">11.7%</td>
						<td class="number">14.3</td>
						<td class="number">1,194</td>
					</tr>
					<tr>
						<td>Bight of Biafra</td>
						<td class="number">19.0%</td>
						<td class="number">18.7</td>
						<td class="number">637</td>
					</tr>
					<tr>
						<td>West-central Africa</td>
						<td class="number">9.1%</td>
						<td class="number">11.7</td>
						<td class="number">2,442</td>
					</tr>
					<tr>
						<td>South-east Africa</td>
						<td class="number">18.9%</td>
						<td class="number">16.3</td>
						<td class="number">334</td>
					</tr>
					<tr>
						<td>Region cannot be identified</td>
						<td class="number">17.4%</td>
						<td class="number">17.7</td>
						<td class="number">338</td>
					</tr>
					<tr>
						<td>All Africa</td>
						<td class="number">11.9%</td>
						<td class="number">14.2</td>
						<td class="number">6,332</td>
					</tr>						
					</table>	
			
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
					
					<p>As already noted, the demographic structure of the trade—the age and gender composition of the Africans carried 
					off as slaves—is well represented in the data grouping of variables. Some modest inferences, however, are required 
					to present an accurate picture of any age and gender pattern that requires computations of two or more voyages. 
					Ships left the African coast with varying numbers of men, women and children on board. It makes little sense to 
					combine, say, the Merced, taken into Sierra Leone with only one man slave on board, and the Alerta, which landed 
					69 men among 606 slaves disembarked in Havana in September 1818. The ratio of men in the first voyage was 100 
					percent, the ratio in the second case was 11 percent. Averaging without any further adjustment produces a ratio 
					of men of 56 percent, which, given the different numbers of people on board, misrepresents historical reality. 
					With large numbers of cases, this problem will tend to be unimportant, but if users select a small number of 
					cases, they should employ a simple weighting technique to correct for the differences in the number of people 
					being counted. Thus, in the above example, the weighted average of men on the two ships is very much closer to 
					the 11 percent on the Alerta than the 100 percent on the Merced. Alternatively, users might disregard our 
					voyage-based age and gender ratios and simply divide the total of males (or females) by the total number of 
					slaves in the sample they select. As the above discussion suggests, the ratios for age and sex made available 
					in the Voyages Database are calculated without weighting. Thus, for example, “Percentage male*” (MALRAT7) is 
					computed by averaging the ratios computed for each voyage. Thus a mean of, say, 70 percent male for a group of 
					years or a region is the unweighted average of male ratios for individual voyages in the selected group. If users 
					wish to group all males in the selection (or all children) and divide by all slaves, they will most likely 
					retrieve a different result from that provided in the search interface, but they will have to first download 
					the database to make that calculation.  </p>
					
					<p>Users should also note that age and sex information was recorded on some vessels at the beginning of the 
					voyage and others at the end of the voyage. We have created composite male and child ratio variables, 
					“Percentage male*” (MALRAT7) and “Percentage children*” (CHILRAT7) that lump together information from 
					both ends depending on availability, and where information has survived on both we gave precedence to 
					the ratios at the point of disembarkation. This procedure was justified by the fact that shipboard 
					mortality was only modestly age and sex specific, and those users who wish to eliminate these modest 
					effects should download that database first<span class="superscript">xii</span>.</p>
					
					<p>We have also made some assumptions in order to project a fuller picture of national carriers in the trade. 
					The set identifies the national affiliations of 25,570 or 73 percent of the voyages in the data set are identified. 
					The variable that carries this information is “Flag” (NATIONAL). For a further 7,705 ventures, the context of the 
					voyage and the name of the ship owner or captain make inferences about place of registration possible, and thus we 
					created an imputed variable of national affiliation that contains affiliations for 33,275 voyages. This is “Flag*” 
					(NATINIMP). For some ships, no method enables one to impute the national registration. From 1839, the British allowed 
					their cruisers to take slave ships flying the Portuguese flag into British Vice-Admiralty Courts for confiscation 
					under British law. In response to this (and to similar legislation in 1845 that extended the provision to the 
					Brazilian flag), many slave ships abandoned ship registration papers altogether. In addition, there are undoubtedly 
					some voyages that registered in one country but that belonged to nationals of another, and others that sailed under 
					false papers. Some British ships sailing under the French flag in the late eighteenth century are examples of the 
					first; both British and United States owners sailing with Portuguese and Spanish papers after 1807—sometimes fraudulent, 
					sometimes not—are examples of the second. Overall, these cases probably account for less than 1 percent of the ships 
					included in the data set. It is also difficult to separate voyages made by ships owned in Britain from those owned in 
					the British Americas and later in the United States. Some vessels identified as "British" were likely registered in 
					the British Americas, and a similar problem of geographic specificity arises with the Portuguese and Brazilian ships 
					in the nineteenth century. A frequency count of this imputed flag variable is nevertheless of interest and indicates 
					that 12,014 voyages were British and that a further 2,175 were registered in the British Americas. Of the other principal 
					nations, 11,366 are identified as Portuguese or Brazilian; 4,181 were French; 1,574 were Dutch; 1,551 were Spanish or 
					Uruguayan; and 402 flew the flags of various Baltic states (mainly Danish), including Brandenburg-Prussia. Many of 
					the voyages of unknown nationality were likely Portuguese, but even if they all were, Portuguese voyages are still 
					somewhat underrepresented in the Voyages Database.</p>
					
					<p>Perhaps the most difficult of the imputed variables developed for the Voyages database is “Standardized tonnage” 
					(TONMOD) derived from the data variable “Tonnage.” This may be the least reliable of the imputed variables provided 
					in the Voyages database. It is offered here as a guide to ship size and to provide a crude basis for calculating 
					indices of crowding on ships for both slaves and crew, as well as a basis for examining the efficiency with which 
					the trade was conducted over time and between major ports and carriers. The reasons for the relative unreliability 
					of this variable begin with the differences between deadweight tonnage, tons burden (for loose-packed cargo), and 
					freight tons (for merchandise). But even within these types, jurisdictions often had their own methods of computation.
					<span class="superscript">xiii</span>  
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
					tons in their official documentation; from 1786, measured tons alone became the standard.<span class="superscript">xiv</span> 
					After 1807, slave ships were not usually of British origin, but reports of their activities originated from or were 
					transmitted through British channels. Much of the data were converted into British tons in the process. Reports from 
					the British Vice Admiralty Courts almost invariably list British tonnage, and in 1840 the Foreign Office instructed 
					its overseas "observers" to give tonnages as provided in the ships’ papers where possible, as well as in British tons.
					<span class="superscript">xv</span> Many tonnage data, however, are from non-British jurisdictions. 
					Several independent contemporary observers suggested that the Portuguese (and Brazilian) ton was perhaps 5 percent 
					smaller than its 1773–1835 British counterpart, and the Spanish ton 50 percent larger.<span class="superscript">xvi</span> 
					The differences between Portuguese and British tonnage for the nineteenth century, at least, seem small enough to 
					disregard. A regression equation is estimated for converting Spanish into British tons that suggests that the 
					former was perhaps two-thirds larger, with the difference varying somewhat by size of ship. United States tonnages 
					are taken to be the so-called "Custom House Measure" of 1789, which was modeled on the British formula. Although 
					some differences existed in the application of this rule among American ports, no adjustment is made here.
					<span class="superscript">xvii</span></p>
	
					<p>For the period before 1786, a further regression equation is estimated for converting Royal African Company 
					tonnages into the pre-1786 registered ton.<span class="superscript">xviii</span>  Also for this period, 
					the Dutch ton, or last, is taken to be double the size of the British registered ton, and the French tonneau 
					de mer is treated as equivalent to the British registered ton.<span class="superscript">xix</span> In addition to 
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
					French government based their subsidy of the slave trade on tonnages.<span class="superscript">xx</span> 
					The size of the bias is unknown and tonnages of French ships in this period are simply ignored in the conversion 
					procedure. A second bias (also not made here) is apparent in Portuguese tonnages between 1815 and 1830. A 
					Portuguese law of 1684, and clauses in the 1815 and 1817 Anglo-Portuguese slave trade treaties, limited 
					Portuguese and later Brazilian ships to a ratio of between 2.5 and 3.5 slaves per ton, depending on the 
					construction of the ship.<span class="superscript">xxi</span> Ratios were normally lower than this in 
					every branch of the trade for which data survive, and the regulation must have had little practical 
					impact. As pressure to suppress the trade mounted in the nineteenth century and conditions on board 
					deteriorated, it is possible that these strictures began to have some application. In any event, British 
					officials in Brazil between 1815 and 1830 (after which the complete Brazilian trade was illegal and such 
					regulations became moot) became convinced that the Portuguese tonnage measurements were being inflated by 
					60 percent on average so that more slaves could be confined on board.<span class="superscript">xxii</span> 
					The issue cannot be resolved on the available evidence and no adjustment is made here, but users have been warned.</p>
					
					<p>Finally, we consider two variables that will attract more attention than most. First, we use the data variable 
					“African resistance” (RESISTANCE) to compute a “Rate of Resistance” variable which is available only on the time 
					line and in Custom Graphs. This is simply the number of vessels experiencing some recorded act of resistance 
					divided by the total number of vessels in a given year and expressed as a percentage. The second is the “Sterling 
					cash price (of slaves) in Jamaica” variable (JAMCASPR), which may be used to track the price paid for slaves in 
					the Americas as they were sold from the vessel. The Voyages Database contains prices for those on board 959 voyages. 
					The full derivation of these data is described elsewhere, but a summary description is appropriate here.
					<span class="superscript">xxiii</span> Prices for human beings in the Americas were subject to as many 
					influences as were prices in any other market. Key factors included the characteristics of the person being 
					sold, the distance between slave markets in the Americas and Africa and the price of the captive in Africa. 
					This variable attempts to adjust for several of these factors so that the underlying price trends become 
					apparent to the user of Voyages. In most cases the data are taken from the slave traders’ accounts and 
					correspondence. Our first goal was to ensure that we recorded a single category of captive – what was 
					frequently referred to at the time as “a prime male”.<span class="superscript">xxiv</span> Second, we adjusted 
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
					
					<p style="text-align: center;">Appendix</p>
					<p style="text-align: center;">Derivation of Estimated number of Captives Carried on Vessels in the Voyages 
					Database for which such Information cannot be obtained from the Sources </p>
					
					<table>
					</table>
					
					<div style="border-top: 1px solid #CCCCCC;width:180px;"></div>
					<p><span class="superscript">i</span> David Eltis and David Richardson (eds.), Extending the Frontiers: 
					Essays on the New Transatlantic Slave trade Database (New Haven, 2008).</p>
					
					<p><span class="superscript">ii</span> Joseph Inikori has given "a preferred global figure of 15.4 million 
					for the European slave trade." Adjusting for those carried to the offshore islands and Europe, this implies 
					14.9 million headed for the Americas. See Cahiers d’Etudes africaines, 32 (1993):686</p>
					
					<p><span class="superscript">iii</span> H.C.V. Leibbrandt, Precis of the Archives of the Cape of Good Hope, 
					Vol. 14, Journal 1662–1670 (Cape Town, 1901), 127–8</p>
					
					<p><span class="superscript">iv</span> British slave ships trading from Africa to Lisbon include the 
					Kent (1731), the Mary (1737) and the Betsey and Hennie (1755). For sources see the data set. For the 
					removal of slaves from Ambriz to St. Helena and Sierra Leone, see Kelly Muspratt to Aberdeen, 31 July 1843, 
					British National Archives (henceforth BNA), FO84/501.</p>
					
					<p><span class="superscript">v</span> A separate discussion of tonnage is to be found below.</p>
					
					<p><span class="superscript">vi</span> One frequently cited shipping list reports that there were no 
					children on board several British slave voyages in the 1790s (House of Lords Record Office, House of Lords, 
					Main Papers, 28 July 1800). This document, however, omitted to report the children embarked (cf. BNA, T70/1574; 
					House of Lords Record Office, House of Lords, Main Papers, 14, 25 June 1799). In Luanda and Benguela Portuguese 
					customs reports of departures for Brazil report very low numbers of children embarked. But in this instance 
					“children” refers to infants only and was above a tax category that indicated exemption from customs duties.</p>
					
					<p><span class="superscript">vii</span> Mediterranean passes were issued by most European nations as a result of 
					treaties with the Barbary powers. In theory, these documents allowed the vessels of the signing nation to pass 
					freely through the "Mediterranean" waters frequented by Barbary corsairs. The passes record vessel and captain’s 
					names, tonnage, the date the pass was issued, and intended trading location, such as "Africa" or "Africa and the 
					Americas" or "Barbary" or "Madeira." See David Richardson, The Mediterranean Passes (Wakefield, 1981). On different 
					numbers reported in the Americas, in the date given for the arrival of slave vessels might be the date the vessel 
					cleared customs, but in fact this could easily occur 2–4 weeks after the actual arrival.</p>
					
					<p><span class="superscript">viii</span> Robert Louis Stein, The French Slave Trade in the Eighteenth Century: 
					An Old Regime Business, (Madison, Wisc, 1979), p. xv. Some armateurs also may have owned the vessel. French 
					dictionaries define armateurs firstly as those merchants who fit out the ship or expedition and secondly as 
					(ship)owners (E. Littré, Dictionnaire de la Langue Française (Paris, 1881), I, 194).</p>
					
					<p><span class="superscript">ix</span> One major aid in identifying produce vessels is the Seaman’s Sixpence 
					ledgers (BNA, ADM 68 series). One of our team, Jelmer Vos went through this large series with great care. </p>
					
					<p><span class="superscript">x</span> One of the most widely used contemporary surveys of African regional 
					preferences was Lt. Edward Bold, The Merchants and Mariners’ African Guide (London, 1819). For a very detailed 
					private record see the manuscript in the Sidney Jones Library, University of Liverpool, "Memorandum of African 
					Trade, 1830–1840," for W.A. Maxwell and Co.</p>
					
					<p><span class="superscript">xi</span> See Pierre Verger, Trade Relations Between Bahia de Todos os Santos and 
					the Bight of Benin, 17th to the 19th Century (Ibadan, 1976), pp. 358–61, and David Eltis, "The Export of Slaves 
					from Africa, 1820–43," Journal of Economic History, 37 (1977), 417–20, for a fuller discussion.</p>
					
					<p><span class="superscript">xii</span> In the downloadable version they would use the data variables 
					“Percentage male embarked*” (MALRAT1), “Percentage male disembarked*” (MALRAT3), “Percentage children embarked*” 
					(CHILRAT1) and “Percentage children disembarked*” (CHILRAT3) variables instead of the variables, “Percentage male*” 
					(MALRAT7) and “Percentage children*” CHILRAT7.</p>
					
					<p><span class="superscript">xiii</span> For discussion of the general problem see Frederick C. Lane, "Tonnages, 
					Medieval and Modern," Economic History Review, 17 (1964–5), 213–33.</p>
					
					<p><span class="superscript">xiv</span> The 1773 legislation is 13 Geo III, c. 74. See W. Salisbury, "Early 
					Tonnage Measurements in England: I, H.M. Customs and Statutory Rules," Mariner’s Mirror, 52 (1966), 329–40. 
					To convert registered tons into measured tons, we used the formulae in Christopher J. French, "Eighteenth 
					Century Shipping Tonnage Measurements," Journal of Economic History, 33 (1973), 434–43. The 1786 act is 26 
					Geo III, c. 60, and its 1835 counterpart is 5 and 6 Will IV, c. 56, which introduced different rules for 
					empty ships (s. 2) and those with cargo (s. 6). As the latter appears to have been used on slave ships, 
					it is the one adopted here, and a further regression equation allows us to convert post-1836 tonnages 
					into the measured ton of 1773–1835. It is: <br>
					Y = 52.86 + (1.22 x X)   N = 63, R² = 0.77 <br>
					where Y = measured tons, 1773–1835, and X = measured tons after 1835.</p>
					
					<p><span class="superscript">xv</span> Palmerston to Kennedy, May 4, 1840 (circular dispatch), BNA, FO84/312</p>
					
					<p><span class="superscript">xvi</span> H. Chamberlain to Canning, 18 Sept. 1824 (enc.), FO84/31; W. Cole and 
					H. W. Macaulay to Palmerston, 1 Jan. 1835 (enc.), BNA, FO84/169; W. W. Lewis and R. Docherty to Palmerston, 
					9 Sept. 1837 (enc.), BNA, FO84/214; J. Barrow to Aberdeen, 16 May 1842 (enc.), BNA, FO84/439; G. Jackson and 
					F. Grigg to Aberdeen, 2 Jan. 1841 (enc.), BNA, FO84/350.</p>
					
					<p><span class="superscript">xvii</span> For Spanish into British tonnage data are limited. The equation is:<br>
				    Y = 71 + (0.86 x X)   N = 32, R² = 0.66.<br>
				    Where Y = British measured tons, 1773–1835, and X = Spanish tons.<br> 
					For US and British see United States Statutes at Large, Vol. 1, p. 55. For a discussion see W. Salisbury, 
					"Early Tonnage Measurements in England: IV, Rules Used by Shipwrights and Merchants," Mariner’s Mirror, 
					53 (1967): 260–64.</p>
					
					<p><span class="superscript">xviii</span> See David Eltis and David Richardson, "Productivity in the 
					Transatlantic Slave Trade," Explorations in Economic History, 32 (1995), 481, for the formula and a discussion.</p>
					
					<p><span class="superscript">xix</span> See Lane, "Tonnages, Medieval and Modern," 217–33 for a discussion.</p>
					
					<p><span class="superscript">xx</span>  Stein, French Slave Trade, 40–1; Patrick Villiers, "The Slave and Colonial 
					Trade in France just before the Revolution," in Babara Solow and Stanley L. Engerman (eds.), Slavery and the Rise 
					of the Atlantic System (Cambridge, 1991), p. 228.</p>
					
					<p><span class="superscript">xxi</span> See Herbert S. Klein, The Middle Passage: Comparative Studies in the Atlantic 
					Slave Trade (Princeton, 1978), p. 29–31.</p>
					
					<p><span class="superscript">xxii</span> See for example H. Chamberlain to Canning, 7 July 1824 (enc.), BNA, FO84/31.</p>
					
					<p><span class="superscript">xxiii</span> David Eltis and David Richardson, ASlave Prices of Newly Arrived Africans 
					in the Americas, 1673-1807: A Quinquennial Series," in Susan Carter, Scott Gartner et al. (eds.), Historical 
					Statistics of the United States (Cambridge, 2006), 5: 690-1, idem, AMarkets for Newly Arrived Slaves in the 
					Americas, 1673-1864,@ in David Eltis, Frank Lewis and Kenneth Sokoloff (eds.), Slavery in the Development of 
					the Americas (Cambridge, 2004), pp, 183-221.</p>
					
					<p><span class="superscript">xxiv</span> More specifically, the price presented here is the average of the first ten 
					males sold off the vessel.</p>				
					
				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
</f:view>
</body>
</html>