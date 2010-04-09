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

import edu.emory.library.tast.util.StringUtils;

public class GridColumn
{
	
	private String name;
	private String header;
	private String cssClass;
	private boolean clickable = false;
	
	public GridColumn(String header)
	{
		this.header = header;
	}
	
	public GridColumn(String name, String header)
	{
		this.name = name;
		this.header = header;
	}

	public GridColumn(String name, String header, String cssClass)
	{
		this.name = name;
		this.header = header;
		this.cssClass = cssClass;
	}

	public GridColumn(String name, String header, boolean clickable)
	{
		this.name = name;
		this.header = header;
		this.clickable = clickable;
	}

	public GridColumn(String name, String header, boolean clickable, String cssClass)
	{
		this.name = name;
		this.header = header;
		this.cssClass = cssClass;
		this.clickable = clickable;
	}

	public String getCssClass()
	{
		return cssClass;
	}
	
	public boolean hasCssClass()
	{
		return !StringUtils.isNullOrEmpty(this.cssClass);
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public void setHeader(String header)
	{
		this.header = header;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isClickable()
	{
		return clickable;
	}

	public void setClickable(boolean clickable)
	{
		this.clickable = clickable;
	}

}