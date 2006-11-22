package edu.emory.library.tast.ui.maps.component;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.emory.library.tast.AppConfig;
import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet {

	private static final long serialVersionUID = 5538255560078657125L;

	private final static double SCALE_FACTOR = AppConfig.getConfiguration().getDouble(AppConfig.MAP_SCALE_FACTOR);

	public static final int META_SIZE_X = 160;
	public static final int META_SIZE_Y = 60;

	private TileCache cache = new TileCache();
	private int cacheHits = 0;
	private int cacheMisses = 0;

	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

//		response.sendRedirect("../blank.png");
//		if (true) {
//			return;
//		}
		
		String mapFile = request.getParameter("m");

		int col = 0;
		int row = 0;
		int scale = 0;
		int tileWidth = 0;
		int tileHeight = 0;

		try {
			col = Integer.parseInt(request.getParameter("c"));
			row = Integer.parseInt(request.getParameter("r"));
			scale = Integer.parseInt(request.getParameter("s"));
			tileWidth = Integer.parseInt(request.getParameter("w"));
			tileHeight = Integer.parseInt(request.getParameter("h"));
		} catch (NumberFormatException nfe) {
			response.sendRedirect("../blank.png");
			return;
		}

		cache.clean();

		byte[] cachedImg = cache.get(mapFile, col, row, scale);
		if (cachedImg != null)
		{
			cacheHits++;
			response.getOutputStream().write(cachedImg);
			return;
		}
		cacheMisses++;

		String path = (String) session.getAttribute(mapFile);
		//path = "/home/juri/gis/tests/map_test.map";
		if (path == null) {
			response.sendRedirect("../blank.png");
			return;
		}
		
		double realScale = (double) scale / SCALE_FACTOR;

		double realTileWidth = (double) tileWidth / realScale;
		double realTileHeight = (double) tileHeight / realScale;

		double x1 = col * realTileWidth - META_SIZE_X / realScale;
		double y1 = row * realTileHeight - META_SIZE_Y / realScale;
		double x2 = (col + 1) * realTileWidth + META_SIZE_X / realScale;
		double y2 = (row + 1) * realTileHeight + META_SIZE_Y / realScale;

		mapObj map = new mapObj(path);
		map.setSize(tileWidth + 2 * META_SIZE_X, tileHeight + 2 * META_SIZE_Y);
		map.setExtent(x1, y1, x2, y2);

		//map.setMetaData("labelcache_map_edge_buffer", (META_SIZE_X) + "");

		imageObj img = map.draw();
		byte[] imgBytes = img.getBytes();

		Image image = ImageIO.read(new ByteArrayInputStream(imgBytes));
		BufferedImage rimage = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D gr = rimage.createGraphics();
		
		gr.drawImage(
				image, 0, 0, tileWidth, tileHeight,
				META_SIZE_X, META_SIZE_Y, META_SIZE_X + tileWidth, META_SIZE_Y + tileHeight,
				null);
		
		gr.dispose();

		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		ImageIO.write(rimage, "png", oStream);
		imgBytes = oStream.toByteArray();

		cache.put(mapFile, col, row, scale, imgBytes);

		response.getOutputStream().write(imgBytes);
		
		img.delete();
		map.delete();

	}

	public void init() throws ServletException
	{
	}

}
