/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.query;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * JSP tag for {@link edu.emory.library.tast.database.query.HistoryListComponent}. It does
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