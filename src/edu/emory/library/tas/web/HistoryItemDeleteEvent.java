package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * An event which is fired when a user clicks on the delete button in the
 * history list. The event holds the history ID of the history item to be
 * deleted.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryItemDeleteEvent extends FacesEvent
{
	
	private static final long serialVersionUID = 7525094898372642047L;

	private String historyId;

	public HistoryItemDeleteEvent(UIComponent uiComponent, String deleteId)
	{
		super(uiComponent);
		this.historyId = deleteId;
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

	public void setHistoryId(String deleteId)
	{
		this.historyId = deleteId;
	}

}
