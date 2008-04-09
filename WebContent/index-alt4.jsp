<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Trans-Atlantic Slave Trade</title>
	
	<link href="./styles/main.css" rel="stylesheet" type="text/css">
	<link href="./styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="./styles/index.css" rel="stylesheet" type="text/css">
	
	<script src="./scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="./scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="./scripts/welcome-map.js" language="javascript" type="text/javascript"></script>
	
	<script language="javascript" type="text/javascript">
	
	WelcomeMapGlobals.register(new WelcomeMap(
		"main", "map-hint", [
			
			new WelcomeMapPlace(
				"map_north_america_label",
				"./images/index/map-north-america.png",
				"./images/index/map-north-america-active.png",
				"map_north_america_text"),
			
			new WelcomeMapPlace(
				"map_caribbean_label",
				"./images/index/map-caribbean.png",
				"./images/index/map-caribbean-active.png",
				"map_caribbean_text"),
			
			new WelcomeMapPlace(
				"map_brazil_label",
				"./images/index/map-brazil.png",
				"./images/index/map-brazil-active.png",
				"map_brazil_text"),
				
			new WelcomeMapPlace(
				"map_europe_label",
				"./images/index/map-europe.png",
				"./images/index/map-europe-active.png",
				"map_europe_text"),

			new WelcomeMapPlace(
				"map_africa_label",
				"./images/index/map-africa.png",
				"./images/index/map-africa-active.png",
				"map_africa_text")
				
		]));
	
	</script>
	
