package edu.emory.library.tas.web;

public class QueryConditionRange extends QueryCondition
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	private int type = TYPE_BETWEEN;
	private Object from;
	private Object to;
	private Object ge;
	private Object le;
	private Object eq;

	public QueryConditionRange(String attributeName, int type)
	{
		super(attributeName);
		this.type = type;
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

	public Object getEq()
	{
		return eq;
	}

	public void setEq(Object eq)
	{
		this.eq = eq;
	}

	public Object getGe()
	{
		return ge;
	}

	public void setGe(Object ge)
	{
		this.ge = ge;
	}

	public Object getLe()
	{
		return le;
	}

	public void setLe(Object le)
	{
		this.le = le;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}
