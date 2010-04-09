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

public class GlossaryLettersTag extends UIComponentTag
{
	
	private String letters;
	private String glossaryListId;

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
		
		GlossaryLettersComponent glossaryLetters = (GlossaryLettersComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (letters != null && isValueReference(letters))
		{
			ValueBinding vb = app.createValueBinding(letters);
			glossaryLetters.setValueBinding("letters", vb);
		}
		
		if (glossaryListId != null && isValueReference(glossaryListId))
		{
			ValueBinding vb = app.createValueBinding(glossaryListId);
			glossaryLetters.setValueBinding("glossaryListId", vb);
		}
		else
		{
			glossaryLetters.setGlossaryListId(glossaryListId);
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

	public String getGlossaryListId()
	{
		return glossaryListId;
	}

	public void setGlossaryListId(String glossaryListId)
	{
		this.glossaryListId = glossaryListId;
	}

}

