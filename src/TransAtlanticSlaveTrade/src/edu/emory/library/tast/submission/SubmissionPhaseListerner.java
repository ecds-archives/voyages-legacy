/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
		if (viewId.equals("/submission/submission-select-voyage-edit.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!bean.isStateValidForSelectVoyageForEdit())
				JsfUtils.navigateTo("start");
		}
		
		else if (viewId.equals("/submission/submission-select-voyages-merge.jsp"))
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
		else if (viewId.equals("/submission/submission-select-voyage-edit.jsp"))
		{
			if (bean.getAuthenticatedUser() == null) {
				JsfUtils.navigateTo("login");
			}
			if (!bean.isStateValidForForm())
				JsfUtils.navigateTo("start");
		}
		else if (viewId.equals("/submission/submission-select-voyages-merge.jsp"))
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
		else if (viewId.equals("/submission/submission-verify.jsp"))
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
		else if (viewId.equals("/submission/submission-thankyou.jsp"))
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