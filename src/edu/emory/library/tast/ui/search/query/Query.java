package edu.emory.library.tast.ui.search.query;

import java.util.Iterator;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.ui.search.query.searchables.SearchableAttribute;
import edu.emory.library.tast.ui.search.query.searchables.SearchableAttributeSimpleDate;
import edu.emory.library.tast.ui.search.query.searchables.SearchableAttributeSimpleNumeric;
import edu.emory.library.tast.ui.search.query.searchables.Searchables;
import edu.emory.library.tast.util.query.Conditions;

public class Query implements Cloneable
{
	
	private QueryBuilderQuery builderQuery = new QueryBuilderQuery();
	private int yearFrom;
	private int yearTo;

	public QueryBuilderQuery getBuilderQuery()
	{
		return builderQuery;
	}
	
	public void addConditionOn(String searchableAttributeId)
	{
		
		SearchableAttribute searchableAttribute = Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
		QueryCondition queryCondition = searchableAttribute.createQueryCondition();
		
		if (searchableAttribute instanceof SearchableAttributeSimpleDate)
		{
			QueryConditionDate queryConditionDate = (QueryConditionDate) queryCondition;
			queryConditionDate.setFromMonth("01");
			queryConditionDate.setFromYear(String.valueOf(yearFrom));
			queryConditionDate.setToMonth("12");
			queryConditionDate.setToYear(String.valueOf(yearTo));
		}
		
		if (searchableAttribute instanceof SearchableAttributeSimpleNumeric)
		{
			SearchableAttributeSimpleNumeric searchableAttributeNumeric = (SearchableAttributeSimpleNumeric) searchableAttribute;
			if (searchableAttributeNumeric.getType() == SearchableAttributeSimpleNumeric.TYPE_YEAR)
			{
				QueryConditionNumeric queryConditionNumeric = (QueryConditionNumeric) queryCondition;
				queryConditionNumeric.setFrom(String.valueOf(yearFrom));
				queryConditionNumeric.setTo(String.valueOf(yearTo));
			}
		}
		
		builderQuery.addCondition(queryCondition);
		
	}
	
	public boolean addToDbConditions(boolean markErrors, Conditions dbConds)
	{
		
		dbConds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearFrom),
				Conditions.OP_GREATER_OR_EQUAL);

		dbConds.addCondition(
				Voyage.getAttribute("yearam"),
				new Integer(yearTo),
				Conditions.OP_SMALLER_OR_EQUAL);

		boolean errors = false;
		for (Iterator iterQueryCondition = builderQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(dbConds, markErrors)) errors = true;
		}

		return errors;
		
	}

	public void setBuilderQuery(QueryBuilderQuery builderQuery)
	{
		this.builderQuery = builderQuery;
	}

	public int getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(int yearFrom)
	{
		this.yearFrom = yearFrom;
	}

	public int getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(int yearTo)
	{
		this.yearTo = yearTo;
	}
	
	protected Object clone() throws CloneNotSupportedException
	{
		Query newQuery = new Query();
		newQuery.setYearFrom(yearFrom);
		newQuery.setYearTo(yearTo);
		newQuery.setBuilderQuery((QueryBuilderQuery) builderQuery.clone());
		return newQuery;
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof Query))
			return false;

		Query that = (Query) obj;
		
		if (that.yearFrom != yearFrom)
			return false;
		
		if (that.yearTo != yearTo)
			return false;
		
		if ((that.builderQuery == null && this.builderQuery == null) || !that.builderQuery.equals(this.builderQuery))
			return false;
		
		return true;
		
	}

}
