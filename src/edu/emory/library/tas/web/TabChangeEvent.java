package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class TabChangeEvent extends FacesEvent
{

	private static final long serialVersionUID = -1216486761966348663L;
	
	private String tabId;

	public TabChangeEvent(UIComponent uiComponent, String tabId)
	{
		super(uiComponent);
		this.tabId = tabId;
	}

	public TabChangeEvent(UIComponent uiComponent)
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
