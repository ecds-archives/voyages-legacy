package edu.emory.library.tast.common;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class TabChangedEvent extends FacesEvent
{

	private static final long serialVersionUID = -1216486761966348663L;
	
	private String tabId;

	public TabChangedEvent(UIComponent uiComponent, String tabId)
	{
		super(uiComponent);
		this.tabId = tabId;
		this.setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	public TabChangedEvent(UIComponent uiComponent)
	{
		super(uiComponent);
	}

	public String getTabId()
	{
		return tabId;
	}

	public void setTabId(String tabId)
	{
		this.tabId = tabId;
	}

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

}
