package edu.emory.library.tas.maps.test;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet
{

	private static final long serialVersionUID = 5538255560078657125L;
	
	private static Map cache = new Hashtable();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		long timeStart = System.currentTimeMillis();
		
		response.setContentType("image/png");
		response.setDateHeader("Expires", (new Date()).getTime() + 1000 * 60 * 60);
		response.setHeader("Cache-Control", "public");

		byte[] imgBytes = (byte[]) cache.get(request.getQueryString());
		
		if (imgBytes == null)
		{
			
			//String mapFile = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\shapefiles\\voyages.map";
			String mapFile = "D:\\Library\\SlaveTrade\\shapefiles\\voyages.map";
			
			double x = 0;
			double y = 0;
			double scale = 0;
			int width = 0;
			int height = 0;
			
			try
			{
				x = Double.parseDouble(request.getParameter("x"));
				y = Double.parseDouble(request.getParameter("y"));
				scale = Double.parseDouble(request.getParameter("s"));
				width = Integer.parseInt(request.getParameter("w"));
				height = Integer.parseInt(request.getParameter("h"));
			}
			catch (NumberFormatException nfe)
			{
				response.sendRedirect("blank.png");
				return;
			}
			
			double x2 = x + ((double)width) / scale;
			double y2 = y + ((double)height) / scale;
			
			mapObj map = new mapObj(mapFile);
			map.setSize(width, height);
			map.setExtent(x, y, x2, y2);
			
			imageObj img = map.draw();
			imgBytes = img.getBytes();
			
			cache.put(request.getQueryString(), imgBytes);
		
		}
		
		response.getOutputStream().write(imgBytes);

		long timeEnd = System.currentTimeMillis();
		System.out.println(request.getQueryString() + ", time = " + (timeEnd - timeStart));

	}
	
	public void init() throws ServletException
	{
		System.loadLibrary("mapscript");
	}

}
