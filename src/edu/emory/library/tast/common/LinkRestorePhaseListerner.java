package edu.emory.library.tast.common;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.emory.library.tast.images.site.ImagesBean;
import edu.emory.library.tast.slaves.SlavesBean;
import edu.emory.library.tast.util.StringUtils;

public class LinkRestorePhaseListerner implements PhaseListener
{

	private static final long serialVersionUID = -8850865315549962662L;

	public void afterPhase(PhaseEvent event)
	{
	}

	public void beforePhase(PhaseEvent event)
	{
		
		FacesContext fc = event.getFacesContext();
		String viewId = fc.getViewRoot().getViewId();
		
		System.out.println("View [in LinkRestore]: " + viewId);
		if (viewId.equals("/resources/slaves.jsp")) {
			SlavesBean bean = (SlavesBean) fc.getApplication().createValueBinding("#{SlavesBean}").getValue(fc);
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			if (!params.containsKey("permlink"))
				return;

			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return;
			
			bean.restoreLink(new Long(permlink));
		}
		
		if (viewId.equals("/resources/images.jsp") || viewId.equals("/resources/images-query.jsp")) {
			ImagesBean bean = (ImagesBean) fc.getApplication().createValueBinding("#{ImagesBean}").getValue(fc);
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			if (!params.containsKey("permlink"))
				return;

			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return;
			
			bean.restoreLink(new Long(permlink));
		}
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}