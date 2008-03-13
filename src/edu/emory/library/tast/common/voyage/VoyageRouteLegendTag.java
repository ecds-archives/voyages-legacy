package edu.emory.library.tast.common.voyage;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class VoyageRouteLegendTag extends UIComponentTag
{
	
	private String route;

	public String getComponentType()
	{
		return "VoyageRouteLegend";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		VoyageRouteLegendComponent voyageRouteLegend = (VoyageRouteLegendComponent) component;
		
		if (route != null && isValueReference(route))
		{
			ValueBinding vb = app.createValueBinding(route);
			voyageRouteLegend.setValueBinding("route", vb);
		}
		
	}

	public String getRoute()
	{
		return route;
	}

	public void setRoute(String route)
	{
		this.route = route;
	}

}
