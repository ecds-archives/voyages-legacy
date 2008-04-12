package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SimpleListTag extends UIComponentTag
{
	
	private String elements;

	public String getComponentType()
	{
		return "SimpleList";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SimpleListComponent list = (SimpleListComponent) component;
		
		if (elements != null && isValueReference(elements))
		{
			ValueBinding vb = app.createValueBinding(elements);
			list.setValueBinding("elements", vb);
		}

	}

	public String getElements()
	{
		return elements;
	}

	public void setElements(String elements)
	{
		this.elements = elements;
	}

}
