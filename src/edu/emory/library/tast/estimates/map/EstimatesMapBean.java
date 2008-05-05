package edu.emory.library.tast.estimates.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapDataTransformer;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapQueryHolder;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomChangedEvent;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.ChosenMap;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;

/**
 * Backing bean for map tab in estimates.
 * The bean provides paths for map/minimap.
 * It also provides legend / points of interests.
 *
 */
public class EstimatesMapBean {

	/**
	 * Reference to estimates bean
	 */
	private EstimatesSelectionBean estimatesBean;

	/**
	 * Map data.
	 */
	private MapData mapData = new MapData();

	/**
	 * Points of interest - points visible on map
	 */
	private List pointsOfInterest = new ArrayList();

	/**
	 * Current conditions
	 */
	private Conditions conditions;
	
	/**
	 * Type of point of interest (ports/regions/broad regions);
	 */
	private int poiType;
	
	private int zoomLevel = 0;

	/**
	 * Forces query when setData called
	 */
	private boolean forceQuery = false;
	
	/**
	 * Type of visible points (emb/disembarkation)
	 */
	private int type = -1;

	private boolean zoomLevelLocked = false;

	public EstimatesSelectionBean getEstimatesBean() {
		return estimatesBean;
	}

	public void setEstimatesBean(EstimatesSelectionBean estimatesBean) {
		this.estimatesBean = estimatesBean;
	}

	public PointOfInterest[] getPointsOfInterest() {
		this.setData();
		return this.mapData.getPointsOfInterest();
	}

	/**
	 * Queries the database and sets currently visible points on the map
	 */
	private void setData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		//Check whether query is required
		if (!this.getEstimatesBean().getConditions().equals(this.conditions) || forceQuery) {
			this.conditions = this.getEstimatesBean().getConditions();
			forceQuery  = false;
			this.pointsOfInterest.clear();
			EstimateMapQueryHolder queryHolder = new EstimateMapQueryHolder(
					conditions);
			queryHolder.executeQuery(session, poiType, type);

			EstimateMapDataTransformer transformer = new EstimateMapDataTransformer(
					queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
		}
		
		t.commit();
		session.close();
		
	}

	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}
	
	/**
	 * When Refresh button clicked.
	 * @return
	 */
	public String refresh() {
		type = determineType();
		forceQuery = true;
		this.setData();
		return null;
	}
	
	/**
	 * Finds type of visible ports
	 * @return
	 */
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
	 * Returns information of zoom levels for map
	 * @return
	 */
	public ZoomLevel[] getZoomLevels() {
		
		return StandardMaps.getZoomLevels(this);
	}
	
	/**
	 * Returns information on zoom level for minimap
	 * @return
	 */
	public ZoomLevel getMiniMapZoomLevel() {
		
		return StandardMaps.getMiniMapZoomLevel(this);
	}
	
	/**
	 * Sets active map.
	 * @param value
	 */
	public void setChosenMap(String value) {
		if (!StandardMaps.getSelectedMap(this).encodeMapId().equals(value)) {
			StandardMaps.setSelectedMapType(this, value);
			ChosenMap map = StandardMaps.getSelectedMap(this);
			this.zoomLevel = map.mapId;
			this.zoomLevelLocked = true;
			this.estimatesBean.setYearFrom(String.valueOf(map.ident.yearFrom));
			this.estimatesBean.setYearTo(String.valueOf(map.ident.yearTo));
			this.estimatesBean.changeSelection();
			this.estimatesBean.lockYears(true);
			this.estimatesBean.changeSelection();
		}
	}

	/**
	 * Returns id of chosen map
	 * @return
	 */
	public String getChosenMap() {
		this.estimatesBean.lockYears(false);
		return StandardMaps.getSelectedMap(this).encodeMapId();
	}

	/**
	 * Gets list of available maps.
	 * @return
	 */
	public SelectItem[] getAvailableMaps() {
		return StandardMaps.getMapTypes(this);
	}

	public int getZoomLevel() {
		this.zoomLevelLocked = false;
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		if (zoomLevelLocked) {
			return;
		}
		if (this.zoomLevel != zoomLevel) {
			forceQuery = true;
			StandardMaps.zoomChanged(this, zoomLevel);
		}
		this.zoomLevel = zoomLevel;
	}
	
	/**
	 * Gets list of available place types
	 */
	public SelectItem[] getAvailableAttributes() {
		return new SelectItem[] {
				new SelectItem("0", TastResource.getText("estimates_components_map_broadregions")),
				new SelectItem("1", TastResource.getText("estimates_components_map_regions")),
		};
	}
	
	/**
	 * Gets chosen place type
	 * @return
	 */
	public Integer getChosenAttribute() {
		return new Integer(poiType);
	}
	
	/**
	 * Sets chosen place type
	 * @param id
	 */
	public void setChosenAttribute(Integer id) {
		if (this.poiType != id.intValue()) {
			StandardMaps.zoomChanged(this, id.intValue());
			this.forceQuery = true;
		}
		poiType = id.intValue();
	}
	
	public void onZoomChanged(ZoomChangedEvent e) {
		System.out.println("Zoom was changed!");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request)) {
			AAUtils.addZonesToRefresh(request, "map-legend");
		}
	}
}
