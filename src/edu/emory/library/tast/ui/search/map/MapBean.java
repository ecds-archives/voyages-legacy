package edu.emory.library.tast.ui.search.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapData;
import edu.emory.library.tast.ui.maps.MapLayer;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.ui.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.ui.search.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.util.query.Conditions;

/**
 * ./configure --with-proj --with-ogr --with-gdal --with-postgis=yes
 * --with-threads --verbose=yes
 * 
 * @author juri
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 2;

	public static int PORT_ARRIVAL = 3;

	public static int PORT_BOTH = 5;

	private static final String[] MAPS = new String[] { "Ports", "Regions" };

	private static final String[] ATTRS = new String[] { "Raw", "Adjusted" };

	private static final String[] MARKERS = new String[] { "1519", "1600",
			"1650", "1675", "1700", "1725", "1750", "1800", "1825", "1850",
			"1867" };

	private static final String[] markersList = MARKERS;

	// private static final String markersList =
	// "1519,1600,1650,1675,1700,1725,1750,1800,1825,1850,1867";

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

	private String sessionParamMini;

	private List pointsOfInterest = new ArrayList();

	private MapData mapData = new MapData();

	private int chosenMap = 0;

	private int chosenAttribute = 0;

	private Integer yearBegin = new Integer(0);

	private Integer yearEnd = new Integer(MARKERS.length - 1);

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

			if (params.getValuesType() != SearchParameters.NOT_SPECIFIED) {
				if (params.getMapElements() == SearchParameters.VALUES_ADJUSTED) {
					this.chosenAttribute = 1;
				} else {
					this.chosenAttribute = 0;
				}
			}

			neededQuery = true;
		}

		if (this.neededQuery) {

			Conditions conditions = (Conditions) this.conditions.clone();
//			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
//					MARKERS[this.yearBegin.intValue()]),
//					Conditions.OP_GREATER_OR_EQUAL);
//			conditions.addCondition(Voyage.getAttribute("yearam"), new Integer(
//					MARKERS[this.yearEnd.intValue()]),
//					Conditions.OP_SMALLER_OR_EQUAL);

			this.pointsOfInterest.clear();
			GlobalMapQueryHolder queryHolder = new GlobalMapQueryHolder(
					conditions);
			queryHolder.executeQuery(this.chosenMap + this.chosenAttribute
					* ATTRS.length);

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

	public String getMapPath() {
		setMapData();
		return sessionParam;
	}

	public String getMiniMapFile() {
		setMapData();
		return sessionParamMini;
	}

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

	public PointOfInterest[] getPointsOfInterest() {

		// if (this.mapData.getToolTip().length != 0) {
		// System.out.println(this.mapData.getToolTip()[0].getX());
		// }

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
		String[] maps = MAPS;
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(new Integer(i), maps[i]);
		}
		return items;
	}

	public Integer getChosenAttribute() {
		return new Integer(this.chosenAttribute);
	}

	public void setChosenAttribute(Integer value) {
		if (this.chosenAttribute != value.intValue()) {
			this.neededQuery = true;
		}
		this.chosenAttribute = value.intValue();
	}

	public SelectItem[] getAvailableAttributes() {
		String[] maps = ATTRS;
		SelectItem[] items = new SelectItem[maps.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new SelectItem(new Integer(i), maps[i]);
		}
		return items;
	}

	public Integer getYearBegin() {
		return this.yearBegin;
	}

	public Integer getYearEnd() {
		return this.yearEnd;
	}

	public void setYearBegin(Integer yearBegin) {
		if (!this.yearBegin.equals(yearBegin)) {
			this.neededQuery = true;
		}
		this.yearBegin = yearBegin;
	}

	public void setYearEnd(Integer yearEnd) {
		if (!this.yearEnd.equals(yearEnd)) {
			this.neededQuery = true;
		}
		this.yearEnd = yearEnd;
	}

	public String[] getMarkers() {
		return markersList;
	}

	// public void setLayers(MapLayer[] layers) {
	// this.creator.setLayers(layers);
	// }

}
