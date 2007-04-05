package edu.emory.library.tast.homepage;

public class HomePageBean
{
	
	public int getWelcomeMapHeight()
	{
		return 240;
	}
	
	public int getWelcomeMapWidth()
	{
		return 320;
	}
	
	public String getWelcomeMapImage()
	{
		return "./images/welcome-maps/02/map.png"; 
	}

	public String getWelcomeMapInitialText()
	{
		return
			"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
			"<div class=\"welcome-main-text\">" +
			"When the trans-Atlantic slave trade ended in 1867, over 12 million Africans had been forcibly relocated. " +
			"Though little trace is left of the individual histories of these displaced peoples, thousands of records " +
			"remain of the voyages that brought them to the Americas. This database, with information on over 35,000 " +
			"voyages, offers descendants, researchers, students, and the general public a chance to rediscover the " +
			"reality of one of the largest movements of peoples in world history.</div>"; 
	}

	public WelcomeMapPlace[] getWelcomeMapPlaces()
	{
		/*
		
		return new WelcomeMapPlace[] {

				new WelcomeMapPlace(35, 69, 60, 40,
						"./images/welcome-map-01-place-01.png",
						"./images/welcome-map-01-place-01-highlighted.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Europe</div>" +
						"<div class=\"welcome-place-text\">" +
						"Europe was the starting point for about half all transatlantic slaving voyages. This traffic dominated the West African to Caribbean section of the slave trade. The major ports were at first located in the Iberian peninsular but by the eighteenth century northern European ports had become dominant. After 1807, France and the Iberian ports sent out the great majority of European-based slaving voyages. The European consumers’ demand for sugar was the driving force behind 350 years of transatlantic slave trading." +
						"</div>"),
						
				new WelcomeMapPlace(191, 21, 50, 50,
						"./images/welcome-map-01-place-02.png",
						"./images/welcome-map-01-place-02-highlighted.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">North American</div>" +
						"<div class=\"welcome-place-text\">" +
						"The North American mainland played a relatively minor role in the transatlantic slave. Its ports sent out less than five percent of all known voyages, and its slave markets absorbed less than four percent of all slaves carried off from Africa. An intra-American trade in slaves – originating in the Caribbean - supplied additional slaves however. This region was exceptional in the Americas in that a positive rate of natural population growth began relatively early, thus reducing the dependence of the region on coerced migrants." +
						"</div>"),

				new WelcomeMapPlace(95, 117, 50, 50,
						"./images/welcome-map-01-place-03.png",
						"./images/welcome-map-01-place-03-highlighted.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Caribbean </div>" +
						"<div class=\"welcome-place-text\">" +
						"The Caribbean was one of the two major broad regional markets for slaves from Africa. Over the two centuries when the trade was at its height, the major locations for sugar production, and therefore the major slave markets, shifted from the eastern Caribbean to the west. Here, first Jamaica, then St Domingue and finally in the 19th century, Cuba absorbed most of the slaves brought into the region. As this implies, few islands developed self-sustaining populations at any point in the slave trade era. Caribbean ports also sent out more slaving expeditions to Africa than did the North American mainland ports." +
						"</div>"),

				new WelcomeMapPlace(199, 126, 60, 40,
						"./images/welcome-map-01-place-04.png",
						"./images/welcome-map-01-place-04-highlighted.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Brazil</div>" +
						"<div class=\"welcome-place-text\">" +
						"Brazil was the center of the slave trade carried on under the Portuguese flag, both before and after Brazilian independence in 1822 - and Portugal was by far the largest of the national carriers. Brazil dominated the slave trade in the sense that Rio de Janeiro and Bahia sent out more slaving voyages than any port in Europe, and certainly many times more than did Lisbon. Over nearly three centuries between 1560 and 1852, Brazil was consistently the largest destination for slaves in the Americas. Almost all the slaves coming in to the region came from just two coastal areas in Africa, the Bight of Benin and West-central Africa." +
						"</div>")

		};
		
		*/

		return new WelcomeMapPlace[] {

				new WelcomeMapPlace(0, 0, 140, 105,
						"./images/welcome-maps/01/north-america.png",
						"./images/welcome-maps/01/north-america.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">North American</div>" +
						"<div class=\"welcome-place-text\">" +
						"The North American mainland played a relatively minor role in the transatlantic " +
						"slave. Its ports sent out less than five percent of all known voyages, and its " +
						"slave markets absorbed less than four percent of all slaves carried off from Africa. " +
						"An intra-American trade in slaves – originating in the Caribbean - supplied additional " +
						"slaves however. This region was exceptional in the Americas in that a positive rate " +
						"of natural population growth began relatively early, thus reducing the dependence " +
						"of the region on coerced migrants." +
						"</div>"),
						
				new WelcomeMapPlace(140, 0, 180, 85,
						"./images/welcome-maps/02/europe.png",
						"./images/welcome-maps/02/europe.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Europe</div>" +
						"<div class=\"welcome-place-text\">" +
						"Europe was the starting point for about half all transatlantic slaving voyages. " +
						"This traffic dominated the West African to Caribbean section of the slave trade. " +
						"The major ports were at first located in the Iberian peninsular but by the eighteenth " +
						"century northern European ports had become dominant. After 1807, France and the " +
						"Iberian ports sent out the great majority of European-based slaving voyages. " +
						"The European consumers’ demand for sugar was the driving force behind 350 years " +
						"of transatlantic slave trading." +
						"</div>"),

				new WelcomeMapPlace(200, 85, 120, 155,
						"./images/welcome-maps/02/africa.png",
						"./images/welcome-maps/02/africa.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Africa</div>" +
						"<div class=\"welcome-place-text\">" +
						"Sub-Saharan Africa lost over twelve and a half million people to the " +
						"transatlantic slave trade alone between 1526 and 1867. Perhaps as many " +
						"again were carried of to slave markets across the Sahara and the Indian Ocean. " +
						"Over forty percent of captives left from West-central Africa alone with most of " +
						"the remainder leaving from the Bight of Benin, the Bight of Biafra and the Gold " +
						"Coast. About one in eight died on board the slave vessel and many others died " +
						"prior to departure and after arrival. Departures were channeled through a dozen " +
						"or so major embarkation points such as Whydah, Bonny, Loango, Luanda and Benguela, " +
						"though many smaller ports also supplied slaves." +
						"</div>"),

				new WelcomeMapPlace(0, 105, 150, 40,
						"./images/welcome-maps/02/caribbean.png",
						"./images/welcome-maps/02/caribbean.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Caribbean</div>" +
						"<div class=\"welcome-place-text\">" +
						"The Caribbean was one of the two major broad regional markets for slaves from " +
						"Africa. Over the two centuries when the trade was at its height, the major " +
						"locations for sugar production, and therefore the major slave markets, shifted " +
						"from the eastern Caribbean to the west. Here, first Jamaica, then St Domingue " +
						"and finally in the 19th century, Cuba absorbed most of the slaves brought into " +
						"the region. As this implies, few islands developed self-sustaining populations " +
						"at any point in the slave trade era. Caribbean ports also sent out more slaving " +
						"expeditions to Africa than did the North American mainland ports." +
						"</div>"),

				new WelcomeMapPlace(0, 160, 160, 80,
						"./images/welcome-maps/02/brazil.png",
						"./images/welcome-maps/02/brazil.png",
						"<div><img src=\"./images/index-welcome.png\" width=\"300\" height=\"50\" border=\"0\" alt=\"Welcome\"></div>" +
						"<div class=\"welcome-place-title\">Brazil</div>" +
						"<div class=\"welcome-place-text\">" +
						"Brazil was the center of the slave trade carried on under the Portuguese " +
						"flag, both before and after Brazilian independence in 1822 - and Portugal was by " +
						"far the largest of the national carriers. Brazil dominated the slave trade in the " +
						"sense that Rio de Janeiro and Bahia sent out more slaving voyages than any port in " +
						"Europe, and certainly many times more than did Lisbon. Over nearly three centuries " +
						"between 1560 and 1852, Brazil was consistently the largest destination for slaves " +
						"in the Americas. Almost all the slaves coming in to the region came from just two " +
						"coastal areas in Africa, the Bight of Benin and West-central Africa." +
						"</div>")

		};
	
	}

}