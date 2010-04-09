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

import java.io.Serializable;

public class SimpleListStyle implements Serializable
{

	private static final long serialVersionUID = 2445965207252391047L;
	
	private static final int STYLE_PLAIN = 1;
	private static final int STYLE_UL = 2;
	private static final int STYLE_OL = 3;
	
	public static final SimpleListStyle Plain = new SimpleListStyle(STYLE_PLAIN); 
	public static final SimpleListStyle UnorderedList = new SimpleListStyle(STYLE_UL); 
	public static final SimpleListStyle OrderedList = new SimpleListStyle(STYLE_OL); 
	
	private int mode = 0;

	private SimpleListStyle(int mode)
	{
		this.mode = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof SimpleListStyle)
		{
			return ((SimpleListStyle)obj).mode == mode;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		switch (mode)
		{
			case STYLE_UL: return "ul";
			case STYLE_OL: return "ol";
			default: return "plain";
		}
	}
	
	public static SimpleListStyle parse(String value)
	{
		if (value != null && value.equals("ul"))
			return UnorderedList;
		else if (value != null && value.equals("ol"))
			return OrderedList;
		else
			return Plain;
	}
	
}
