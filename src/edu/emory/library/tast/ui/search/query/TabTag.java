package edu.emory.library.tast.ui.search.query;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class TabTag extends UIComponentTag
{
	
	private String text;
	private String tabId;
//	private String selected;
	
	protected void setProperties(UIComponent component)
	{
		
		TabComponent tab = (TabComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (text != null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			tab.setValueBinding("text", vb);
		}
		else
		{
			tab.setText(text);
		}
		
		if (tabId != null && isValueReference(tabId))
		{
			ValueBinding vb = app.createValueBinding(tabId);
			tab.setValueBinding("tabId", vb);
		}
		else
		{
			tab.setTabId(tabId);
		}
		
//		if (selected != null && isValueReference(selected))
//		{
//			ValueBinding vb = app.createValueBinding(selected);
//			tab.setValueBinding("selected", vb);
//		}
//		else
//		{
//			tab.setSelected("true".equals(selected));
//		}

	}

	public String getComponentType()
	{
		return "Tab";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getTabId()
	{
		return tabId;
	}

	public void setTabId(String tabId)
	{
		this.tabId = tabId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

//	public String getSelected()
//	{
//		return selected;
//	}
//
//	public void setSelected(String selected)
//	{
//		this.selected = selected;
//	}

}