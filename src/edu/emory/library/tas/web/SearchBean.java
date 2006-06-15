package edu.emory.library.tas.web;

import java.util.ArrayList;
import java.util.List;

public class SearchBean
{
	
	private List historyList = null;
	private List currentQuery = null;
	
	public SearchBean()
	{

		HistoryItem a = new HistoryItem();
		a.setId("a");
		
		HistoryItem b = new HistoryItem();
		b.setId("b");
		
		HistoryItem c = new HistoryItem();
		c.setId("c");

		historyList = new ArrayList();
		historyList.add(a);
		historyList.add(b);
		historyList.add(c);
		
	}
	
	public void AddQueryCondition()
	{
		
		if (currentQuery == null)
			currentQuery = new ArrayList();
		
		// from <select> -> create a new
		// QueryCondition and insert it
		// to currentQuery
		
	}
	
	public void HistoryItemDelete(HistoryItemDeleteEvent event)
	{
		
	}

	public List getHistoryList()
	{
		return historyList;
	}

	public void setHistoryList(List historyList)
	{
		this.historyList = historyList;
	}

	public List getCurrentQuery()
	{
		return currentQuery;
	}

	public void setCurrentQuery(List currentQuery)
	{
		this.currentQuery = currentQuery;
	}

}
