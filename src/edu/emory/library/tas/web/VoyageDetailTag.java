package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class VoyageDetailTag extends UIComponentTag
{
	
	private String voyageId;
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		VoyageDetailComponent detail = (VoyageDetailComponent) component;
		
		if (voyageId != null && isValueReference(voyageId))
		{
			ValueBinding vb = app.createValueBinding(voyageId);
			component.setValueBinding("voyageId", vb);
		}
		else
		{
			detail.setVoyageId(new Long(voyageId));
		}
	
	}

	public String getComponentType()
	{
		return "VoyageDetail";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getVoyageId()
	{
		return voyageId;
	}

	public void setVoyageId(String voyageId)
	{
		this.voyageId = voyageId;
	}

}
