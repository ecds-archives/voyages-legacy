package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Servlet generating PNG image from stored JFreeChart session object.
 * @author Pawel Jurczyk
 *
 */
public class ImageFeederServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		OutputStream stream = response.getOutputStream();
		
		//Redeem attributes passed in path
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		String path = request.getParameter("path");
		
		if (path != null) {
			//Prepare image
			JFreeChart chart = (JFreeChart)session.getAttribute(path);
			response.setContentType("image/png");
			//Write image
			ChartUtilities.writeChartAsPNG(stream, chart, width, height);
		}
		//close streams
		stream.close();			
	}
}
