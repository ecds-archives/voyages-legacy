package edu.emory.library.tast.reditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class FieldSchemaTextbox extends FieldSchema
{

	public FieldSchemaTextbox(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueText.TYPE;
	}

	public void encode(EditorComponent editor, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
	{
		
		// type check
		if (!(value instanceof FieldValueText))
			throw new RuntimeException("FieldSchemaTextbox expected FieldValueText");
				
		// cast
		FieldValueText valueText = (FieldValueText) value;
		
		// get writer
		ResponseWriter writer = context.getResponseWriter();
		
		// input name
		String htmlFieldName = FieldValueText.getHtmlFieldName(editor, context, getName());
		
		// main div container
		writer.startElement("input", editor);
		writer.writeAttribute("name", htmlFieldName, null);
		writer.writeAttribute("value", valueText.getValue(), null);
		writer.endElement("input");
		
	}

}