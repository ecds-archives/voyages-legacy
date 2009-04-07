package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class PopupTag extends UIComponentTag
{
	
	private String htmlToDisplay = null;
	private String width;
	private String height;

	public String getComponentType()
	{
		return "Popup";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		PopupComponent popup = (PopupComponent) component;
		
		if (width != null && isValueReference(width))
		{
			ValueBinding vb = app.createValueBinding(width);
			popup.setValueBinding("width", vb);
		}
		else
		{
			popup.setWidth(Integer.parseInt(width));
		}
		
		if (height != null && isValueReference(height))
		{
			ValueBinding vb = app.createValueBinding(height);
			popup.setValueBinding("height", vb);
		}
		else
		{
			popup.setHeight(Integer.parseInt(height));
		}

	}

	public String getHtmlToDisplay()
	{
		return htmlToDisplay;
	}

	public void setHtmlToDisplay(String htmlToDisplay)
	{
		this.htmlToDisplay = htmlToDisplay;
	}

	public String getWidth()
	{
		return width;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

}
