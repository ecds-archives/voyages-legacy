package edu.emory.library.tas.web.maps;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageWriterSpi;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import com.keypoint.PngEncoder;
import com.sun.imageio.plugins.png.PNGImageWriter;

import sun.awt.image.PNGImageDecoder;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet {

	public static final int META_SIZE_X = 20;
	
	public static final int META_SIZE_Y = 20;

	public static final long CLEAN_PERIOD = 10000;

	public static final long MAX_CACHE = 104857600;

	private static final long serialVersionUID = 5538255560078657125L;

	private static Map cache = new HashMap();

	private static long sizeOfCache = 0;

	private long lastClean = 0;

	public TileServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

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

		CachedTile tile = new CachedTile(mapFile, col, row, scale);
		byte[] cachedObject;
		synchronized (cache) {
			cachedObject = (byte[]) cache.get(tile);
		}

		clean();

		if (cachedObject == null) {
			String path = (String) session.getAttribute(mapFile);
			if (path != null) {
				double realTileWidth = (double) tileWidth / (double) scale;
				double realTileHeight = (double) tileHeight / (double) scale;
				double x1 = col * realTileWidth - META_SIZE_X / (double) scale;
				double y1 = row * realTileHeight - META_SIZE_Y / (double) scale;
				double x2 = (col + 1) * realTileWidth + META_SIZE_X / (double) scale;
				double y2 = (row + 1) * realTileHeight + META_SIZE_Y / (double) scale;

				mapObj map = new mapObj(path);
				map.setSize(tileWidth + 2 * META_SIZE_X, tileHeight + 2 * META_SIZE_Y);
				map.setExtent(x1, y1, x2, y2);

				map.setMetaData("labelcache_map_edge_buffer", ((int)-Math.max(META_SIZE_X, META_SIZE_Y)) + "");

				imageObj img = map.draw();
				byte[] imgBytes = img.getBytes();
				//img.save("/home/juri/gis/pleple1" + col + "_" + row + ".png", map);
				Image image = Toolkit.getDefaultToolkit().createImage(imgBytes);
				RenderedImage rimage = toBufferedImage(Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), new CropImageFilter(META_SIZE_X, META_SIZE_Y, tileWidth,
								tileHeight))));
				
				ByteArrayOutputStream oStream = new ByteArrayOutputStream();
				ImageIO.write(rimage, "png", oStream);

				imgBytes = oStream.toByteArray();
				
				response.getOutputStream().write(imgBytes);

				
				synchronized (cache) {
					if (sizeOfCache < MAX_CACHE) {
						cache.put(tile, imgBytes);
						sizeOfCache += imgBytes.length;
					}
				}

			} else {
				response.sendRedirect("blank.png");
				return;
			}
		} else {
			synchronized (cache) {
				cache.put(tile, cachedObject);
			}
			response.getOutputStream().write(cachedObject);
			return;
		}

	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	protected void clean() {

		long time = System.currentTimeMillis();
		if (lastClean + CLEAN_PERIOD > time) {
			return;
		}
		lastClean = time;

		ArrayList toRemove = new ArrayList();
		synchronized (cache) {
			Iterator iter = cache.keySet().iterator();
			while (iter.hasNext()) {
				CachedTile tile = (CachedTile) iter.next();
				if (tile.isExpired(time)) {
					toRemove.add(tile);
				}
			}
			iter = toRemove.iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				byte[] bytes = (byte[]) cache.get(key);
				sizeOfCache -= bytes.length;
				cache.remove(key);
			}
		}
	}

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
