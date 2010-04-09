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


public class TextareaFieldType extends AbstractTextFieldType
{
	
	public static final int ROWS_DEFAULT = -1;
	private int rows = ROWS_DEFAULT;

	public TextareaFieldType(String name, int rows)
	{
		super(name);
		this.rows = rows;
	}

	public TextareaFieldType(String name, String cssClass, int rows)
	{
		super(name, cssClass);
		this.rows = rows;
	}

	public TextareaFieldType(String name, String cssClass, String cssStyle, int rows)
	{
		super(name, cssClass, cssStyle);
		this.rows = rows;
	}

	public TextareaFieldType(String name)
	{
		super(name);
	}

	public TextareaFieldType(String name, String cssClass)
	{
		super(name, cssClass);
	}

	public TextareaFieldType(String name, String cssClass, String cssStyle)
	{
		super(name, cssClass, cssStyle);
	}

	public String getType()
	{
		return TextareaAdapter.TYPE;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

}
