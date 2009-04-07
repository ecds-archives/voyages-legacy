package edu.emory.library.tast.maps.component;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class MapTag extends UIComponentTag
{
	
	private String zoomLevels;
	private String pointsOfInterest;
	private String lines;
	private String miniMap;
	private String miniMapZoomLevel;
	private String miniMapPosition;
	private String miniMapWidth;
	private String miniMapHeight;
	private String pointsSelectId;
	private String zoomHistory;
	private String x1;
	private String y1;
	private String x2;
	private String y2;
	
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
		
		if (zoomLevels != null && isValueReference(zoomLevels))
		{
			ValueBinding vb = app.createValueBinding(zoomLevels);
			component.setValueBinding("zoomLevels", vb);
		}
		
		if (pointsOfInterest != null && isValueReference(pointsOfInterest))
		{
			ValueBinding vb = app.createValueBinding(pointsOfInterest);
			component.setValueBinding("pointsOfInterest", vb);
		}

		if (lines != null && isValueReference(lines))
		{
			ValueBinding vb = app.createValueBinding(lines);
			component.setValueBinding("lines", vb);
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
		
		if (miniMapZoomLevel != null && isValueReference(miniMapZoomLevel))
		{
			ValueBinding vb = app.createValueBinding(miniMapZoomLevel);
			component.setValueBinding("miniMapZoomLevel", vb);
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
			map.setMiniMapWidth(Integer.parseInt(miniMapWidth));
		}

		if (miniMapHeight != null && isValueReference(miniMapHeight))
		{
			ValueBinding vb = app.createValueBinding(miniMapHeight);
			component.setValueBinding("miniMapHeight", vb);
		}
		else
		{
			map.setMiniMapHeight(Integer.parseInt(miniMapHeight));
		}
		
		if (pointsSelectId != null && isValueReference(pointsSelectId))
		{
			ValueBinding vb = app.createValueBinding(pointsSelectId);
			component.setValueBinding("pointsSelectId", vb);
		}
		else
		{
			map.setPointsSelectId(pointsSelectId);
		}
		
		if (zoomHistory != null && isValueReference(zoomHistory))
		{
			ValueBinding vb = app.createValueBinding(zoomHistory);
			component.setValueBinding("zoomHistory", vb);
		}

		if (x1 != null && isValueReference(x1))
		{
			ValueBinding vb = app.createValueBinding(x1);
			component.setValueBinding("x1", vb);
		}
		else if (x1 != null)
		{
			map.setX1(Double.parseDouble(x1));
		}
		
		if (x2 != null && isValueReference(x2))
		{
			ValueBinding vb = app.createValueBinding(x2);
			component.setValueBinding("x2", vb);
		}
		else if (x2 != null)
		{
			map.setX2(Double.parseDouble(x2));
		}

		if (y1 != null && isValueReference(y1))
		{
			ValueBinding vb = app.createValueBinding(y1);
			component.setValueBinding("y1", vb);
		}
		else if (y1 != null)
		{
			map.setY1(Double.parseDouble(y1));
		}
		
		if (y2 != null && isValueReference(x2))
		{
			ValueBinding vb = app.createValueBinding(y2);
			component.setValueBinding("y2", vb);
		}
		else if (y2 != null)
		{
			map.setY2(Double.parseDouble(y2));
		}

	}

	public String getZoomLevels()
	{
		return zoomLevels;
	}

	public void setZoomLevels(String zoomLevels)
	{
		this.zoomLevels = zoomLevels;
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

	public String getMiniMapZoomLevel()
	{
		return miniMapZoomLevel;
	}

	public void setMiniMapZoomLevel(String miniMapZoomLevel)
	{
		this.miniMapZoomLevel = miniMapZoomLevel;
	}

	public String getLines()
	{
		return lines;
	}

	public void setLines(String lines)
	{
		this.lines = lines;
	}

	public String getPointsSelectId()
	{
		return pointsSelectId;
	}

	public void setPointsSelectId(String placesSelectId)
	{
		this.pointsSelectId = placesSelectId;
	}

	public String getZoomHistory()
	{
		return zoomHistory;
	}

	public void setZoomHistory(String zoomHistory)
	{
		this.zoomHistory = zoomHistory;
	}

	public String getX1()
	{
		return x1;
	}

	public void setX1(String x1)
	{
		this.x1 = x1;
	}

	public String getY1()
	{
		return y1;
	}

	public void setY1(String y1)
	{
		this.y1 = y1;
	}

	public String getX2()
	{
		return x2;
	}

	public void setX2(String x2)
	{
		this.x2 = x2;
	}

	public String getY2()
	{
		return y2;
	}

	public void setY2(String y2)
	{
		this.y2 = y2;
	}

}
