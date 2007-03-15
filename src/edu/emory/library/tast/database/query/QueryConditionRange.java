package edu.emory.library.tast.database.query;


public abstract class QueryConditionRange extends QueryCondition
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	protected int type = TYPE_BETWEEN;

	public QueryConditionRange(String searchableAttributeId)
	{
		super(searchableAttributeId);
		this.type = TYPE_BETWEEN;
	}

	public QueryConditionRange(String searchableAttributeId, int type)
	{
		super(searchableAttributeId);
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	public abstract String getFromForDisplay();
	public abstract String getToForDisplay();
	public abstract String getLeForDisplay();
	public abstract String getGeForDisplay();
	public abstract String getEqForDisplay();

	protected abstract Object clone();
	
	public boolean equals(Object obj)
	{
		return 
			super.equals(obj) &&
			obj instanceof QueryConditionRange;
	}

}