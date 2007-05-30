package edu.emory.library.tast.database.table;

import edu.emory.library.tast.database.table.mapimpl.DetailQueryHolder;
import edu.emory.library.tast.database.table.mapimpl.DetailVoyageDataTransformer;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapData;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.util.query.Conditions;

public class DetailVoyageMap {

	private long iid;

	private boolean queryNeeded = false;

	private MapData mapData = new MapData();

	public void setVoyageIid(long iid) {
		if (iid != this.iid) {
			this.iid = iid;
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
		conditions.addCondition(Voyage.getAttribute("iid"), new Long(iid), Conditions.OP_EQUALS);
		
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
}
