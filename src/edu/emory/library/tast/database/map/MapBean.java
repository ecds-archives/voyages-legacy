package edu.emory.library.tast.database.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.hql.ast.tree.QueryNode;

import edu.emory.library.tast.database.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.database.query.SearchParameters;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.MapIdent;
import edu.emory.library.tast.util.HibernateUtil;
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

	//Information show on-mouse-over (when mouse is over given point)
	private List pointsOfInterest = new ArrayList();

	//Data that is in map
	private MapData mapData = new MapData();
	
	private int zoomLevelId = 0;

	private int type = -1;

	private void setMapData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();		

		if (!this.searchBean.getSearchParameters().getConditions().equals(
				this.conditions)) {

			SearchParameters params = this.searchBean.getSearchParameters();
			this.conditions = (Conditions) params.getConditions().clone();
//			if (params.getMapElements() != SearchParameters.NOT_SPECIFIED) {
//				if (params.getMapElements() == SearchParameters.MAP_PORTS) {
//					this.chosenMap = 0;
//				} else {
//					this.chosenMap = 1;
//				}
//			}

			neededQuery = true;
		}

		if (this.neededQuery) {

			Conditions conditions = (Conditions) this.conditions.clone();

			this.pointsOfInterest.clear();
			GlobalMapQueryHolder queryHolder = new GlobalMapQueryHolder(conditions);
			//queryHolder.executeQuery(session, this.chosenMap/* + this.chosenAttribute * ATTRS.length*/);
			
			queryHolder.executeQuery(session, zoomLevelId, type);

			GlobalMapDataTransformer transformer = new GlobalMapDataTransformer(
					queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
			
			this.neededQuery = false;
		}
		
		t.commit();
		session.close();

	}

	private int determineType() {
		if (this.mapData.getLegend() == null || this.mapData.getLegend().length < 2) {
			return -1;
		}
		LegendItemsGroup legend = this.mapData.getLegend()[1];
		if (legend.getItems()[0].isEnabled() && legend.getItems()[1].isEnabled()) {
			return -1;
		} else if (legend.getItems()[0].isEnabled()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Refreshes any data in map. It queries the database if needed.
	 * @return
	 */
	public String refresh() {
		type = determineType();
		neededQuery = true;
		this.setMapData();
		return null;
	}

	/**
	 * Returns list of points of interests. Point of interest shows some description on-mouse-over event.
	 * @return
	 */
	public PointOfInterest[] getPointsOfInterest() {
		setMapData();
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
	 * Sets chosen map (regions/ports)
	 * @param value
	 */
	public void setChosenMap(String value) {
		if (!value.equals(StandardMaps.getSelectedMap(this).mapPath)) {
			this.neededQuery = true;
			StandardMaps.setSelectedMapType(this, value);
			MapIdent map = StandardMaps.getSelectedMap(this);
			this.searchBean.setYearFrom(map.yearFrom);
			this.searchBean.setYearTo(map.yearTo);
			
			this.searchBean.lockYears(true);
		}
		StandardMaps.setSelectedMapType(this, value);
	}

	public String getChosenMap() {
		this.searchBean.lockYears(false);
		return ((MapIdent)StandardMaps.getSelectedMap(this)).mapPath;
	}

	public SelectItem[] getAvailableMaps() {
		return StandardMaps.getMapTypes(this);
	}
	
	public ZoomLevel[] getZoomLevels() {
		setMapData();
		return StandardMaps.getZoomLevels(this);
	}
	
	public ZoomLevel getMiniMapZoomLevel() {
		setMapData();
		return StandardMaps.getMiniMapZoomLevel(this);
	}

	public int getZoomLevel() {
		return zoomLevelId;
	}

	public void setZoomLevel(int zoomLevelId) {
		if (this.zoomLevelId != zoomLevelId) {
			this.neededQuery = true;
		}
		this.zoomLevelId = zoomLevelId;
	}
}
