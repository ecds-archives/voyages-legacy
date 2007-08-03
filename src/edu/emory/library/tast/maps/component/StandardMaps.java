package edu.emory.library.tast.maps.component;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import edu.emory.library.tast.estimates.map.EstimatesMapBean;

public class StandardMaps
{
	
	public static class MapIdent {
		public String mapPath;
		public String mapLabel;
		public int yearFrom;
		public int yearTo;
		public ZoomLevel levels[];
		
		private MapIdent(String path, String label, int yearFrom, int yearTo, ZoomLevel[] levels) {
			this.mapLabel = label;
			this.mapPath = path;
			this.yearFrom = yearFrom;
			this.yearTo = yearTo;
			this.levels = levels;
		}		
	}
	
	private static String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	private static ZoomLevel levelMini = new ZoomLevel(160, 120, -110.83, -61.83, 1, 1, 1.0/0.95, contextPath + "/map-assets/tiles/minimap");
	private static ZoomLevel levelGeo20 = new ZoomLevel(160, 120, -110.79, -61.85, 5, 5, 1.0/4.69, contextPath + "/map-assets/tiles/20");
	private static ZoomLevel levelGeo3 = new ZoomLevel(160, 120, -110.83, -61.83, 32, 32, 1.0/30.0,	contextPath + "/map-assets/tiles/geo/regions/3"); 
	private static ZoomLevel levelGeo3_1 = new ZoomLevel(160, 120, -110.83, -61.83, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/geo/regions/1");
	private static ZoomLevel levelGeo1 = new ZoomLevel(160, 120, -110.79, -61.85, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/geo/ports/1");
	private static ZoomLevel level1650_3 = new ZoomLevel(160, 120, -110.83, -61.83, 32, 32, 1.0/30.0,	contextPath + "/map-assets/tiles/1650/regions/3"); 
	private static ZoomLevel level1650_3_1 = new ZoomLevel(160, 120, -110.83, -61.83, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1650/regions/1");
	private static ZoomLevel level1650_1 = new ZoomLevel(160, 120, -110.79, -61.85, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1650/ports/1"); 
	private static ZoomLevel level1750_3 = new ZoomLevel(160, 120, -110.83, -61.83, 32, 32, 1.0/30.0,	contextPath + "/map-assets/tiles/1750/regions/3");
	private static ZoomLevel level1750_3_1 = new ZoomLevel(160, 120, -110.83, -61.83, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1750/regions/1");
	private static ZoomLevel level1750_1 = new ZoomLevel(160, 120, -110.79, -61.85, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1750/ports/1");;
	private static ZoomLevel level1850_3 = new ZoomLevel(160, 120, -110.83, -61.83, 32, 32, 1.0/30.0,	contextPath + "/map-assets/tiles/1850/regions/3"); 
	private static ZoomLevel level1850_3_1 = new ZoomLevel(160, 120, -110.83, -61.83, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1850/regions/1");
	private static ZoomLevel level1850_1 = new ZoomLevel(160, 120, -110.79, -61.85, 64, 64, 1.0/60.0,	contextPath + "/map-assets/tiles/1850/ports/1");;
	
	private static final MapIdent[] mapsEstimates = new MapIdent[] {
		new MapIdent("geophysical", "Geophysical maps", 1501, 1867, new ZoomLevel[] {levelGeo20, levelGeo3, levelGeo3_1}),
		new MapIdent("h_1650", "Historical maps: 1650", 1501, 1641, new ZoomLevel[] {levelGeo20, level1650_3, level1650_3_1}),
		new MapIdent("h_1750", "Historical maps: 1750", 1642, 1807, new ZoomLevel[] {levelGeo20, level1750_3, level1750_3_1}),
		new MapIdent("h_1850", "Historical maps: 1850", 1808, 1867, new ZoomLevel[] {levelGeo20, level1850_3, level1850_3_1})		
	};
	
	private static final MapIdent[] mapsDatabase = new MapIdent[] {
		new MapIdent("geophysical", "Geophysical maps", 1501, 1867, new ZoomLevel[] {levelGeo20, levelGeo3, levelGeo1}),
		new MapIdent("h_1650", "Historical maps: 1650", 1501, 1641, new ZoomLevel[] {levelGeo20, level1650_3, level1650_1}),
		new MapIdent("h_1750", "Historical maps: 1750", 1642, 1807, new ZoomLevel[] {levelGeo20, level1750_3, level1750_1}),
		new MapIdent("h_1850", "Historical maps: 1850", 1808, 1867, new ZoomLevel[] {levelGeo20, level1850_3, level1850_1})
	};
	
	private static Map chosenMaps = new HashMap();
	
	public static ZoomLevel[] getZoomLevels(Object key)
	{			
		MapIdent chosenMap = getSelectedMap(key);
		return chosenMap.levels;
	}
	
	public static ZoomLevel getMiniMapZoomLevel(Object key)
	{
		return levelMini;

	}
	
	public static SelectItem[] getMapTypes(Object key) {
		MapIdent[] maps = null;
		if (key instanceof EstimatesMapBean) {
			maps = mapsEstimates;
		} else {
			maps = mapsDatabase;
		}
		
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(maps[i].mapPath, maps[i].mapLabel);
		}
		return items;
	}
	
	public static void setSelectedMapType(Object key, String selectedMap) {
		MapIdent[] maps = null;
		if (key instanceof EstimatesMapBean) {
			maps = mapsEstimates;
		} else {
			maps = mapsDatabase;
		}
		
		for (int i = 0; i < maps.length; i++) {
			if (maps[i].mapPath.equals(selectedMap)) {
				chosenMaps.put(key, maps[i]);
			}
		}
	}
	
	public static MapIdent getSelectedMap(Object key) {
		MapIdent[] maps = null;
		if (key instanceof EstimatesMapBean) {
			maps = mapsEstimates;
		} else {
			maps = mapsDatabase;
		}
		
		if (!chosenMaps.containsKey(key)) {
			chosenMaps.put(key, maps[0]);
		}
		return (MapIdent) chosenMaps.get(key);
	}

}
