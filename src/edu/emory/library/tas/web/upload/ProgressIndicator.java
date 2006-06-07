package edu.emory.library.tas.web.upload;

public class ProgressIndicator
{
	
	private String currMessage = null;

	public synchronized String getCurrMessage()
	{
		return currMessage;
	}

	public synchronized void setCurrMessage(String currMessage)
	{
		this.currMessage = currMessage;
	}
	
	public synchronized void add(String currMessage)
	{
		this.currMessage = currMessage;
	}

}
