package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class CheckboxListComponent extends UIComponentBase
{
	
	private boolean selectedValuesSet = false;
	private String selectedValues[];
	
	private boolean itemsSet = false;
	private SelectItem[] items;
	
	public String getFamily()
	{
		return null;
	}
	
	private String getHtmlNameForItem(FacesContext context, SelectItem item)
	{
		return getClientId(context) + "_" + item.getValue();
	}

	public void decode(FacesContext context)
	{
		
		String clientId = getClientId(context);
		List selecteItemsList = new ArrayList();
		
		for (Iterator iter = context.getExternalContext().getRequestParameterNames(); iter.hasNext();)
		{
			String paramName = (String) iter.next();
			if (paramName.startsWith(clientId))
			{
				selecteItemsList.add(paramName.substring(clientId.length()));
			}
		}
		
		selectedValues = new String[selecteItemsList.size()];
		selecteItemsList.toArray(selectedValues);
		
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedValues");
		if (vb != null && selectedValues != null) vb.setValue(context, selectedValues);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{

		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from beans
		SelectItem[] items = getItems();
		Set selectedValuesSet = StringUtils.toStringSet(getSelectedValues());

		// main table
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		// items
		for (int i = 0; i < items.length; i++)
		{
			SelectItem item = items[i];
			boolean checked = selectedValuesSet.contains(item.getValue());
			String inputName = getHtmlNameForItem(context, item);

			writer.startElement("tr", this);
			
			// checkbox
			writer.startElement("td", this);
			writer.startElement("input", this);
			writer.writeAttribute("type", "checkbox", null);
			writer.writeAttribute("value", inputName, null);
			writer.writeAttribute("id", inputName, null);
			if (checked) writer.writeAttribute("checked", "checked", null);
			writer.endElement("input");
			writer.endElement("td");

			// label
			writer.startElement("td", this);
			writer.startElement("label", this);
			writer.writeAttribute("for", inputName, null);
			writer.write(item.getText());
			writer.endElement("label");
			writer.endElement("td");

			writer.endElement("tr");
		}
		
		// end mail table
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public SelectItem[] getItems()
	{
		return (SelectItem[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"items", itemsSet, items);
	}

	public void setItems(SelectItem[] items)
	{
		itemsSet = true;
		this.items = items;
	}

	public String[] getSelectedValues()
	{
		return JsfUtils.getCompPropStringArray(this, getFacesContext(),
				"selectedValues", selectedValuesSet, selectedValues);
	}

	public void setSelectedValues(String[] selectedValues)
	{
		selectedValuesSet = true;
		this.selectedValues = selectedValues;
	}
	
	

}
