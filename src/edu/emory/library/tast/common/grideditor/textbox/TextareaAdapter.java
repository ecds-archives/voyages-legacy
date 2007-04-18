package edu.emory.library.tast.common.grideditor.textbox;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;

public class TextareaAdapter extends Adapter
{

	public static final String TYPE = "textarea";
	
	protected String getSubmittedValue(FacesContext context, String inputPrefix)
	{
		return (String) context.getExternalContext().getRequestParameterMap().get(inputPrefix);
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		String submittedValue = getSubmittedValue(context, inputPrefix);
		if (submittedValue == null) return null;
		return new TextareaValue(submittedValue.split("\n"));
	}
	
	private void encodeEditMode(GridEditorComponent gridEditor, String inputPrefix, TextareaValue textboxValue, ResponseWriter writer) throws IOException
	{

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "textarea", null);
		writer.writeAttribute("name", inputPrefix, null);
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, TextareaValue textboxValue, ResponseWriter writer) throws IOException
	{

		writer.write(textboxValue.getText().replaceAll("\n", "<br>"));

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", inputPrefix, null);
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, Map extensions, String inputPrefix, Value value, boolean readOnly) throws IOException
	{
		
		TextareaValue textboxValue = (TextareaValue) value;
		ResponseWriter writer = context.getResponseWriter();
		
		if (readOnly)
		{
			encodeEditMode(gridEditor, inputPrefix, textboxValue, writer);
		}
		else
		{
			encodeReadOnlyMode(gridEditor, inputPrefix, textboxValue, writer);
		}
		
	}

}