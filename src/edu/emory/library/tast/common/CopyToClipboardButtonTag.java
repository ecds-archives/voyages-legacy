package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class CopyToClipboardButtonTag extends UIComponentTag
{
	
	private String text;
	private String data;

	public String getComponentType()
	{
		return "CopyToClipboardButton";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		CopyToClipboardButtonComponent button = (CopyToClipboardButtonComponent) component;
		
		if (text != null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			button.setValueBinding("text", vb);
		}
		else
		{
			button.setText(text);
		}
		
		if (data != null && isValueReference(data))
		{
			ValueBinding vb = app.createValueBinding(data);
			button.setValueBinding("data", vb);
		}
		else
		{
			button.setData(data);
		}

	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

}
