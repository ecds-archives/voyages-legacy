package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class MapFeederServlet extends HttpServlet {

	private static final long serialVersionUID = 5538255560078657125L;

	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
        
		int a = 0;
   		long timeStart = System.currentTimeMillis();
		byte[] imgBytes;

		if (a == 0) {
			a = 1;
		}
		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

		// String mapFile = "C:\\Documents and Settings\\zich\\My
		// Documents\\Library\\SlaveTrade\\shapefiles\\voyages.map";

		double x = 0;
		double y = 0;
		double scale = 0;
		int width = 0;
		int height = 0;
		String path;

		try {
			x = Double.parseDouble(request.getParameter("x"));
			y = Double.parseDouble(request.getParameter("y"));
			scale = Double.parseDouble(request.getParameter("s"));
			width = Integer.parseInt(request.getParameter("w"));
			height = Integer.parseInt(request.getParameter("h"));
			path = request.getParameter("path");
		} catch (NumberFormatException nfe) {
			response.sendRedirect("blank.png");
			return;
		}

		if (path != null) {
			// Prepare image
			String mapFile = (String) session.getAttribute(path);
			if (mapFile != null) {

				
				
				double x2 = x + ((double) width) / scale;
				double y2 = y + ((double) height) / scale;
//
//				mapObj map = new mapObj(mapFile);
//				map.setSize(width, height);
//				map.setExtent(x, y, x2, y2);
//
//				imageObj img = map.draw();
//				imgBytes = img.getBytes();
//
//				response.getOutputStream().write(imgBytes);
//
//				img.delete();
//				map.delete();
				
				long timeEnd = System.currentTimeMillis();
				System.out.println(request.getQueryString() + ", time = " + (timeEnd - timeStart));
			}
		}

	}

//	public void init() throws ServletException {
//		System.loadLibrary("mapscript");
//	}

}

/**
 * package edu.emory.library.tas.web.components.tabs;
 * 
 * import java.io.IOException; import java.io.OutputStream;
 * 
 * import javax.servlet.ServletException; import javax.servlet.http.HttpServlet;
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import
 * javax.servlet.http.HttpSession;
 * 
 * import edu.umn.gis.mapscript.imageObj; import edu.umn.gis.mapscript.mapObj;
 * 
 * public class MapFeederServlet extends HttpServlet {
 * 
 * private static final long serialVersionUID = 1L;
 * 
 * protected void doGet(HttpServletRequest request, HttpServletResponse
 * response) throws ServletException, IOException {
 * 
 * HttpSession session = request.getSession(); OutputStream stream =
 * response.getOutputStream();
 * 
 * //Redeem attributes passed in path String path =
 * request.getParameter("path");
 * 
 * if (path != null) { //Prepare image String file =
 * (String)session.getAttribute(path); if (file != null) {
 * 
 * mapObj map = new mapObj(file); map.setSize(800, 600); map.setExtent(0, 0, 60,
 * 60); imageObj img = map.draw();
 * 
 * response.setContentType("image/png"); //Write image
 * stream.write(img.getBytes());
 * 
 * img.delete(); map.delete(); } } //close streams stream.close(); } }
 */
