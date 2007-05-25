package edu.emory.library.tast.estimates.map;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapDataTransformer;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapQueryHolder;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
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
		if (!this.getEstimatesBean().getConditions().equals(this.conditions)) {
			this.conditions = this.getEstimatesBean().getConditions();
			this.pointsOfInterest.clear();
			EstimateMapQueryHolder queryHolder = new EstimateMapQueryHolder(
					conditions);
			queryHolder.executeQuery(0);

			EstimateMapDataTransformer transformer = new EstimateMapDataTransformer(
					queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
		}
	}

	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}
	
	public String refresh() {	
		this.setData();
		return null;
	}
	
	public ZoomLevel[] getZoomLevels() {
		
		return StandardMaps.getZoomLevels();
	}
	
	public ZoomLevel getMiniMapZoomLevel() {
		
		return StandardMaps.getMiniMapZoomLevel();
	}
}
