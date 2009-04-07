package edu.emory.library.tast.estimates.map;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapDataTransformer;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapQueryHolder;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.ChosenMap;

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
	
	private double mapX1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X1);
	private double mapY1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y1);
	private double mapX2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X2);
	private double mapY2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y2);
	
	/**
	 * Current conditions
	 */
	private TastDbConditions conditions;
	
	/**
	 * Type of point of interest (ports/regions/broad regions);
	 */
	private int poiType;

	/**
	 * Forces query when setData called
	 */
	private boolean needRefresh = false;
	
	/**
	 * Type of visible points (emb/disembarkation)
	 */
	private int type = -1;

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
	private void setData()
	{
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		//Check whether query is required
		if (!this.getEstimatesBean().getConditions().equals(this.conditions) || needRefresh)
		{
			this.conditions = this.getEstimatesBean().getConditions();
			needRefresh = false;
			EstimateMapQueryHolder queryHolder = new EstimateMapQueryHolder(conditions);
			queryHolder.executeQuery(session, poiType, type);
			EstimateMapDataTransformer transformer = new EstimateMapDataTransformer(queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
		}
		
		t.commit();
		session.close();
		
	}

	public LegendItemsGroup[] getLegend()
	{
		return this.mapData.getLegend();
	}
	
	public String refresh()
	{
		
		ChosenMap map = StandardMaps.getSelectedMap(this);
		this.estimatesBean.setYearFrom(String.valueOf(map.ident.yearFrom));
		this.estimatesBean.setYearTo(String.valueOf(map.ident.yearTo));
		this.estimatesBean.changeSelection();
		
		type = determineType();
		needRefresh = true;
		
		setData();
		
		return null;
		
	}
	
	public String zoomToAll()
	{
		
		setData();
		adjustMapExtentByPointsOfInterest();
		
		return null;
		
	}

	private void adjustMapExtentByPointsOfInterest()
	{
		
		PointOfInterest[] pointsOfInterest = mapData.getPointsOfInterest();
		if (pointsOfInterest == null || pointsOfInterest.length == 0)
			return;
		
		double minX = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double minY  = Double.MAX_VALUE;
		double maxY  = -Double.MAX_VALUE;
		
		for (int i = 0; i < pointsOfInterest.length; i++)
		{
			PointOfInterest point = pointsOfInterest[i];
			minX = Math.min(point.getX(), minX);
			maxX = Math.max(point.getX(), maxX);
			minY = Math.min(point.getY(), minY);
			maxY = Math.max(point.getY(), maxY);
		}
		
		mapX1 = Math.max(minX, -180);
		mapX2 = Math.min(maxX, +180);
		mapY1 = Math.max(minY, -90);
		mapY2 = Math.min(maxY, +90);
		
	}
	
	/**
	 * Finds type of visible ports
	 * @return
	 */
	private int determineType()
	{
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
	public ZoomLevel[] getZoomLevels()
	{
		return StandardMaps.getZoomLevels(this);
	}
	
	/**
	 * Returns information on zoom level for minimap
	 * @return
	 */
	public ZoomLevel getMiniMapZoomLevel()
	{
		return StandardMaps.getMiniMapZoomLevel(this);
	}
	
	/**
	 * Sets active map.
	 * @param value
	 */
	public void setChosenMap(String value)
	{
		if (!StandardMaps.getSelectedMap(this).encodeMapId().equals(value))
		{
			needRefresh = true;
			StandardMaps.setSelectedMapType(this, value);
			ChosenMap map = StandardMaps.getSelectedMap(this);
			poiType = map.mapId;
			// this.zoomLevel = map.mapId;
		}
	}

	/**
	 * Returns id of chosen map
	 * @return
	 */
	public String getChosenMap()
	{
		return StandardMaps.getSelectedMap(this).encodeMapId();
	}

	/**
	 * Gets list of available maps.
	 * @return
	 */
	public SelectItem[] getAvailableMaps()
	{
		return StandardMaps.getMapTypes(this);
	}

	public SelectItem[] getAvailableAttributes()
	{
		return new SelectItem[] {
				new SelectItem("0", TastResource.getText("estimates_components_map_broadregions")),
				new SelectItem("1", TastResource.getText("estimates_components_map_regions")),
		};
	}

	public double getMapX1()
	{
		return mapX1;
	}

	public void setMapX1(double mapX1)
	{
		this.mapX1 = mapX1;
	}

	public double getMapY1()
	{
		return mapY1;
	}

	public void setMapY1(double mapY1)
	{
		this.mapY1 = mapY1;
	}

	public double getMapX2()
	{
		return mapX2;
	}

	public void setMapX2(double mapX2)
	{
		this.mapX2 = mapX2;
	}

	public double getMapY2()
	{
		return mapY2;
	}

	public void setMapY2(double mapY2)
	{
		this.mapY2 = mapY2;
	}
	
}
	

