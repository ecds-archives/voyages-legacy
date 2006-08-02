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
	private String miniMap;
	private String miniMapPosition;
	private String miniMapWidth;
	private String miniMapHeight;

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

		if (miniMap != null && isValueReference(miniMap))
		{
			ValueBinding vb = app.createValueBinding(miniMap);
			component.setValueBinding("miniMap", vb);
		}
		else
		{
			map.setMiniMap("true".equalsIgnoreCase(miniMap));
		}

		if (miniMapPosition != null && isValueReference(miniMapPosition))
		{
			ValueBinding vb = app.createValueBinding(miniMapPosition);
			component.setValueBinding("miniMapPosition", vb);
		}
		else
		{
			map.setMiniMapPosition(MiniMapPosition.parse(miniMapPosition));
		}

		if (miniMapWidth != null && isValueReference(miniMapWidth))
		{
			ValueBinding vb = app.createValueBinding(miniMapWidth);
			component.setValueBinding("miniMapWidth", vb);
		}
		else
		{
			try
			{
				map.setMiniMapWidth(Integer.parseInt(miniMapWidth));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (miniMapHeight != null && isValueReference(miniMapHeight))
		{
			ValueBinding vb = app.createValueBinding(miniMapHeight);
			component.setValueBinding("miniMapHeight", vb);
		}
		else
		{
			try
			{
				map.setMiniMapHeight(Integer.parseInt(miniMapHeight));
			}
			catch (NumberFormatException nfe)
			{
			}
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

	public String getMiniMap()
	{
		return miniMap;
	}

	public void setMiniMap(String miniMap)
	{
		this.miniMap = miniMap;
	}

	public String getMiniMapHeight()
	{
		return miniMapHeight;
	}

	public String getMiniMapPosition()
	{
		return miniMapPosition;
	}

	public String getMiniMapWidth()
	{
		return miniMapWidth;
	}

	public void setMiniMapHeight(String miniMapHeight)
	{
		this.miniMapHeight = miniMapHeight;
	}

	public void setMiniMapPosition(String miniMapPosition)
	{
		this.miniMapPosition = miniMapPosition;
	}

	public void setMiniMapWidth(String miniMapWidth)
	{
		this.miniMapWidth = miniMapWidth;
	}

}
