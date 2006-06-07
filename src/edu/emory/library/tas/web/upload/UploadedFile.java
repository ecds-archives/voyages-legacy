package edu.emory.library.tas.web.upload;

import java.io.File;

public class UploadedFile
{
	
	private String htmlFieldName;
	private String clientFileName;
	private String serverFileName;
	private String contentType;
	private int fileSize = 0;
	
	public int getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(int fileSize)
	{
		this.fileSize = fileSize;
	}

	public void incrementFileSize(int fileSize)
	{
		this.fileSize += fileSize;
	}

	public String getClientFileName()
	{
		return clientFileName;
	}
	
	public void setClientFileName(String clientFieldName)
	{
		this.clientFileName = clientFieldName;
	}
	
	public String getClientFileNameWithoutParent()
	{
		return new File(clientFileName).getName();
	}

	public String getHtmlFieldName()
	{
		return htmlFieldName;
	}
	
	public void setHtmlFieldName(String htmlFieldName)
	{
		this.htmlFieldName = htmlFieldName;
	}
	
	public String getServerFileName()
	{
		return serverFileName;
	}
	
	public void setServerFieldName(String serverFieldName)
	{
		this.serverFileName = serverFieldName;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

}
