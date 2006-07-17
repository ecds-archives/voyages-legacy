package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MapFeederServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		OutputStream stream = response.getOutputStream();
		
		//Redeem attributes passed in path
		String path = request.getParameter("path");
		
		if (path != null) {
			//Prepare image
			byte[] picture = (byte[])session.getAttribute(path);
			if (picture != null) {
				response.setContentType("image/png");
				//Write image
				stream.write(picture);
			}
		}
		//close streams
		stream.close();			
	}
}
