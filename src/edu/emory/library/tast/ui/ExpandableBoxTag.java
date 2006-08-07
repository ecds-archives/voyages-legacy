package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ExpandableBoxTag extends UIComponentTag
{
	
	private String text;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		ExpandableBoxComponent box  = (ExpandableBoxComponent) component;
		
		if (text !=null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			component.setValueBinding("query", vb);
		}
		else
		{
			box.setText(text);
		}
		
	}

	public String getComponentType()
	{
		return "ExpandableBox";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}
