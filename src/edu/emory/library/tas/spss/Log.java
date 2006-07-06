package edu.emory.library.tas.spss;

import java.util.Date;
import java.util.List;

public class Log
{
	
	private Date timeStart = null;
	private Date timeFinish = null;
	private List items  = null;
	private boolean finishedOK;
	
	public boolean isFinished()
	{
		return timeFinish != null;
	}

	public boolean isFinishedOK()
	{
		return finishedOK;
	}
	
	public void setFinishedOK(boolean finishedOK)
	{
		this.finishedOK = finishedOK;
	}
	
	public List getItems()
	{
		return items;
	}
	
	public void setItems(List items)
	{
		this.items = items;
	}
	
	public Date getTimeFinish()
	{
		return timeFinish;
	}
	
	public void setTimeFinish(Date timeFinish)
	{
		this.timeFinish = timeFinish;
	}
	
	public Date getTimeStart()
	{
		return timeStart;
	}
	
	public void setTimeStart(Date timeStart)
	{
		this.timeStart = timeStart;
	}

}
