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

	public void encode(EditorComponent editor, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException
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
			
			writer.startElement("select", editor);
			writer.writeAttribute("name", selectName, null);
			
			if (i == 0 || !StringUtils.isNullOrEmpty(parentValue))
			{
			
				for (int j = 0; j < list.length; j++)
				{
					ListItem item = list[j];
					if (i == 0 || parentValue.equals(item.getValue()))
					{
						writer.startElement("option", editor);
						if (item.getValue().equals(selectedValue)) writer.writeAttribute("selected", "selected", null);
						writer.writeAttribute("value", item.getValue(), null);
						writer.write(StringUtils.coalesce(item.getText(), ""));
						writer.endElement("option");
					}
				}
			
			}
			
			writer.endElement("select");
			
			parentValue = selectedValue;

		}
		
		
	}

}