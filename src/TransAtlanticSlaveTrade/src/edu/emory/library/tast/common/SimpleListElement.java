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


public class SimpleListElement
{
	
	private String text;
	private String cssClass;
	private String cssStyle;
	private SimpleListElement[] subElements;
	
	public SimpleListElement(String text)
	{
		this.text = text;
	}

	public SimpleListElement(String text, String cssClass)
	{
		this.text = text;
		this.cssClass = cssClass;
	}

	public SimpleListElement(String text, String cssClass, String cssStyle)
	{
		this.text = text;
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
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
	
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public String getCssStyle()
	{
		return cssStyle;
	}

	public void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

	public SimpleListElement[] getSubElements()
	{
		return subElements;
	}

	public void setSubElements(SimpleListElement[] subElements)
	{
		this.subElements = subElements;
	}

	public int getSubElementsLength()
	{
		if (subElements == null) return 0;
		return subElements.length;
	}

	public boolean hasSubelements()
	{
		return getSubElementsLength() != 0;
	}

}
