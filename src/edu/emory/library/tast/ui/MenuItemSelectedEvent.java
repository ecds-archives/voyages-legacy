package edu.emory.library.tast.ui;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class MenuItemSelectedEvent extends FacesEvent
{
	
	private String menuId;

	public MenuItemSelectedEvent(UIComponent uiComponent, String menuId)
	{
		super(uiComponent);
		this.menuId = menuId;
		this.setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	private static final long serialVersionUID = -5152126665565544584L;

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getMenuId()
	{
		return menuId;
	}

	public void setMenuId(String menuId)
	{
		this.menuId = menuId;
	}

}
