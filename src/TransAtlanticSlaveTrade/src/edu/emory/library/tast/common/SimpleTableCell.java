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

public class SimpleTableCell
{

	private String text;
	private String cssStyle;
	private String cssClass;
	private int rowspan = 1;
	private int colspan = 1;
	
	public SimpleTableCell(String text)
	{
		this.text = text;
	}
	
	public SimpleTableCell(String text, String cssClass)
	{
		this.text = text;
		this.cssClass = cssClass;
	}
	
	public SimpleTableCell(String text, String cssClass, String cssStyle)
	{
		this.text = text;
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	public SimpleTableCell(String text, String cssStyle, String cssClass, int rowspan, int colspan)
	{
		this.text = text;
		this.cssStyle = cssStyle;
		this.cssClass = cssClass;
		this.rowspan = rowspan;
		this.colspan = colspan;
	}

	public SimpleTableCell(String text, int rowspan, int colspan)
	{
		this.text = text;
		this.rowspan = rowspan;
		this.colspan = colspan;
	}

	public boolean hasCssStyle()
	{
		return cssStyle != null && cssStyle.length() > 0;
	}

	public boolean hasCssClass()
	{
		return cssClass != null && cssClass.length() > 0;
	}

	public int getColspan()
	{
		return colspan;
	}
	
	public SimpleTableCell setColspan(int colspan)
	{
		this.colspan = colspan;
		return this;
	}
	
	public String getCssStyle()
	{
		return cssStyle;
	}
	
	public SimpleTableCell setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
		return this;
	}

	public int getRowspan()
	{
		return rowspan;
	}
	
	public SimpleTableCell setRowspan(int rowspan)
	{
		this.rowspan = rowspan;
		return this;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public SimpleTableCell setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
		return this;
	}
	
}
