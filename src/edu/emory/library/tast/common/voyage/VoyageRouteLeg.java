package edu.emory.library.tast.common.voyage;

import java.util.ArrayList;

public class VoyageRouteLeg
{
	
	private ArrayList places = new ArrayList();
	
	public VoyageRouteLeg()
	{
	}

	public void addPlace(VoyageRoutePlace place)
	{
		places.add(place);
	}

	public ArrayList getPlaces()
	{
		return places;
	}
	
}
