package edu.emory.library.tast.maps.component;

import javax.faces.context.FacesContext;

public class StandardMaps
{
	
	public static ZoomLevel[] getZoomLevels()
	{
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		return new ZoomLevel[] {
				new ZoomLevel(
						160, 120,
						-105.89815, -44.2191667,
						5, 4,
						1.0/5.0,
						contextPath + "/map-assets/tiles/05"),
				new ZoomLevel(
						160, 120,
						-111.93815, -53.6591667,
						27, 24,
						1.0/25.0,
						contextPath + "/map-assets/tiles/25"),
				new ZoomLevel(
						160, 120,
						-111.91815, -54.9591667,
						54, 49,
						1.0/50.0,
						contextPath + "/map-assets/tiles/50") };
	}
	
	public static ZoomLevel getMiniMapZoomLevel()
	{
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		return new ZoomLevel(
			160, 120,
			-105.89815, -44.2191667,
			5, 4,
			1.0/5.0,
			contextPath + "/map-assets/tiles/05");

	}

}
