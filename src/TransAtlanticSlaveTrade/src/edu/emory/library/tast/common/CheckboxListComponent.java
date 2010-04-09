/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public abstract class CheckboxListComponent extends UIComponentBase
{

	private boolean selectedValuesSet = false;
	protected String selectedValues[];
	
	private boolean itemsSet = false;
	private SelectItem[] items;

	private boolean showSelectAllSet = false;
	protected boolean showSelectAll = true;

	public String getFamily()
	{
		return null;
	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = new Boolean(showSelectAll);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		showSelectAll = ((Boolean) values[1]).booleanValue();
	}
	
	protected String getHtmlNameForSelectemValues(FacesContext context)
	{
		return getClientId(context);
	}

	protected String getHtmlIdForCheckbox(FacesContext context, String checkboxListId, SelectItem item)
	{
		return checkboxListId + "_checkbox_" + item.getValue();
	}

	protected String getHtmlIdForItem(FacesContext context, String checkboxListId, SelectItem item)
	{
		return checkboxListId + "_item_" + item.getValue();
	}

	public void decode(FacesContext context)
	{
		selectedValues = (String[])
			context.getExternalContext().
			getRequestParameterValuesMap().
			get(getHtmlNameForSelectemValues(context));
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vbSelectedValues = getValueBinding("selectedValues");
		if (vbSelectedValues != null && selectedValues != null)
			vbSelectedValues.setValue(context, selectedValues);
	}
	
	protected void registerItems(FacesContext context, String checkboxListId, StringBuffer regJS, SelectItem []items)
	{
		regJS.append("[");
		if (items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				SelectItem item = items[i];
				if (i > 0) regJS.append(", ");
				regJS.append("new SelectItem(");
				regJS.append("'").append(item.getValue()).append("'");
				regJS.append(", ");
				regJS.append("'").append(getHtmlIdForCheckbox(context, checkboxListId, item)).append("'");
				regJS.append(", ");
				registerItems(context, checkboxListId, regJS, item.getSubItems());
				regJS.append(")");
			}
		}
		regJS.append("]");
	}
	
	protected void encodeSelectDeselectAll(ResponseWriter writer, String checkboxListId) throws IOException
	{
		
		String selectAllJS =
			"CheckboxListGlobals.checkAll(" +
			"'" + checkboxListId + "');";
	
		String deselectAllJS =
			"CheckboxListGlobals.uncheckAll(" +
			"'" + checkboxListId + "');";
		
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "checkbox-list-select-deselect-all", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);

		// label
//		writer.startElement("td", this);
//		writer.writeAttribute("class", "checkbox-list-select-deselect-all-label", null);
//		writer.write(TastResource.getText("checkbox_list_select_deselect_all"));
//		writer.endElement("td");

		// select all
		writer.startElement("td", this);
		writer.writeAttribute("class", "checkbox-list-select-all", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("onclick", selectAllJS, null);
		writer.writeAttribute("value", "Select all", null);
		writer.endElement("input");
		writer.endElement("td");
		
		// deselect all
		writer.startElement("td", this);
		writer.writeAttribute("class", "checkbox-list-deselect-all", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("onclick", deselectAllJS, null);
		writer.writeAttribute("value", "Deselect all", null);
		writer.endElement("input");
		writer.endElement("td");

		// done
		writer.endElement("tr");
		writer.endElement("table");

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

	public boolean isShowSelectAll()
	{
		return JsfUtils.getCompPropBoolean(this, getFacesContext(),
				"showSelectAll", showSelectAllSet, showSelectAll);
	}

	public void setShowSelectAll(boolean showSelectAll)
	{
		this.showSelectAllSet = true;
		this.showSelectAll = showSelectAll;
	}

}