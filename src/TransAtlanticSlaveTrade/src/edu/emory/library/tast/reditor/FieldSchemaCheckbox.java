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
import javax.faces.context.ResponseWriter;

public class FieldSchemaCheckbox extends FieldSchema
{

	public String getType()
	{
		return FieldValueCheckbox.TYPE;
	}

	public FieldSchemaCheckbox(String name, String description)
	{
		super(name, description);
	}
	
	public void createRegJS(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, StringBuffer regJS) throws IOException
	{
		regJS.append("new RecordEditorCheckbox(");
		regJS.append("'").append(getName()).append("'");
		regJS.append(")");
	}

	public void encode(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
	{
		
		// type check
		if (!(value instanceof FieldValueCheckbox))
			throw new RuntimeException("FieldSchemaCheckbox expected FieldValueCheckbox");
		
		// cast
		FieldValueCheckbox valueCheckbox = (FieldValueCheckbox) value;

		// get writer
		ResponseWriter writer = context.getResponseWriter();

		// input name
		String htmlFieldName = FieldValueText.getHtmlFieldName(editor, context, getName());
		
		// main div container
		writer.startElement("input", editor);
		writer.writeAttribute("name", htmlFieldName, null);
		writer.writeAttribute("type", "checkbox", null);
		if (valueCheckbox.isChecked()) writer.writeAttribute("checked", "checked", null);
		writer.endElement("input");

	}

}
