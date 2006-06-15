package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class HistoryItemRestoreEvent extends FacesEvent
{

	private static final long serialVersionUID = 4734979705211084907L;
	
	private String historyId;

	public HistoryItemRestoreEvent(UIComponent uiComponent, String historyId)
	{
		super(uiComponent);
		this.historyId = historyId;
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
