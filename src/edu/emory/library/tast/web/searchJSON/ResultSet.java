package edu.emory.library.tast.web.searchJSON;

import java.util.List;

public class ResultSet
{
	
	private List results;
	private HistoryItem history;
	
	public List getResults()
	{
		return results;
	}
	public void setResults(List results)
	{
		this.results = results;
	}
	public HistoryItem getHistory()
	{
		return history;
	}
	public void setHistory(HistoryItem history)
	{
		this.history = history;
	}

}
