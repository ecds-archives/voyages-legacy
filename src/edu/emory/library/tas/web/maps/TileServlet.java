package edu.emory.library.tas.web.maps;

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

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet {

	private static final long serialVersionUID = 5538255560078657125L;

	private final static double SCALE_FACTOR = 1000.0;
	public static final int META_SIZE_X = 160; // 160;
	public static final int META_SIZE_Y = 60; //60;

	private TileCache cache = new TileCache();
	private int cacheHits = 0;
	private int cacheMisses = 0;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

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

		// CachedTileKey tile = new CachedTileKey(mapFile, col, row, scale);
		// byte[] cachedObject;
		// synchronized (cache) {
		// cachedObject = (byte[]) cache.get(tile);
		// }

		cache.clean();

		byte[] cachedImg = cache.get(mapFile, col, row, scale);
		if (cachedImg != null) {
			cacheHits++;
			response.getOutputStream().write(cachedImg);
			return;
		}
		cacheMisses++;

		System.out.println(((double) cacheHits / ((double) cacheHits + cacheMisses) * 100) + "%");

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
		
		//gr.drawImage(image, 0, 0, tileWidth+2*META_SIZE_X, tileHeight+2*META_SIZE_Y, null);
		gr.drawImage(image, 0, 0, tileWidth, tileHeight, META_SIZE_X, META_SIZE_Y, META_SIZE_X + tileWidth, META_SIZE_Y
				+ tileHeight, null);
		gr.dispose();

		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		ImageIO.write(rimage, "png", oStream);
		imgBytes = oStream.toByteArray();

		cache.put(mapFile, col, row, scale, imgBytes);

		response.getOutputStream().write(imgBytes);
		
		img.delete();
		map.delete();

	}

	public void init() throws ServletException {
	}

	/*
	 * public static BufferedImage toBufferedImage(Image image) { if (image
	 * instanceof BufferedImage) { return (BufferedImage) image; }
	 *  // This code ensures that all the pixels in the image are loaded image =
	 * new ImageIcon(image).getImage();
	 *  // Determine if the image has transparent pixels; for this method's //
	 * implementation, see e661 Determining If an Image Has Transparent //
	 * Pixels
	 *  // Create a buffered image with a format that's compatible with the //
	 * screen BufferedImage bimage = null; GraphicsEnvironment ge =
	 * GraphicsEnvironment.getLocalGraphicsEnvironment(); try { // Determine the
	 * type of transparency of the new buffered image int transparency =
	 * Transparency.OPAQUE;
	 *  // Create the buffered image GraphicsDevice gs =
	 * ge.getDefaultScreenDevice(); GraphicsConfiguration gc =
	 * gs.getDefaultConfiguration(); bimage =
	 * gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),
	 * transparency); } catch (HeadlessException e) { // The system does not
	 * have a screen }
	 * 
	 * if (bimage == null) { // Create a buffered image using the default color
	 * model int type = BufferedImage.TYPE_INT_RGB; bimage = new
	 * BufferedImage(image.getWidth(null), image.getHeight(null), type); }
	 *  // Copy image to buffered image Graphics g = bimage.createGraphics();
	 *  // Paint the image onto the buffered image g.drawImage(image, 0, 0,
	 * null); g.dispose();
	 * 
	 * return bimage; }
	 */

	/*
	 * protected void clean() {
	 * 
	 * long time = System.currentTimeMillis(); if (lastClean + CLEAN_PERIOD >
	 * time) { return; } lastClean = time;
	 * 
	 * ArrayList toRemove = new ArrayList(); synchronized (cache) { Iterator
	 * iter = cache.keySet().iterator(); while (iter.hasNext()) { CachedTileKey
	 * tile = (CachedTileKey) iter.next(); if (tile.isExpired(time)) {
	 * toRemove.add(tile); } } iter = toRemove.iterator(); while
	 * (iter.hasNext()) { Object key = iter.next(); byte[] bytes = (byte[])
	 * cache.get(key); sizeOfCache -= bytes.length; cache.remove(key); } } }
	 */
	// public synchronized void init() throws ServletException {
	// super.init();
	// if (threadCleaner == null) {
	// threadCleaner = new Thread(new Runnable() {
	//
	// public void run() {
	// while (true) {
	// TileServlet.this.clean();
	// try {
	// Thread.currentThread().sleep(SLEEP_TIME);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// });
	// threadCleaner.run();
	// }
	// System.out.println("init done");
	// }
	//	
	// public synchronized void destroy() {
	// super.destroy();
	// if (threadCleaner != null) {
	// threadCleaner.destroy();
	// threadCleaner = null;
	// }
	// }
}
