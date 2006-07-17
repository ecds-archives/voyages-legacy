package edu.emory.library.tas.web.components.tabs;

import java.io.File;
import java.io.FileOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.styleObj;

public class MapBean {

	private static final String MAP_OBJECT_ATTR_NAME = "__map__object_bytes";
	private static final String IMAGE_FEEDED_SERVLET = "servlet/MapFeederServlet";
	private static boolean linked = false;
	static {
		try {
			Class.forName("MapscriptLoader");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMapPath() {
		
		
	    mapObj map = new mapObj("gis/tests/voyages.map");
//	    layerObj layer = new layerObj(map);
//	    classObj obj = new classObj(layer);
//	    obj.
	    map.getImagecolor().setRGB(153, 153, 204);
	    styleObj st = map.getLayer(1).getClass(0).getStyle(0);
	    st.getColor().setHex("#000000");
	    imageObj img = map.draw();
	    
	    ExternalContext servletContext = FacesContext.getCurrentInstance().getExternalContext();
		((HttpSession) servletContext.getSession(true)).setAttribute(MAP_OBJECT_ATTR_NAME, img.getBytes());

		
		return IMAGE_FEEDED_SERVLET + "?path=" + MAP_OBJECT_ATTR_NAME;
	}
	
}
