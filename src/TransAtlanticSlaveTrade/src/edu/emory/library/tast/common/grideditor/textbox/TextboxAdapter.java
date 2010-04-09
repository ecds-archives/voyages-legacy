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

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.FieldType;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.JsfUtils;

public class TextboxAdapter extends Adapter
{

	public static final String TYPE = "textbox";
	
	protected String getSubmittedValue(FacesContext context, String inputPrefix)
	{
		return (String) context.getExternalContext().getRequestParameterMap().get(inputPrefix);
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		String submittedValue = getSubmittedValue(context, inputPrefix);
		if (submittedValue == null) return null;
		return new TextboxValue(submittedValue);
	}
	
	private String getInputName(String inputPrefix)
	{
		return inputPrefix;
	}
	
	public void createValueJavaScript(FacesContext context, StringBuffer regJS, GridEditorComponent gridEditor, String inputPrefix, Row row, Column column, String cellId, Value value, boolean readOnly) throws IOException
	{
		regJS.append("new GridEditorTextbox(");
		regJS.append("'").append(cellId).append("'");
		regJS.append(", ");
		regJS.append("'").append(getInputName(inputPrefix)).append("'");
		regJS.append(")");
	}
	
	private void encodeEditMode(GridEditorComponent gridEditor, String clientGridId, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer, Row row, Column column, TextboxFieldType textboxFieldType, boolean invokeCompare) throws IOException
	{

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getInputName(inputPrefix), null);
		JsfUtils.writeParamIfNotDefault(writer, "maxlength", textboxFieldType.getMaxLength(), Integer.MAX_VALUE);
		JsfUtils.writeParamIfNotNull(writer, "size", textboxFieldType.getInputSize());
		JsfUtils.writeParamIfNotNull(writer, "class", textboxFieldType.getCssClass());
		JsfUtils.writeParamIfNotNull(writer, "style", textboxFieldType.getCssStyle());
		writer.writeAttribute("value", (textboxValue != null ? textboxValue.getText(): null), null);
		if (invokeCompare)
		{
			String compareJS = "GridEditorGlobals.compare('" + clientGridId + "', '" + row.getName() + "', '" + column.getName() + "')";
			writer.writeAttribute("onkeyup", compareJS, null);
			writer.writeAttribute("onchange", compareJS, null);
		}
		writer.endElement("input");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer) throws IOException
	{
		String text=null;

		if (textboxValue !=null && textboxValue.getText() != null) {
			text=textboxValue.getText();
			writer.write(text);
		}
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", getInputName(inputPrefix), null);
		writer.writeAttribute("value", text, null);
		writer.endElement("input");

	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, FieldType fieldType, String inputPrefix, Value value, boolean readOnly, boolean invokeCompare) throws IOException
	{

		TextboxValue textboxValue = (TextboxValue) value;
		ResponseWriter writer = context.getResponseWriter();
		TextboxFieldType textboxFieldType = (TextboxFieldType) fieldType;
		
		if (readOnly)
		{
			encodeReadOnlyMode(gridEditor, inputPrefix, textboxValue, writer);
		}
		else
		{
			encodeEditMode(gridEditor, clientGridId, inputPrefix, textboxValue, writer, row, column, textboxFieldType, invokeCompare);
		}
		
	}

}
