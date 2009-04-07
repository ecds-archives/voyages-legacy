package edu.emory.library.tast.common.grideditor;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class ColumnActionEvent extends FacesEvent
{
	
	private String columnName;
	private String actionName;

	public ColumnActionEvent(UIComponent uiComponent, String columnName, String actionName)
	{
		super(uiComponent);
		this.columnName = columnName;
		this.actionName = actionName;
		setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	private static final long serialVersionUID = -2925923321936293553L;

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public String getActionName()
	{
		return actionName;
	}

	public String getColumnName()
	{
		return columnName;
	}

}
