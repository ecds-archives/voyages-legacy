package edu.emory.library.tas.web.components.tabs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.DirectValue;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.SearchParameters;
import edu.emory.library.tas.web.components.tabs.mapFile.MapFileCreator;
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
 * --verbose=yes
 * 
 * @author juri
 * 
 */
public class MapBean {

	public static int PORT_DEPARTURE = 1;

	public static int PORT_ARRIVAL = 2;

	private static final String MAP_OBJECT_ATTR_NAME = "__map__object_bytes";

	private static final String IMAGE_FEEDED_SERVLET = "servlet/MapFeederServlet";

	private static boolean linked = false;

	// static {
	// try {
	// Class.forName("MapscriptLoader");
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public class MapItem {

		public String label;

		public float x;

		public float y;

		public float size;

		public int portType;

		public MapItem(String label, float x, float y, float size, int portType) {
			this.label = label;
			this.x = x;
			this.y = y;
			this.size = size;
			this.portType = portType;
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

		public float getSize() {
			return size;
		}

		public void setSize(float size) {
			this.size = size;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

	}

	private int category;

	private Conditions conditions;

	private boolean neededQuery = false;

	private MapFileCreator creator;

	private MapItem[] getMapItems() {

		Conditions localCondition = this.conditions.addAttributesPrefix("v.");
		localCondition.addCondition(VoyageIndex.getRecent().addAttributesPrefix("vi."));

		// We will need join condition (to join VoyageIndex and Voyage).
		localCondition.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);

		QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);

		qValue.addPopulatedAttribute("v.portdep.name", false);
		qValue.addPopulatedAttribute("sum(v.slaximp)", false);
		qValue.setGroupBy(new String[] { "v.portdep.name" });
		qValue.setOrderBy(new String[] {"sum(v.slaximp)"});
		
		Object[] voyages = qValue.executeQuery();

		ArrayList response = new ArrayList();
		for (int i = 0; i < voyages.length; i++) {
			String portName = (String) ((Object[]) voyages[i])[0];
			GISPortLocation gisPort = GISPortLocation.getGISPortLocation(portName);
			if (gisPort != null) {
				response.add(new MapItem(portName, gisPort.getX(), gisPort.getY(),
						((Number) ((Object[]) voyages[i])[1]).floatValue(), PORT_DEPARTURE));
			}
		}
		
		
		qValue = new QueryValue("VoyageIndex as vi, Voyage v", localCondition);

		qValue.addPopulatedAttribute("v.portret.name", false);
		qValue.addPopulatedAttribute("sum(v.slaximp)", false);
		qValue.setGroupBy(new String[] { "v.portret.name" });
		qValue.setOrderBy(new String[] {"sum(v.slaximp)"});
		
		voyages = qValue.executeQuery();
		
		for (int i = 0; i < voyages.length; i++) {
			String portName = (String) ((Object[]) voyages[i])[0];
			GISPortLocation gisPort = GISPortLocation.getGISPortLocation(portName);
			if (gisPort != null) {
				response.add(new MapItem(portName, gisPort.getX(), gisPort.getY(),
						((Number) ((Object[]) voyages[i])[1]).floatValue(), PORT_ARRIVAL));
			}
		}

		return (MapItem[]) response.toArray(new MapItem[] {});
	}

	public MapItem[] getPorts() {
		if (conditions != null) {
			return this.getMapItems();
		}
		return new MapItem[] {};
	}

	public synchronized String getMapPath() {

		if (this.neededQuery) {

			MapItem[] items = this.getMapItems();

			
				if (this.creator == null) {
					this.creator = new MapFileCreator();
				}

				if (items.length > 0) {
					this.creator.setMapData(items, items[0].size, items[items.length - 1].size);
				}
				if (this.creator.createMapFile()) {
					ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
					((HttpSession) servletContext.getSession(true)).setAttribute(MAP_OBJECT_ATTR_NAME, creator
							.getFilePath());
				} else {
					return null;
				}

				neededQuery = false;
		}

		return IMAGE_FEEDED_SERVLET + "?path=" + MAP_OBJECT_ATTR_NAME;
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

}
