package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionNumeric;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleNumeric extends SearchableAttributeSimpleRange
{
	
	final public static int TYPE_GENERAL = 0;
	final public static int TYPE_YEAR = 1;
	final public static int TYPE_RATIO = 2;
	
	private int type = TYPE_GENERAL;

	public SearchableAttributeSimpleNumeric(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, int type)
	{
		super(id, userLabel, userCategories, attributes);
		this.type = type;
	}

	public SearchableAttributeSimpleNumeric(String id, String userLabel, UserCategories userCategories, Attribute[] attributes)
	{
		super(id, userLabel, userCategories, attributes);
		this.type = TYPE_GENERAL;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionNumeric(getId());
	}
	
	private void addSingleAttributeToConditions(QueryConditionNumeric queryConditionNumeric, Attribute attribute, Conditions conditions, Object fromConverted, Object toConverted, Object leConverted, Object geConverted, Object eqConverted)
	{
		switch (queryConditionNumeric.getType())
		{
			case QueryConditionNumeric.TYPE_BETWEEN:
				conditions.addCondition(attribute, fromConverted, Conditions.OP_GREATER_OR_EQUAL);
				conditions.addCondition(attribute, toConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_LE:
				conditions.addCondition(attribute, leConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				conditions.addCondition(attribute, geConverted, Conditions.OP_GREATER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_EQ:
				conditions.addCondition(attribute, eqConverted, Conditions.OP_EQUALS);
				break;
		}
	}
	
	private Object parseNumber(String number)
	{

		if (number == null)
			throw new NumberFormatException();
		
		number = number.trim();

		if (type == TYPE_RATIO && number.endsWith("%"))
			number = number.substring(0, number.length() - 1);
		
		switch (((NumericAttribute)getAttributes()[0]).getType())
		{
			case NumericAttribute.TYPE_INTEGER:
				return new Integer(number);
			
			case NumericAttribute.TYPE_FLOAT:
				float value = Float.parseFloat(number);
				if (type == TYPE_RATIO) value /= 100.0;
				return new Float(value);
			
			case NumericAttribute.TYPE_LONG:
				return new Long(number);
	
			default:
				throw new RuntimeException("unsupported type");
		}

	}

	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{

		if (!(queryCondition instanceof QueryConditionNumeric))
			throw new IllegalArgumentException("expected QueryConditionNumeric"); 
		
		QueryConditionNumeric queryConditionNumeric =
			(QueryConditionNumeric) queryCondition;
		
		Object from = null;
		Object to = null;
		Object le = null;
		Object ge = null;
		Object eq = null;
		
		try
		{
			switch (queryConditionNumeric.getType())
			{

				case QueryConditionNumeric.TYPE_BETWEEN:
					from = parseNumber(queryConditionNumeric.getFrom());
					to = parseNumber(queryConditionNumeric.getTo());
					break;

				case QueryConditionNumeric.TYPE_LE:
					le = parseNumber(queryConditionNumeric.getLe());
					break;
					
				case QueryConditionNumeric.TYPE_GE:
					ge = parseNumber(queryConditionNumeric.getGe());
					break;

				case QueryConditionNumeric.TYPE_EQ:
					eq = parseNumber(queryConditionNumeric.getEq());
					break;

			}
		}
		catch (NumberFormatException nfe)
		{
			if (markErrors) queryConditionNumeric.setErrorFlag(true);
			return false;
		}

		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			addSingleAttributeToConditions(queryConditionNumeric,
					attributes[0], conditions,
					from, to, le, ge, eq);
		}
		else
		{
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				addSingleAttributeToConditions(queryConditionNumeric,
						attributes[i], orCond,
						from, to, le, ge, eq);
		}
		
		return true;
		
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getLabelFrom()
	{
		switch (type)
		{
			case TYPE_YEAR: return "After";
			default: return "At least";
		}
	}

	public String getLabelTo()
	{
		switch (type)
		{
			case TYPE_YEAR: return "Before";
			default: return "At most";
		}
	}
	
	public String getLabelEquals()
	{
		switch (type)
		{
			case TYPE_YEAR: return "In";
			default: return "Is equal";
		}
	}

	public String getLabelBetween()
	{
		return "Between";
	}

}
