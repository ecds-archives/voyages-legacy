package edu.emory.library.tast.ui;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public abstract class CheckboxListComponent extends UIComponentBase
{

	private boolean selectedValuesSet = false;
	protected String selectedValues[];
	private boolean itemsSet = false;
	private SelectItem[] items;

	public String getFamily()
	{
		return null;
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