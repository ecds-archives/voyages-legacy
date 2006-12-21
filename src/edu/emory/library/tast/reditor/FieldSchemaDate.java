package edu.emory.library.tast.reditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class FieldSchemaDate extends FieldSchema
{
	
	public FieldSchemaDate(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueDate.TYPE;
	}

	public void createRegJS(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, StringBuffer regJS) throws IOException
	{
		regJS.append("new RecordEditorDate(");
		regJS.append("'").append(getName()).append("'");
		regJS.append(")");
	}

	public void encode(EditorComponent editor, String editorId, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
	{
		
		// type check
		if (!(value instanceof FieldValueDate))
			throw new RuntimeException("FieldSchemaDate expected FieldValueDate");
				
		// cast
		FieldValueDate valueDate = (FieldValueDate) value;
		
		// get writer
		ResponseWriter writer = context.getResponseWriter();
		
		// input names
		String dayFieldName = FieldValueDate.getHtmlDayFieldName(editor, context, getName());
		String monthFieldName = FieldValueDate.getHtmlDayFieldName(editor, context, getName());
		String yearFieldName = FieldValueDate.getHtmlDayFieldName(editor, context, getName());
		
		// day
		writer.startElement("input", editor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", dayFieldName, null);
		writer.writeAttribute("value", valueDate.getDayOrEmpty(), null);
		writer.writeAttribute("class", "record-editor-date-day", null);
		writer.endElement("input");
		
		// month
		writer.startElement("input", editor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", monthFieldName, null);
		writer.writeAttribute("value", valueDate.getMonthOrEmpty(), null);
		writer.writeAttribute("class", "record-editor-date-month", null);
		writer.endElement("input");

		// year
		writer.startElement("input", editor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", yearFieldName, null);
		writer.writeAttribute("value", valueDate.getYearOrEmpty(), null);
		writer.writeAttribute("class", "record-editor-date-year", null);
		writer.endElement("input");

	}

}