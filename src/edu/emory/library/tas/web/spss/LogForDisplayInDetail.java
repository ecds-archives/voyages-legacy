package edu.emory.library.tas.web.spss;


public class LogForDisplayInDetail
{
	
	private String timeStart;
	private String timeFinish;
	private String duration;
	private String statusText;
	private boolean finished = false;
	private boolean voyagesPresent = false;
	private boolean slavesPresent = false;
	private LogItemForDisplayInDetail[] logItems;
	
	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

	public String getTimeFinish()
	{
		return timeFinish;
	}

	public void setTimeFinish(String finished)
	{
		this.timeFinish = finished;
	}

	public LogItemForDisplayInDetail[] getLogItems()
	{
		return logItems;
	}

	public void setLogItems(LogItemForDisplayInDetail[] logItems)
	{
		this.logItems = logItems;
	}

	public boolean isSlavesPresent()
	{
		return slavesPresent;
	}

	public void setSlavesPresent(boolean slavesPresent)
	{
		this.slavesPresent = slavesPresent;
	}

	public String getTimeStart()
	{
		return timeStart;
	}

	public void setTimeStart(String started)
	{
		this.timeStart = started;
	}

	public String getStatusText()
	{
		return statusText;
	}

	public void setStatusText(String status)
	{
		this.statusText = status;
	}

	public boolean isVoyagesPresent()
	{
		return voyagesPresent;
	}

	public void setVoyagesPresent(boolean voyagesPresent)
	{
		this.voyagesPresent = voyagesPresent;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

}