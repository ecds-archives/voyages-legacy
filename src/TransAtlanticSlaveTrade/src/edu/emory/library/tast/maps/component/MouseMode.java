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
package edu.emory.library.tast.maps.component;

import java.io.Serializable;

public class MouseMode implements Serializable
{

	private static final long serialVersionUID = 2445965207252391047L;
	
	private static final int MODE_PAN = 1;
	private static final int MODE_ZOOM = 2;
	
	public static final MouseMode Zoom = new MouseMode(MODE_ZOOM); 
	public static final MouseMode Pan = new MouseMode(MODE_PAN); 
	
	private int mode = 0;

	private MouseMode(int mode)
	{
		this.mode = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof MouseMode)
		{
			return ((MouseMode)obj).mode == mode;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isZoom()
	{
		return mode == MODE_ZOOM;
	}
	
	public boolean isPan()
	{
		return mode == MODE_PAN;
	}
	
	public String getStyleCursor()
	{
		switch (mode)
		{
			case MODE_PAN: return "move";
			case MODE_ZOOM: return "crosshair";
			default: return "default";
		}
	}

	public String toString()
	{
		switch (mode)
		{
			case MODE_PAN: return "pan";
			case MODE_ZOOM: return "zoom";
			default: return "";
		}
	}
	
	public static MouseMode parse(String value)
	{
		if (value == null || value.equals("pan"))
			return Pan;
		else
			return Zoom;
	}
	
}