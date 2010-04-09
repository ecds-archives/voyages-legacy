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
package edu.emory.library.tast.reditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

public abstract class FieldSchema
{
	
	private String name;
	private String description;
	private String label;
	
	public abstract void encode(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException;
	public abstract void createRegJS(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, StringBuffer regJS) throws IOException;
	public abstract String getType();
	
	public FieldSchema(String name, String label)
	{
		this.name = name;
		this.label = label;
	}

	public FieldSchema(String name, String label, String description)
	{
		this.name = name;
		this.label = label;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public boolean hasDescription()
	{
		return description != null && description.length() > 0;
	}
	
}