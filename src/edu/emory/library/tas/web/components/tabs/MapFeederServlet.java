package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.umn.gis.mapscript.imageObj;
import edu.umn.gis.mapscript.mapObj;

public class MapFeederServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		OutputStream stream = response.getOutputStream();
		
		//Redeem attributes passed in path
		String path = request.getParameter("path");
		
		if (path != null) {
			//Prepare image
			String file = (String)session.getAttribute(path);
			if (file != null) {
				
				mapObj map = new mapObj(file);
				map.setSize(800, 600);
				map.setExtent(0, 0, 60, 60);				
				imageObj img = map.draw();
				
				response.setContentType("image/png");
				//Write image
				stream.write(img.getBytes());
				
				img.delete();
				map.delete();
			}
		}
		//close streams
		stream.close();			
	}
}
