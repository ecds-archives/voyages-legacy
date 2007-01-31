package edu.emory.library.tast.ui.search.query.searchables;

import java.util.Calendar;
import java.util.Date;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionDate;
import edu.emory.library.tast.ui.search.query.QueryConditionNumeric;
import edu.emory.library.tast.util.query.Conditions;

public class SearchableAttributeSimpleDate extends SearchableAttributeSimpleRange
{

	public SearchableAttributeSimpleDate(String id, String userLabel, UserCategories userCategories, Attribute[] attributes)
	{
		super(id, userLabel, userCategories, attributes);
	}

	private Date parseDate(String monthStr, String yearStr, int roundDirection)
	{

		int month = 0;
		int year = 0;
	
		if (yearStr == null)
			return null;
		
		if (monthStr == null)
			monthStr = "";

		monthStr = monthStr.trim();
		yearStr = yearStr.trim();
		
		if (yearStr.length() == 0)
			return null;
		
		try { year = Integer.parseInt(yearStr); }
		catch (NumberFormatException nfe) { return null; }
		
		if (monthStr.length() > 0)
		{
			try { month = Integer.parseInt(monthStr); }
			catch (NumberFormatException nfe) { return null; }
		}
		else
		{
			if (roundDirection > 0)
				month = 12;
			else
				month = 1;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, 1);
		
		if (roundDirection > 0)
		{
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
		}

		return cal.getTime();
		
	}

	private void addSingleAttributeToConditions(QueryConditionDate queryConditionDate, Attribute attribute, Conditions conditions, Date fromDateQuery, Date toDateQuery)
	{
		switch (queryConditionDate.getType())
		{
			case QueryConditionNumeric.TYPE_BETWEEN:
			case QueryConditionNumeric.TYPE_EQ:
				conditions.addCondition(attribute, fromDateQuery, Conditions.OP_GREATER_OR_EQUAL);
				conditions.addCondition(attribute, toDateQuery, Conditions.OP_SMALLER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_LE:
				conditions.addCondition(attribute, toDateQuery, Conditions.OP_SMALLER_OR_EQUAL);
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				conditions.addCondition(attribute, fromDateQuery, Conditions.OP_GREATER_OR_EQUAL);
				break;

		}
		
		if (!queryConditionDate.areAllMonthsSelected())
			conditions.addCondition(
					new FunctionAttribute("date_part", new Attribute[] {new DirectValueAttribute("'month'"), attribute}),
					queryConditionDate.getSelectedMonthsAsArray(),
					Conditions.OP_IN);
		
	}
	
	public boolean addToConditions(boolean markErrors, Conditions conditions, QueryCondition queryCondition)
	{
		
		if (!(queryCondition instanceof QueryConditionDate))
			throw new IllegalArgumentException("expected QueryConditionDate"); 
		
		QueryConditionDate queryConditionDate =
			(QueryConditionDate) queryCondition;

		Date from = null;
		Date to = null;
		
		if (queryConditionDate.noMonthSelected())
		{
			if (markErrors) queryConditionDate.setErrorFlag(true);
			return false;
		}

		switch (queryConditionDate.getType())
		{

			case QueryConditionNumeric.TYPE_BETWEEN:
				from = parseDate(queryConditionDate.getFromMonth(), queryConditionDate.getFromYear(), -1);
				to = parseDate(queryConditionDate.getToMonth(), queryConditionDate.getToYear(), 1);
				if (from == null || to == null) 
				{
					if (markErrors) queryConditionDate.setErrorFlag(true);
					return false;
				}
				break;

			case QueryConditionNumeric.TYPE_LE:
				to = parseDate(queryConditionDate.getLeMonth(), queryConditionDate.getLeYear(), 1);
				if (to == null)
				{
					if (markErrors) queryConditionDate.setErrorFlag(true);
					return false;
				}
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				from = parseDate(queryConditionDate.getGeMonth(), queryConditionDate.getGeYear(), -1);
				if (from == null)
				{
					if (markErrors) queryConditionDate.setErrorFlag(true);
					return false;
				}
				break;

			case QueryConditionNumeric.TYPE_EQ:
				from = parseDate(queryConditionDate.getEqMonth(), queryConditionDate.getEqYear(), -1);
				to = parseDate(queryConditionDate.getEqMonth(), queryConditionDate.getEqYear(), 1);
				if (from == null || to == null) 
				{
					if (markErrors) queryConditionDate.setErrorFlag(true);
					return false;
				}
				break;

		}
		
		Attribute[] attributes = getAttributes();
		if (attributes.length == 1)
		{
			addSingleAttributeToConditions(queryConditionDate, attributes[0],
					conditions, from, to);
		}
		else
		{
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (int i = 0; i < attributes.length; i++)
				addSingleAttributeToConditions(queryConditionDate, attributes[i],
						conditions, from, to);
		}
		
		return true;

	}

	public QueryCondition createQueryCondition()
	{
		return new QueryConditionDate(getId());
	}

	public String getLabelFrom()
	{
		return TastResource.getText("components_search_after");
	}

	public String getLabelTo()
	{
		return TastResource.getText("components_search_before");
	}
	
	public String getLabelEquals()
	{
		return TastResource.getText("components_search_in");
	}

	public String getLabelBetween()
	{
		return TastResource.getText("components_search_between");
	}

}