package edu.emory.library.tas.web.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class Upload
{

	private final static int BUF_SIZE = 4*1024;
	
	private byte[] boundary;
	private byte[] newlineAndBoundary;
	private ArrayList files = new ArrayList();
	
	private HttpServletRequest request;
	private String saveToDir = null;
	private Map saveAs = null;
	private ProgressIndicator progressIndicator = null;
	
	public Upload(HttpServletRequest request)
	{
		this.request = request;
	}

	public Upload(HttpServletRequest request, String saveToDir)
	{
		this.request = request;
		this.saveToDir = saveToDir;
	}

	public Upload(HttpServletRequest request, String saveToDir, ProgressIndicator progressIndicator)
	{
		this.request = request;
		this.saveToDir = saveToDir;
		this.progressIndicator = progressIndicator;
	}

	public Upload(HttpServletRequest request, String saveToDir, Map saveAs, ProgressIndicator progressIndicator)
	{
		this.request = request;
		this.saveToDir = saveToDir;
		this.saveAs = saveAs;
		this.progressIndicator = progressIndicator;
	}

	public String getSaveToDir()
	{
		return saveToDir;
	}

	public void setSaveToDir(String saveToDir)
	{
		this.saveToDir = saveToDir;
	}

	public Map getSaveAs()
	{
		return saveAs;
	}

	public void setSaveAs(Map saveAs)
	{
		this.saveAs = saveAs;
	}

	public void setSaveAs(String htmlFieldName, String fileName)
	{
		if (saveAs == null) saveAs = (Map) new Hashtable();
		saveAs.put(htmlFieldName, fileName);
	}

	public ProgressIndicator getProgressIndicator()
	{
		return progressIndicator;
	}

	public void setProgressIndicator(ProgressIndicator progressIndicator)
	{
		this.progressIndicator = progressIndicator;
	}

	private boolean findBoundary()
	{
		
		HttpHeader header = new HttpHeader(request.getContentType(), false);
		if (header.getMainValue().compareTo("multipart/form-data") != 0)
			return false;
		
		String boundaryStr = header.getNamedPart("boundary");
		if (boundaryStr == null)
			return false;
		
		boundary = ("--" + boundaryStr).getBytes();
		newlineAndBoundary = ("\r\n--" + boundaryStr).getBytes();
		return true;

	}
	
	private void updateProgressIndicator(UploadedFile currFile)
	{
		if (progressIndicator != null)
			progressIndicator.add(
					"Uploading file: " +
					currFile.getClientFileNameWithoutParent() + " " +
					"(" + currFile.getFileSize() + "B)");
	}
	
	public boolean upload() throws IOException
	{
		
		// buffer and streams
		byte[] buf = new byte[BUF_SIZE];
		ServletInputStream is = request.getInputStream();
		StreamSplitter splitter = new StreamSplitter(is, buf);
		
		// file and header information
		BufferedOutputStream currFileStream = null; 
		UploadedFile currFile = null;
		HttpHeader contentType = null;
		HttpHeader contentDisp = null;
		
//		buf = new byte[1024*1024];
//		int x = is.read(buf);
//		System.out.println(new String(buf,0,x));
		
		// counters
		int read;
		int totalPartLength;
		boolean done = false;

		try
		{
		
			// find MIME boundary from the Content-Type header
			if (!findBoundary())
				throw new UploadErrorInternalException("Not a valid MIME. No boundary found.");
			
			// the data should start with the boundary
			splitter.gotoNextPart(boundary);
			if ((read = splitter.getNextChunk()) != 0)
				throw new UploadErrorInternalException("No first boundary found.");
	
			// main loop
			while (!done)
			{
				
				// comsume the extra newline before headers
				splitter.gotoNextPart("\r\n".getBytes());
				read = splitter.getNextChunk();

				// end of the data?
				if (read == 2 && buf[0] == '-' && buf[1] == '-')
				{
					done = true;
					break;
				}

				// otherwise there should be no chars before
				// the new line
				else if (read != 0)
				{
					throw new UploadErrorInternalException("No newline after boundary.");
				}
				
				// read headers
				contentType = null;
				contentDisp = null;
				while (true)
				{
					
					splitter.gotoNextPart("\r\n".getBytes());
					read = splitter.getNextChunk();
					String headerLine = new String(buf, 0, read);
				
					// unexpected eof
					if (read == -1)
						throw new UploadErrorInternalException("Unexpected EOF while reading headers.");

					// too long header (probably a garbage)
					if (read == buf.length)
						throw new UploadErrorInternalException("Too long header.");
					
					// end of headers
					if (read == 0)
						break;
					
					// process headers
					//System.out.println(headerLine);
					HttpHeader header = new HttpHeader(headerLine, true);
					if (header.getHeaderName().equals("Content-Type"))
					{
						contentType = header;
					}
					else if (header.getHeaderName().equals("Content-Disposition"))
					{
						contentDisp = header;
					}				
				}
				
				// is it a file?
				currFile = null;
				if (contentDisp != null && contentDisp.getMainValue().equals("form-data") && contentDisp.getNamedPart("filename") != null)
				{
					
					// are we supposed to save it under a given name?
					String serverFileName = null;
					if (saveAs != null)
						serverFileName = (String) saveAs.get(contentDisp.getNamedPart("name"));
					
					// no -> generate random name
					if (serverFileName == null)
						serverFileName = UUID.randomUUID().toString();
					
					// save to the given directory
					if (saveToDir != null)
						serverFileName = saveToDir + File.separator + serverFileName;
					
					// create file record
					currFile = new UploadedFile();
					currFile.setHtmlFieldName(contentDisp.getNamedPart("name"));
					currFile.setClientFileName(contentDisp.getNamedPart("filename"));
					currFile.setServerFieldName(serverFileName);
					if (contentType != null) currFile.setContentType(contentType.getMainValue());
	
					// open file
					currFileStream = new BufferedOutputStream(new FileOutputStream(serverFileName));
					
					//System.out.println("Found file: " + contentDisp.getNamedPart("filename") + " -> " + serverFileName);
				}	
				
				
				// reading data
				totalPartLength = 0;
				splitter.gotoNextPart(newlineAndBoundary);
				while ((read = splitter.getNextChunk()) > 0)
				{
					// save data to the current file
					if (currFile != null)
					{
						currFileStream.write(buf, 0, read);
						currFile.incrementFileSize(read);
						updateProgressIndicator(currFile);
					}
					totalPartLength += read;
				}
				
				// unexpected eof
				if (read == -1)
					throw new UploadErrorInternalException("Unexpected EOF while reading a file." + totalPartLength);
	
				// close file and add it to the list
				if (currFile != null)
				{
					currFileStream.close();
					files.add(currFile);
				}
	
			}
			
			return true;
		
		}
		catch (UploadErrorInternalException ie)
		{
			System.out.println(ie.getMessage());
			if (currFile != null) currFileStream.close();
			files.clear();
			return false;
		}
		
	}
	
	public ArrayList getFiles()
	{
		return files;
	}

	public UploadedFile getFileByHtmlFieldName(String fieldName)
	{
		for (int i=0; i<files.size(); i++)
		{
			UploadedFile file = (UploadedFile) files.get(i);
			if (file.getHtmlFieldName().compareTo(fieldName) == 0)
			{
				return file;
			}
		}
		return null;
	}

}
