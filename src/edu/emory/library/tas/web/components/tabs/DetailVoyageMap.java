package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.tabs.map.AttributesMap;
import edu.emory.library.tas.web.components.tabs.map.AttributesRange;
import edu.emory.library.tas.web.components.tabs.map.MapData;
import edu.emory.library.tas.web.components.tabs.map.specific.DetailVoyageDataTransformer;
import edu.emory.library.tas.web.components.tabs.mapFile.MapFileCreator;
import edu.emory.library.tas.web.maps.PointOfInterest;

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

		/**
		 * 1. portdep Port of Departure 2. deptreg Region of departure
		 * 
		 * 3. embport First place of intended embarkation 4. majbuyrg Principal
		 * region of embarkation 4a.majbyimp Imputed principal region of
		 * embarkation 5. regem1 First region of embarkation 6. regem2 Second
		 * region of embarkation 7. regem3 Third region of embarkation
		 * 
		 * 8. arrport First port of intended disembarkation 9. adpsale1 Second
		 * port of disembarkation 10.adpsale2 Third port of disembarkation
		 * 11.majselrg Principal region of disembarkation
		 * 
		 * 12.majselpt Imputed port of slave arrival 13.mjselimp Imputed
		 * principal region of sale
		 * 
		 * 14.portret Port at which voyage ended
		 */

		if (!this.queryNeeded) {
			return true;
		}

		this.queryNeeded = false;

		Conditions conditions = new Conditions();
		conditions.addCondition("v.voyageId", this.voyageId, Conditions.OP_EQUALS);
		conditions.addCondition(VoyageIndex.getApproved());
		QueryValue qValue = new QueryValue("VoyageIndex as v", conditions);

		qValue.addPopulatedAttribute("v.voyage.portdep", true);
		qValue.addPopulatedAttribute("v.voyage.deptreg", true);

//		qValue.addPopulatedAttribute("v.voyage.majbuyrg", true);
//		qValue.addPopulatedAttribute("v.voyage.majbyimp", true);

		qValue.addPopulatedAttribute("v.voyage.embport", true);
		qValue.addPopulatedAttribute("v.voyage.regem1", true);

//		qValue.addPopulatedAttribute("v.voyage.regem2", true);
//
//		qValue.addPopulatedAttribute("v.voyage.regem3", true);

		qValue.addPopulatedAttribute("v.voyage.arrport", true);

//		qValue.addPopulatedAttribute("v.voyage.adpsale1", true);
//
//		qValue.addPopulatedAttribute("v.voyage.adpsale2", true);

		qValue.addPopulatedAttribute("v.voyage.majselrg", true);

		qValue.addPopulatedAttribute("v.voyage.majselpt", true);
		qValue.addPopulatedAttribute("v.voyage.mjselimp", true);

		qValue.addPopulatedAttribute("v.voyage.portret", true);

		AttributesMap attrsMap = new AttributesMap();
		List col1 = new ArrayList();
		col1.add(new AttributesRange(Voyage.getAttribute("portdep"), 0 ,1));
		attrsMap.addColumn(col1);
		List col2 = new ArrayList();
		col2.add(new AttributesRange(Voyage.getAttribute("deptreg"), 0 ,1));
		attrsMap.addColumn(col2);
		List col3 = new ArrayList();
		col3.add(new AttributesRange(Voyage.getAttribute("embport"), 0 ,1));
		attrsMap.addColumn(col3);
		List col4 = new ArrayList();
		col4.add(new AttributesRange(Voyage.getAttribute("regem1"), 0 ,1));
		attrsMap.addColumn(col4);
		List col5 = new ArrayList();
		col5.add(new AttributesRange(Voyage.getAttribute("arrport"), 0 ,1));
		attrsMap.addColumn(col5);
		List col6 = new ArrayList();
		col6.add(new AttributesRange(Voyage.getAttribute("majselrg"), 0 ,1));
		attrsMap.addColumn(col6);
		List col7 = new ArrayList();
		col7.add(new AttributesRange(Voyage.getAttribute("majselpt"), 0 ,1));
		attrsMap.addColumn(col7);
		List col8 = new ArrayList();
		col8.add(new AttributesRange(Voyage.getAttribute("mjselimp"), 0 ,1));
		attrsMap.addColumn(col8);
		List col9 = new ArrayList();
		col9.add(new AttributesRange(Voyage.getAttribute("portret"), 0 ,1));
		attrsMap.addColumn(col9);
		
		Object[] voyages = qValue.executeQuery();
		if (voyages.length > 0) {

			DetailVoyageDataTransformer transformer = new DetailVoyageDataTransformer(attrsMap);
			this.mapData.setMapData(voyages, 0, 0, transformer);
			this.creator.setMapData(this.mapData.getItems());
			return this.creator.createMapFile();

		}
		return false;
	}

	public String getCurrentMapFilePath() {
		return this.creator.getFilePath();
	}
	
	public PointOfInterest[] getPointsOfInterest() {
		return this.mapData.getToolTip();
	}
}
