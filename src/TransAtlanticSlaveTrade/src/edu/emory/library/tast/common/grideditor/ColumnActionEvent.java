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
package edu.emory.library.tast.common.grideditor;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class ColumnActionEvent extends FacesEvent
{
	
	private String columnName;
	private String actionName;

	public ColumnActionEvent(UIComponent uiComponent, String columnName, String actionName)
	{
		super(uiComponent);
		this.columnName = columnName;
		this.actionName = actionName;
		setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	private static final long serialVersionUID = -2925923321936293553L;

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getActionName()
	{
		return actionName;
	}

	public String getColumnName()
	{
		return columnName;
	}

}
