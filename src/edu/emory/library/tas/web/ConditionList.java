package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class ConditionList extends UIComponentBase
{

	private List conditions = new ArrayList();

	public String getFamily()
	{
		return null;
	}

	public Object saveState(FacesContext state)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(state);
		values[1] = conditions;
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		conditions = (List) values[1];
	}

	public void decode(FacesContext context)
	{
	}

	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
	}

	public void encodeBegin(FacesContext context) throws IOException
	{

		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);

		for (Iterator iterFieldName = conditions.iterator(); iterFieldName.hasNext();)
		{
			String fieldName = (String) iterFieldName.next();
			SchemaColumn col = Voyage.getSchemaColumn(fieldName);
			
			switch (col.getType())
			{
				case SchemaColumn.TYPE_STRING:
					encodeSingleValueCondition(col, context, writer);
					break;

				case SchemaColumn.TYPE_INTEGER:
				case SchemaColumn.TYPE_LONG:
				case SchemaColumn.TYPE_FLOAT:
				case SchemaColumn.TYPE_DATE:
					encodeRangeCondition(col, context, writer);
					break;
			
				case SchemaColumn.TYPE_DICT:
					encodeChoiceCondition(col, context, form, writer);
					break;

			}
			
		}

	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}

	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	private void encodeSingleValueCondition(SchemaColumn col, FacesContext context, ResponseWriter writer) throws IOException
	{

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.write(col.getName());
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getClientId(context) + "_" + col.getName(), null);
		writer.endElement("input");
		writer.endElement("td");
		
		writer.endElement("tr");
		writer.endElement("table");

	}

	private void encodeRangeCondition(SchemaColumn col, FacesContext context, ResponseWriter writer) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.write(col.getName());
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", getClientId(context) + "_" + col.getName(), null);
		writer.endElement("input");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
	
	}
	
	private void encodeChoiceCondition(SchemaColumn col, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
	}

	public void addCondition(String columnName)
	{
		if (Voyage.getSchemaColumn(columnName) == null) return;
		conditions.add(columnName);
	}

	public void removeCondition(String columnName)
	{
		conditions.remove(columnName);
	}

}