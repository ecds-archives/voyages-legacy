package edu.emory.library.tas.web.components.tabs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.attrGroups.Attribute;
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
		double[] minmax = executeMapQuery(response, localCondition, new Attribute[] {Voyage.getAttribute("majbuypt"), Voyage.getAttribute("slaximp")}, 
				new String[] { "v.majbuypt.name",
				"case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end" }, new String[] { "v.majbuypt.name" },
				new String[] { "sum(v.slaximp)" }, 0, 0);
		min = minmax[1];
		max = minmax[0];
		minmax = executeMapQuery(response, localCondition, new Attribute[] {Voyage.getAttribute("majselpt"), Voyage.getAttribute("slamimp")},  
				new String[] { "v.majselpt.name",
				"case when sum(v.slamimp) is null then 0 else sum(v.slamimp) end" }, new String[] { "v.majselpt.name" },
				new String[] { "sum(v.slamimp)" }, 0, 1);
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

	private double[] executeMapQuery(List response, Conditions localCondition, Attribute[] usedAttrs, String[] populatedAttrs, String[] groupBy,
			String[] orderBy, int shape, int color) {

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
					List attrs = new ArrayList();
					List data = new ArrayList();
					attrs.add(usedAttrs);
					data.add(((Object[]) voyages[i])[1]);
					MapItem item = (MapItem) response.get(response.indexOf(new MapItem(portName, gisPort.getX(),
							gisPort.getY(), data, 
							attrs, shape, color, true)));
					item.setColor(2);
					attrs.addAll(item.getUsedAttrs());
					data.addAll(item.getData());
					PointOfInterest point = (PointOfInterest)this.pointsOfInterest.get(this.pointsOfInterest.indexOf(gisPort));
					point.setText(buildToolTipInfo(gisPort, attrs, data));
				} else {
					List attrs = new ArrayList();
					List data = new ArrayList();
					attrs.add(usedAttrs);
					data.add(((Object[]) voyages[i])[1]);
					this.pointsOfInterest.add(new PointOfInterest(gisPort.getX(), gisPort.getY(),
							gisPort.getPortName(), buildToolTipInfo(gisPort, usedAttrs, (Object[]) voyages[i])));
					response.add(new MapItem(portName, gisPort.getX(), gisPort.getY(),
							data, attrs, shape, color, true));
				}
			}
		}
		
		return new double[] {
				((Number) ((Object[]) voyages[voyages.length - 1])[1]).doubleValue(),
				((Number) ((Object[]) voyages[0])[1]).doubleValue()				
		};

	}

	private String buildToolTipInfo(GISPortLocation port, Attribute[] usedAttrs, Object[] data) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append(usedAttrs[0].getUserLabelOrName()).append(": ").append(port.getPortName());
		buffer.append("</b><br/>");
		buffer.append(usedAttrs[1].getUserLabelOrName()).append(": ");
		buffer.append(((Number) data[1]).intValue());
		buffer.append("</div>");

		return buffer.toString();
	}
	
	private String buildToolTipInfo(GISPortLocation port, List usedAttrs, List data) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append("Port name: ").append(port.getPortName());
		buffer.append("</b><br/>");
		int i = 0;
		for (Iterator iter = usedAttrs.iterator(); iter.hasNext();) {
			Attribute[] element = (Attribute[]) iter.next();
			buffer.append(element[1].getUserLabelOrName()).append(": ");
			buffer.append(((Number)data.get(i)).intValue()).append("<br/>");
			i++;
		}
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
