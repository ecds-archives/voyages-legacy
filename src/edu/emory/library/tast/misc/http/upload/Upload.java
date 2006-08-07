package edu.emory.library.tast.misc.http.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import edu.emory.library.tast.spss.LogWriter;

public class Upload
{
	
	private static long nextUploadId = 0;
	private static Hashtable uploadsInfo = new Hashtable();

	private final static int BUF_SIZE = 4*1024;
	
	private String uploadId = null;
	private UploadInfo uploadInfo = null;

	private byte[] boundary;
	private byte[] newlineAndBoundary;
	private ArrayList files = new ArrayList();
	
	private HttpServletRequest request;
	private String saveToDir = null;
	private Map saveAs = null;
	
	private LogWriter importLog = null;
	
	public Upload(HttpServletRequest request)
	{
		this.request = request;
	}

	public Upload(HttpServletRequest request, String saveToDir)
	{
		this.request = request;
		this.saveToDir = saveToDir;
	}

	public Upload(HttpServletRequest request, String saveToDir, LogWriter importLog)
	{
		this.request = request;
		this.saveToDir = saveToDir;
		this.importLog = importLog;
	}

	public Upload(HttpServletRequest request, String saveToDir, Map saveAs, LogWriter importLog)
	{
		this.request = request;
		this.saveToDir = saveToDir;
		this.saveAs = saveAs;
		this.importLog = importLog;
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

	public LogWriter getProgressIndicator()
	{
		return importLog;
	}

	public void setProgressIndicator(LogWriter importLog)
	{
		this.importLog = importLog;
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
	
	private boolean findUploadId()
	{

		Pattern regex = Pattern.compile("UPLOAD=(\\d+)");
		Matcher matcher = regex.matcher(request.getQueryString());

		if (!matcher.find()) return false;
		uploadId = matcher.group(1);
		return true;
	
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
			
			// find upload id
			if (findUploadId())
				uploadInfo = createUploadInfo(uploadId, request.getContentLength());
			
			// the data should start with the boundary
			splitter.gotoNextPart(boundary);
			if ((read = splitter.getNextChunk()) != 0)
				throw new UploadErrorInternalException("No first boundary found.");

			// update total bytes read
			if (uploadId != null)
				uploadInfo.updateProgress(boundary.length);
			
			// main loop
			while (!done)
			{
				
				// comsume the extra newline before headers
				splitter.gotoNextPart("\r\n".getBytes());
				read = splitter.getNextChunk();
				
				// update total bytes read
				if (uploadId != null)
					uploadInfo.updateProgress(read + 2);

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
					
					// update total bytes read
					if (uploadId != null)
						uploadInfo.updateProgress(read + 2);
				
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
				if (contentDisp != null && contentDisp.getMainValue().equals("form-data") && contentDisp.getNamedPart("filename") != null && !contentDisp.getNamedPart("filename").equals("\"\""))
				{
					
					// determine file name
					String serverFileName = null;
					if (saveAs != null)
					{
						serverFileName = (String) saveAs.get(contentDisp.getNamedPart("name"));
						if (serverFileName != null && saveToDir != null)
								serverFileName = saveToDir + File.separator + serverFileName;
					}
					if (serverFileName == null)
					{
						if (saveToDir != null)
							serverFileName =
								File.createTempFile("upload", null, new File(saveToDir)).getAbsolutePath();
						else
							serverFileName =
								File.createTempFile("upload", null).getAbsolutePath();
					}
					
					// create file record
					currFile = new UploadedFile();
					currFile.setHtmlFieldName(contentDisp.getNamedPart("name"));
					currFile.setClientFileName(contentDisp.getNamedPart("filename"));
					currFile.setServerFieldName(serverFileName);
					if (contentType != null) currFile.setContentType(contentType.getMainValue());
	
					// open file
					currFileStream = new BufferedOutputStream(new FileOutputStream(serverFileName));
					
					// show some progress
					if (uploadId != null)
						uploadInfo.updateNextFile(currFile.getClientFileNameWithoutParent());
					
					// log
					if (importLog != null)
						importLog.logInfo(
								"Uploading of file " +
								currFile.getClientFileNameWithoutParent() + " " +
								"started.");
					
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
					}
					
					// update total bytes read
					if (uploadId != null)
						uploadInfo.updateFileProgress(read);

					totalPartLength += read;
				}
				
				// unexpected eof
				if (read == -1)
					throw new UploadErrorInternalException("Unexpected EOF while reading a file." + totalPartLength);
	
				// update total bytes read
				if (uploadId != null)
					uploadInfo.updateProgress(newlineAndBoundary.length);

				// finalize file
				if (currFile != null)
				{

					// close and add it to the list
					currFileStream.close();
					files.add(currFile);
					
					// log
					if (importLog != null)
						importLog.logInfo(
								"File " + currFile.getClientFileNameWithoutParent() + " " + 
								"succesfully uploaded. " + 
								"Size " + currFile.getFileSize() + "B.");
					
				}
	
			}
			
			removeUploadInfo(uploadId);
			return true;
		
		}
		catch (UploadErrorInternalException ie)
		{

			removeUploadInfo(uploadId);

			files.clear();
			if (currFile != null)
				currFileStream.close();
			
			if (importLog != null)
				importLog.logError(
						"Upload error. " + ie.getMessage());
			
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
	
	public static synchronized String getNextUploadId()
	{
		nextUploadId++;
		if (nextUploadId < 0) nextUploadId = 1;
		return String.valueOf(nextUploadId);
	}
	
	public static synchronized UploadInfo createUploadInfo(String uploadId, int expectedSize)
	{
		UploadInfo uploadInfo = new UploadInfo(expectedSize);
		uploadsInfo.put(uploadId, uploadInfo);
		return uploadInfo;
	}

	public static synchronized UploadInfo getUploadInfo(String uploadId)
	{
		return (UploadInfo) uploadsInfo.get(uploadId);
	}

	public static synchronized void removeUploadInfo(String uploadId)
	{
		if (uploadId != null) uploadsInfo.remove(uploadId);
	}

}