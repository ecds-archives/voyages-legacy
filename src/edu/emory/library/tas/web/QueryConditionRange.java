package edu.emory.library.tas.web;

import java.util.Iterator;

import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.StringTooLongException;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionRange extends QueryCondition
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	private int type = TYPE_BETWEEN;
	private String from;
	private String to;
	private String ge;
	private String le;
	private String eq;

	public QueryConditionRange(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public QueryConditionRange(AbstractAttribute attribute, int type)
	{
		super(attribute);
		this.type = type;
	}

	public void addSingleAttributeToConditions(Attribute attribute, Conditions conditions, Object fromConverted, Object toConverted, Object leConverted, Object geConverted, Object eqConverted)
	{
		switch (type)
		{
			case QueryConditionRange.TYPE_BETWEEN:
				conditions.addCondition(attribute.getName(), fromConverted, Conditions.OP_GREATER_OR_EQUAL);
				conditions.addCondition(attribute.getName(), toConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;

			case QueryConditionRange.TYPE_LE:
				conditions.addCondition(attribute.getName(), leConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;
				
			case QueryConditionRange.TYPE_GE:
				conditions.addCondition(attribute.getName(), geConverted, Conditions.OP_GREATER_OR_EQUAL);
				break;

			case QueryConditionRange.TYPE_EQ:
				conditions.addCondition(attribute.getName(), eqConverted, Conditions.OP_EQUALS);
				break;
		}
	}

	public boolean addToConditions(Conditions conditions)
	{

		AbstractAttribute attribute = getAttribute();
		
		Object fromConverted = null;
		Object toConverted = null;
		Object leConverted = null;
		Object geConverted = null;
		Object eqConverted = null;

		try
		{

			switch (type)
			{
	
				case QueryConditionRange.TYPE_BETWEEN:
					fromConverted = attribute.parse(from);
					toConverted = attribute.parse(to);
					if (fromConverted == null || toConverted == null) 
					{
						setErrorFlag(true);
						return false;
					}
					break;
	
				case QueryConditionRange.TYPE_LE:
					leConverted = attribute.parse(le);
					if (leConverted == null)
					{
						setErrorFlag(true);
						return false;
					}
					break;
					
				case QueryConditionRange.TYPE_GE:
					geConverted = attribute.parse(ge);
					if (geConverted == null)
					{
						setErrorFlag(true);
						return false;
					}
					break;
	
				case QueryConditionRange.TYPE_EQ:
					eqConverted = attribute.parse(eq);
					if (eqConverted == null)
					{
						setErrorFlag(true);
						return false;
					}
					break;
	
			}
			
		}
		catch (InvalidNumberOfValuesException e)
		{
			setErrorFlag(true);
			return false;
		}
		catch (InvalidNumberException e)
		{
			setErrorFlag(true);
			return false;
		}
		catch (InvalidDateException e)
		{
			setErrorFlag(true);
			return false;
		}
		catch (StringTooLongException e)
		{
			setErrorFlag(true);
			return false;
		}
		
		if (isOnAttribute())
		{
			Attribute attr = (Attribute) getAttribute();
			addSingleAttributeToConditions(attr, conditions,
					fromConverted, toConverted, leConverted, geConverted, eqConverted);
		}
		else if (isOnCompountAttribute())
		{
			CompoundAttribute compAttr = (CompoundAttribute) getAttribute();
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (Iterator iterAttr = compAttr.getAttributes().iterator(); iterAttr.hasNext();)
			{
				Attribute attr = (Attribute) iterAttr.next();
				addSingleAttributeToConditions(attr, orCond,
						fromConverted, toConverted, leConverted, geConverted, eqConverted);
			}
		}
		
		return true;
		
	}
	
	public String getEq()
	{
		return eq;
	}

	public void setEq(String eq)
	{
		this.eq = eq;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getGe()
	{
		return ge;
	}

	public void setGe(String ge)
	{
		this.ge = ge;
	}

	public String getLe()
	{
		return le;
	}

	public void setLe(String le)
	{
		this.le = le;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
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

	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionRange))
			return false;

		QueryConditionRange queryConditionRange = (QueryConditionRange) obj;
		return
			type == queryConditionRange.getType() &&
			compareTextFields(from, queryConditionRange.getFrom()) &&
			compareTextFields(to, queryConditionRange.getTo()) &&
			compareTextFields(le, queryConditionRange.getLe()) &&
			compareTextFields(ge, queryConditionRange.getGe()) &&
			compareTextFields(eq, queryConditionRange.getEq());
	}
	
	protected Object clone()
	{
		QueryConditionRange newQueryCondition = new QueryConditionRange(getAttribute(), getType());
		newQueryCondition.setType(type);
		newQueryCondition.setFrom(from);
		newQueryCondition.setTo(to);
		newQueryCondition.setLe(le);
		newQueryCondition.setGe(ge);
		newQueryCondition.setEq(eq);
		return newQueryCondition;
	}
	
}
