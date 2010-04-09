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
package edu.emory.library.tast.glossary;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GlossaryLettersTagV1 extends UIComponentTag
{
	
	private String letters;
	private String columns;

	public String getComponentType()
	{
		return "GlossaryLetters";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		GlossaryLettersComponentV1 glossaryLetters = (GlossaryLettersComponentV1) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (letters != null && isValueReference(letters))
		{
			ValueBinding vb = app.createValueBinding(letters);
			glossaryLetters.setValueBinding("letters", vb);
		}
		
		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			glossaryLetters.setValueBinding("columns", vb);
		}
		else
		{
		}
		
	}

	public String getLetters()
	{
		return letters;
	}

	public void setLetters(String letters)
	{
		this.letters = letters;
	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columns)
	{
		this.columns = columns;
	}

}

