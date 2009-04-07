package edu.emory.library.tast.common;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class GridColumnClickEvent extends FacesEvent
{
	
	private String columnName;

	public GridColumnClickEvent(UIComponent uiComponent, String columnName)
	{
		super(uiComponent);
		this.columnName = columnName;
		this.setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	private static final long serialVersionUID = -6358835994078452257L;

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

}