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
		return "./images/welcome-map-01.png"; 
	}

	public WelcomeMapPlace[] getWelcomeMapPlaces()
	{
		
		return new WelcomeMapPlace[] {

				new WelcomeMapPlace(35, 69, 60, 40,
						"./images/welcome-map-01-place-01.png",
						"./images/welcome-map-01-place-01-highlighted.png",
						"The Caribbean is a region of the Americas consisting of the Caribbean Sea, " +
						"its islands, and the surrounding coasts."),
						
				new WelcomeMapPlace(191, 21, 50, 50,
						"./images/welcome-map-01-place-02.png",
						"./images/welcome-map-01-place-02-highlighted.png",
						"Europe is one of the seven traditional continents of the Earth. " +
						"Physically and geologically, Europe is the westernmost peninsula of Eurasia, west of Asia. " +
						"Europe is bound to the north by the Arctic Ocean, to the west by the Atlantic Ocean"),

				new WelcomeMapPlace(95, 117, 50, 50,
						"./images/welcome-map-01-place-03.png",
						"./images/welcome-map-01-place-03-highlighted.png",
						"Brazil was colonized by Portugal and it is the only Portuguese-speaking country in the Americas. " +
						"It is a multiracial country with a population composed of European, Amerindian, African and Asian elements."),

				new WelcomeMapPlace(199, 126, 60, 40,
						"./images/welcome-map-01-place-04.png",
						"./images/welcome-map-01-place-04-highlighted.png",
						"In West Africa, the decline of the Atlantic slave trade in the 1820s caused dramatic economic shifts " +
						"in local polities. The gradual decline of slave-trading, prompted by a lack of demand for slaves in the New World.")

		};
	}

}