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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ZoomHistory implements Serializable
{
	
	private static final long serialVersionUID = 5731386622192110293L;
	
	private List items = new ArrayList();
	private int position = -1;
	
	public void addItem(ZoomHistoryItem item)
	{
		items.add(item);
	}
	
	public void addItem(double x1, double y1, double x2, double y2)
	{
		items.add(new ZoomHistoryItem(x1, y1, x2, y2));
	}

	public List getItems()
	{
		return items;
	}

	public void setItems(List items)
	{
		if (items == null) return;
		this.items = items;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public boolean canGoBack()
	{
		return position > 0;
	}
	
	public boolean canGoForward()
	{
		return position+1 < items.size();
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append(position);
		for (Iterator iter = items.iterator(); iter.hasNext();)
		{
			ZoomHistoryItem item = (ZoomHistoryItem) iter.next();
			if (str.length() > 0) str.append(" ");
			str.append(item.getX1()).append(" ");
			str.append(item.getY1()).append(" ");
			str.append(item.getX2()).append(" ");
			str.append(item.getY2());
		}
		return str.toString();
	}

	public static ZoomHistory parse(String str)
	{
		if (str == null)
			throw new RuntimeException("null zoom history");
		
		String[] values = str.trim().split("\\s+");
		
		if (values == null || values.length % 4 != 1)
			throw new RuntimeException("invalid number of arguments in zoom history");
		
		ZoomHistory history = new ZoomHistory();
		try
		{
			
			history.setPosition(Integer.parseInt(values[0]));
			
			int n = (values.length - 1) / 4;
			for (int i = 0; i < n; i++)
			{
				ZoomHistoryItem item = new ZoomHistoryItem(
						Double.parseDouble(values[4 * i + 1]),
						Double.parseDouble(values[4 * i + 2]),
						Double.parseDouble(values[4 * i + 3]),
						Double.parseDouble(values[4 * i + 4]));
				history.addItem(item);
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new RuntimeException("invalid zoom history " + str);
		}
		
		return history;
		
	}

}