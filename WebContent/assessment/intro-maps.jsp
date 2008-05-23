<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Introductory Maps</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-intro-maps.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
	<script language="javascript" type="text/javascript">
	function popitup(url) {
		newwindow=window.open(url,'name','height=768,width=1024,resizable, scrollbars, location=0');
		if (window.focus) {newwindow.focus()}
		return false;
	}
	</script>

</head>
<body>
<a name="top"></a>

<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Introductory Maps" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="intro-maps-layout">
		<tr>
			<td id="intro-maps-left-column">
			
				<t:div styleClass="left-menu-box">
					<s:simpleBox>
					
						<t:div styleClass="left-menu-title">
							<h:outputText value="INTRODUCTORY MAPS" />
						</t:div>
						
						<s:secondaryMenu>
							<s:secondaryMenuItem label="Map 1: Overview of the slave trade out of Africa, 1500-1900" menuId="map1" href="#map1"/>
							<s:secondaryMenuItem label="Map 2: Migration of sugar cultivation from Asia into the Atlantic" menuId="map2" href="#map2"/>
							<s:secondaryMenuItem label="Map 3: Old World slave trade routes in the Atlantic before 1759" menuId="map3" href="#map3"/>
							<s:secondaryMenuItem label="Map 4: Wind and ocean currents of the Atlantic basins" menuId="map4" href="#map4"/>
							<s:secondaryMenuItem label="Map 5: Major regions and ports involved in the transatlantic slave trade, all years" menuId="map5" href="#map5"/>
							<s:secondaryMenuItem label="Map 6: Countries and Regions in the Atlantic World where Slave Voyages were Organized, by Share of Captives Carried off from Africa" menuId="map6" href="#map6"/>
							<s:secondaryMenuItem label="Map 7: Major coastal regions from which captives left Africa, all years" menuId="map7" href="#map7"/>
							<s:secondaryMenuItem label="Map 8: Major regions where captives disembarked, all years" menuId="map8" href="#map8"/>
							<s:secondaryMenuItem label="Map 9: Volume and direction of the transatlantic slave trade from all African to all American regions" menuId="map9" href="#map9"/>
						</s:secondaryMenu>						
					
					</s:simpleBox>
				</t:div>
			</td>
			<td id="intro-maps-right-column">
				<s:simpleBox>
			
					<h1>Introductory Maps</h1>
					
					<a name="map1"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/1_Slave_Trade_Overview.jsp">
									<img src="./intro-maps/01-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/1_Slave_Trade_Overview.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 1: Overview of the slave trade out of Africa, 1500-1900</h2>
		
							<p>Captive Africans followed many routes from their homelands
							to other parts of the world. The map shows the trans-Atlantic
							movement of these captives in comparative perspective for the
							centuries since 1500 only. Estimates of the ocean-borne trade are
							more robust than are those for the trans-Saharan, Red sea and
							Persian Gulf routes, but it is thought that for the period from the
							end of the Roman Empire to 1900 about the same number of captives
							crossed the Atlantic as left Africa by all other routes combined.</p>
	
						</div>
					
					</div>
					
					<a name="map2"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/2_Migration_Sugar_Cultivation.jsp">
									<img src="./intro-maps/02-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/2_Migration_Sugar_Cultivation.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 2: Migration of sugar cultivation from Asia into the Atlantic</h2>
	
							<p>Sugar cultivation began in the Pacific in the pre-Christian
							era and gradually spread to the eastern Mediterranean, the Gulf of
							Guinea, then to Brazil, before entering the Caribbean in the
							mid-seventeenth century. Eighty percent of all captives carried
							from Africa were taken to sugar-growing areas.</p>

						</div>

					</div>
					
					<a name="map3"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/3_Old_World_SlaveTrade.jsp">
									<img src="./intro-maps/03-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/3_Old_World_SlaveTrade.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 3: Old World slave trade routes in the Atlantic before 1759</h2>
	
							<p>Before the Atlantic slave trade began and for two centuries
							thereafter, some African captives were taken to Europe as well as
							to the Atlantic islands and between African ports. It is hard to
							get precise estimates of these flows, but they were certainly much
							smaller than the transatlantic traffic. Many of the captives
							involved in this traffic were subsequently carried to sugar
							plantations in the Old World.</p>

						</div>
						
					</div>
					
					<a name="map4"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/4_Wind_currents.jsp">
									<img src="./intro-maps/04-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/4_Wind_currents.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 4: Wind and ocean currents of the Atlantic basins</h2>
		
							<p>In the age of sail, winds and ocean currents shaped the
							direction of the transatlantic slave trade, effectively creating
							two separate slave-trading systems – one in the north with voyages
							originating in Europe and North America, the other in the south
							with voyages originating in Brazil.</p>
		
						</div>
						
					</div>
					
					<a name="map5"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/5_Atlantic_Basin.jsp">
									<img src="./intro-maps/05-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/5_Atlantic_Basin.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 5: Major regions and ports involved in the transatlantic slave trade, all years</h2>

							<p>Few commercial centers in the Atlantic world were untouched by the slave 
							trade, and all the major ports had strong connections with the traffic.</p>

						</div>
						
					</div>
					
					<a name="map6"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/6_Europe_Overview.jsp">
									<img src="./intro-maps/06-thumb.png" width="200" height="200" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/6_Europe_Overview.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 6: Countries and Regions in the Atlantic World where
							Slave Voyages were Organized, by Share of Captives Carried
							off from Africa</h2>
		
							<p>Slave voyages were organized and left from all major
							Atlantic ports at some point over the nearly four centuries of the
							transatlantic slave trade. Nevertheless, vessels from the largest
							seven ports, Rio de Janeiro, Bahia, Liverpool, London, Nantes,
							Bristol, and Pernambuco carried off almost three-quarters of all
							captives removed from Africa via the Atlantic Ocean. There was a
							major shift in the organization of slaving voyages first from the
							Iberian peninsular to Northern Europe, and then later back again to
							ports in southern Europe. A similar, but less pronounced shift may
							be observed in the Americas from South to North and then back
							again.</p>
							
							<p>
								Total documented embarkations: <b>8,973,701 captives</b><br>	
								Percent of estimated embarkations: <b>72.1%</b>
							</p>
		
						</div>
						
					</div>
					
					<a name="map7"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/7_Africa_Overview.jsp">
									<img src="./intro-maps/07-thumb.png" width="200" height="200" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/7_Africa_Overview.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 7: Major coastal regions from which captives left Africa,
							all years</h2>

							<p>The limits of the regions shown here are “Senegambia,”
							anywhere north of the Rio Nunez. Sierra Leone region comprises the
							Rio Nunez to just short of Cape Mount. The Windward Coast is
							defined as Cape Mount south-east to and including the Assini river.
							The Gold Coast runs east of here up to and including the Volta
							River. Bight of Benin covers the Rio Volta to Rio Nun, and the
							Bight of Biafra, east of the Nun to Cape Lopez inclusive.
							West-central Africa is defined as the rest of the western coast of
							the continent south of this point, and south-eastern Africa
							anywhere from and to the north and east of the Cape of Good Hope.
							West-Central Africa was the largest regional departure point for
							captives through most the slave trade era. Regions closer to the
							Americas and Europe generated a relatively small share of the total
							carried across the Atlantic. Voyage length was determined as much
							by wind and ocean currents shown in Map 4 as by relative proximity
							of ports of embarkation and disembarkation.</p>

							<p>
								Total documented embarkations: <b>7,878,500 captives</b><br>	
								Percent of estimated embarkations: <b>63.3%</b>
							</p>
		
						</div>
						
					</div>
					
					<a name="map8"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/8_Americas_Overview.jsp">
									<img src="./intro-maps/08-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/8_Americas_Overview.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 8: Major regions where captives disembarked, all years</h2>

							<p>The Caribbean and South America received 95 percent of the
							slaves arriving in the Americas. Some captives disembarked in
							Africa rather than the Americas because their transatlantic voyage
							was diverted as a result of a slave rebellion or, during the era of
							suppression, because of capture by patrolling naval cruisers. Less
							than 4 percent disembarked in North America, and only just over
							10,000 in Europe.</p>

							<p>
								Total documented embarkations: <b>9,371,001 captives</b><br>	
								Percent of estimated embarkations: <b>88.5%</b>
							</p>
		
						</div>
						
					</div>
					
					<a name="map9"></a>
					
					<div class="map">
					
						<div class="map-thumbnail">
							<div class="map-img">
								<a href="./intro-maps/8A_Transatlantic.jsp">
									<img src="./intro-maps/09-thumb.png" width="200" height="160" border="0" />
								</a>
							</div>
							<div class="map-caption">
								<a href="./intro-maps/8A_Transatlantic.jsp">
									Show detail
								</a>
							</div>
						</div>
	
						<div class="map-description">
	
							<h2>Map 9: Volume and direction of the transatlantic slave
							trade from all African to all American regions</h2>

							<p>This map summarizes and combines the many different paths by
							which captives left Africa and reached the Americas. While there
							were strong connections between particular embarkation and
							disembarkation regions, it was also the case that captives from any
							of the major regions of Africa could disembark in almost any of the
							major regions of the Americas. Even captives leaving Southeast
							Africa, the region most remote from the Americas, could disembark
							in mainland North America, as well as the Caribbean and South
							America. The data in this map are based on estimates of the total
							slave trade rather than documented departures and arrivals.</p>
		
						</div>
						
					</div>
					
				</s:simpleBox>
			</td>
		</tr>
		</table>

	</div>

</h:form>
	
</f:view>

</body>
</html>
