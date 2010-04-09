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
package edu.emory.library.tast.common;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class GridOpenRowEvent extends FacesEvent
{
	
	private String rowId;

	private static final long serialVersionUID = -8046208212453289991L;

	public GridOpenRowEvent(UIComponent component, String rowId)
	{
		super(component);
		this.rowId = rowId;
		//this.setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	public boolean isAppropriateListener(FacesListener arg0)
	{
		return false;
	}

	public void processListener(FacesListener arg0)
	{
	}

	public String getRowId()
	{
		return rowId;
	}

	public void setRowId(String rowId)
	{
		this.rowId = rowId;
	}

}
