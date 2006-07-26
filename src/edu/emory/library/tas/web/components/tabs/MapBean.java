package edu.emory.library.tas.web.components.tabs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.SearchParameters;
import edu.emory.library.tas.web.components.tabs.mapFile.MapFileCreator;
import edu.emory.library.tas.web.maps.PointOfInterest;
import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.colorObj;
import edu.umn.gis.mapscript.fontSetObj;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.labelObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.lineObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscript;
import edu.umn.gis.mapscript.mapscriptConstants;
import edu.umn.gis.mapscript.outputFormatObj;
import edu.umn.gis.mapscript.pointObj;
import edu.umn.gis.mapscript.shapeObj;
import edu.umn.gis.mapscript.styleObj;
import edu.umn.gis.mapscript.symbolObj;

/**
 * ./configure --with-proj --with-ogr --with-gdal --with-postgis=yes
 * --with-threads --verbose=yes
 * 
 * @author juri
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 1;

	public static int PORT_ARRIVAL = 2;

	public static int PORT_BOTH = 3;

	private static final String MAP_OBJECT_ATTR_NAME = "__map__file_";

	public class MapItem {

		public String label;

		public double x;

		public double y;

		public double size;

		public int portType;
		
		private String[] usedAttrs;

		public MapItem(String label, double x, double y, double size, String[] usedAttrs, int portType) {
			this.label = label;
			this.x = x;
			this.y = y;
			this.size = size;
			this.portType = portType;
			this.usedAttrs = usedAttrs;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getPortType() {
			return portType;
		}

		public void setPortType(int portType) {
			this.portType = portType;
		}

		public double getSize() {
			return size;
		}

		public void setSize(double size) {
			this.size = size;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof MapItem)) {
				return false;
			}
			MapItem that = (MapItem)o;
			return this.x == that.x && this.y == that.y;
		}

		public String[] getUsedAttrs() {
			return usedAttrs;
		}

		public void setUsedAttrs(String[] usedAttrs) {
			this.usedAttrs = usedAttrs;
		}

	}
	
	private class MapItemResponse {
		MapItem[] items;
		double min;
		double max;
	}

	private int category;

	private Conditions conditions;

	private boolean neededQuery = false;

	private MapFileCreator creator;

	private String sessionParam;

	private List pointsOfInterest = new ArrayList();

	private MapItemResponse getMapItems() {

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		this.pointsOfInterest.clear();
		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
		localCondition.addCondition(VoyageIndex.getRecent().addAttributesPrefix("vi."));

		// We will need join condition (to join VoyageIndex and Voyage).
		localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);

		ArrayList response = new ArrayList();
		double[] minmax = executeMapQuery(response, localCondition, new String[] {"majbuypt", "slaximp"}, 
				new String[] { "v.majbuypt.name",
				"case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end" }, new String[] { "v.majbuypt.name" },
				new String[] { "sum(v.slaximp)" }, PORT_DEPARTURE);
		min = minmax[1];
		max = minmax[0];
		minmax = executeMapQuery(response, localCondition, new String[] {"majselpt", "slamimp"},  
				new String[] { "v.majselpt.name",
				"case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end" }, new String[] { "v.majselpt.name" },
				new String[] { "sum(v.slamimp)" }, PORT_ARRIVAL);
		if (min > minmax[1]) {
			min = minmax[1];
		}
		if (max < minmax[0]) {
			max = minmax[0];
		}
		
		MapItemResponse mapresponse = new MapItemResponse();
		mapresponse.items = (MapItem[]) response.toArray(new MapItem[] {});
		mapresponse.max = max;
		mapresponse.min = min;
		return mapresponse;
	}

	private double[] executeMapQuery(List response, Conditions localCondition, String[] usedAttrs, String[] populatedAttrs, String[] groupBy,
			String[] orderBy, int type) {

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);

		for (int i = 0; i < populatedAttrs.length; i++) {
			qValue.addPopulatedAttribute(populatedAttrs[i], false);
		}

		qValue.setGroupBy(groupBy);
		qValue.setOrderBy(orderBy);
		qValue.setOrder(QueryValue.ORDER_ASC);

		Object[] voyages = qValue.executeQuery();

		for (int i = 0; i < voyages.length; i++) {
			String portName = (String) ((Object[]) voyages[i])[0];
			GISPortLocation gisPort = GISPortLocation.getGISPortLocation(portName);
			if (gisPort != null) {

				if (this.pointsOfInterest.contains(gisPort)) {
					MapItem item = (MapItem) response.get(response.indexOf(new MapItem(portName, gisPort.getX(),
							gisPort.getY(), ((Number) ((Object[]) voyages[i])[1]).doubleValue(), 
							usedAttrs, type)));
					item.setPortType(PORT_BOTH);
					PointOfInterest point = (PointOfInterest)this.pointsOfInterest.get(this.pointsOfInterest.indexOf(gisPort));
					point.setText(buildToolTipInfo(gisPort, usedAttrs, item.getUsedAttrs(), item.getSize(), ((Number) ((Object[]) voyages[i])[1]).doubleValue()));
				} else {
					this.pointsOfInterest.add(new PointOfInterest(gisPort.getX(), gisPort.getY(),
							gisPort.getPortName(), buildToolTipInfo(gisPort, usedAttrs, (Object[]) voyages[i])));
					response.add(new MapItem(portName, gisPort.getX(), gisPort.getY(),
							((Number) ((Object[]) voyages[i])[1]).doubleValue(), 
							usedAttrs, type));
				}
			}
		}
		
		return new double[] {
				((Number) ((Object[]) voyages[voyages.length - 1])[1]).doubleValue(),
				((Number) ((Object[]) voyages[0])[1]).doubleValue()				
		};

	}

	private String buildToolTipInfo(GISPortLocation port, String[] usedAttrs, Object[] data) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append(Voyage.getAttribute(usedAttrs[0])).append(": ").append(port.getPortName());
		buffer.append("</b><br/>");
		buffer.append(Voyage.getAttribute(usedAttrs[1]).getUserLabelOrName()).append(": ");
		buffer.append(((Number) data[1]).intValue());
		buffer.append("</div>");

		return buffer.toString();
	}
	
	private String buildToolTipInfo(GISPortLocation port, String[] usedAttrs1, String[] usedAttrs2, double data1, double data2) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append("Port name: ").append(port.getPortName());
		buffer.append("</b><br/>");
		buffer.append(Voyage.getAttribute(usedAttrs1[1]).getUserLabelOrName()).append(": ");
		buffer.append((int)data1).append("<br/>");
		buffer.append(Voyage.getAttribute(usedAttrs2[1]).getUserLabelOrName()).append(": ");
		buffer.append((int)data2);
		buffer.append("</div>");

		return buffer.toString();
	}

	public MapItem[] getPorts() {
		if (conditions != null) {
			return this.getMapItems().items;
		}
		return new MapItem[] {};
	}

	public synchronized String getMapPath() {

		try {

			if (this.neededQuery) {

				MapItemResponse response = this.getMapItems(); 
				MapItem[] items = response.items;

				if (this.creator == null) {
					this.creator = new MapFileCreator();
				}

				if (items.length > 0) {
					this.creator.setMapData(items, response.min, response.max);
				}
				if (this.creator.createMapFile()) {
					sessionParam = MAP_OBJECT_ATTR_NAME + System.currentTimeMillis();
					ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
					((HttpSession) servletContext.getSession(true)).setAttribute(sessionParam, creator.getFilePath());

				} else {
					return null;
				}

				neededQuery = false;
			}

			return sessionParam;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * Sets currently chosen search parameters.
	 * 
	 * @param params
	 */
	public void setConditions(SearchParameters params) {
		if (params == null) {
			return;
		}
		this.category = params.getCategory();
		Conditions conditions = params.getConditions();
		if (conditions != null && !conditions.equals(this.conditions)) {
			this.conditions = conditions;
			this.neededQuery = true;
		}

	}

	public PointOfInterest[] getPointsOfInterest() {
		System.out.println("returning " + pointsOfInterest.size());
		return (PointOfInterest[]) pointsOfInterest.toArray(new PointOfInterest[] {});
	}

	public void setPointsOfInterest(PointOfInterest[] pointsOfInterest) {
		this.pointsOfInterest = Arrays.asList(pointsOfInterest);
	}

}
