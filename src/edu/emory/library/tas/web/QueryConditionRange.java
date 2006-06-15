package edu.emory.library.tas.web;

import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.StringTooLongException;
import edu.emory.library.tas.Voyage;
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

	public QueryConditionRange(String attributeName)
	{
		super(attributeName);
	}

	public QueryConditionRange(String attributeName, int type)
	{
		super(attributeName);
		this.type = type;
	}

	public void addToConditions(Conditions conditions) throws QueryInvalidValueException
	{
		
		SchemaColumn col = Voyage.getSchemaColumn(getAttributeName());
		
		try
		{

			switch (type)
			{
	
				case QueryConditionRange.TYPE_BETWEEN:
					Object fromConverted;
					fromConverted = col.parse(from);
					Object toConverted = col.parse(to);
					conditions.addCondition(getAttributeName(), fromConverted, Conditions.OP_GREATER_OR_EQUAL);
					conditions.addCondition(getAttributeName(), toConverted, Conditions.OP_SMALLER_OR_EQUAL);
					break;
	
				case QueryConditionRange.TYPE_LE:
					Object leConverted = col.parse(le);
					conditions.addCondition(getAttributeName(), leConverted, Conditions.OP_SMALLER_OR_EQUAL);
					break;
					
				case QueryConditionRange.TYPE_GE:
					Object geConverted = col.parse(ge);
					conditions.addCondition(getAttributeName(), geConverted, Conditions.OP_GREATER_OR_EQUAL);
					break;
	
				case QueryConditionRange.TYPE_EQ:
					Object eqConverted = col.parse(eq);
					conditions.addCondition(getAttributeName(), eqConverted, Conditions.OP_EQUALS);
					break;
	
			}
			
		}
		catch (InvalidNumberOfValuesException e)
		{
			throw new QueryInvalidValueException(getAttributeName());
		}
		catch (InvalidNumberException e)
		{
			throw new QueryInvalidValueException(getAttributeName());
		}
		catch (InvalidDateException e)
		{
			throw new QueryInvalidValueException(getAttributeName());
		}
		catch (StringTooLongException e)
		{
			throw new QueryInvalidValueException(getAttributeName());
		}
	
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

}
