package edu.emory.library.tas.web.search3;

public class HistoryItem
{
	
	private long id;
	private SearchCondition[] conditions;
	public SearchCondition[] getConditions()
	{
		return conditions;
	}
	public void setConditions(SearchCondition[] conditions)
	{
		this.conditions = conditions;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	
	

}
