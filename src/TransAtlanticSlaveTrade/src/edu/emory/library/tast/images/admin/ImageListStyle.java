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
package edu.emory.library.tast.images.admin;

import java.io.Serializable;


public class ImageListStyle implements Serializable
{
	
	private static final long serialVersionUID = 6788716676709166618L;
	
	private static final int TABLE = 1;
	private static final int LIST = 2;
	private static final int GALLERY = 3;
	
	public static ImageListStyle Table = new ImageListStyle(TABLE); 
	public static ImageListStyle List = new ImageListStyle(LIST); 
	public static ImageListStyle Gallery = new ImageListStyle(GALLERY); 
	
	private int style;
	
	private ImageListStyle(int style)
	{
		this.style = style;
	}
	
	public boolean isList()
	{
		return style == TABLE; 
	}

	public boolean Thumbnails()
	{
		return style == GALLERY; 
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ImageListStyle)
		{
			return ((ImageListStyle)obj).style == style;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		switch (style)
		{
		case TABLE: return "table"; 
		case LIST: return "list"; 
		case GALLERY: return "gallery";
		default: return ""; 
		}
	}
	
	public static ImageListStyle parse(String str)
	{
		if ("table".equalsIgnoreCase(str))
			return Table; 
		else if ("list".equalsIgnoreCase(str))
			return List; 
		else
			return Gallery; 
	}

}