package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.JsfUtils;

public class LookupCheckboxListComponent extends UIComponentBase
{
	
	public static final String ID_SEPARATOR = ":";
	
	private boolean selectedValuesSet = false;
	private String[] selectedValues;

	private boolean itemsSet = false;
	private LookupCheckboxItem[] items;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public LookupCheckboxItem[] getItems()
	{
		return (LookupCheckboxItem[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"items", itemsSet, items);
	}

	public void setItems(LookupCheckboxItem[] items)
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
