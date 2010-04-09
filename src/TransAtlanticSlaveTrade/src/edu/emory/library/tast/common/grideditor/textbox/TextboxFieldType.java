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
package edu.emory.library.tast.common.grideditor.textbox;


public class TextboxFieldType extends AbstractTextFieldType
{
	
	private int maxLength = Integer.MAX_VALUE;

	public TextboxFieldType(String name)
	{
		super(name);
	}

	public String getType()
	{
		return TextboxAdapter.TYPE;
	}

	public TextboxFieldType(String name, String cssClass)
	{
		super(name, cssClass);
	}

	public TextboxFieldType(String name, String cssClass, String cssStyle)
	{
		super(name, cssClass, cssStyle);
	}

	public TextboxFieldType(String name, int maxLength)
	{
		super(name);
		this.maxLength = maxLength;
	}

	public TextboxFieldType(String name, String cssClass, int maxLength)
	{
		super(name, cssClass);
		this.maxLength = maxLength;
	}

	public TextboxFieldType(String name, String cssClass, String cssStyle, int maxLength)
	{
		super(name, cssClass, cssStyle);
		this.maxLength = maxLength;
	}

	public int getMaxLength()
	{
		return maxLength;
	}

	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
	}

}
