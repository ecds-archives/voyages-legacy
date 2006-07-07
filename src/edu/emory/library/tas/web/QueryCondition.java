package edu.emory.library.tas.web;

import java.io.Serializable;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.util.query.Conditions;

public abstract class QueryCondition implements Serializable
{
	
	private AbstractAttribute attribute;
	private transient boolean errorFlag;

	public abstract boolean addToConditions(Conditions conditions);
	protected abstract Object clone();

	public QueryCondition(AbstractAttribute attribute)
	{
		this.attribute = attribute;
		this.errorFlag = false;
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
		if (obj == null || !(obj instanceof QueryCondition)) return false;
		QueryCondition theOther = (QueryCondition) obj;
		return attribute.equals(theOther.getAttribute());
	}
	
	public boolean isOnAttribute()
	{
		return attribute instanceof Attribute;
	}
	
	public boolean isOnCompoundAttribute()
	{
		return attribute instanceof CompoundAttribute;
	}

	public AbstractAttribute getAttribute()
	{
		return attribute;
	}
	
	public void setAttribute(AbstractAttribute attribute)
	{
		this.attribute = attribute;
	}
	
}