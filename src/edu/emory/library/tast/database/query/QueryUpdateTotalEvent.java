package edu.emory.library.tast.database.query;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class QueryUpdateTotalEvent extends FacesEvent
{

	private static final long serialVersionUID = 7987387019490390399L;

	public QueryUpdateTotalEvent(UIComponent component)
	{
		super(component);
		setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	public boolean isAppropriateListener(FacesListener arg0)
	{
		return false;
	}

	public void processListener(FacesListener arg0)
	{
	}

}
