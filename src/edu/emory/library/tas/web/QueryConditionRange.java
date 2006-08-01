package edu.emory.library.tas.web;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.util.query.Conditions;

public abstract class QueryConditionRange extends QueryCondition
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	protected int type = TYPE_BETWEEN;

	public QueryConditionRange(AbstractAttribute attribute)
	{
		super(attribute);
		this.type = TYPE_BETWEEN;
	}

	public QueryConditionRange(AbstractAttribute attribute, int type)
	{
		super(attribute);
		this.type = type;
	}

	public abstract boolean addToConditions(Conditions conditions, boolean markErrors);

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
