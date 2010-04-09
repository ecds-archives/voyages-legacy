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

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class FieldSchemaDropdowns extends FieldSchema
{
	
	private String[] listIds;
	private String cssClass;

	public FieldSchemaDropdowns(String name, String description, String listId)
	{
		super(name, description);
		this.listIds = new String[] {listId};
	}

	public FieldSchemaDropdowns(String name, String description, String[] listIds)
	{
		super(name, description);
		this.listIds = listIds;
	}

	public FieldSchemaDropdowns(String name, String description, String listId, String cssClass)
	{
		super(name, description);
		this.listIds = new String[] {listId};
		this.cssClass = cssClass;
	}

	public FieldSchemaDropdowns(String name, String description, String[] listIds, String cssClass)
	{
		super(name, description);
		this.listIds = listIds;
		this.cssClass = cssClass;
	}

	public String getType()
	{
		return FieldValueDropdowns.TYPE;
	}
	
	public String[] getListIds()
	{
		return listIds;
	}

	public void setListIds(String[] listIds)
	{
		this.listIds = listIds;
	}
	
	public String getCssClass()
	{
		return cssClass;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public void createRegJS(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, StringBuffer regJS) throws IOException
	{
		regJS.append("new RecordEditorDropdowns(");
		regJS.append("'").append(getName()).append("'");
		regJS.append(", [");
		for (int i = 0; i < listIds.length; i++)
		{
			if (i > 0) regJS.append(", ");
			regJS.append("'").append(FieldValueDropdowns.getHtmlSelectName(editor, context, getName(), i)).append("'");
		}
		regJS.append("], [");
		for (int i = 0; i < listIds.length; i++)
		{
			if (i > 0) regJS.append(", ");
			regJS.append("'").append(listIds[i]).append("'");
		}
		regJS.append("])");
	}

	public void encode(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
	{
		
		// type check
		if (!(value instanceof FieldValueDropdowns))
			throw new RuntimeException("FieldSchemaDropdowns expected FieldValueDropdowns");
		
		// cast
		FieldValueDropdowns valueDropdowns = (FieldValueDropdowns) value;
		
		// get writer
		ResponseWriter writer = context.getResponseWriter();
		
		// number of lists
		int listCount = listIds != null ? listIds.length : 0;
		
		// hidden field with the number of lists
		String countFieldName = FieldValueDropdowns.getHtmlCountHiddenFieldName(editor, context, getName());
		JsfUtils.encodeHiddenInput(editor, writer, countFieldName, String.valueOf(listCount));
		
		// selects
		String parentValue = null;
		for (int i = 0; i < listCount; i++)
		{
		
			String selectName = FieldValueDropdowns.getHtmlSelectName(editor, context, getName(), i);
			ListItem[] list = schema.getListById(listIds[i]);
			String selectedValue = valueDropdowns.getValue(i);
			
			String onChange = "RecordEditorGlobals.dropdownValueChanged(" +
					"'" + editorId + "'," +
					"'" + getName() + "'," +
					+ i + ")";
			
			writer.startElement("div", editor);
			
			writer.startElement("select", editor);
			writer.writeAttribute("name", selectName, null);
			writer.writeAttribute("onchange", onChange, null);
			if (!StringUtils.isNullOrEmpty(cssClass)) writer.writeAttribute("class", cssClass, null);
			
			for (int j = 0; j < list.length; j++)
			{
				ListItem item = list[j];
				if (i == 0 ||
						(item.getParentValue() == null) ||
						(parentValue == null && item.getValue() == null) ||
						(parentValue != null && parentValue.equals(item.getParentValue())))
				{
					writer.startElement("option", editor);
					if (StringUtils.compareStrings(item.getValue(), selectedValue)) writer.writeAttribute("selected", "selected", null);
					writer.writeAttribute("value", item.getValue(), null);
					writer.write(StringUtils.coalesce(item.getText(), ""));
					writer.endElement("option");
				}
			}
			
			writer.endElement("select");
			writer.endElement("div");
			
			parentValue = selectedValue;

		}
		
		
	}

}