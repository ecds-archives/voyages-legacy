package edu.emory.library.tast.ui.search.table;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapData;
import edu.emory.library.tast.ui.maps.MapLayer;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.ui.maps.mapfile.MapFileCreator;
import edu.emory.library.tast.ui.search.table.mapimpl.DetailQueryHolder;
import edu.emory.library.tast.ui.search.table.mapimpl.DetailVoyageDataTransformer;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class DetailVoyageMap {

	private MapFileCreator creator = new MapFileCreator();

	private Long voyageId = null;

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
		conditions.addCondition(Voyage.getAttribute("voyageId"), this.voyageId, Conditions.OP_EQUALS);
		
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
}
