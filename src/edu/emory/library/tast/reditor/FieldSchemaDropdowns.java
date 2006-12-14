package edu.emory.library.tast.reditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class FieldSchemaDropdowns extends FieldSchema
{
	
	private ListItem[][] lists;

	public FieldSchemaDropdowns(String name, String description)
	{
		super(name, description);
	}

	public ListItem[][] getLists()
	{
		return lists;
	}

	public void setLists(ListItem[][] lists)
	{
		this.lists = lists;
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
		int listCount = lists != null ? lists.length : 0;
		
		// hidden field with the number of lists
		String countFieldName = FieldValueDropdowns.getHtmlCountHiddenFieldName(editor, context, getName());
		JsfUtils.encodeHiddenInput(editor, writer, countFieldName, String.valueOf(listCount));
		
		// selects
		for (int i = 0; i < listCount; i++)
		{
		
			String selectName = FieldValueDropdowns.getHtmlSelectName(editor, context, getName(), i);
			ListItem[] list = lists[i];
			String selectedValue = valueDropdowns.getValue(i);
			String parentValue = null;

			writer.startElement("select", editor);
			writer.writeAttribute("name", selectName, null);
			for (int j = 0; j < list.length; i++)
			{
				ListItem item = list[i];
				writer.startElement("option", editor);
				if (item.getValue().equals(selectedValue))
				{
					writer.writeAttribute("selected", "selected", null);
					parentValue = item.getParentValue();
				}
				writer.writeAttribute("value", item.getValue(), null);
				writer.write(item.getText());
				writer.endElement("option");
			}
			
			writer.endElement("select");

		}
		
		
	}

}