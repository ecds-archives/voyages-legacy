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

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * An event which is fired when a user clicks on the restore button in the
 * history list. The event holds the history ID of the history item which should
 * be restore from the history.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryItemRestoreEvent extends FacesEvent
{

	private static final long serialVersionUID = 4734979705211084907L;
	
	private String historyId;

	public HistoryItemRestoreEvent(UIComponent uiComponent, String historyId)
	{
		super(uiComponent);
		this.historyId = historyId;
		this.setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getHistoryId()
	{
		return historyId;
	}

	public void setHistoryId(String historyId)
	{
		this.historyId = historyId;
	}

}
