package edu.emory.library.tas.web;

public class QueryConditionRange
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	private int type = TYPE_EQ;
	private Object from;
	private Object to;
	private Object ge;
	private Object le;
	private Object eq;

	public QueryConditionRange()
	{
		super();
	}

	public QueryConditionRange(int type)
	{
		super();
		this.type = type;
	}

	public Object getEq()
	{
		return eq;
	}

	public void setEq(Object eq)
	{
		this.eq = eq;
	}

	public Object getFrom()
	{
		return from;
	}

	public void setFrom(Object from)
	{
		this.from = from;
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

	public Object getTo()
	{
		return to;
	}

	public void setTo(Object to)
	{
		this.to = to;
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
