package edu.emory.library.tast.database.map;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapDataTransformer;
import edu.emory.library.tast.database.map.mapimpl.GlobalMapQueryHolder;
import edu.emory.library.tast.database.query.SearchBean;
import edu.emory.library.tast.database.query.SearchParameters;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.ChosenMap;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.TastDbConditions;

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
	private int zoomLevelId;
	
	public MapBean()
	{
		resetToDefault();
	}
	
	public void resetToDefault()
	{
		mapData = new MapData();
		type = -1;
		zoomLevelId = 0;
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
		// this.zoomLevelId = map.mapId;

		this.searchBean.setYearFrom(String.valueOf(map.ident.yearFrom));
		this.searchBean.setYearTo(String.valueOf(map.ident.yearTo));
		this.searchBean.search();
		
		needQuery = true;
		this.setMapData();
		return null;

	}

	public void setChosenMap(String value)
	{
		if (!value.equals(StandardMaps.getSelectedMap(this).encodeMapId()))
		{
			this.needQuery = true;
			StandardMaps.setSelectedMapType(this, value);
			ChosenMap map = StandardMaps.getSelectedMap(this);
			attributeId = map.mapId; 
			// zoomLevelId = map.mapId;
		}
	}

	public String getChosenMap()
	{
		System.out.println("getChosenMap = " + StandardMaps.getSelectedMap(this).encodeMapId());
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

	public int getZoomLevel()
	{
		return zoomLevelId;
	}

	public void setZoomLevel(int zoomLevelId)
	{
//		if (this.zoomLevelId != zoomLevelId)
//		{
//			StandardMaps.zoomChanged(this, zoomLevelId);
//			this.needQuery = true;
//		}
		this.zoomLevelId = zoomLevelId;
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

}
