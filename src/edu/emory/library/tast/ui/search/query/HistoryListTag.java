package edu.emory.library.tast.ui.search.query;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * JSP tag for {@link edu.emory.library.tast.ui.search.query.HistoryListComponent}. It does
 * the standard stuff as a typical JSP tag for a JSF component.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryListTag extends UIComponentTag
{
	
	private String history;
	private String onDelete;
	private String onRestore;
	private String onPermlink;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		HistoryListComponent historyList = (HistoryListComponent) component;
		
		if (history != null && isValueReference(history))
		{
			ValueBinding vb = app.createValueBinding(history);
			component.setValueBinding("history", vb);
		}
		
		if (onDelete != null && isValueReference(onDelete))
		{
			MethodBinding mb = app.createMethodBinding(onDelete, new Class[] {HistoryItemDeleteEvent.class});
			historyList.setOnDelete(mb);
		}

		if (onRestore != null && isValueReference(onRestore))
		{
			MethodBinding mb = app.createMethodBinding(onRestore, new Class[] {HistoryItemRestoreEvent.class});
			historyList.setOnRestore(mb);
		}

		if (onPermlink != null && isValueReference(onPermlink))
		{
			MethodBinding mb = app.createMethodBinding(onPermlink, new Class[] {HistoryItemPermlinkEvent.class});
			historyList.setOnPermlink(mb);
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

	public String getOnDelete()
	{
		return onDelete;
	}

	public void setOnDelete(String ondelete)
	{
		this.onDelete = ondelete;
	}

	public String getHistory()
	{
		return history;
	}

	public void setHistory(String list)
	{
		this.history = list;
	}

	public String getOnRestore()
	{
		return onRestore;
	}

	public void setOnRestore(String onrestore)
	{
		this.onRestore = onrestore;
	}

	public String getOnPermlink()
	{
		return onPermlink;
	}

	public void setOnPermlink(String onPermlink)
	{
		this.onPermlink = onPermlink;
	}

}