package edu.emory.library.tas.web;


public class HistoryItem
{

	private String id;
	private Query query;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Query getQuery()
	{
		return query;
	}

	public void setQuery(Query query)
	{
		this.query = query;
	}

}
