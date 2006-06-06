package edu.emory.library.tas.spss;

public class LogItemStage
{
	
	private LogItem[] logItems;
	
	public LogItem[] getLogItems()
	{
		return logItems;
	}
	
	public void setLogItems(LogItem[] logItems)
	{
		this.logItems = logItems;
	}
	
	public int getStage()
	{
		if (logItems == null || logItems.length == 0) return -1;
		return logItems[0].getType();
	}
	
}
