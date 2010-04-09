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

import edu.emory.library.tast.util.StringUtils;

public class FieldSchemaTextbox extends FieldSchema
{
	
	private boolean textArea = false;
	private int lines = -1;
	private String cssClass;

	public FieldSchemaTextbox(String name, String description)
	{
		super(name, description);
	}
	
	public FieldSchemaTextbox(String name, String description, String cssClass)
	{
		super(name, description);
		this.cssClass = cssClass;
	}

	public FieldSchemaTextbox(String name, String description, boolean textArea)
	{
		super(name, description);
		this.textArea = textArea;
	}

	public FieldSchemaTextbox(String name, String description, boolean textArea, int lines)
	{
		super(name, description);
		this.textArea = textArea;
		this.lines = lines;
	}

	public FieldSchemaTextbox(String name, String description, boolean textArea, String cssClass)
	{
		super(name, description);
		this.textArea = textArea;
		this.cssClass = cssClass;
	}

	public FieldSchemaTextbox(String name, String description, boolean textArea, int lines, String cssClass)
	{
		super(name, description);
		this.textArea = textArea;
		this.lines = lines;
		this.cssClass = cssClass;
	}
	
	public String getType()
	{
		return FieldValueText.TYPE;
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public FieldSchemaTextbox setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
		return this;
	}
	
	public void createRegJS(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, StringBuffer regJS) throws IOException
	{
		regJS.append("new RecordEditorTexbox(");
		regJS.append("'").append(getName()).append("'");
		regJS.append(")");
	}

	public void encode(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
	{
		
		// type check
		if (!(value instanceof FieldValueText))
			throw new RuntimeException("FieldSchemaTextbox expected FieldValueText, name = " + getName());
				
		// cast
		FieldValueText valueText = (FieldValueText) value;
		
		// get writer
		ResponseWriter writer = context.getResponseWriter();
		
		// input name
		String htmlFieldName = FieldValueText.getHtmlFieldName(editor, context, getName());
		
		// input
		if (!textArea)
		{
			writer.startElement("input", editor);
			writer.writeAttribute("type", "text", null);
			writer.writeAttribute("name", htmlFieldName, null);
			if (!StringUtils.isNullOrEmpty(cssClass)) writer.writeAttribute("class", cssClass, null);
			if (valueText.getValue() != null) writer.writeAttribute("value", valueText.getValue(), null);
			writer.endElement("input");
		}
		else
		{
			writer.startElement("textarea", editor);
			writer.writeAttribute("name", htmlFieldName, null);
			if (!StringUtils.isNullOrEmpty(cssClass)) writer.writeAttribute("class", cssClass, null);
			if (lines != -1) writer.writeAttribute("rows", String.valueOf(lines), null);
			if (valueText.getValue() != null) writer.write(valueText.getValue());
			writer.endElement("textarea");
		}
		
	}

}