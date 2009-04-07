package edu.emory.library.tast.common.voyage;

import java.util.LinkedList;
import java.util.List;

public class VoyageRouteLeg
{
	
	private List places;
	private List infoLines;
	
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
	
	public void addInfoLine(String name, String value)
	{
		getInfoLines().add(
				new VoyageRouteInfoLine(name, value));
	}
	
	public List getInfoLines()
	{
		if (infoLines == null) infoLines = new LinkedList();
		return infoLines;
	}
	
	public void setInfoLines(List infoLines)
	{
		this.infoLines = infoLines;
	}
	
}
