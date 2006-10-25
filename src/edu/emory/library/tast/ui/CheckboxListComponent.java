package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Map;
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
	
	private String getHtmlNameForItem(FacesContext context)
	{
		return getClientId(context);
	}

	public void decode(FacesContext context)
	{
		String inputName = getHtmlNameForItem(context);
		Map paramValues = context.getExternalContext().getRequestParameterValuesMap();
		selectedValues = (String[]) paramValues.get(inputName);
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedValues");
		if (vb != null && selectedValues != null) vb.setValue(context, selectedValues);
	}
	
	private void encodeLevel(FacesContext context, ResponseWriter writer, String inputName, SelectItem[] items, Set selectedValuesSet, int level) throws IOException
	{
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "checkbox-list-table-" + level, null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		// items
		for (int i = 0; i < items.length; i++)
		{
			SelectItem item = items[i];
			boolean checked = selectedValuesSet.contains(item.getValue());
			String inputId = getClientId(context) + "_" + item.getValue();

			// item TR begin
			writer.startElement("tr", this);
			
			// checkbox
			writer.startElement("td", this);
			writer.writeAttribute("class", "checkbox-list-checkbox-" + level, null);
			if (item.isSelectable())
			{
				writer.startElement("input", this);
				writer.writeAttribute("type", "checkbox", null);
				writer.writeAttribute("name", inputName, null);
				writer.writeAttribute("id", inputId, null);
				writer.writeAttribute("value", item.getValue(), null);
				if (checked) writer.writeAttribute("checked", "checked", null);
				writer.endElement("input");
			}
			writer.endElement("td");

			// label
			writer.startElement("td", this);
			writer.writeAttribute("class", "checkbox-list-label-" + level, null);
			writer.startElement("label", this);
			writer.writeAttribute("for", inputId, null);
			writer.write(item.getText());
			writer.endElement("label");
			writer.endElement("td");

			// item TR end
			writer.endElement("tr");
			
			// subitems
			if (item.hasSubItems())
			{
				writer.startElement("tr", this);
				writer.startElement("td", this);
				writer.endElement("td");
				writer.startElement("td", this);
				encodeLevel(context, writer, inputName, item.getSubItems(), selectedValuesSet, level + 1);
				writer.endElement("td");
				writer.endElement("tr");
			}

		}
		
		// end mail table
		writer.endElement("table");
	}
	
	
	public void encodeBegin(FacesContext context) throws IOException
	{

		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from beans
		SelectItem[] items = getItems();
		Set selectedValuesSet = StringUtils.toStringSet(getSelectedValues());

		// encode recursivelly
		if (items == null) return;
		encodeLevel(context, writer,
				getHtmlNameForItem(context),
				items, selectedValuesSet, 0);
		
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
