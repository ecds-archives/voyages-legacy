package edu.emory.library.tas.web.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			PrintWriter writer = response.getWriter();
			writer.println("<html><head></head><body>");

			Upload upload = new Upload(request, "C:\\");
			upload.setSaveAs("file1", "voyages.dat");
			
			if (upload.upload())
			{
				response.getWriter().println("<h1>Uploaded files</h1>");
				for (Iterator iterFile = upload.getFiles().iterator(); iterFile.hasNext();)
				{
					UploadedFile file = (UploadedFile) iterFile.next();
					writer.println("<b>ClientFileName</b> = " + file.getClientFileName() + "<br/>");
					writer.println("<b>ClientFileNameWithoutParent</b> = " + file.getClientFileNameWithoutParent() + "<br/>");
					writer.println("<b>ServerFileName</b> = " + file.getServerFileName() + "<br/>");
					writer.println("<b>ContentType</b> = " + file.getContentType() + "<br/>");
					writer.println("<b>FileSize</b> = " + file.getFileSize() + "<br/>");
					writer.println("<hr/>");
				}
			}
			else
			{
				response.getWriter().println("Some problem.");
			}
			
			writer.println("</body></html>");
			
		}
		catch (Exception e)
		{
			e.printStackTrace(response.getWriter());
		}
	}

}