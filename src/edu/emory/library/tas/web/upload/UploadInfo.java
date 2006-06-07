package edu.emory.library.tas.web.upload;

public class UploadInfo
{
	
	private String currentFile;
	private int currentFileBytes;
	private int totalBytes;
	private int expectedLength;

	public UploadInfo()
	{
		expectedLength = -1;
	}
	
	public UploadInfo(int expectedSize)
	{
		this.expectedLength = expectedSize;
	}

	public void updateNextFile(String file)
	{
		currentFile = file;
		currentFileBytes = 0;
	}

	public void updateProgress(int bytes)
	{
		totalBytes += bytes;
	}

	public void updateFileProgress(int bytes)
	{
		currentFileBytes += bytes;
		totalBytes += bytes;
	}

	public String getCurrentFile()
	{
		return currentFile;
	}
	
	public int getCurrentFileBytes()
	{
		return currentFileBytes;
	}
	
	public int getTotalBytes()
	{
		return totalBytes;
	}
	
	public int getExpectedLength()
	{
		return expectedLength;
	}

}
