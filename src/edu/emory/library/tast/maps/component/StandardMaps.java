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
						-110.83, -61.83,
						7, 7,
						1.0/6.54,
						contextPath + "/map-assets/tiles/all_102.png_tiles"),
				new ZoomLevel(
						160, 120,
						-110.83, -61.83,
						32, 32,
						1.0/30.0,
						contextPath + "/map-assets/tiles/1650_467.png_tiles"),
				new ZoomLevel(
						160, 120,				
						-110.79, -61.85,
						64, 64,
						1.0/60.0,
						contextPath + "/map-assets/tiles/1650_935.png_tiles") };
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
