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

public class ImageFeederServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		OutputStream stream = response.getOutputStream();
		
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		String path = request.getParameter("path");
		if (path != null) {
			JFreeChart chart = (JFreeChart)session.getAttribute(path);
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(stream, chart, width, height);
		}
		stream.close();			
	}
}
