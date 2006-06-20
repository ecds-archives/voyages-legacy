package edu.emory.library.tas.web;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
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

	public boolean addToConditions(Conditions conditions)
	{
		
//		SchemaColumn col = Voyage.getSchemaColumn(getAttributeId());
//		
//		try
//		{
//
//			switch (type)
//			{
//	
//				case QueryConditionRange.TYPE_BETWEEN:
//					Object fromConverted = col.parse(from);
//					Object toConverted = col.parse(to);
//					if (fromConverted == null || toConverted == null) 
//					{
//						setErrorFlag(true);
//						return false;
//					}
//					conditions.addCondition(getAttributeId(), fromConverted, Conditions.OP_GREATER_OR_EQUAL);
//					conditions.addCondition(getAttributeId(), toConverted, Conditions.OP_SMALLER_OR_EQUAL);
//					break;
//	
//				case QueryConditionRange.TYPE_LE:
//					Object leConverted = col.parse(le);
//					if (leConverted == null)
//					{
//						setErrorFlag(true);
//						return false;
//					}
//					conditions.addCondition(getAttributeId(), leConverted, Conditions.OP_SMALLER_OR_EQUAL);
//					break;
//					
//				case QueryConditionRange.TYPE_GE:
//					Object geConverted = col.parse(ge);
//					if (geConverted == null)
//					{
//						setErrorFlag(true);
//						return false;
//					}
//					conditions.addCondition(getAttributeId(), geConverted, Conditions.OP_GREATER_OR_EQUAL);
//					break;
//	
//				case QueryConditionRange.TYPE_EQ:
//					Object eqConverted = col.parse(eq);
//					if (eqConverted == null)
//					{
//						setErrorFlag(true);
//						return false;
//					}
//					conditions.addCondition(getAttributeId(), eqConverted, Conditions.OP_EQUALS);
//					break;
//	
//			}
//			
//		}
//		catch (InvalidNumberOfValuesException e)
//		{
//			setErrorFlag(true);
//			return false;
//		}
//		catch (InvalidNumberException e)
//		{
//			setErrorFlag(true);
//			return false;
//		}
//		catch (InvalidDateException e)
//		{
//			setErrorFlag(true);
//			return false;
//		}
//		catch (StringTooLongException e)
//		{
//			setErrorFlag(true);
//			return false;
//		}
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
		if (!super.equals(obj)) return false;
		if (obj instanceof QueryConditionRange)
		{
			QueryConditionRange queryConditionRange = (QueryConditionRange) obj;
			return
				type == queryConditionRange.getType() &&
				compareTextFields(from, queryConditionRange.getFrom()) &&
				compareTextFields(to, queryConditionRange.getTo()) &&
				compareTextFields(le, queryConditionRange.getLe()) &&
				compareTextFields(ge, queryConditionRange.getGe()) &&
				compareTextFields(eq, queryConditionRange.getEq());
		}
		else
		{
			return false;
		}
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
