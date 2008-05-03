package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.database.query.QueryConditionNumeric;
import edu.emory.library.tast.database.query.QueryConditionRange;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleNumeric extends SearchableAttributeSimpleRange
{
	
	final public static int TYPE_GENERAL = 0;
	final public static int TYPE_YEAR = 1;
	final public static int TYPE_RATIO = 2;
	
	private int type = TYPE_GENERAL;
	private boolean percent = false;
	
	public SearchableAttributeSimpleNumeric(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, int type, int defaultSearchType, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, defaultSearchType, spssName, listDescription, inEstimates);
		this.type = type;
	}

	public SearchableAttributeSimpleNumeric(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, int subType, boolean isPercent, int defaultSearchType, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, defaultSearchType, spssName, listDescription, inEstimates);
		this.type = TYPE_GENERAL;
		this.percent = isPercent;
	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionNumeric(getId(), defaultSearchType);
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
				double div = 1;
				if (percent) {
					div = 100;
				}
				return new Float(Float.parseFloat(number) / div);
			
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
	
	public QueryCondition restoreFromUrl(Session session, Map params)
	{
		
		String valueEq = StringUtils.getFirstElement((String[]) params.get(getId()));
		String valueLe = StringUtils.getFirstElement((String[]) params.get(getId() + "To"));
		String valueGe = StringUtils.getFirstElement((String[]) params.get(getId() + "From"));
		
		if (!StringUtils.isNullOrEmpty(valueEq))
		{
			QueryConditionNumeric queryCondition = new QueryConditionNumeric(getId());
			queryCondition.setType(QueryConditionRange.TYPE_EQ);
			queryCondition.setEq(valueEq);
			return queryCondition;
		}
		
		if (!StringUtils.isNullOrEmpty(valueLe) && !StringUtils.isNullOrEmpty(valueGe))
		{
			QueryConditionNumeric queryCondition = new QueryConditionNumeric(getId());
			queryCondition.setType(QueryConditionRange.TYPE_BETWEEN);
			queryCondition.setFrom(valueGe);
			queryCondition.setTo(valueLe);
			return queryCondition;
		}

		if (!StringUtils.isNullOrEmpty(valueGe))
		{
			QueryConditionNumeric queryCondition = new QueryConditionNumeric(getId());
			queryCondition.setType(QueryConditionRange.TYPE_GE);
			queryCondition.setGe(valueGe);
			return queryCondition;
		}

		if (!StringUtils.isNullOrEmpty(valueLe))
		{
			QueryConditionNumeric queryCondition = new QueryConditionNumeric(getId());
			queryCondition.setType(QueryConditionRange.TYPE_LE);
			queryCondition.setLe(valueLe);
			return queryCondition;
		}

		return null;
		
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getLabelFrom()
	{
		switch (type)
		{
			case TYPE_YEAR: return TastResource.getText("components_search_after");
			default: return TastResource.getText("components_search_atleast");
		}
	}

	public String getLabelTo()
	{
		switch (type)
		{
			case TYPE_YEAR: return TastResource.getText("components_search_before");
			default: return TastResource.getText("components_search_atmost");
		}
	}
	
	public String getLabelEquals()
	{
		switch (type)
		{
			case TYPE_YEAR: return TastResource.getText("components_search_in");
			default: return TastResource.getText("components_search_isequalto");
		}
	}

	public String getLabelBetween()
	{
		return TastResource.getText("components_search_between");
	}

}
