package edu.emory.library.tast.ui.spss;


public class LogForDisplayInList
{
	
	private String importDir;
	private String started;
	private String finished;
	private String duration;
	private String outcome;
	
	public String getFinished()
	{
		return finished;
	}
	
	public void setFinished(String finished)
	{
		this.finished = finished;
	}
	
	public String getOutcome()
	{
		return outcome;
	}
	
	public void setOutcome(String outcome)
	{
		this.outcome = outcome;
	}
	
	public String getStarted()
	{
		return started;
	}
	
	public void setStarted(String started)
	{
		this.started = started;
	}

	public String getImportDir()
	{
		return importDir;
	}

	public void setImportDir(String id)
	{
		this.importDir = id;
	}

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

}
