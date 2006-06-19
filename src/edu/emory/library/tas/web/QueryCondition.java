package edu.emory.library.tas.web;

import edu.emory.library.tas.util.query.Conditions;

public abstract class QueryCondition
{
	
	private String attributeName;
	private boolean errorFlag = false;

	public abstract boolean addToConditions(Conditions conditions);
	protected abstract Object clone();

	public QueryCondition(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public boolean isErrorFlag()
	{
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag)
	{
		this.errorFlag = errorFlag;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj instanceof QueryCondition)
		{
			QueryCondition queryCondition = (QueryCondition) obj;
			return this.attributeName.equals(queryCondition.getAttributeName());
		}
		else
		{
			return false;
		}
	}
	
}
