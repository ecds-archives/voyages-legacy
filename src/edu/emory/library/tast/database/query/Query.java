package edu.emory.library.tast.database.query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleDate;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleNumeric;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.ConversionUtils;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

public class Query implements Cloneable
{

	private static final long serialVersionUID = 6693392749775025817L;

	private QueryBuilderQuery builderQuery = new QueryBuilderQuery();

	private String yearFrom;

	private String yearTo;

	public QueryBuilderQuery getBuilderQuery()
	{
		return builderQuery;
	}

	public void addConditionOn(String searchableAttributeId)
	{

		if (builderQuery.containsConditionOn(searchableAttributeId))
			return;

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
	
	public void addCondition(QueryCondition queryCondition)
	{
		
		if (builderQuery.containsConditionOn(queryCondition.getSearchableAttributeId()))
			return;
		
		builderQuery.addCondition(queryCondition);
		
	}

	public boolean addToDbConditions(boolean markErrors, Conditions dbConds)
	{
		
		boolean errors = false;
		
		Integer yearFromInt = ConversionUtils.toInteger(yearFrom);
		Integer yearToInt = ConversionUtils.toInteger(yearTo);
		
		if (yearFromInt == null || yearToInt == null || yearFromInt.compareTo(yearToInt) < 0)
		{
			if (yearFromInt != null)
				dbConds.addCondition(
					Voyage.getAttribute("yearam"),
					yearFromInt,
					Conditions.OP_GREATER_OR_EQUAL);
			
			if (yearToInt != null)
				dbConds.addCondition(
					Voyage.getAttribute("yearam"),
					yearToInt,
					Conditions.OP_SMALLER_OR_EQUAL);
		}
		else
		{
			errors = true;
		}

		for (Iterator iterQueryCondition = builderQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(dbConds, markErrors))
				errors = true;
		}

		return errors;

	}
	
	public String createUrl()
	{
		
		StringBuffer url = new StringBuffer();

		try
		{
			
			int i = 0;

			if (!StringUtils.isNullOrEmpty(yearFrom))
			{
				if (i > 0) url.append("&");
				url.append("yearFrom");
				url.append("=");
				url.append(URLEncoder.encode(yearFrom, "UTF-8"));
				i++;
			}

			if (!StringUtils.isNullOrEmpty(yearTo))
			{
				if (i > 0) url.append("&");
				url.append("yearTo");
				url.append("=");
				url.append(URLEncoder.encode(yearTo, "UTF-8"));
				i++;
			}
			
			for (Iterator iterator = builderQuery.getConditions().iterator(); iterator.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iterator.next();
				UrlParam[] params = queryCondition.createUrlParamValue();
				if (params != null)
				{
					for (int j = 0; j < params.length; j++)
					{
						if (i > 0) url.append("&");
						url.append(params[j].getName());
						url.append("=");
						url.append(URLEncoder.encode(params[j].getValue(), "UTF-8"));
						i++;
					}
				}
			}

		}
		catch (UnsupportedEncodingException e)
		{
		}
		
		return url.toString();
		
	}
	
	public static Query restoreFromUrl(Map params)
	{
		
		Query newQuery = new Query();
		
		SearchableAttribute[] attrs =
			Searchables.getCurrent().getSearchableAttributes();
		
		for (int i = 0; i < attrs.length; i++)
		{
			QueryCondition queryCondition = attrs[i].restoreFromUrl(null, params);
			if (queryCondition != null)
			{
				newQuery.addCondition(queryCondition);
			}
		}
		
		newQuery.setYearFrom(
				StringUtils.getFirstElement(
						(String[]) params.get("yearFrom")));
		
		newQuery.setYearTo(
				StringUtils.getFirstElement(
						(String[]) params.get("yearTo")));
		
		return newQuery;
		
	}
	
	public boolean isEmpty()
	{
		return
			builderQuery.getConditionCount() == 0 &&
			StringUtils.isNullOrEmpty(yearFrom) &&
			StringUtils.isNullOrEmpty(yearTo);
	}

	public void setBuilderQuery(QueryBuilderQuery builderQuery)
	{
		this.builderQuery = builderQuery;
	}

	public String getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(String yearFrom)
	{
		this.yearFrom = yearFrom;
	}

	public String getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(String yearTo)
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

		if (!StringUtils.compareStrings(that.yearFrom, yearFrom))
			return false;

		if (!StringUtils.compareStrings(that.yearTo, yearTo))
			return false;

		if ((that.builderQuery == null && this.builderQuery == null) ||
				!that.builderQuery.equals(this.builderQuery))
			return false;

		return true;

	}

}
