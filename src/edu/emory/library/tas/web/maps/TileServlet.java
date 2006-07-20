package edu.emory.library.tas.web.maps;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.emory.library.tas.AppConfig;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet {

	private static final long serialVersionUID = 5538255560078657125L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		long timeStart = System.currentTimeMillis();

		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

		// String mapFile = "C:\\Documents and Settings\\zich\\My
		// Documents\\Library\\SlaveTrade\\shapefiles\\voyages.map";
		// String mapFile =
		// "D:\\Library\\SlaveTrade\\shapefiles\\voyages.map";

		String mapFile = request.getParameter("m");

		double col = 0;
		double row = 0;
		double scale = 0;
		int tileWidth = 0;
		int tileHeight = 0;

		try {
			col = Integer.parseInt(request.getParameter("c"));
			row = Integer.parseInt(request.getParameter("r"));
			scale = Integer.parseInt(request.getParameter("s"));
			tileWidth = Integer.parseInt(request.getParameter("w"));
			tileHeight = Integer.parseInt(request.getParameter("h"));
		} catch (NumberFormatException nfe) {
			response.sendRedirect("blank.png");
			return;
		}

		String path = (String) session.getAttribute(mapFile);
		if (path != null) {
			double realTileWidth = (double) tileWidth / (double) scale;
			double realTileHeight = (double) tileHeight / (double) scale;
			double x1 = col * realTileWidth;
			double y1 = row * realTileHeight;
			double x2 = (col + 1) * realTileWidth;
			double y2 = (row + 1) * realTileHeight;

			mapObj map = new mapObj(path);
			map.setSize(tileWidth, tileHeight);
			map.setExtent(x1, y1, x2, y2);

			imageObj img = map.draw();
			response.getOutputStream().write(img.getBytes());
		} else {
			response.sendRedirect("blank.png");
			return;
		}

		long timeEnd = System.currentTimeMillis();
		System.out.println(request.getQueryString() + ", time = " + (timeEnd - timeStart));

	}

}
