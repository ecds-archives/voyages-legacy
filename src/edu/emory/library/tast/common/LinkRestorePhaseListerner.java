package edu.emory.library.tast.common;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.slaves.SlavesBean;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

/**
 * This class is responsible for handling permanent link restore feature.
 * It also checks correctness of requests for some cases
 *
 */
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
		
		if (viewId.equals("/resources/slaves.jsp"))
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			
			if (!params.containsKey("permlink"))
				return;

			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return;
			
			SlavesBean bean = (SlavesBean) fc.getApplication().createValueBinding("#{SlavesBean}").getValue(fc);
			bean.restoreLink(new Long(permlink));
			
		}
		
		if (viewId.equals("/assessment/estimates.jsp"))
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			
			if (!params.containsKey("module"))
				return;

			String module = (String) params.get("module");
			if (StringUtils.isNullOrEmpty(module))
				return;
			
			EstimatesSelectionBean bean = (EstimatesSelectionBean) fc.getApplication().createValueBinding("#{EstimatesSelectionBean}").getValue(fc);
			bean.setSelectedTab(module);
			
		}

//		if (viewId.equals("/resources/images.jsp") || viewId.equals("/resources/images-query.jsp") ||
//				viewId.equals("/resources/images-detail.jsp")) {
//			ImagesBean bean = (ImagesBean) fc.getApplication().createValueBinding("#{ImagesBean}").getValue(fc);
//			FacesContext context = FacesContext.getCurrentInstance();
//			Map params = context.getExternalContext().getRequestParameterMap();
//			
//			if (viewId.equals("/resources/images-query.jsp") && params.containsKey("port")) {
//				String id = (String) params.get("port");
//				bean.restoreToPortId(new Long(id));
//			} else if (viewId.equals("/resources/images-query.jsp") && params.containsKey("region")) {
//				String id = (String) params.get("region");
//				bean.restoreToRegionId(new Long(id));
//			} else {			
//				if (!params.containsKey("permlink")) {
//					if ((bean.getImageId() == null && viewId.equals("/resources/images-detail.jsp")) ||
//							(bean.getGalleryImages() == null && viewId.equals("/resources/images-query.jsp"))) {
//						JsfUtils.navigateTo("images");
//					}
//				}
//				String permlink = (String) params.get("permlink");
//				if (StringUtils.isNullOrEmpty(permlink))
//					return;			
//				bean.restoreLink(new Long(permlink));
//			}
//		}
		
		if (viewId.equals("/database/voyage.jsp"))
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			String permlink = (String) params.get("permlink");

			// go to default search if no permlink
			if (StringUtils.isNullOrEmpty(permlink))
			{
				VoyageDetailBean bean = (VoyageDetailBean) fc.getApplication().createValueBinding("#{VoyageDetailBean}").getValue(fc);
				if (bean.getVoyageIid() == -1) JsfUtils.navigateTo("database");
				return;
			}
			
		}
		
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}