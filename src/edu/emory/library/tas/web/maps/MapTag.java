package edu.emory.library.tas.web.maps;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class MapTag extends UIComponentTag
{
	
	private String mapFile;
	private String serverBaseUrl;
	private String pointsOfInterest;

	public String getComponentType()
	{
		return "Map";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		MapComponent map = (MapComponent) component;
		
		if (mapFile != null && isValueReference(mapFile))
		{
			ValueBinding vb = app.createValueBinding(mapFile);
			component.setValueBinding("mapFile", vb);
		}
		else
		{
			map.setMapFile(mapFile);
		}
		
		if (serverBaseUrl != null && isValueReference(serverBaseUrl))
		{
			ValueBinding vb = app.createValueBinding(serverBaseUrl);
			component.setValueBinding("serverBaseUrl", vb);
		}
		else
		{
			map.setServerBaseUrl(serverBaseUrl);
		}
		
		if (pointsOfInterest != null && isValueReference(pointsOfInterest))
		{
			ValueBinding vb = app.createValueBinding(pointsOfInterest);
			component.setValueBinding("pointsOfInterest", vb);
		}

	}

	public String getMapFile()
	{
		return mapFile;
	}

	public void setMapFile(String mapFile)
	{
		this.mapFile = mapFile;
	}

	public String getServerBaseUrl()
	{
		return serverBaseUrl;
	}

	public void setServerBaseUrl(String serverBaseUrl)
	{
		this.serverBaseUrl = serverBaseUrl;
	}

	public String getPointsOfInterest()
	{
		return pointsOfInterest;
	}

	public void setPointsOfInterest(String pointsOfInterest)
	{
		this.pointsOfInterest = pointsOfInterest;
	}

}
