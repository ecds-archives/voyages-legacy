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
	
	private void encodeEditMode(GridEditorComponent gridEditor, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer, TextboxFieldType textboxFieldType) throws IOException
	{

		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", inputPrefix, null);
		JsfUtils.writeParamIfNotDefault(writer, "maxlength", textboxFieldType.getMaxLength(), Integer.MAX_VALUE);
		JsfUtils.writeParamIfNotNull(writer, "class", textboxFieldType.getCssClass());
		JsfUtils.writeParamIfNotNull(writer, "style", textboxFieldType.getCssStyle());
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	private void encodeReadOnlyMode(GridEditorComponent gridEditor, String inputPrefix, TextboxValue textboxValue, ResponseWriter writer) throws IOException
	{

		if (textboxValue.getText() != null) {
			writer.write(textboxValue.getText());
		}
		writer.startElement("input", gridEditor);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", inputPrefix, null);
		writer.writeAttribute("value", textboxValue.getText(), null);
		writer.endElement("input");

	}

	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, FieldType fieldType, String inputPrefix, Value value, boolean readOnly) throws IOException
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
			encodeEditMode(gridEditor, inputPrefix, textboxValue, writer, textboxFieldType);
		}
		
	}

}