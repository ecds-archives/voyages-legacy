package edu.emory.library.tast.database.query;

/**
 * This class represents a sinle item in the histoty list in the search UI. In
 * fact, it only contains a query of the type
 * {@link edu.emory.library.tast.database.query.QueryBuilderQuery} and a history ID. The history ID is
 * used to access, reffer and manipulate the history items in the history list.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryItem
{

	private String id;
	private Query query;
	private int timeFrameFrom;
	private int timeFrameTo;

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

	public int getTimeFrameFrom()
	{
		return timeFrameFrom;
	}

	public void setTimeFrameFrom(int timeFrameFrom)
	{
		this.timeFrameFrom = timeFrameFrom;
	}

	public int getTimeFrameTo()
	{
		return timeFrameTo;
	}

	public void setTimeFrameTo(int timeFrameTo)
	{
		this.timeFrameTo = timeFrameTo;
	}

}