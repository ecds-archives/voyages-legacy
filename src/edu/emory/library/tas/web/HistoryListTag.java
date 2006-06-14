package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class HistoryListTag extends UIComponentTag
{
	
	private String list;
	private String ondelete;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		HistoryList historyList = (HistoryList) component;
		
		if (list != null && isValueReference(list))
		{
			ValueBinding vb = app.createValueBinding(list);
			component.setValueBinding("history", vb);
		}
		
		if (ondelete != null && isValueReference(ondelete))
		{
			MethodBinding mb = app.createMethodBinding(ondelete, new Class[] {HistoryItemDeleteEvent.class});
			historyList.setOndelete(mb);
		}

	}
	
	public String getComponentType()
	{
		return "HistoryList";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getOndelete()
	{
		return ondelete;
	}

	public void setOndelete(String ondelete)
	{
		this.ondelete = ondelete;
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