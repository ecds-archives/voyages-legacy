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

public class SimpleTableLabel
{
	
	private String label;
	private int span = 1;
	
	public SimpleTableLabel(String label, int span)
	{
		this.label = label;
		this.span = span;
	}

	public SimpleTableLabel(String label)
	{
		this.label = label;
		this.span = 1;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public int getSpan()
	{
		return span;
	}
	
	public void setSpan(int span)
	{
		this.span = span;
	}
	
	public static SimpleTableLabel[] createFromStringArray(String[] textLabels)
	{
		return createFromStringArray(textLabels, 1);
	}

	public static SimpleTableLabel[] createFromStringArray(String[] textLabels, int span)
	{
		SimpleTableLabel[] labels =
			new SimpleTableLabel[textLabels.length];
		
		for (int i = 0; i < textLabels.length; i++)
			labels[i] = new SimpleTableLabel(textLabels[i], span);
		
		return labels;
	}

}
