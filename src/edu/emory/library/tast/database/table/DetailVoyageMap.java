package edu.emory.library.tast.database.table;

import edu.emory.library.tast.database.table.mapimpl.DetailQueryHolder;
import edu.emory.library.tast.database.table.mapimpl.DetailVoyageDataTransformer;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.MapLayer;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.util.query.Conditions;

public class DetailVoyageMap {

	private MapFileCreator creator = new MapFileCreator();

	private Long voyageId = null;
	
	private String attribute;

	private boolean queryNeeded = false;

	private MapData mapData = new MapData();

	public void setVoyageId(Long voyageId) {
		if (!voyageId.equals(this.voyageId)) {
			this.voyageId = voyageId;
			this.queryNeeded = true;
		}
	}

	public boolean prepareMapFile() {

		if (!this.queryNeeded) {
			return true;
		}

		this.queryNeeded = false;

		Conditions conditions = new Conditions();
		conditions.addCondition(Voyage.getAttribute(getAttribute()), this.voyageId, Conditions.OP_EQUALS);
		
		DetailQueryHolder queryHolder = new DetailQueryHolder(conditions);
		queryHolder.executeQuery(0);
		
		if (queryHolder.getRawQueryResponse().length > 0) {

			DetailVoyageDataTransformer transformer = new DetailVoyageDataTransformer(queryHolder.getAttributesMap());
			this.mapData.setMapData(queryHolder, transformer);
			this.creator.setMapData(this.mapData.getItems());
			this.creator.setMapLegend(this.mapData.getLegend());
			return this.creator.createMapFile();

		}
		return false;
	}
	
	public void refresh() {
		this.creator.createMapFile();
	}

	public String getCurrentMapFilePath() {
		return this.creator.getFilePath();
	}
	
	public PointOfInterest[] getPointsOfInterest() {
		return this.mapData.getToolTip();
	}

	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}

	public MapLayer[] getLayers() {
		return this.creator.getLayers();
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
