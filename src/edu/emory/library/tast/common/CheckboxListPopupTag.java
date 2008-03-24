package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class CheckboxListPopupTag extends UIComponentTag
{
	
	private String items;
	private String selectedValues;
	private String expandedValues;
	private String showSelectAll;

	public String getComponentType()
	{
		return "CheckboxListPopup";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		CheckboxListComponent checkboxList = (CheckboxListComponent) component;
		
		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			checkboxList.setValueBinding("items", vb);
		}
		
		if (selectedValues != null && isValueReference(selectedValues))
		{
			ValueBinding vb = app.createValueBinding(selectedValues);
			checkboxList.setValueBinding("selectedValues", vb);
		}

		if (expandedValues != null && isValueReference(expandedValues))
		{
			ValueBinding vb = app.createValueBinding(expandedValues);
			checkboxList.setValueBinding("expandedValues", vb);
		}

		if (showSelectAll != null && isValueReference(showSelectAll))
		{
			ValueBinding vb = app.createValueBinding(showSelectAll);
			checkboxList.setValueBinding("showSelectAll", vb);
		}
		else
		{
			checkboxList.setShowSelectAll(Boolean.parseBoolean(showSelectAll));
		}
		
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

	public String getExpandedValues()
	{
		return expandedValues;
	}

	public void setExpandedValues(String expandedValues)
	{
		this.expandedValues = expandedValues;
	}

	public String getShowSelectAll()
	{
		return showSelectAll;
	}

	public void setShowSelectAll(String showSelectAll)
	{
		this.showSelectAll = showSelectAll;
	}

}
