package edu.emory.library.tas.web.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet {
	
	public static final long CLEAN_PERIOD = 10000;

	public static final long MAX_CACHE = 104857600;
	
	private static final long serialVersionUID = 5538255560078657125L;

	private static Map cache = new HashMap();

	private static long sizeOfCache = 0;
	
	private long lastClean = 0;

	
	public TileServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//System.out.println("doGet");
		
		HttpSession session = request.getSession();
		//long timeStart = System.currentTimeMillis();

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

		//System.out.println("Size: " + sizeOfCache);
		clean();
		
		if (cachedObject == null) {
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
				byte [] imgBytes = img.getBytes();
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
			//System.out.println("FROM cache: " + tile);
			synchronized (cache) {
				cache.put(tile, cachedObject);
			}
			response.getOutputStream().write(cachedObject);
			return;
		}
		
		//long timeEnd = System.currentTimeMillis();
		//System.out.println(request.getQueryString() + ", time = " + (timeEnd - timeStart));

	}

	protected void clean() {
		
		long time = System.currentTimeMillis();
		if (lastClean + CLEAN_PERIOD > time) {
			return;
		}
		lastClean = time;
		
		ArrayList toRemove = new ArrayList();
		synchronized (cache) {
			//System.out.println("Cleaner!!! " + cache.size());
			Iterator iter = cache.keySet().iterator();
			while (iter.hasNext()) {
				CachedTile tile = (CachedTile) iter.next();
				if (tile.isExpired(time)) {
					//System.out.println("Will remove: " + tile);
					toRemove.add(tile);
				}
			}
			iter = toRemove.iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				byte [] bytes = (byte[])cache.get(key);
				sizeOfCache -= bytes.length;
				cache.remove(key);
			}
			//System.out.println("Cleaner exits. " + cache.size());
		}
	}

//	public synchronized void init() throws ServletException {
//		super.init();
//		if (threadCleaner == null) {
//			threadCleaner = new Thread(new Runnable() {
//
//				public void run() {
//					while (true) {
//						TileServlet.this.clean();
//						try {
//							Thread.currentThread().sleep(SLEEP_TIME);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//
//			});
//			threadCleaner.run();
//		}
//		System.out.println("init done");
//	}
//	
//	public synchronized void destroy() {
//		super.destroy();
//		if (threadCleaner != null) {
//			threadCleaner.destroy();
//			threadCleaner = null;
//		}
//	}

}
