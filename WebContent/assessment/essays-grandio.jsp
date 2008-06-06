<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-grandio" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Dobo: A Liberated African in Nineteenth Century Havana</h1>
					
					<div class="essay-info">
						<span class="essay-author">Oscar Grandio Moraguez</span>
						<span class="essay-location">(York University)</span>
						<span class="essay-date"></span>
					</div>

					<p>In the sixty years after 1807 many former slave trading nations, 
					in particular, the British, launched a major effort to suppress the slave 
					trade. In this era, the voyages database shows that naval cruisers captured 
					1,985 slave ships and conferred liberated African status on 177,000 of the 
					slaves found on board some of these vessels. The fate of these “re-captives” 
					as they were called varied considerably. The majority spent lives as self-sufficient 
					farmers or fishermen with only occasional involvement in the free labor market. 
					About one quarter of the group went to the British West Indies as contract laborers. 
					For a few thousand others, there was a life little different from slavery. 
					The story which follows is for one of the latter group. It is a story which 
					the search interfaces of this site have helped reconstruct.</p>

					<p>In March, 1826 HMS “Speedwell” detained a small Spanish schooner named “Fingal” 
					off the Cuban coast with 58 slaves, the survivors of 61, en route from Cape Mount, 
					near Galinhas. After detention, the slaver, its crew and human cargo were taken into 
					Havana where the slaves disembarked. In the voyages database the details of the voyage 
					are recorded under voyageid 558. British and Spanish officials interviewed the captives 
					and recorded their names, ages and places of habitation. The officials added data on height, 
					sex and a description of the most obvious cicatrisation. From the docks, the newly disembarked 
					Africans were taken to a nearby barracon, where they were held by the Cuban colonial government 
					prior to their transfer as emancipated Africans to residents of the island from whom they were 
					supposed to receive religious and work training. After a five year term, they were to be 
					integrated into Cuban society as free persons.</p>

					<p>The “Fingal” was one of many slavers adjudicated at the Havana Court of Mixed 
					Commission which, under the 1817 Anglo-Spanish treaty, was one of the courts 
					established to adjudicate suspected slave vessels detained under the Spanish 
					or British flags and to declare any slaves on board to be emancipados or liberated 
					Africans.  Under the provisions of the treaty the two governments agreed that the 
					emancipados should become free people in the territory where the adjudication took 
					place. It soon became clear, however, that the lives of the liberated Africans on 
					the island were little different from those of slaves.(2) Certainly, the Cuban 
					authorities did not welcome the responsibility of distributing and monitoring 
					thousands of free Africans. </p>

					<p>The story of one of the Africans on board the “Fingal” disembarked in Havana would prove 
					how difficult was living in Cuba for all emancipados that fell under the Spanish government 
					jurisdiction. This African was Dobo, a ten years old boy from the Galinhas hinterland who was 
					one of the unfortunate souls on board the “Fingal”. Dobo’s story, or Gabino as he was renamed 
					after his arrival in Cuba, is one of the most revealing accounts of the trauma and re-identification 
					that accompanied each of the liberated Africans that had the unfortunate fate of been enslaved in 
					Africa, shipped off to Cuba as a slave, and then, after apparent rescue, became a Cuban emancipado. 
					His personal story offers insight into how the slave trade was conducted in Africa, and how emancipated 
					Africans in Cuba were effectively slaves.</p>
					
					<p>When the youth was landed in Havana, court officials interviewed him. Because Dobo’s 
					slaver took slaves on the Windward coast, at Cape Mount, near Galinhas, he was described 
					as a Ganga, a term given in Cuba to all people embarked at ports from the Sierra Leone 
					and Windward Coasts.(3) But Dobo also identified himself as a member of the Kongoba nation 
					and entered the Court’s Liberated African register as “Ganga-longová”. This register is 
					one of the two sources for the names database and in it Dobo has a slaveid of 70345.  
					He came from the interior of Galinhas, from an area occupied by Gola people, an ethno 
					linguistic subgroup of Mel speakers that during the seventeenth and eighteenth centuries 
					had spread to the Galinhas hinterland from their northern homeland in the Kongba 
					region – this is the reason they called themselves Kongoba. They eventually occupied 
					a large section of the region between the Lofa and Cape Mount, and displacing in their 
					expansion Dei groups who formerly occupied the area.(4)</p>
					
					<p>By the time Dobo was shipped to Cuba, Spanish slave agents were very active in the Galinhas 
					coast, and traded extensively with local chiefs, especially from the Dei and the Gola, who were 
					at war with each other. The Spanish obtained prisoners from both sides.(5) Dei people belonged to 
					the Kwa linguistic family, who occupied in the nineteenth century an area that extended from the 
					coast to the hinterland of Galinhas. Being gradually displaced from the hinterland by the continuous 
					expansion toward the coast of Gola groups, Dei by the nineteenth century had a conflictive relationship 
					with their Gola neighbors.(6) The results of constant skirmishes between the two parties was reflected in 
					the permanent production of captives, that were eagerly exchanged for imported goods at coastal entrepots 
					operated by Spanish agents, the most notorious of whom was Pedro Blanco. Vai traders, who belonged to the 
					Mande linguistic family and who inhabited the coastal area of Galinhas, acted as middlemen between European 
					traders and Dei and Gola slave traders.(7) Blanco and other Spanish traders had struck up a profitable and 
					efficient working relationship with local Vai chiefs, while rapidly expanding the scope of their operations, 
					stationing agents at Cape Mount, Shebar, Digby (near Monrovia), Young Sestos, and elsewhere. </p>
					
					<p>Young Dobo was traded by one of these slave trader networks. There is no information available 
					on how he entered slavery in his Gola homeland. Probably he was a victim of a Dei raid to a Gola 
					settlement, was sold by relatives, or was born as a slave and traded out of his original masters’ 
					community. It was common for Dei or Gola rulers desiring imported goods to raid neighboring peoples 
					and send their captives to the coast in exchange for such items as salt, tobacco, and various metals, 
					particularly brass objects. However, not all slaves traded from the interior were victims of raids or 
					were prisoners of war. There were several ways in which people became slaves in the hinterland of the 
					Galinhas region.(8) One of the more important was through birth. People born of slave parents 
					automatically were considered slaves. People were not born as slaves, might become so through 
					being orphaned. The practice of bartering children and even sometimes adults for food during 
					famines - to save the rest of the community - was not unknown. Children were also usually used 
					as payment to another kin group to compensate for homicides and other crimes committed by their 
					relatives. Adults expelled from their original kin groups might also risk enslavement when forced 
					to leave their own kin groups because of quarrels, threats, hunger, or criminal activity. Dobo 
					could have entered slavery in any one of these ways.</p>
					
					<p>Before reaching Galinhas coast, Dobo had to travel many miles from the Gola hinterland, and 
					almost certainly he was bought and sold more than once during his journey. From the moment he 
					was captured or sold for the first time, Dobo found himself surrounded by strangers. As Vai 
					traders acted as middlemen in the coastal slave trade, Dobo could have been transferred from 
					Gola or Dei traders to Vai merchants who brought him to the coast. As a newcomer in alien 
					communities and separated from his own ethnic group, Dobo must have had to redefine a new 
					identity, social personality, and status. New surroundings usually implied new customs, languages or dialects.(9) </p>
					
					<p>When he arrived to the coast, he was probably housed in a slave factory belonging to one of 
					the Spanish factors around Galinhas before being traded for imported commodities. Cape Mount 
					from whence the “Fingal” had sailed, functioned as an auxiliary shipping point of the main 
					slave entrepot in the area, the Galinhas River between the Mano and Moa Rivers. At Cape Mount 
					several slave depots operated in the 1820s and 1830s, including one owned by Pedro Blanco. 
					Once the exchange of slaves for merchandise was made, Dobo and 60 others were taken on board 
					the slaver by canoes. The “Fingal,” a very small vessel, was not well suited to a trans-Atlantic 
					voyage, and the conditions must have been horrifying. Three slaves died during the Atlantic crossing. </p>
					
					<p>As already noted the treaty of 1817 clearly stipulated that emancipados, once disembarked, came under 
					the jurisdiction of the nation in whose territory the court was located. Dobo and his African companions 
					thus remained under the custody of Spain after registration. They were assigned a new “Christian” name 
					and a tin ticket for identification, and began a five-year labor term under some responsible trustee 
					“to ease their transition to civilization.” But these terms could be legally extended to a maximum of 
					three more years, and beyond that other mechanisms existed to prolong servitude indefinitely. Spanish 
					colonial law also established that emancipados were to be fed, clothed, and given medical attention by 
					their trustees in addition to religious instruction and training in some trade.(10) However, as Dobo’s story 
					shows, many, if not most emancipados fell victim to corruption and fraud. The most common practice was to 
					keep them in servitude and pay only nominal monthly wages after the expiry of their nominal term. </p>
					
					<p>Dobo was consigned for five years to Luisa Aper de la Paz, a rich widow from Havana, who after having 
					paid 612 pesos as a bribe to the authorities, used Dobo, now Gabino, as a water-carrier for a salary of 
					one peso a day.(11) After the end of the five-year term, further bribes extended Dobo’s services for another 
					two consecutive five-year term.(12) For Donna Aper de la Paz these arrangements provided her greater flexibility 
					than absolute ownership of a slave, with its large outlays of cash and the risk of loss in the case of the 
					slave's death or escape to freedom. In Havana free labor was not only very expensive but also very mobile 
					and unreliable. Emancipados like Dobo represented a more controllable and easier to exploit form of labor 
					than free wage workers. They enabled their “trustees” to obtain labor at below-market rates and avoid the 
					long-term commitments, preoccupations, and maintenance costs associated with slavery. </p>
					
					<p>As an emancipado Dobo was in a limbo status, neither free nor slave. Dobo’s legal status and his 
					day-to-day existence combined the worst of two worlds. He did not enjoy the freedoms and higher wages 
					of free persons of color nor did he have the few legal and material protections that most slaves could 
					reasonably expect. As if this were not enough, emancipados had to pay taxes to the colonial government. 
					Dobo’s pay of a dollar per day was thus reduced by two-thirds. His situation was further damaged by his 
					isolation as an emancipado from the slave community in Havana, which devised their own solidarity 
					mechanisms for survival, resistance, and the pursuit of freedom. Neither slaves nor free, emancipados 
					were the object of contempt of both free blacks and slaves. In fact, blacks in Cuba used “emancipado” 
					as a derogatory term.(13) People like Dobo were at the bottom of the Cuban’s social latter. They had no 
					protection in the Cuban colonial legal system, and their only hope was getting the attention of a foreign official.</p>
					
					<p>The Superintendent of Liberated Africans in Havana was one such official. The post was created an 
					additional Anglo-Spanish anti-slave trade treaty signed in 1835. The new treaty allowed the adjudication 
					of vessels with slave trading equipment on board but no slaves.(14) It also reflected a renewed interest of 
					the British government in the welfare of the emancipados. It stipulated that emancipados would hereafter 
					come under the authority of the capturing nation rather than under the nation in whose territory the court 
					was located. Spanish officials also were to provide the Havana Mixed Commission with updated registers of 
					emancipados every six months.(15) The new measures had an immediate effect: the British government ceased to 
					hand over emancipados to the Spanish authorities and began relocating them to several of its Caribbean 
					possession, in which coincidentally slavery had just been abolished. A Superintendent was appointed to 
					oversee these arrangements.</p>
					
					<p>From the mid 1830s until 1841 the post was occupied in sequence by two well-known British abolitionists: 
					Dr. Richard Robert Madden and David Turnbull. The two were members of the British and Foreign Anti-Slavery 
					Society and came to Cuba ideologically committed to the interests of liberated Africans. Turnbull replaced 
					Madden 1840 and also held the position of British Consul in Havana. His instructions from the British foreign 
					secretary were to persuade the Cuban authorities to obey the treaties of 1817 and 1835, and to investigate the 
					conditions of the emancipados and protect their rights. However, emancipados like Dobo, landed in Havana before 
					1835, were under the jurisdiction of Spanish law, and subject to Spanish officials. The Superintendent could do 
					no more than investigate and denounce cases that were in clear violation of the earlier treaty. Many emancipados 
					learned of the Superintendent’s position and sought his assistance. One of them was Dobo.</p>
					
					<p>When Mrs. Aper de la Paz’s died in 1840, Dobo’s position deteriorated. Felix Pinero, who inherited the 
					widow’s property, cut off Dobo’s tiny remuneration as a water-carrier, in violations of all the conventions 
					regarding liberated Africans.(16) Dobo’s desperation led him to appeal to Turnbull.  In his declaration to the 
					Superintendent of Liberated Africans, Dobo, who identified himself as Gabino, narrated the abuse to which he 
					had been subjected since his arrival to the Havana’s harbor. Turnbull immediately sent a strong note of protest 
					to the Cuban government claiming for him 'the immediate and unconditional enjoyment of the freedom which was 
					guaranteed to him by the treaties in force between Spain and Great Britain.' By this time Dobo had been in 
					Cuba for fifteen years, and understood that the Spanish legal system was closed to him. He searched for an 
					alternative and apparently found one. </p>
					
					<p>In his note to the Captain General, Turnbull also claimed the full repayment of the money earned by Dobo 
					during the years he was unlawfully held. The petition was not well received by Captain General of Cuba, 
					Principe de Anglona, who sent a letter to Turnbull in which he was declared as 'persona non grata' for 
					being a danger to the island's security because of his links with 'the people of color'. He was also 
					told in this letter that his intervention in Dobo’s case ‘supposes that you are qualified to listen to 
					complaints and to offer protection to the people of color, and to support their pretensions.’ The Captain 
					General feared that ‘such a state of things might loosen the ties of subordination and obedience among 
					emancipados.(17) The Captain General thus opposed Turnbull’s defense of emancipados rights, without addressing 
					the specifics of Dobo’s case.(18) Turnbull had in fact previously traveled through the island to check on the 
					conditions of the emancipados working on plantations, and had listened to the complaints of many other 
					emancipados in Havana. He was also seen accompanied by white Cubans who openly opposed slavery in the island. 
					The Cuban colonial government wanted Turnbull out of the island, and his official complain in Dobo’s case gave 
					them the opportunity. He had to abandon his offices at the British Consulate in Havana and move to a British 
					vessel anchored in the harbor, as he was not allowed to remain on Spanish soil. Eventually, he had to leave 
					the island for Jamaica.(19) </p>
					
					<p>But while Turnbull had to leave Cuba, his immediate objective was achieved: Dobo, at the age of 25, 
					won his freedom. A letter from the Anglona’s successor as Cuban Captain General, Geronimo Valdes, to the 
					British Consulate, dated June 23, 1841, stated that a letter of emancipation had been issued for Gabino 
					who was now a free man. The report also mentioned that Dobo had married a slave, Candelaria.(20) Dobo had 
					achieved the nearly impossible; he had overcome all the barriers imposed on emancipados by the Spanish 
					colonial government. Dobo’s decision to by-pass the Spanish legal system also produced a diplomatic crisis 
					between two European governments. </p>
					
					<p>Dobo now began a new life as a free black man with a wife. However, the colonial government did not forget Dobo. 
					A few months after his letter of emancipation was issued, he was accused of conspiracy to foment a black uprising, 
					and condemned to eight years in prison. The sentence would be served in Ceuta, a Spanish enclave north of Morocco, 
					far from his wife. Paradoxically, Dobo now returned to the African continent as a captive. His freedom had not 
					lasted long. In November 1841 he left Havana on board a Spanish naval war, “Correo 4”. It was the second time 
					that Dobo crossed the Atlantic, in both cases under conditions as a captive. The conditions on board the “Correo 4” 
					were likely as bad as those on a slave vessel. When the vessel arrived to Cadiz Dobo was gravely ill. A few days 
					after disembarkation in Cadiz Dobo died in the military prison.(21) He never reached the African continent. Dobo’s 
					story is an example of the fate of thousands of liberated Africans in Cuba. Dobo did not arrive in Cuba as a slave 
					but like thousands of other emancipados, he became like one of them. </p>
					
					<br>
					
					<div class="essay-note">						
						<p style="text-align:center">Notes</p>																		
					</div>
					
					<p class="note"> 1 Leslie Bethell, "The Mixed Commissions for the Suppression of the Transatlantic Slave Trade in 
					the Nineteenth Century." <font style="font-style: italic;">Journal of African History 7 </font>(1966): 79-93; David Murray, 
					<font style="text-decoration:underline">Odious Commerce:</font> (Cambridge, 1972).</p>
					
					<p class="note">2 AHN, Estado, Esclavitud, Legajo: 8034/21.</p>
					
					<p class="note">3 The term seems to come from the Gbangá River in Sierra Leone or a toponimic that refers to the 
					zone of Gbangbama, where that river come into the sea. See Basso Ortiz, A. (2001). "Los Gangá Longobá: el Nacimiento 
					de los Dioses." <font style="text-decoration:underline">Boletín Antropológico. Universidad de Los Andes. Mérida. </font>II(52): 195-208.	 </p>
					
					<p class="note">4 On Gola see: de Azevedo, W. L. (1969). "A Tribal Reaction to Nationalism (Part I)." 
					<font style="text-decoration:underline">Liberian Studies Journal </font>I(2): 1-21; Holsoe, S. E. (1971). 
					"A Study of Relations between Settlers and Indigenous People in Western Liberia, 1821-1847." 
					<font style="text-decoration:underline">African Historical Studies</font> 4(2): 331-62.</p>
					
					<p class="note">5 Calvo L. Novás, <font style="text-decoration:underline">Pedro Blanco, el Negrero </font>(Barcelona, 1999); Riddell, W. R. (1930). 
					"Observation on Slavery and Privateering." <font style="text-decoration:underline">The Journal of Negro History</font> 15(3): 337-71.</p>
					
					<p class="note">6 See: Holsoe, S. E. (1969). "Chiefdoms and Clan Maps of Western Liberia." 
					<font style="text-decoration:underline">Liberian Studies Journal </font>1 (1969): 23-39.	</p>
					
					<p class="note">7 On Vai see: Holsoe, S. E. (1967). The cassava-leaf people: An ethno-historical study of the 
					Vai people with particular emphasis on the Tewo chiefdom. Ph.D. dissertation, Boston University.	</p>
					
					<p class="note">8 Sierra Leone and Liberia see: Holsoe, S. E. (1977). Slavery and Economic Response among the 
					Vai (Liberia and Sierra Leone).<font style="text-decoration:underline"> Slavery in Africa: Historical and Anthropological Perspectives</font>. 
					S. Miers and I. Kopytoff. Madison, 287-303.	</p>
					
					<p class="note">9 For slavery in Africa see: Kopytoff, I. and S. Miers, "African ‘Slavery’ as an Institution of Marginality," 
					Kopytoff and and Miers, <font style="text-decoration:underline">Slavery in Africa</font>, 3-78.	</p>
					
					<p class="note">10 See: de la Concha, J. (1861). <font style="text-decoration:underline">Memoria sobre el ramo de emancipados de la 
					Isla de Cuba</font>. Madrid, Imprenta de la América.	</p>
					
					<p class="note">11 Some reports have mentioned that Spanish officials were selling emancipados in Havana 
					during the 1830s at nine gold ounces, or about one-third of the cost of a slave. Archivo Nacional de Cuba 
					(thereafter ANC), Gobierno Superior Civil, Legajo 105/5363; ANC, Reales Órdenes y Cédulas, Legajo 100/14. </p>
					
					<p class="note">12 AHN, Estado, Esclavitud, Legajo: 8019/39</p>
					
					<p class="note">13 Roldán de Montaud, I. (1982). "Origen, evolución y supresión del grupo de negros 'emancipados' 
					en Cuba (1817-1870)." <font style="text-decoration:underline">Revista de Indias </font>42(July-December): 580 </p>
					
					<p class="note">14 BFSP, vol. XXIII, The treaty of 1835, 343-71; AHN, Ultramar, Legajo 3547/6.	</p>
					
					<p class="note">15 ANC, Reales Órdenes y Cédulas, Legajo 51/123.</p>
					
					<p class="note">16 This paragraph from AHN, Estado, Esclavitud, Legajo: 8019/39/4</p>
					
					<p class="note">17 Ibid. AHN, Estado, Esclavitud, Legajo: 8019/39/1-7</p>
					
					<p class="note">18 Before his apointment as Bristish Consul in Havana Turnbull spent almost two years travelling 
					in Cuba and writing his best known book, <font style="font-style: italic;">Travels in the West: Cuba; with Notices 
					of Porto Rico and the Slave Trade</font> (London, 1840).	</p>
					
					<p class="note">19 Turnbull would return to Cuba in 1842 from Bahamas and accompanied by several 
					free blacks, hoping to free some emancipados who were held as slaves. For his arrest and deportation see Thomas, 
					<font style="font-style: italic;">The Slave Trade,</font> 668</p>
					
					<p class="note">20 AHN, Estado, Esclavitud, Legajo: 8019/39/10</p>
					
					<p class="note">21 AHN, Estado, Esclavitud, Legajo: 8019/39/15-18</p>				
					
				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>