package edu.emory.library.tast.ui.search.query;

import java.util.Iterator;

import edu.emory.library.tast.dm.attributes.AbstractAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.CompoundAttribute;
import edu.emory.library.tast.util.query.Conditions;

public class QueryConditionText extends QueryCondition
{
	
	private static final long serialVersionUID = -650415782530711623L;
	
	private String value = "";
	
	public QueryConditionText(AbstractAttribute attribute)
	{
		super(attribute);
	}
	
	private boolean addSingleAttributeToConditions(Attribute attribute, Conditions conditions, String value)
	{
		String localValue = value;
		if (!localValue.endsWith("%")) localValue += "%";
		conditions.addCondition(attribute.getName(), localValue, Conditions.OP_LIKE);
		return true;
	}

	public boolean addToConditions(Conditions conditions, boolean markErrors) 
	{

		if (!isNonEmpty())
			return true;
		
		String trimmedValue = value.trim();
		
		if (isOnAttribute())
		{
			Attribute attr = (Attribute) getAttribute();
			addSingleAttributeToConditions(attr, conditions, trimmedValue);
		}
		else if (isOnCompoundAttribute())
		{
			CompoundAttribute compAttr = (CompoundAttribute) getAttribute();
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (Iterator iterAttr = compAttr.getAttributes().iterator(); iterAttr.hasNext();)
			{
				Attribute attr = (Attribute) iterAttr.next();
				addSingleAttributeToConditions(attr, orCond, trimmedValue);
			}
		}
		
		return true;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	public boolean isNonEmpty()
	{
		return value != null && value.trim().length() > 0;
	}
	
	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionText)) return false;
		QueryConditionText queryConditionText = (QueryConditionText) obj;
		return compareTextFields(queryConditionText.getValue(), value);
	}
	
	protected Object clone()
	{
		QueryConditionText newQueryCondition = new QueryConditionText(getAttribute());
		newQueryCondition.setValue(value);
		return newQueryCondition;
	}

}