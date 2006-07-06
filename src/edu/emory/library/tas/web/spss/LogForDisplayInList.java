package edu.emory.library.tas.web.spss;


public class LogForDisplayInList
{
	
	private String id;
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
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
