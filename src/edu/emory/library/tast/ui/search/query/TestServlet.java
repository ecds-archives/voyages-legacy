package edu.emory.library.tast.ui.search.query;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class TestServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest arg0, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			response.getWriter().println(transformer.getClass().toString());
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		}
	}

}
