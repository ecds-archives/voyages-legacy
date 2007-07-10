package edu.emory.library.tast.maps.component;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class StandardMaps
{
	
	private static final String[] maps = new String[] {
			"all/ports/",
			"all/regions/",
			"1650/ports/",
			"1650/regions/",
			"1750/ports/",
			"1750/regions/",
			"1850/ports/",
			"1850/regions/"
	};
	
	private static final String[] labels = new String[] {
			"Ports",
			"Regions",
			"Ports, historical map (1650)", 
			"Regions, historical map (1650)", 
			"Ports, historical map (1750)", 
			"Regions, historical map (1750)", 
			"Ports, historical map (1850)", 
			"Regions, historical map (1850)"
	};
	
	private static String chosenMap = "all/ports/";
	
	public static ZoomLevel[] getZoomLevels()
	{
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		return new ZoomLevel[] {
				new ZoomLevel(
						160, 120,
						-110.83, -61.83,
						7, 7,
						1.0/6.54,
						contextPath + "/map-assets/tiles/" + chosenMap + "05"),
				new ZoomLevel(
						160, 120,
						-110.83, -61.83,
						32, 32,
						1.0/30.0,
						contextPath + "/map-assets/tiles/" + chosenMap + "25"),
				new ZoomLevel(
						160, 120,				
						-110.79, -61.85,
						64, 64,
						1.0/60.0,
						contextPath + "/map-assets/tiles/" + chosenMap + "50") };
	}
	
	public static ZoomLevel getMiniMapZoomLevel()
	{
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
		return new ZoomLevel(
				160, 120,
				-110.83, -61.83,
				7, 7,
				1.0/6.54,
				contextPath + "/map-assets/tiles/" + chosenMap + "05");
		
//		return new ZoomLevel(
//			160, 120,
//			-105.89815, -44.2191667,
//			5, 4,
//			1.0/5.0,
//			contextPath + "/map-assets/tiles/" + chosenMap + "05");

	}
	
	public static SelectItem[] getMapTypes() {
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(maps[i], labels[i]);
		}
		return items;
	}
	
	public static void setSelectedMapType(String selectedMap) {
		chosenMap = selectedMap;
	}
	
	public static String getSelectedMap() {
		return chosenMap;
	}

}
