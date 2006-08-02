package edu.emory.library.tas.web.components.tabs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.tabs.mapFile.MapFileCreator;
import edu.emory.library.tas.web.maps.PointOfInterest;

public class DetailVoyageMap {

	private class MapPointInfo {

		public class MapPoint {
			public Attribute attr;

			public Number value;

			public MapPoint(Attribute attr, Number value) {
				this.attr = attr;
				this.value = value;
			}
		}

		public GISPortLocation port;

		public List info = new ArrayList();

		public MapPointInfo(GISPortLocation port) {
			this.port = port;
		}

		public void addMapPoint(Attribute attr, Number value) {
			this.info.add(new MapPoint(attr, value));
		}

		public List getMapPoints() {
			return this.info;
		}

		public boolean equals(Object o) {
			if (o instanceof GISPortLocation) {
				GISPortLocation that = (GISPortLocation) o;
				return this.port.equals(that);
			} else if (o instanceof MapPointInfo) {
				MapPointInfo info = (MapPointInfo) o;
				return info.port.equals(this.port);
			} else {
				return false;
			}
		}

		public List getAttrsList() {
			List attrs = new ArrayList();
			for (Iterator iter = info.iterator(); iter.hasNext();) {
				MapPoint pointInfo = (MapPoint) iter.next();
				attrs.add(pointInfo.attr);
			}
			return attrs;
		}

		public PointOfInterest getPointOgInterest() {
			PointOfInterest point = new PointOfInterest(this.port.getX(), this.port.getY());
			StringBuffer buffer = new StringBuffer();
			buffer.append("<b>");
			buffer.append("Place name: ").append(port.getPortName());
			buffer.append("</b><br/>");
			boolean first = true;
			for (Iterator iter = info.iterator(); iter.hasNext();) {
				MapPoint element = (MapPoint) iter.next();
				if (!first) {
					buffer.append(", ");
				}
				buffer.append(element.attr.getUserLabelOrName());
				first = false;
			} 
			point.setText(buffer.toString());
			return point;
		}
	}

	private MapFileCreator creator = new MapFileCreator();

	private Long voyageId = null;

	private boolean queryNeeded = false;

	private PointOfInterest[] pointOfInterest = new PointOfInterest[] {};

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

		Attribute[] attrs = new Attribute[9];
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

		attrs[0] = Voyage.getAttribute("portdep");
		attrs[1] = Voyage.getAttribute("deptreg");
//		attrs[2] = Voyage.getAttribute("majbuyrg");
//		attrs[3] = Voyage.getAttribute("majbyimp");
		attrs[2] = Voyage.getAttribute("embport");
		attrs[3] = Voyage.getAttribute("regem1");
//		attrs[6] = Voyage.getAttribute("regem2");
//		attrs[7] = Voyage.getAttribute("regem3");
		attrs[4] = Voyage.getAttribute("arrport");
//		attrs[9] = Voyage.getAttribute("adpsale1");
//		attrs[10] = Voyage.getAttribute("adpsale2");
		attrs[5] = Voyage.getAttribute("majselrg");
		attrs[6] = Voyage.getAttribute("majselpt");
		attrs[7] = Voyage.getAttribute("mjselimp");
		attrs[8] = Voyage.getAttribute("portret");

		Object[] voyages = qValue.executeQuery();
		if (voyages.length > 0) {

			Object[] row = (Object[]) voyages[0];

			
			List toMap = new ArrayList();
			if (row[0] == null) {
				toMap.add(row[1]);
			} else {
				toMap.add(row[0]);
			}
			if (row[2] == null) {
				toMap.add(row[3]);
			} else {
				toMap.add(row[2]);
			}
			if (row[4] == null) {
				toMap.add(row[5]);
			} else {
				toMap.add(row[4]);
			}
			if (row[6] == null) {
				toMap.add(row[7]);
			} else {
				toMap.add(row[6]);
			}
			toMap.add(row[8]);
			
			List mapPointsInfo = new ArrayList();
			int i = 0;
			for (Iterator iter = toMap.iterator(); iter.hasNext(); i++) {				
				Dictionary dict = (Dictionary) iter.next();
				GISPortLocation gisLoc = GISPortLocation.getGISPortLocation(dict);
				if (gisLoc != null) {
					MapPointInfo info = new MapPointInfo(gisLoc);
					int index;
					if ((index = mapPointsInfo.indexOf(info)) != -1) {
						MapPointInfo oldInfo = (MapPointInfo) mapPointsInfo.get(index);
						oldInfo.addMapPoint(attrs[i], new Integer(0));
					} else {
						mapPointsInfo.add(info);
					}
				}
			}

			List items = new ArrayList();
			List size = new ArrayList();
			size.add(new Integer(0));

			this.pointOfInterest = new PointOfInterest[mapPointsInfo.size()];
			
			i = 0;
			for (Iterator iter = mapPointsInfo.iterator(); iter.hasNext(); i++) {
//				MapPointInfo info = (MapPointInfo) iter.next();
//				size.clear();
//				size.add(new Integer(i % 6));
//				items.add(new MapItem(info.port.getPortName(), info.port.getX(), info.port.getY(), size, info
//						.getAttrsList(), 1, 0, false));
//				pointOfInterest[i] = info.getPointOgInterest();
			}

//			this.creator.setMapData((MapItem[]) items.toArray(new MapItem[] {}), 1, 1);
//			return this.creator.createMapFile();

		}
		return false;
	}

	public String getCurrentMapFilePath() {
		return this.creator.getFilePath();
	}
	
	public PointOfInterest[] getPointsOfInterest() {
		return this.pointOfInterest;
	}
}
