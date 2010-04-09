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

public class TextareaAdapter extends Adapter
{

	public static final String TYPE = "textarea";
	
	private String getInputName(String inputPrefix)
	{
		return inputPrefix;
	}

	protected String getSubmittedValue(FacesContext context, String inputPrefix)
	{
		return (String) context.getExternalContext().getRequestParameterMap().get(getInputName(inputPrefix));
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		String submittedValue = getSubmittedValue(context, inputPrefix);
		if (submittedValue == null) return null;
		return new TextareaValue(submittedValue.split("[\n\r]+"));
	}
	
	public void createValueJavaScript(FacesContext context, StringBuffer regJS, GridEditorComponent gridEditor, String inputPrefix, Row row, Column column, String cellId, Value value, boolean readOnly) throws IOException
	{
		regJS.append("new GridEditorTextarea(");
		regJS.append("'").append(cellId).append("'");
		regJS.append(", ");
		regJS.append("'").append(getInputName(inputPrefix)).append("'");
		regJS.append(")");
	}

	private void encodeEditMode(GridEditorComponent gridEditor, String clientGridId, String inputPrefix, TextareaValue textboxValue, ResponseWriter writer, Row row, Column column, TextareaFieldType textareaFieldType, boolean invokeCompare) throws IOException
	{

		writer.startElement("textarea", gridEditor);
		writer.writeAttribute("type", "textarea", null);
		writer.writeAttribute("name", getInputName(inputPrefix), null);
		JsfUtils.writeParamIfNotDefault(writer, "rows", textareaFieldType.getRows(), TextareaFieldType.ROWS_DEFAULT);
		JsfUtils.writeParamIfNotNull(writer, "class", textareaFieldType.getCssClass());
		JsfUtils.writeParamIfNotNull(writer, "style", textareaFieldType.getCssStyle());
		if (invokeCompare)
		{
			String compareJS = "GridEditorGlobals.compare('" + clientGridId + "', '" + row.getName() + "', '" + column.getName() + "')";
			writer.writeAttribute("onkeyup", compareJS, null);
			writer.writeAttribute("onchange", compareJS, null);
		}
		writer.write(textboxValue.getText());
		writer.endElement("textarea");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, TextareaValue textboxValue, ResponseWriter writer) throws IOException
	{

		JsfUtils.encodeHiddenInput(
				gridEditor, writer,
				getInputName(inputPrefix),
				textboxValue.getText());

		String[] texts = textboxValue.getTexts();
		String[] rollovers = textboxValue.getRollovers();
		
		writer.startElement("table", gridEditor);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("style", "width: 100%;", null);
		writer.writeAttribute("class", "multiline-attr-table", null);
		
		for (int i = 0; i < texts.length; i++) {
			writer.startElement("tr", gridEditor);
			writer.startElement("td", gridEditor);
			writer.writeAttribute("id", "cell_" + i, null);
			if (rollovers != null && i < rollovers.length && rollovers[i] != null) {
				writer.writeAttribute("onmouseover", "showToolTipOnRight('" + "tooltip_" + i + "','" + "cell_" + i + "')", null);
				writer.writeAttribute("onmouseout", "hideToolTip('" + "tooltip_" + i + "')", null);
				
				writer.startElement("div", gridEditor);
				writer.writeAttribute("id", "tooltip_" + i, null);
				writer.writeAttribute("class", "grid-tooltip", null);
				writer.startElement("table", gridEditor);
				writer.writeAttribute("cellspacing", "0", null);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("cellpadding", "0", null);
				writer.startElement("tr", gridEditor);
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-11", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-12", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-13", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");								
				writer.endElement("tr");
				
				
				writer.startElement("tr", gridEditor);
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-21", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-22", null);								
				writer.startElement("div", gridEditor);
				writer.write(rollovers[i]);
				writer.endElement("div");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-23", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");								
				writer.endElement("tr");
				
				
				writer.startElement("tr", gridEditor);
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-31", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-32", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");
				writer.startElement("td", gridEditor);
				writer.writeAttribute("class", "bubble-33", null);
				writer.startElement("div", gridEditor);writer.endElement("td");
				writer.endElement("td");								
				writer.endElement("tr");
				writer.endElement("table");
				
				writer.endElement("div");
			}
			
			writer.write(texts[i]);
			
			writer.endElement("td");
			writer.endElement("tr");
			
		}
		writer.endElement("table");										
		
		
		
	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, FieldType fieldType, String inputPrefix, Value value, boolean readOnly, boolean invokeCompare) throws IOException
	{
		
		TextareaValue textboxValue = (TextareaValue) value;
		TextareaFieldType textareaFieldType = (TextareaFieldType) fieldType;
		ResponseWriter writer = context.getResponseWriter();
		
		if (readOnly)
			encodeReadOnlyMode(gridEditor, inputPrefix, textboxValue, writer);
		else
			encodeEditMode(gridEditor, clientGridId, inputPrefix, textboxValue, writer, row, column, textareaFieldType, invokeCompare);
		
	}

}