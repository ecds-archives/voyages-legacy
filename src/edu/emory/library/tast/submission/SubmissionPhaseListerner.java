package edu.emory.library.tast.submission;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.emory.library.tast.util.JsfUtils;

public class SubmissionPhaseListerner implements PhaseListener
{

	private static final long serialVersionUID = -8850865315549962662L;

	public void afterPhase(PhaseEvent event)
	{
	}

	public void beforePhase(PhaseEvent event)
	{
		
		FacesContext fc = event.getFacesContext();
		String viewId = fc.getViewRoot().getViewId();
		
		SubmissionBean bean = (SubmissionBean) fc.getApplication().createValueBinding("#{SubmissionBean}").getValue(fc);
		
		if (viewId.equals("/submission/submission-select-voyage-edit.jsp"))
		{
			if (!bean.isStateValidForSelectVoyageForEdit())
				JsfUtils.navigateTo("start");
		}
		
		else if (viewId.equals("/submission/submission-select-voyage-merge.jsp"))
		{
			if (!bean.isStateValidForSelectVoyagesForMerge())
				JsfUtils.navigateTo("start");
		}
		
		else if (viewId.equals("/submission/submission-form.jsp"))
		{
			if (!bean.isStateValidForForm())
				JsfUtils.navigateTo("start");
		}

	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}