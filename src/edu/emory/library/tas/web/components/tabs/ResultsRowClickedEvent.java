package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class ResultsRowClickedEvent extends FacesEvent
{

	private Long voyageId;
	
	private static final long serialVersionUID = -6310479443922753685L;

	public ResultsRowClickedEvent(UIComponent component, Long voyageId) {
		super(component);
		this.voyageId = voyageId;
		setPhaseId(PhaseId.INVOKE_APPLICATION);
	}

	public boolean isAppropriateListener(FacesListener faceslistener)
	{
		return false;
	}

	public void processListener(FacesListener faceslistener)
	{
	}

	public Long getVoyageId()
	{
		return voyageId;
	}

	public void setVoyageId(Long voyageId)
	{
		this.voyageId = voyageId;
	}

}
