package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class LookupCheckboxListTag extends UIComponentTagBase
{
	
	private String items;
	private String selectedValues;
	private String expandedValues;

	public String getComponentType()
	{
		return "LookupCheckboxList";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		LookupCheckboxListComponent lookupList = (LookupCheckboxListComponent) component;
		
		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			lookupList.setValueBinding("items", vb);
		}

		if (selectedValues != null && isValueReference(selectedValues))
		{
			ValueBinding vb = app.createValueBinding(selectedValues);
			lookupList.setValueBinding("selectedValues", vb);
		}

		if (expandedValues != null && isValueReference(expandedValues))
		{
			ValueBinding vb = app.createValueBinding(expandedValues);
			lookupList.setValueBinding("expandedValues", vb);
		}

	}

	public String getExpandedValues()
	{
		return expandedValues;
	}

	public void setExpandedValues(String expandedValues)
	{
		this.expandedValues = expandedValues;
	}

	public String getItems()
	{
		return items;
	}

	public void setItems(String items)
	{
		this.items = items;
	}

	public String getSelectedValues()
	{
		return selectedValues;
	}

	public void setSelectedValues(String selectedValues)
	{
		this.selectedValues = selectedValues;
	}

}
