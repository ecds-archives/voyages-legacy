package edu.emory.library.tast.common.grideditor.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.common.grideditor.Adapter;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.SharedListExtension;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class ListAdapter extends Adapter
{

	public static final String TYPE = "list";
	
	private String getHtmlSelectNamePrefix(String inputPrefix)
	{
		return inputPrefix + "_select_";
	}

	private String getDepthFieldName(String inputPrefix)
	{
		return inputPrefix + "_depth";
	}

	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		try
		{

			int valuesCount = Integer.parseInt((String) params.get(getDepthFieldName(inputPrefix)));
			if (valuesCount < 0) return null;
		
			String[] values = new String[valuesCount];
			for (int i = 0; i < valuesCount; i++)
			{
				String value = (String) params.get(getHtmlSelectNamePrefix(inputPrefix) + i);
				if (value == null) return null;
				values[i] = value;
			}
			
			return new ListValue(values);

		}
		catch (NumberFormatException nfe)
		{
			return null;
		}

	}

	public void encodeRegJS(FacesContext context, StringBuffer regJS, GridEditorComponent gridEditor, String inputPrefix, Row row, Column column, Value value, boolean readOnly) throws IOException
	{
		
		ListRow listRow = (ListRow) row;

		regJS.append("new GridEditorList(");
		regJS.append("'").append(listRow.getListName()).append("'");
		regJS.append(", ");
		regJS.append("'").append(getHtmlSelectNamePrefix(inputPrefix)).append("'");
		regJS.append(", ");
		regJS.append("'").append(getDepthFieldName(inputPrefix)).append("'");
		regJS.append(")");

	}
	
	private String[] matchValues(String[] values, ListItem[] items)
	{
		
		List newValues = new ArrayList();
		
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				ListItem item = ListItem.findSubItemByValue(items, values[i]);
				if (item == null)
				{
					break;
				}
				else
				{
					items = item.getSubItems();
				}
				newValues.add(item.getValue());
			}
		}
		
		while (items != null && items.length > 0)
		{
			newValues.add(items[0].getValue());
			items = items[0].getSubItems();
		}
		
		String[] newValuesArray = new String[newValues.size()];
		newValues.toArray(newValuesArray);
		return newValuesArray;
		
	}
	
	public void encode(FacesContext context, GridEditorComponent gridEditor, String clientGridId, UIForm form, Row row, Column column, Map extensions, String inputPrefix, Value value, boolean readOnly) throws IOException
	{

		ListValue listValue = (ListValue) value;
		ListRow listRow = (ListRow) row;
		ResponseWriter writer = context.getResponseWriter();
		SharedListExtension sharedListExt = (SharedListExtension) extensions.get(listRow.getListName());
		
		String[] listValues = matchValues(listValue.getValues(), sharedListExt.getList());
		int maxDepth = ListItem.determineMaxDepth(sharedListExt.getList());
		
		JsfUtils.encodeHiddenInput(gridEditor, writer,
				getDepthFieldName(inputPrefix),
				String.valueOf(listValues.length));

		writer.startElement("table", gridEditor);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		writer.startElement("tr", gridEditor);

		String cssStyle = null;
		ListItem[] items = sharedListExt.getList();
		ListItem[] nextItems = null;

 		for (int i = 0; i < maxDepth; i++)
 		{
 			
 			if (i == listValues.length)
 				cssStyle = "display: none";

 			String selectName =
 					getHtmlSelectNamePrefix(inputPrefix) + i;
 			
 			String onchange =
 				"GridEditorGlobals.listItemSelected(" +
 				"'" + clientGridId + "', " +
 				"'" + row.getName() + "', " +
 				"'" + column.getName() + "', " +
 				"" + i + ")";

 			writer.startElement("td", gridEditor);

 			writer.startElement("select", gridEditor);
			writer.writeAttribute("name", selectName, null);
 			writer.writeAttribute("onchange", onchange, null);
 			if (cssStyle != null) writer.writeAttribute("style", cssStyle, null);

	 	 	if (i < listValues.length)
	 	 	{
	 	 		for (int j = 0; j < items.length; j++)
				{
	 	 			
	 	 			ListItem item = items[j];
	 	 			
					boolean selected;
					if (StringUtils.compareStrings(item.getValue(), listValues[i]))
					{
						selected = true;
						if (i < listValues.length - 1) nextItems = item.getSubItems();
					}
					else
					{
						selected = false;
					}
					
		 			writer.startElement("option", gridEditor);
		 			if (selected) writer.writeAttribute("selected", "selected", null);
		 			writer.writeAttribute("value", item.getValue(), null);
		 			writer.write(item.getText());
		 			writer.endElement("option");

				}
	 	 	}

	 		writer.endElement("select");
	 		writer.endElement("td");
		 	
	 	 	items = nextItems;

 		}
 		
 		writer.endElement("tr");
 		writer.endElement("table");
		
	}

}