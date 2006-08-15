package edu.emory.library.tast.ui.search.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapData;
import edu.emory.library.tast.ui.maps.MapLayer;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.ui.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.DirectValue;
import edu.emory.library.tast.util.query.QueryValue;


/**
 * ./configure --with-proj --with-ogr --with-gdal --with-postgis=yes --with-threads --verbose=yes
 * 
 * @author juri
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 1;

	public static int PORT_ARRIVAL = 2;

	public static int PORT_BOTH = 3;

	private static final String MAP_OBJECT_ATTR_NAME = "__map__file_";

	/**
	 * Reference to Search bean.
	 */
	private SearchBean searchBean = null;

	/**
	 * Conditions used in query.
	 */
	private Conditions conditions = null;

	private boolean neededQuery = false;

	private MapFileCreator creator = new MapFileCreator();

	private String sessionParam;

	private List pointsOfInterest = new ArrayList();

	private MapData mapData = new MapData();

	private int chosenMap = 0;

	private void setMapData() {

		if (!this.searchBean.getSearchParameters().getConditions().equals(this.conditions)) {
			this.conditions = (Conditions) this.searchBean.getSearchParameters().getConditions().clone();
			neededQuery = true;
		}

		if (this.neededQuery) {

			this.pointsOfInterest.clear();
			GlobalMapQueryHolder queryHolder = new GlobalMapQueryHolder(this.conditions);
			queryHolder.executeQuery(this.chosenMap);
			
			GlobalMapDataTransformer transformer = new GlobalMapDataTransformer(queryHolder.getAttributesMap());					
			this.mapData.setMapData(queryHolder, transformer);
			
			this.neededQuery = false;
		}
	}

	public String getMapPath() {

		try {

			this.setMapData();
			AbstractMapItem[] items = this.mapData.getItems();

			if (items.length > 0) {
				this.creator.setMapData(items);
				this.creator.setMapLegend(this.mapData.getLegend());
			}
			if (this.creator.createMapFile()) {
				sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
				ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
				((HttpSession) servletContext.getSession(true)).setAttribute(sessionParam, creator.getFilePath());

			} else {
				return null;
			}

			return sessionParam;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
	
	public String refresh() {		
//		if (this.creator.createMapFile()) {
//			sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
//			ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
//			((HttpSession) servletContext.getSession(true)).setAttribute(sessionParam, creator.getFilePath());
//
//		}
		return null;
	}

	public PointOfInterest[] getPointsOfInterest() {
		
		if (this.mapData.getToolTip().length != 0) {
			System.out.println(this.mapData.getToolTip()[0].getX());
		}
		
		return this.mapData.getToolTip();
	}
	
	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
	public MapLayer[] getLayers() {
		return this.creator.getLayers();
	}
	
	public void setChosenMap(Integer value) {
		if (this.chosenMap != value.intValue()) {
			this.neededQuery = true;
		}
		this.chosenMap = value.intValue();
	}
	
	public Integer getChosenMap() {
		return new Integer(this.chosenMap);
	}
	
	public SelectItem[] getAvailableMaps() {
		String[] maps = GlobalMapQueryHolder.getAvailableQuerySets();
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(new Integer(i), maps[i]);
		}
		return items; 
	}
	
	public void setAvailableMaps(Object[] par) {}
	
//	public void setLayers(MapLayer[] layers) {
//		this.creator.setLayers(layers);
//	}
	
}
