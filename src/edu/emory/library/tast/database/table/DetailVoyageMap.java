package edu.emory.library.tast.database.table;

import edu.emory.library.tast.database.table.mapimpl.DetailQueryHolder;
import edu.emory.library.tast.database.table.mapimpl.DetailVoyageDataTransformer;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.util.query.Conditions;

public class DetailVoyageMap {

	private Long voyageId = null;
	
	private String attribute;

	private boolean queryNeeded = false;

	private MapData mapData = new MapData();

	public void setVoyageId(Long voyageId) {
		if (!voyageId.equals(this.voyageId)) {
			this.voyageId = voyageId;
			this.queryNeeded = true;
		}
		this.refreshData();
	}

	public boolean refreshData() {

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
		}
		return true;
	}
	
	public void refresh() {
		this.refreshData();
	}

	public PointOfInterest[] getPointsOfInterest() {
		this.refreshData();
		return this.mapData.getToolTip();
	}

	public LegendItemsGroup[] getLegend() {
		return this.mapData.getLegend();
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
