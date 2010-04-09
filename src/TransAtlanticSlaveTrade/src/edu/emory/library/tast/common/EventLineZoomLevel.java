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

public class EventLineZoomLevel
{
	
	private int barWidth;
	private int labelSpacing;
	private int majorLabels;
	private int viewSpan;
	
	public EventLineZoomLevel(int barWidth, int labelsSpacing, int viewSpan)
	{
		this.barWidth = barWidth;
		this.labelSpacing = labelsSpacing;
		this.viewSpan = viewSpan;
		this.majorLabels = labelsSpacing;
	}

	public EventLineZoomLevel(int barWidth, int labelsSpacing, int viewSpan, int majorSpaces)
	{
		this.barWidth = barWidth;
		this.labelSpacing = labelsSpacing;
		this.viewSpan = viewSpan;
		this.majorLabels = majorSpaces;
	}

	public int getBarWidth()
	{
		return barWidth;
	}
	
	public void setBarWidth(int barWidth)
	{
		this.barWidth = barWidth;
	}
	
	public int getLabelSpacing()
	{
		return labelSpacing;
	}
	
	public void setLabelSpacing(int labelsSpacing)
	{
		this.labelSpacing = labelsSpacing;
	}
	
	public int getViewSpan()
	{
		return viewSpan;
	}
	
	public void setViewSpan(int viewSpan)
	{
		this.viewSpan = viewSpan;
	}

	public int getMajorLabels()
	{
		return majorLabels;
	}

	public void setMajorLabels(int majorSpaces)
	{
		this.majorLabels = majorSpaces;
	}

}