</head>
<body>
<f:view>

	<s:siteHeader>
		<h:outputText value="Home"/>
	</s:siteHeader>
	
	<div class="main-content">
		
		<table border="0" cellspacing="0" cellpadding="0" class="welcome-band">
		<tr>
			<td valign="top">
			
				<div id="welcome-map">
					
					<img
						style="position: absolute; top: 0px; left: 0px;"
						id="welcome-image"
						src="./images/index/map.png"
						width="430" height="340" border="0" />
						
					<img
						style="position: absolute; top: 50px; left: 50px;"
						id="map_north_america_label"
						src="./images/index/map-north-america.png"
						width="120" height="80" border="0"  />
					
					<img
						style="position: absolute; top: 200px; left: 100px;"
						id="map_brazil_label"
						src="./images/index/map-brazil.png"
						width="100" height="60" border="0"  />
					
					<img
						style="position: absolute; top: 125px; left: 95px;"
						id="map_caribbean_label"
						src="./images/index/map-caribbean.png"
						width="120" height="60" border="0"  />
					
					<img
						style="position: absolute; top: 50px; left: 260px;"
						id="map_europe_label"
						src="./images/index/map-europe.png"
						width="100" height="60" border="0"  />
					
					<img
						style="position: absolute; top: 130px; left: 270px;"
						id="map_africa_label"
						src="./images/index/map-africa.png"
						width="100" height="60" border="0"  />
				
				</div>
				
				<div id="welcome-map-region-placeholder" style="margin-left: 30px;">
				
					<div id="map-hint" style="text-align: left; padding: 10px 0px 0px 0px;">
					
						<div>
						Roll over names of designated regions on the map
						above for descriptions of the role of each in
						the trans-Atlantic slave trade.
						</div>
						
						<div style="margin-top: 10px;">
						Click on the links below to view two special features
						of the <i>Voyages</i> website:
						</div>
						
						<div id="other-links" style="background: none; margin: 0px; padding: 15px 0px 0px 0px">
							<table border="0" cellspacing="0" cellpadding="0" style="margin-top: 0px;">
							<tr>
								<td><img src="./images/index/link-arrow.png" border="0" width="16" height="16" align="top" alt="" /></td>
								<td style="padding-left: 5px;"><a href="./assessment/intromaps.faces">Introductory Maps</a></td>
								<td style="padding-left: 15px;"><img src="./images/index/link-arrow.png" border="0" width="16" height="16" align="top" alt="" /></td>
								<td style="padding-left: 5px;"><a href="./assessment/estimates.faces?module=timeline">Estimates Timeline</a></td>
							</tr>
							</table>
						</div>
					
					</div>
				
					<div class="welcome-map-region-text" id="map_north_america_text" style="display: none">
						<div class="welcome-map-region-name"><img src="./images/index/map-north-america-title.png" border="0" width="180" height="40" align="top" alt="" /></div>
						<div class="welcome-map-region-desc">
							The North American mainland played a relatively minor role in the transatlantic
							slave. Its ports sent out less than five percent of all known voyages, and its
							slave markets absorbed less than four percent of all slaves carried off from Africa.
							An intra-American trade in slaves – originating in the Caribbean - supplied additional
							slaves however. This region was exceptional in the Americas in that a positive rate
							of natural population growth began relatively early, thus reducing the dependence
							of the region on coerced migrants.
						</div>
					</div>
					
					<div class="welcome-map-region-text" id="map_caribbean_text" style="display: none">
						<div class="welcome-map-region-name"><img src="./images/index/map-carebbian-title.png" border="0" width="120" height="40" align="top" alt="" /></div>
						<div class="welcome-map-region-desc">
							The Caribbean was one of the two major broad regional markets for slaves from
							Africa. Over the two centuries when the trade was at its height, the major
							locations for sugar production, and therefore the major slave markets, shifted
							from the eastern Caribbean to the west. Here, first Jamaica, then St Domingue
							and finally in the 19th century, Cuba absorbed most of the slaves brought into
							the region. As this implies, few islands developed self-sustaining populations
							at any point in the slave trade era. Caribbean ports also sent out more slaving
							expeditions to Africa than did the North American mainland ports.
						</div>
					</div>
	
					<div class="welcome-map-region-text" id="map_brazil_text" style="display: none">
						<div class="welcome-map-region-name"><img src="./images/index/map-brazil-title.png" border="0" width="75" height="40" align="top" alt="" /></div>
						<div class="welcome-map-region-desc">
							Brazil was the center of the slave trade carried on under the Portuguese
							flag, both before and after Brazilian independence in 1822 - and Portugal was by
							far the largest of the national carriers. Brazil dominated the slave trade in the
							sense that Rio de Janeiro and Bahia sent out more slaving voyages than any port in
							Europe, and certainly many times more than did Lisbon. Over nearly three centuries
							between 1560 and 1852, Brazil was consistently the largest destination for slaves
							in the Americas. Almost all the slaves coming in to the region came from just two
							coastal areas in Africa, the Bight of Benin and West-central Africa.
						</div>
					</div>
	
					<div class="welcome-map-region-text" id="map_europe_text" style="display: none">
						<div class="welcome-map-region-name"><img src="./images/index/map-europe-title.png" border="0" width="90" height="40" align="top" alt="" /></div>
						<div class="welcome-map-region-desc">
							Europe was the starting point for about half all transatlantic slaving voyages.
							This traffic dominated the West African to Caribbean section of the slave trade.
							The major ports were at first located in the Iberian peninsular but by the eighteenth
							century northern European ports had become dominant. After 1807, France and the
							Iberian ports sent out the great majority of European-based slaving voyages.
							The European consumers’ demand for sugar was the driving force behind 350 years
							of transatlantic slave trading.
						</div>
					</div>
	
					<div class="welcome-map-region-text" id="map_africa_text" style="display: none">
						<div class="welcome-map-region-name"><img src="./images/index/map-africa-title.png" border="0" width="80" height="40" align="top" alt="" /></div>
						<div class="welcome-map-region-desc">
							Sub-Saharan Africa lost over twelve and a half million people to the
							transatlantic slave trade alone between 1526 and 1867. Perhaps as many
							again were carried of to slave markets across the Sahara and the Indian Ocean.
							Over forty percent of captives left from West-central Africa alone with most of
							the remainder leaving from the Bight of Benin, the Bight of Biafra and the Gold
							Coast. About one in eight died on board the slave vessel and many others died
							prior to departure and after arrival. Departures were channeled through a dozen
							or so major embarkation points such as Whydah, Bonny, Loango, Luanda and Benguela,
							though many smaller ports also supplied slaves.
	  					</div>
					</div>
					
				</div>

			</td>
			<td valign="top">
			
				<!-- This is ugly, but it's the only way to make sure that each
				browser renders the text exactly on the same number of lines.
				Alternatively, we could create this as an image. -->
			
				<div id="welcome-text">
					<div id="welcome-text-big"><img src="./images/index/welcome-text.png" width="530" height="80" border="0" /></div>
					<div id="welcome-text-small">
						<div>forcibly transporting over 12 million Africans to the Americas between</div>
						<div>the sixteenth and the nineteenth centuries. It offers descendants,</div>
						<div>researchers, students, and the general public a chance to rediscover</div>
						<div>the reality of one of the largest movements of peoples in world history.</div>
					</div>
				</div>
				
				<table border="0" cellspacing="0" cellpadding="0" id="featured-sections">
				<tr>
					<td class="featured-section-image">
						<a href="./database/search.faces">
							<img src="./images/index/link-database.png" width="124" height="64" border="0" />
						</a>
					</td>
					<td class="featured-section-text">
						<div class="featured-section-caption">
							<a href="./database/search.faces">
							Search the Database of Voyages
							<img src="./images/index/link-arrow.png" border="0" width="16" height="16" align="top" alt="" />
							</a>
						</div>
						<div class="featured-section-description">
							The database contains records about more than 
							35,000 trans-atlantic slave voyages and
							was collected over the past 20 years.
						</div>
					</td>
				</tr>
				<tr>
					<td class="featured-section-image">
						<a href="./assessment/estimates.faces">
							<img src="./images/index/link-estimates.png" width="124" height="64" border="0" />
						</a>
					</td>
					<td class="featured-section-text">
						<div class="featured-section-caption">
							<a href="./assessment/estimates.faces">
							View our Estimates of the Slave Trade
							<img src="./images/index/link-arrow.png" border="0" width="16" height="16" align="top" alt="" />
							</a>
						</div>
						<div class="featured-section-description">
							The estimates were obtained by combining
							the voyage database with other sources in order
							to compensate for the missing information
							in the voyage database.
						</div>
					</td>
				</tr>
				<tr>
					<td class="featured-section-image">
						<a href="./resources/slaves.faces">
							<img src="./images/index/link-slaves.png" width="124" height="64" border="0" />
						</a>
					</td>
					<td class="featured-section-text">
						<div class="featured-section-caption">
							<a href="./resources/slaves.faces">
							Explore the Database of Slaves
							<img src="./images/index/link-arrow.png" border="0" width="16" height="16" align="top" alt="" />
							</a>
						</div>
						<div class="featured-section-description">
							Explore the database of names of Africans liberated
							from slave vessels in Sierra Leone and Havana.
						</div>
					</td>
				</tr>
				</table>

			</td>
		</tr>
		</table>
		
		<div class="footer">
		
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="4"><div class="footer-section">Sponsors</div></td>
				<td colspan="4"><div class="footer-section">Partners</div></td>
			</tr>
			<tr>
				<td><a href="http://www.emory.edu" class="footer-link"><img src="./images/emory-logo-index.png" width="82" height="32" border="0" alt="Emory" /></a></td>
				<td style="padding-right: 5px;"><a href="http://www.neh.gov"><img src="./images/neh-logo.png" width="32" height="32" border="0" alt="NEH" /></a></td>
				<td><a href="http://www.neh.gov" class="footer-link">National Endowment<br>for the Humanities</a></td>
				<td><a href="http://www.fas.harvard.edu/~du_bois" class="footer-link">W.E.B. Du Bois Institute<br>(Hardward, USA)</a></td>
				<td><a href="http://web.library.emory.edu" class="footer-link">Emory University<br>Libraries (USA)</a></td>
				<td><a href="http://www.hull.ac.uk" class="footer-link">The University<br>of Hull (UK)</a></td>
				<td><a href="http://www.ufrj.br" class="footer-link">Universidade Federal do<br>Rio de Janeiro (Brazil)</a></td>
				<td style="padding-right: 0px;"><a href="http://www.vuw.ac.nz" class="footer-link">Victoria University of<br>Wellington (New Zealand)</a></td>
			</tr>
			</table>
			
		</div>
	
	</div>

</f:view>
</body>
</html>