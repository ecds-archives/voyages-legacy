package edu.emory.library.tas.web;


public class SearchBean
{
	
	private History history = new History();
	private Query workingQuery = new Query();
	private Query currentQuery = new Query();
	private String selectedAtttibute;
	
	public SearchBean()
	{
		workingQuery.addCondition(new QueryConditionText("shipname"));
		workingQuery.addCondition(new QueryConditionText("captaina"));
		workingQuery.addCondition(new QueryConditionRange("sla32imp", QueryConditionRange.TYPE_EQ));
	}
	
	public void addQueryCondition()
	{
	}
	
	public void search()
	{
		
		// TODO: check if it is ok
		// ...
		
		// copy to currentQuery
		currentQuery = (Query) workingQuery.clone();

		// add to historyList
		HistoryItem historyItem = new HistoryItem();
		historyItem.setQuery(currentQuery);
		history.addItem(historyItem);
	
	}
	
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getDeleteId());
	}
	
	public String getSelectedAtttibute()
	{
		return selectedAtttibute;
	}

	public void setSelectedAtttibute(String selectedAtttibute)
	{
		this.selectedAtttibute = selectedAtttibute;
	}

	public Query getCurrentQuery()
	{
		return currentQuery;
	}

	public void setCurrentQuery(Query currentQuery)
	{
		this.currentQuery = currentQuery;
	}

	public Query getWorkingQuery()
	{
		return workingQuery;
	}

	public void setWorkingQuery(Query currentWorkingQuery)
	{
		this.workingQuery = currentWorkingQuery;
	}

	public History getHistory()
	{
		return history;
	}

	public void setHistory(History history)
	{
		this.history = history;
	}

}
