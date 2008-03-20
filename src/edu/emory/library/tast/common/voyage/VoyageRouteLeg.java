package edu.emory.library.tast.common.voyage;

import java.util.LinkedList;
import java.util.List;

public class VoyageRouteLeg
{
	
	private List places;
	
	public VoyageRouteLeg()
	{
	}

	public void addPlace(VoyageRoutePlace place)
	{
		getPlaces().add(place);
	}

	public List getPlaces()
	{
		if (places == null) places = new LinkedList();
		return places;
	}
	
}
