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

public class UploadOld
{
	
	private final static int BUF_SIZE = 4*1024;
	private final static byte[] BLANK_LINE = new byte[] {13, 10};
	
	private final static int READING_START = 0;
	private final static int READING_HEADERS = 1;
	private final static int READING_FILE = 2;
	private final static int READING_OTHER = 3;
	private final static int SKIPPING_HEDADERS = 4;
	private final static int READING_DONE = 5;
	private final static int READ_BLANK_LINE = 6;
	
	private byte[] midBoundary;
	private byte[] finBoundary;
	private ArrayList files = new ArrayList();
	
	private HttpServletRequest request;
	private String saveToDir = null;
	private Map saveAs = null;
	private ProgressIndicator progressIndicator = null;
	
	public UploadOld(HttpServletRequest request)
	{
		this.request = request;
	}

	public UploadOld(HttpServletRequest request, String saveToDir)
	{
		this.request = request;
		this.saveToDir = saveToDir;
	}

	public UploadOld(HttpServletRequest request, String saveToDir, ProgressIndicator progressIndicator)
	{
		this.request = request;
		this.saveToDir = saveToDir;
		this.progressIndicator = progressIndicator;
	}

	public UploadOld(HttpServletRequest request, String saveToDir, Map saveAs, ProgressIndicator progressIndicator)
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

	private boolean startsWith(byte []haystack, byte[] needle)
	{
		int i;
		for (i=0; i<haystack.length && i<needle.length && haystack[i] == needle[i]; i++);
		return i == needle.length;
	}
	
	private boolean findBoundary()
	{
		
		HttpHeader header = new HttpHeader(request.getContentType(), false);
		if (header.getMainValue().compareTo("multipart/form-data") != 0)
			return false;
		
		String boundary = header.getNamedPart("boundary");
		if (boundary == null)
			return false;
		
		midBoundary = ("--" + boundary).getBytes();
		finBoundary = ("--" + boundary + "--").getBytes();
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
		
		// find MIME boundary from the Content-Type header
		if (!findBoundary())
			return false;
		
		// get POST data as a stream
		ServletInputStream is = request.getInputStream();
		
		// current file
		BufferedOutputStream currFileStream = null; 
		UploadedFile currFile = null;
		HttpHeader contentType = null;
		HttpHeader contentDisp = null;
		boolean fieldDone = false;
		
		// prepare for the main loop
		byte[] currLine = new byte[BUF_SIZE];
		byte[] nextLine = new byte[BUF_SIZE];
		int state = READING_START;
		int read = 0;

		// main loop
		while (state != READING_DONE && (read = is.readLine(currLine, 0, BUF_SIZE)) != -1)
		{
			
			fieldDone = false;
			
			if (state == READING_START)
			{
				if (!startsWith(currLine, midBoundary))
				{
					return false;
				}
				else
				{
					state = READING_HEADERS;
				}
			}
			
			else if (state == READING_OTHER)
			{
				if (startsWith(currLine, finBoundary))
				{
					state = READING_DONE;
				}
				else if (startsWith(currLine, midBoundary))
				{
					state = READING_HEADERS;
				}
			}
			
			else if (state == READING_FILE)
			{
				if (read == BLANK_LINE.length && startsWith(currLine, BLANK_LINE))
				{
					state = READ_BLANK_LINE;
				}
				else if (startsWith(currLine, finBoundary))
				{
					fieldDone = true;
					state = READING_DONE;
				}
				else if (startsWith(currLine, midBoundary))
				{
					fieldDone = true;
					state = READING_HEADERS;
				}
				else
				{
					currFileStream.write(currLine, 0, read);
					currFile.incrementFileSize(read);
					updateProgressIndicator(currFile);
				}
			}
			
			else if (state == READ_BLANK_LINE)
			{
				if (startsWith(currLine, finBoundary))
				{
					fieldDone = true;
					state = READING_DONE;
				}
				else if (startsWith(currLine, midBoundary))
				{
					state = READING_HEADERS;
				}
				else
				{
					currFileStream.write(BLANK_LINE);
					currFile.incrementFileSize(BLANK_LINE.length);
					updateProgressIndicator(currFile);
					state = READING_FILE;
				}
			}

			else if (state == SKIPPING_HEDADERS)
			{
				if (startsWith(currLine, BLANK_LINE))
				{
					state = READING_FILE;
				}
			}

			else if (state == READING_HEADERS)
			{
				if (startsWith(currLine, BLANK_LINE))
				{
					
					System.out.println("Headers end: " + "Content-Disposition: " + contentDisp.getMainValue());
					System.out.println(contentDisp.getMainValue().equals("form-data"));
					System.out.println(contentDisp.getNamedPart("filename"));

					// uploaded file
					if (contentDisp != null && contentDisp.getMainValue().equals("form-data") && contentDisp.getNamedPart("filename") != null)
					{
						
						state = READING_FILE;

						// does client gave as a name?
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
						
						System.out.println("Found file: " + contentDisp.getNamedPart("filename") + " -> " + serverFileName);
						
					}
					
					// normal field
					else
					{
						state = READING_OTHER;
					}
					
				}
				else
				{
					String headerLine = new String(currLine, 0, read);
					System.out.println("Header: " + headerLine);
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
			}
			
			if (fieldDone)
			{
				if (currFile != null)
				{
					currFileStream.close();
					files.add(currFile);
				}
				currFile = null;
				contentType = null;
				contentDisp = null;
			}
			
			byte[] tempLine = currLine;
			currLine = nextLine;
			nextLine = tempLine;
			
		}
		
		if (currFile != null)
			currFileStream.close();
		
		return true;
		
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