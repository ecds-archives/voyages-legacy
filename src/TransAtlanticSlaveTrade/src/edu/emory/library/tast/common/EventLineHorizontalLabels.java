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
package edu.emory.library.tast.common;

public class EventLineHorizontalLabels
{
	
	private String[] labels;
	private int space = 1;
	private int start = 0;
	
	public int getCount()
	{
		if (labels == null) return 0;
		return labels.length;
	}
	
	public String getLabel(int i)
	{
		if (labels == null || i < 0 || i >= labels.length) return "";
		return labels[i];
	}

	public String[] getLabels()
	{
		return labels;
	}

	public void setLabels(String[] labels)
	{
		this.labels = labels;
	}

	public int getSpace()
	{
		return space;
	}

	public void setSpace(int space)
	{
		this.space = space;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

}
