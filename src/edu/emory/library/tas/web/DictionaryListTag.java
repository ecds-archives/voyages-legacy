package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class DictionaryListTag extends UIComponentTag
{
	
	private String attribute;
	private String list;
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		DictionaryListComponent dictionaryList = (DictionaryListComponent) component;
		
		if (attribute != null && isValueReference(attribute))
		{
			ValueBinding vb = app.createValueBinding(attribute);
			component.setValueBinding("attribute", vb);
		}
		else
		{
			dictionaryList.setAttributeName(attribute);
		}

		if (list != null && isValueReference(list))
		{
			ValueBinding vb = app.createValueBinding(list);
			component.setValueBinding("list", vb);
		}

	}

	public String getComponentType()
	{
		return "DictionaryList";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getAttribute()
	{
		return attribute;
	}

	public void setAttribute(String attribute)
	{
		this.attribute = attribute;
	}

	public String getList()
	{
		return list;
	}

	public void setList(String list)
	{
		this.list = list;
	}

}
