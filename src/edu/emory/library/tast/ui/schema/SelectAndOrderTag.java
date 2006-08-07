package edu.emory.library.tast.ui.schema;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SelectAndOrderTag extends UIComponentTag
{
	
	private String availableItems;
	private String selectedItems;
	private String sortable;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SelectAndOrderComponent selectdAndOrder = (SelectAndOrderComponent) component;
		
		if (availableItems != null && isValueReference(availableItems))
		{
			ValueBinding vb = app.createValueBinding(availableItems);
			component.setValueBinding("availableItems", vb);
		}

		if (selectedItems != null && isValueReference(selectedItems))
		{
			ValueBinding vb = app.createValueBinding(selectedItems);
			component.setValueBinding("selectedItems", vb);
		}
		
		if (sortable != null && isValueReference(sortable))
		{
			ValueBinding vb = app.createValueBinding(sortable);
			component.setValueBinding("sortable", vb);
		}
		else
		{
			selectdAndOrder.setSortable("true".compareToIgnoreCase(sortable) == 0);
		}
			
	}
	
	public String getComponentType()
	{
		return "SelectAndOrder";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getAvailableItems()
	{
		return availableItems;
	}

	public void setAvailableItems(String items)
	{
		this.availableItems = items;
	}

	public String getSelectedItems()
	{
		return selectedItems;
	}

	public void setSelectedItems(String selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	public String getSortable()
	{
		return sortable;
	}

	public void setSortable(String sortable)
	{
		this.sortable = sortable;
	}

}
