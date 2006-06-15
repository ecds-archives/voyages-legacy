package edu.emory.library.tas.web;

public class QueryConditionRange extends QueryCondition
{
	
//	public static final int TYPE_BETWEEN = 0;
//	public static final int TYPE_LE = 1;
//	public static final int TYPE_GE = 2;
//	public static final int TYPE_EQ = 3;
//	
//	private int type = TYPE_EQ;
	private Object from;
	private Object to;

	public QueryConditionRange(String attributeName)
	{
		super(attributeName);
	}

	public Object getFrom()
	{
		return from;
	}

	public void setFrom(Object from)
	{
		this.from = from;
	}

	public Object getTo()
	{
		return to;
	}

	public void setTo(Object to)
	{
		this.to = to;
	}

}
