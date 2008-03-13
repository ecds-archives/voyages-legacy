package edu.emory.library.tast.common;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.images.site.ImagesBean;
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
		
		if (viewId.equals("/resources/images.jsp") || viewId.equals("/resources/images-query.jsp") ||
				viewId.equals("/resources/images-detail.jsp")) {
			ImagesBean bean = (ImagesBean) fc.getApplication().createValueBinding("#{ImagesBean}").getValue(fc);
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			
			if (viewId.equals("/resources/images-query.jsp") && params.containsKey("port")) {
				String id = (String) params.get("port");
				bean.restoreToPortId(new Long(id));
			} else if (viewId.equals("/resources/images-query.jsp") && params.containsKey("region")) {
				String id = (String) params.get("region");
				bean.restoreToRegionId(new Long(id));
			} else {			
				if (!params.containsKey("permlink")) {
					if ((bean.getImageId() == null && viewId.equals("/resources/images-detail.jsp")) ||
							(bean.getGalleryImages() == null && viewId.equals("/resources/images-query.jsp"))) {
						JsfUtils.navigateTo("images");
					}
				}
				String permlink = (String) params.get("permlink");
				if (StringUtils.isNullOrEmpty(permlink))
					return;			
				bean.restoreLink(new Long(permlink));
			}
		}
		
		if (viewId.equals("/database/voyage.jsp")) {
			VoyageDetailBean bean = (VoyageDetailBean) fc.getApplication().createValueBinding("#{VoyageDetailBean}").getValue(fc);
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			if (!params.containsKey("permlink")) {
				if (bean.getVoyageIid() == -1) {
					JsfUtils.navigateTo("database");
				}
				return;
			}

			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return;
			
			// bean.restoreLink(new Long(permlink));
		}
		
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}