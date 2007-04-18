package edu.emory.library.tast.common.grideditor.textbox;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;

public class TextboxAdapter extends Adapter
{

	public static final String TYPE = "textbox";
	
	protected String getSubmittedValue(FacesContext context, String inputPrefix)
	{
		String val = (String) context.getExternalContext().getRequestParameterMap().get(inputPrefix);
		if (val != null && !val.trim().equals("")) {
			return val;
		} else {
			return null;
		}
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		return new TextboxValue(getSubmittedValue(context, inputPrefix));
	}
	
	private void encodeEditMode(GridEditorComponent gridEditor, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer) throws IOException
	{

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputPrefix, null);
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer) throws IOException
	{

		writer.write(textboxValue.getText());

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", inputPrefix, null);
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Map extensions, String inputPrefix, Value value, boolean readOnly) throws IOException
	{
		
		TextboxValue textboxValue = (TextboxValue) value;
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