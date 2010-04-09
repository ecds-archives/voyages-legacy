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

import edu.emory.library.tast.common.grideditor.FieldType;

public abstract class AbstractTextFieldType extends FieldType
{
	
	private String cssClass = null;
	private String cssStyle = null;
	protected String inputSize = null;

	public AbstractTextFieldType(String name)
	{
		super(name);
	}

	public AbstractTextFieldType(String name, String cssClass)
	{
		super(name);
		this.cssClass = cssClass;
	}

	public AbstractTextFieldType(String name, String cssClass, String cssStyle)
	{
		super(name);
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	public String getInputSize() {
		return inputSize;
	}

	public void setInputSize(String inputSize) {
		this.inputSize = inputSize;
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

}
