package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.dm.attributes.AbstractAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionNumeric;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleNumeric extends SearchableAttributeSimple
{

	public SearchableAttributeSimpleNumeric(String id, String userLabel, UserCategory userCategory, Attribute[] attributes)
	{
		super(id, userLabel, userCategory, attributes);
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
				conditions.addCondition(attribute.getName(), fromConverted, Conditions.OP_GREATER_OR_EQUAL);
				conditions.addCondition(attribute.getName(), toConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_LE:
				conditions.addCondition(attribute.getName(), leConverted, Conditions.OP_SMALLER_OR_EQUAL);
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				conditions.addCondition(attribute.getName(), geConverted, Conditions.OP_GREATER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_EQ:
				conditions.addCondition(attribute.getName(), eqConverted, Conditions.OP_EQUALS);
				break;
		}
	}
	
	private Object parseNumber(String number)
	{
		switch (getAttributeType())
		{
		case AbstractAttribute.TYPE_INTEGER:
			return new Integer(number);
		
		case AbstractAttribute.TYPE_FLOAT:
			return new Float(number);
		
		case AbstractAttribute.TYPE_LONG:
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

}
