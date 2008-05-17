package edu.emory.library.tast.database.map;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.database.query.SearchParameters;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.ChosenMap;
import edu.emory.library.tast.util.HibernateUtil;

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
public class MapBean
{

	public static final int PORT_DEPARTURE = 2;
	public static final int PORT_ARRIVAL = 3;
	public static final int PORT_BOTH = 5;
	
	private SearchBean searchBean = null;
	private TastDbConditions conditions = null;
	private boolean needQuery = true;

	private MapData mapData = new MapData();

	private int type = -1;
	private int attributeId = 0;
	
	private double mapX1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X1);
	private double mapY1 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y1);
	private double mapX2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_X2);
	private double mapY2 = AppConfig.getConfiguration().getDouble(AppConfig.MAP_DEFAULT_Y2);
	
	public MapBean()
	{
		resetToDefault();
	}
	
	public void resetToDefault()
	{
		mapData = new MapData();
		type = -1;
		conditions = null;
		attributeId = 0;
		setChosenMap("map-0_0");
	}

	private void setMapData()
	{
		
		SearchParameters searchConditions = this.searchBean.getSearchParameters();
		if (!needQuery && searchConditions.getConditions().equals(this.conditions))
			return;
		
		needQuery = false;
		this.conditions = (TastDbConditions) searchConditions.getConditions().clone();
		
		TastDbConditions conditions = (TastDbConditions) this.conditions.clone();

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();		

		GlobalMapQueryHolder queryHolder = new GlobalMapQueryHolder(conditions);
		queryHolder.executeQuery(session, attributeId, type);

		GlobalMapDataTransformer transformer = new GlobalMapDataTransformer(queryHolder.getAttributesMap());
		this.mapData.setMapData(queryHolder, transformer);
		
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

	public String refresh()
	{
		
		type = determineType();
		
		ChosenMap map = StandardMaps.getSelectedMap(this);

		this.searchBean.setYearFrom(String.valueOf(map.ident.yearFrom));
		this.searchBean.setYearTo(String.valueOf(map.ident.yearTo));
		this.searchBean.search();
		
		needQuery = true;

		setMapData();
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

	public void setChosenMap(String value)
	{
		if (!value.equals(StandardMaps.getSelectedMap(this).encodeMapId()))
		{
			this.needQuery = true;
			StandardMaps.setSelectedMapType(this, value);
			ChosenMap map = StandardMaps.getSelectedMap(this);
			attributeId = map.mapId; 
		}
	}

	public String getChosenMap()
	{
		return StandardMaps.getSelectedMap(this).encodeMapId();
	}

	public SelectItem[] getAvailableMaps()
	{
		return StandardMaps.getMapTypes(this);
	}
	
	public ZoomLevel[] getZoomLevels()
	{
		setMapData();
		return StandardMaps.getZoomLevels(this);
	}
	
	public ZoomLevel getMiniMapZoomLevel()
	{
		setMapData();
		return StandardMaps.getMiniMapZoomLevel(this);
	}
	
	public SelectItem[] getAvailableAttributes()
	{
		return new SelectItem[] {
				new SelectItem("0", TastResource.getText("database_components_map_broadregions")),
				new SelectItem("1", TastResource.getText("database_components_map_regions")),
				new SelectItem("2", TastResource.getText("database_components_map_ports"))};
	}
	
	public Integer getChosenAttribute()
	{
		return new Integer(attributeId);
	}
	
	public void setChosenAttribute(Integer id)
	{
//		if (this.attributeId != id.intValue())
//		{
//			StandardMaps.zoomChanged(this, id.intValue());
//			this.needQuery = true;
//		}
//		attributeId = id.intValue();
	}
	
	public PointOfInterest[] getPointsOfInterest()
	{
		setMapData();
		return this.mapData.getPointsOfInterest();
	}

	public LegendItemsGroup[] getLegend()
	{
		return this.mapData.getLegend();
	}

	public SearchBean getSearchBean()
	{
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean)
	{
		this.searchBean = searchBean;
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
