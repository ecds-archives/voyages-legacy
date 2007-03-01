package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class LookupCheckboxListTag extends UIComponentTagBase
{
	
	private String selectedValues;
	private String items;

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
		
		if (selectedValues != null && isValueReference(selectedValues))
		{
			ValueBinding vb = app.createValueBinding(selectedValues);
			lookupList.setValueBinding("selectedValues", vb);
		}
		
		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			lookupList.setValueBinding("items", vb);
		}

	}

}
