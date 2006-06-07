package edu.emory.library.tas.web.upload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServletTest extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		PrintWriter writer = response.getWriter();
		
		writer.println("<html><head></head><body>");
		writer.println("Content-Type: " + request.getContentType() + "<br/>");
		writer.println("Content-Length: " + request.getContentLength() + "<br/>");
		writer.println("<form><textarea rows=\"30\" cols=\"50\">");

		try
		{
		
			FileOutputStream file = new FileOutputStream("test.req");
			
			byte[] buf = new byte[1024];
			ServletInputStream is = request.getInputStream();
			//InputStreamReader reader = new InputStreamReader(is);
			
			int read = 0;
			while ((read = is.readLine(buf, 0, 1024)) != -1)
			{
				//writer.print(buf);
				file.write(buf, 0, read);
			}
			file.close();
		

		}
		catch (IOException ioe)
		{
			writer.println("Problem: " + ioe.getMessage());
		}
		
		writer.println("</textarea></form>");
		writer.println("</body></head></html>");

	}

}