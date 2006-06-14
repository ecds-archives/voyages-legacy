package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class HistoryItemDeleteEvent extends FacesEvent
{
	
	private static final long serialVersionUID = 7525094898372642047L;

	private String deleteId;

	public HistoryItemDeleteEvent(UIComponent uiComponent, String deleteId)
	{
		super(uiComponent);
		this.deleteId = deleteId;
	}

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getDeleteId()
	{
		return deleteId;
	}

	public void setDeleteId(String deleteId)
	{
		this.deleteId = deleteId;
	}

}
