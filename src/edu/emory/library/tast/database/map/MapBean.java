package edu.emory.library.tast.database.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.database.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.database.query.SearchParameters;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.MapLayer;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.util.query.Conditions;

/**
 * The bean provides support for map tab in the database part of the system. It is used
 * in the database/search-tab-map.jsp page. This bean is connected with SearchBean which provides
 * current query that should be used when retrieving data from database. Data retrieved from dababase
 * are mainly ports/regions with number of slaves embarked/disembarked. The data later is shown in
 * map.
 * The main functionalities of this bean include: 
 * 1) provides a path that can be used to obtain map or mini-map tail.  
 * 2) reacts to changes on map and refreshes map.
 * 
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 2;

	public static int PORT_ARRIVAL = 3;

	public static int PORT_BOTH = 5;

	private static final String[] MAPS = new String[] { "Places", "Regions" };
	
	private static final String MAP_OBJECT_ATTR_NAME = "__map__file_";

	/**
	 * Reference to Search bean.
	 */
	private SearchBean searchBean = null;

	/**
	 * Conditions used in query.
	 */
	private Conditions conditions = null;

	//indicates if requery is required
	private boolean neededQuery = false;

	//Map creator - provides link between JSF and MapServer
	private MapFileCreator creator = new MapFileCreator();

	//Parameter for session that indicates map path
	private String sessionParam;

	//Parameter for session that indicates mini-map path
	private String sessionParamMini;

	//Information show on-mouse-over (when mouse is over given point)
	private List pointsOfInterest = new ArrayList();

	//Data that is in map
	private MapData mapData = new MapData();

	//Ports or regions
	private int chosenMap = 1;

	private void setMapData() {

		if (!this.searchBean.getSearchParameters().getConditions().equals(
				this.conditions)) {

			SearchParameters params = this.searchBean.getSearchParameters();
			this.conditions = (Conditions) params.getConditions().clone();
			if (params.getMapElements() != SearchParameters.NOT_SPECIFIED) {
				if (params.getMapElements() == SearchParameters.MAP_PORTS) {
					this.chosenMap = 0;
				} else {
					this.chosenMap = 1;
				}
			}

			neededQuery = true;
		}

		if (this.neededQuery) {

			Conditions conditions = (Conditions) this.conditions.clone();

			this.pointsOfInterest.clear();
			GlobalMapQueryHolder queryHolder = new GlobalMapQueryHolder(conditions);
			queryHolder.executeQuery(this.chosenMap/* + this.chosenAttribute * ATTRS.length*/);

			GlobalMapDataTransformer transformer = new GlobalMapDataTransformer(
					queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);

			AbstractMapItem[] items = this.mapData.getItems();

			if (items.length >= 0) {
				this.creator.setMapData(items);
				this.creator.setMapLegend(this.mapData.getLegend());
			}
			
			if (this.creator.createMapFile()) {
				sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
				sessionParamMini = MAP_OBJECT_ATTR_NAME + "_mini_"
						+ System.currentTimeMillis();
				ExternalContext servletContext = FacesContext.getCurrentInstance()
						.getExternalContext();
				HttpSession session = (HttpSession) servletContext.getSession(true);
				session.setAttribute(sessionParam, creator.getFilePath());
				session.setAttribute(sessionParamMini, creator
						.getSmallMapFilePath());
			}
			
			this.neededQuery = false;
		}

	}

	/**
	 * Returns path that should be used as source of image that presents map
	 * @return
	 */
	public String getMapPath() {
		setMapData();
		return sessionParam;
	}

	/**
	 * Returns path that should be used as source of image that presents mini-map
	 * @return
	 */
	public String getMiniMapFile() {
		setMapData();
		return sessionParamMini;
	}

	/**
	 * Refreshes any data in map. It queries the database if needed.
	 * @return
	 */
	public String refresh() {
		if (this.creator.createMapFile()) {
			sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
			sessionParamMini = MAP_OBJECT_ATTR_NAME + "_mini_"
					+ System.currentTimeMillis();
			ExternalContext servletContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) servletContext.getSession(true);
			session.setAttribute(sessionParam, creator.getFilePath());
			session.setAttribute(sessionParamMini, creator
					.getSmallMapFilePath());
		}
		return null;
	}

	/**
	 * Returns list of points of interests. Point of interest shows some description on-mouse-over event.
	 * @return
	 */
	public PointOfInterest[] getPointsOfInterest() {
		return this.mapData.getToolTip();
	}

	/**
	 * Gets legend of current map.
	 * @return
	 */
	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}

	/**
	 * Gets search bean instance for current application.
	 * @return
	 */
	public SearchBean getSearchBean() {
		return searchBean;
	}

	/**
	 * Invoked by JSF - sets search bean instance for current application context
	 * @param searchBean
	 */
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

	/**
	 * Gets available layers for map.
	 * @return
	 */
	public MapLayer[] getLayers() {
		return this.creator.getLayers();
	}

	/**
	 * Sets chosen map (regions/ports)
	 * @param value
	 */
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
		String[] maps = MAPS;
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(new Integer(i), maps[i]);
		}
		return items;
	}

}
