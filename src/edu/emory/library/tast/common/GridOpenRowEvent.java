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
