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
		AdminSubmissionBean adminBean = (AdminSubmissionBean) fc.getApplication().createValueBinding("#{AdminSubmissionBean}").getValue(fc);
		System.out.println("View: " + viewId);
		if (viewId.equals("/submission/submission-select-voyage-edit.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!bean.isStateValidForSelectVoyageForEdit())
				JsfUtils.navigateTo("start");
		}
		
		else if (viewId.equals("/submission/submission-select-voyage-merge.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!bean.isStateValidForSelectVoyagesForMerge())
				JsfUtils.navigateTo("start");
		}
		
		else if (viewId.equals("/submission/submission-form.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!bean.isStateValidForForm())
				JsfUtils.navigateTo("start");
		}
		else if (viewId.equals("/submission/submission-type.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
		}
		else if (viewId.equals("/submission/submission-admin.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
		}
		else if (viewId.equals("/submission/submission-admin-resolve.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!adminBean.isResolveValid()) {
				JsfUtils.navigateTo("main-menu");
			}
		}
		else if (viewId.equals("/submission/submission-admin-approve-delete.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
		}
		else if (viewId.equals("/submission/submission-admin-approve-delete-voyage.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!adminBean.isDeleteValid()) {
				JsfUtils.navigateTo("main-menu");
			}
		}
		else if (viewId.equals("/admin/edit.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!adminBean.isEditValid()) {
				JsfUtils.navigateTo("back");
			}
		}
		else if (viewId.equals("/submission/submission-admin-user.jsp"))
		{
			if (adminBean.getAuthenticateduser() == null) {
				JsfUtils.navigateTo("login");
			}
		}
		else if (viewId.equals("/submission/submission-sources.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
		}
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}

}