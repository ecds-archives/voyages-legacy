package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class HistoryListTag extends UIComponentTag
{
	
	private String history;
	private String ondelete;
	private String onrestore;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		HistoryListComponent historyList = (HistoryListComponent) component;
		
		if (history != null && isValueReference(history))
		{
			ValueBinding vb = app.createValueBinding(history);
			component.setValueBinding("history", vb);
		}
		
		if (ondelete != null && isValueReference(ondelete))
		{
			MethodBinding mb = app.createMethodBinding(ondelete, new Class[] {HistoryItemDeleteEvent.class});
			historyList.setOndelete(mb);
		}

		if (onrestore != null && isValueReference(onrestore))
		{
			MethodBinding mb = app.createMethodBinding(onrestore, new Class[] {HistoryItemRestoreEvent.class});
			historyList.setOnrestore(mb);
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

	public String getHistory()
	{
		return history;
	}

	public void setHistory(String list)
	{
		this.history = list;
	}

	public String getOnrestore()
	{
		return onrestore;
	}

	public void setOnrestore(String onrestore)
	{
		this.onrestore = onrestore;
	}

}