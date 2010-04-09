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


public class VoyageRoutePlace
{
	
	private long id;
	private String name;
	private double longitude;
	private double latitude;
	private VoyageRouteSymbol symbol;
	private String purpose;
	private List infoLines;
	
	public VoyageRoutePlace(long id, String name, double longitude, double latitude, VoyageRouteSymbol symbol)
	{
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.symbol = symbol;
	}
	
	public VoyageRoutePlace(long id, String name, String purpose, double longitude, double latitude, VoyageRouteSymbol symbol)
	{
		this.id = id;
		this.name = name;
		this.purpose = purpose;
		this.longitude = longitude;
		this.latitude = latitude;
		this.symbol = symbol;
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
	
	public String getPurpose()
	{
		return purpose;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public VoyageRouteSymbol getSymbol()
	{
		return symbol;
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

	public void setPurpose(String purpose)
	{
		this.purpose = purpose;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}