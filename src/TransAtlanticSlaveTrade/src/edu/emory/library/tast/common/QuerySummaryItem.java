package edu.emory.library.tast.common;

public class QuerySummaryItem
{
	
	private String variable;
	private String value;
	
	public QuerySummaryItem()
	{
	}

	public QuerySummaryItem(String variable)
	{
		this.variable = variable;
	}

	public QuerySummaryItem(String variable, String value)
	{
		this.variable = variable;
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public String getVariable()
	{
		return variable;
	}
	
	public void setVariable(String variable)
	{
		this.variable = variable;
	}

}