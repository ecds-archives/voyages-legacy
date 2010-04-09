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

public class MapSize implements Serializable
{
	
	private static final long serialVersionUID = 7831450118155095870L;
	
	private int width;
	private int height;
	
	public MapSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getArea()
	{
		return width * height;
	}
	
	/**
	 * This method ingnores all non-digit characters in the parsed string and
	 * the subsequent pairs number interprets as dimensions. So, for instance,
	 * the following is valid string:
	 * 
	 * "100x100 200 X 200, 600 800"
	 * 
	 * @param sizesStr
	 * @return
	 */
	public static MapSize[] parseList(String sizesStr)
	{
		
		sizesStr = sizesStr.replaceAll("^[^\\d]+", "");
		sizesStr = sizesStr.replaceAll("[^\\d]+$", "");
		String[] numbers = sizesStr.split("[^\\d]+");
		
		int n = numbers.length/2;
		MapSize[] sizes = new MapSize[n];
		
		for (int i = 0; i < n; i++)
		{
			int w = Integer.parseInt(numbers[2*i+0]);
			int h = Integer.parseInt(numbers[2*i+1]);
			sizes[i] = new MapSize(w, h);
		}
		
		return sizes;
		
	}
	
	public static MapSize parse(String sizeStr, boolean strict)
	{
		MapSize[] sizes = MapSize.parseList(sizeStr);
		if (strict)
		{
			if (sizes == null || sizes.length != 1)
				throw new RuntimeException("incorrect mapsize");
		}
		else
		{
			if (sizes == null || sizes.length == 0)
				return null;
		}
		return sizes[0];
	}
	
	public String toString()
	{
		return width + " " + height;
	}
	
	protected Object clone()
	{
		return new MapSize(width, height);
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof MapSize)) return false;
		MapSize theOtherMapSize = (MapSize) obj;
		return
			this.width == theOtherMapSize.getWidth() &&
			this.height == theOtherMapSize.getHeight();
	}
	
	public int compareTo(MapSize theOtherMapSize)
	{
		int a1 = this.getArea();
		int a2 = theOtherMapSize.getArea();
		return (a1 < a2) ? 1 : ((a1 > a2) ? -1 : 0); 
	}
	
	public static void main(String[] args)
	{
		
		MapSize[] sizes = MapSize.parseList("100x100, 200x200");
		
		for (int i = 0; i < sizes.length; i++)
			System.out.println(sizes[i].getWidth() + " x " + sizes[i].getHeight());
		
	}

}
