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

public class SelectItem implements Serializable
{
	
	private static final long serialVersionUID = -5198351959311658847L;

	private String value;
	private String text;
	private int orderNumber = -1;
	private SelectItem[] subItems = null;
	private boolean selectable = true;
	
	public SelectItem()
	{
	}
	
	public SelectItem(String text, String value)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = -1;
	}

	public SelectItem(String text, String value, int orderNumber)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = orderNumber;
	}
	
	public SelectItem(String text, String value, int orderNumber, SelectItem[] subItems)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = orderNumber;
		this.subItems = subItems;
	}
	
	public boolean hasSubItems()
	{
		return subItems != null && subItems.length != 0;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

	public int getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public SelectItem[] getSubItems()
	{
		return subItems;
	}

	public void setSubItems(SelectItem[] subItems)
	{
		this.subItems = subItems;
	}

	public String toString()
	{
		return value + ": " + text;
	}

	public boolean isSelectable()
	{
		return selectable;
	}

	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}

}