package edu.emory.library.tast.estimates.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapDataTransformer;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapQueryHolder;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.maps.component.StandardMaps.ChosenMap;
import edu.emory.library.tast.maps.component.StandardMaps.MapIdent;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;

/**
 * Backing bean for map tab in estimates.
 * The bean provides paths for map/minimap.
 * It also provides legend / points of interests.
 *
 */
public class EstimatesMapBean {

	private EstimatesSelectionBean estimatesBean;

	private MapData mapData = new MapData();

	private List pointsOfInterest = new ArrayList();

	private Conditions conditions;
	
	private int zoomLevel;
	
	private boolean zoomLevelLocked = true;

	private boolean forceQuery = false;
	
	private int type = -1;

	public EstimatesSelectionBean getEstimatesBean() {
		return estimatesBean;
	}

	public void setEstimatesBean(EstimatesSelectionBean estimatesBean) {
		this.estimatesBean = estimatesBean;
	}

	public PointOfInterest[] getPointsOfInterest() {
		this.setData();
		return this.mapData.getToolTip();
	}

	private void setData() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		if (!this.getEstimatesBean().getConditions().equals(this.conditions) || forceQuery) {
			this.conditions = this.getEstimatesBean().getConditions();
			forceQuery  = false;
			this.pointsOfInterest.clear();
			EstimateMapQueryHolder queryHolder = new EstimateMapQueryHolder(
					conditions);
			queryHolder.executeQuery(session, zoomLevel, type);

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
	
	public String refresh() {
		type = determineType();
		forceQuery = true;
		this.setData();
		return null;
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
	
	public ZoomLevel[] getZoomLevels() {
		
		return StandardMaps.getZoomLevels(this);
	}
	
	public ZoomLevel getMiniMapZoomLevel() {
		
		return StandardMaps.getMiniMapZoomLevel(this);
	}
	
	public void setChosenMap(String value) {
		if (!StandardMaps.getSelectedMap(this).encodeMapId().equals(value)) {
			StandardMaps.setSelectedMapType(this, value);
			ChosenMap map = StandardMaps.getSelectedMap(this);
			this.zoomLevel = map.mapId;
			this.zoomLevelLocked = true;
			this.estimatesBean.setYearFrom(map.ident.yearFrom);
			this.estimatesBean.setYearTo(map.ident.yearTo);
			this.estimatesBean.changeSelection();
			this.estimatesBean.lockYears(true);
		}
	}

	public String getChosenMap() {
		this.estimatesBean.lockYears(false);
		return StandardMaps.getSelectedMap(this).encodeMapId();
	}

	public SelectItem[] getAvailableMaps() {
		return StandardMaps.getMapTypes(this);
	}

//	public int getZoomLevel() {
//		this.zoomLevelLocked = false;
//		return zoomLevel;
//	}
//
//	public void setZoomLevel(int zoomLevel) {
//		if (zoomLevelLocked) {
//			return;
//		}
//		if (this.zoomLevel != zoomLevel) {
//			forceQuery = true;
//			StandardMaps.zoomChanged(this, zoomLevel);
//		}
//		this.zoomLevel = zoomLevel;
//	}
	
	public SelectItem[] getAvailableAttributes() {
		return new SelectItem[] {
				new SelectItem("0", "Broad regions"),
				new SelectItem("1", "Regions"),
		};
	}
	
	public Integer getChosenAttribute() {
		return new Integer(zoomLevel);
	}
	
	public void setChosenAttribute(Integer id) {
		if (this.zoomLevel != id.intValue()) {
			StandardMaps.zoomChanged(this, id.intValue());
			this.forceQuery = true;
		}
		zoomLevel = id.intValue();
	}
}
