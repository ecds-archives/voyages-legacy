package edu.emory.library.tast.estimates.map;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapDataTransformer;
import edu.emory.library.tast.estimates.map.mapimpl.EstimateMapQueryHolder;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.MapLayer;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.util.query.Conditions;

public class EstimatesMapBean {

	private static final String MAP_OBJECT_ATTR_NAME = "__map__file_";

	private EstimatesSelectionBean estimatesBean;

	private MapFileCreator creator = new MapFileCreator();

	private MapData mapData = new MapData();

	private List pointsOfInterest = new ArrayList();

	private Conditions conditions;

	private String sessionParam;

	private String sessionParamMini;

	public EstimatesSelectionBean getEstimatesBean() {
		return estimatesBean;
	}

	public void setEstimatesBean(EstimatesSelectionBean estimatesBean) {
		this.estimatesBean = estimatesBean;
	}

	public PointOfInterest[] getPointsOfInterest() {
		return this.mapData.getToolTip();
	}

	public String getMiniMapFile() {
		return this.sessionParamMini;
	}

	public String getMapPath() {
		if (!this.getEstimatesBean().getConditions().equals(this.conditions)) {
			this.conditions = this.getEstimatesBean().getConditions();
			this.pointsOfInterest.clear();
			EstimateMapQueryHolder queryHolder = new EstimateMapQueryHolder(
					conditions);
			queryHolder.executeQuery(0);

			EstimateMapDataTransformer transformer = new EstimateMapDataTransformer(
					queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);

			AbstractMapItem[] items = this.mapData.getItems();

			//if (items.length > 0) {
			this.creator.setMapData(items);
			this.creator.setMapLegend(this.mapData.getLegend());
			//}
			if (this.creator.createMapFile()) {
				sessionParam = MAP_OBJECT_ATTR_NAME
						+ System.currentTimeMillis();
				sessionParamMini = MAP_OBJECT_ATTR_NAME + "_mini_"
						+ System.currentTimeMillis();
				ExternalContext servletContext = FacesContext
						.getCurrentInstance().getExternalContext();
				HttpSession session = (HttpSession) servletContext
						.getSession(true);
				session.setAttribute(sessionParam, creator.getFilePath());
				session.setAttribute(sessionParamMini, creator
						.getSmallMapFilePath());
			}
		}
		return sessionParam;
	}
	
	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}
	
	public String refresh() {	
		if (this.creator.createMapFile()) {
			sessionParam = MAP_OBJECT_ATTR_NAME
					+ System.currentTimeMillis();
			sessionParamMini = MAP_OBJECT_ATTR_NAME + "_mini_"
					+ System.currentTimeMillis();
			ExternalContext servletContext = FacesContext
					.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession) servletContext
					.getSession(true);
			session.setAttribute(sessionParam, creator.getFilePath());
			session.setAttribute(sessionParamMini, creator
					.getSmallMapFilePath());
		}
		return null;
	}
	
	public MapLayer[] getLayers() {
		return this.creator.getLayers();
	}
	
	
}
