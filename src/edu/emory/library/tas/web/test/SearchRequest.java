package edu.emory.library.tas.web.test;

import java.util.ArrayList;

public class SearchRequest
{
	
	private int firstResult;
	private int fetchResults;
	private ArrayList conditions;
	
	public ArrayList getConditions()
	{
		return conditions;
	}
	
	public void setConditions(ArrayList conditions)
	{
		this.conditions = conditions;
	}
	
	public int getFetchResults()
	{
		return fetchResults;
	}
	
	public void setFetchResults(int fetchResults)
	{
		this.fetchResults = fetchResults;
	}
	
	public int getFirstResult()
	{
		return firstResult;
	}
	
	public void setFirstResult(int firstResult)
	{
		this.firstResult = firstResult;
	}
	
}
