package edu.emory.library.tas.maps.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class TileServlet extends HttpServlet
{

	private static final long serialVersionUID = 5538255560078657125L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		long timeStart = System.currentTimeMillis();
		
		String mapFile = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\shapefiles\\voyages.map";
		
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
		img.getBytes();

		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache");
		response.getOutputStream().write(img.getBytes());
		
		long timeEnd = System.currentTimeMillis();
		System.out.println(timeEnd - timeStart);

	}
	
	public void init() throws ServletException
	{
		System.loadLibrary("mapscript");
	}

}
