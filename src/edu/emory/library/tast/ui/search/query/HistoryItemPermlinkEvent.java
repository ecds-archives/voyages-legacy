package edu.emory.library.tast.ui.search.query;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * An event which is fired when a user clicks on the create permlink button in
 * the history list. The event holds the history ID of the history item to which
 * a permlink should be created.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryItemPermlinkEvent extends FacesEvent
{

	private static final long serialVersionUID = 2680863288674553503L;
	
	private String historyId;
	
	public HistoryItemPermlinkEvent(UIComponent uiComponent, String historyId)
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
