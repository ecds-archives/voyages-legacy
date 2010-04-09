/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
