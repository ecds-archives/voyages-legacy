package edu.emory.library.tast;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
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
		
		if (!JsfUtils.isGetRequest(fc))
			return;
		
		if (viewId.equals("/database/search.jsp"))
		{
			
			Map params = fc.getExternalContext().getRequestParameterValuesMap();
			
			if (params.size() != 0)
			{
				SearchBean bean = (SearchBean) JsfUtils.getSessionBean(fc, "SearchBean");
				bean.restoreQueryFromUrl(params);
			}
			
		}

		else if (viewId.equals("/resources/slaves.jsp"))
		{
			
			Map params = fc.getExternalContext().getRequestParameterMap();
			
			String permlink = (String) params.get("permlink");
			if (StringUtils.isNullOrEmpty(permlink))
				return;
			
			SlavesBean bean = (SlavesBean) JsfUtils.getSessionBean(fc, "SlavesBean");
			bean.restoreLink(permlink);
			
		}
		
		else if (viewId.equals("/assessment/estimates.jsp"))
		{
			
			Map params = fc.getExternalContext().getRequestParameterMap();
			
			String module = (String) params.get("module");
			if (StringUtils.isNullOrEmpty(module))
				return;
			
			EstimatesSelectionBean bean = (EstimatesSelectionBean) JsfUtils.getSessionBean(fc, "EstimatesSelectionBean");
			bean.setSelectedTab(module);
			
		}
		
		else if (viewId.equals("/resources/images-detail.jsp"))
		{
			Map params = fc.getExternalContext().getRequestParameterMap();
			
			String image = (String) params.get("image");
			if (StringUtils.isNullOrEmpty(image))
				return;

			ImagesBean bean = (ImagesBean) JsfUtils.getSessionBean(fc, "ImagesBean");
			bean.openImageFromUrl(image);
			
		}
		
		else if (viewId.equals("/database/voyage.jsp"))
		{
			
			Map params = fc.getExternalContext().getRequestParameterMap();

			// go to database search if no link
			String voyageId = (String) params.get("voyageId");
			if (StringUtils.isNullOrEmpty(voyageId))
			{
				// JsfUtils.navigateTo("database");
				return;
			}
			
			// convert to an integer, again fall back to db if nonsense
			int voyageIdInt = 0;
			try
			{
				voyageIdInt = Integer.parseInt(voyageId);
			}
			catch (NumberFormatException nfe)
			{
				// JsfUtils.navigateTo("database");
				return;
			}
			
			// and open pretending that we have came from db search
			VoyageDetailBean bean = (VoyageDetailBean) JsfUtils.getSessionBean(fc, "VoyageDetailBean");
			bean.setPreviousView("search-interface");
			bean.setSelectedTab("variables");
			bean.openVoyageByVoyageId(voyageIdInt);
			
		}
		
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}