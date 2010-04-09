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
package edu.emory.library.tast.dm;


public abstract class Location extends DictionaryOrdered
{
	
	private double latitude;
	private double longitude;
	private int showAtZoom;

	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public double getX()
	{
		return latitude;
	}
	
	public void setX(double x)
	{
		this.latitude = x;
	}
	
	public double getY()
	{
		return longitude;
	}
	
	public void setY(double y)
	{
		this.longitude = y;
	}

	public int getShowAtZoom()
	{
		return showAtZoom;
	}

	public void setShowAtZoom(int showAtZoom)
	{
		this.showAtZoom = showAtZoom;
	}

}
