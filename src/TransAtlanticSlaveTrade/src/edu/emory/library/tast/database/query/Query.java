/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleDate;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleNumeric;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.ConversionUtils;
import edu.emory.library.tast.util.StringUtils;

public class Query implements Cloneable
{

	private static final long serialVersionUID = 6693392749775025817L;

	private QueryBuilderQuery builderQuery = new QueryBuilderQuery();
	private String yearFrom;
	private String yearTo;
	
	private boolean cachedHashCodeValid = false;
	private int cachedHashCode = 0;

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
		
		cachedHashCodeValid = false;

	}
	
	public void addCondition(QueryCondition queryCondition)
	{
		
		if (builderQuery.containsConditionOn(queryCondition.getSearchableAttributeId()))
			return;
		
		builderQuery.addCondition(queryCondition);
		
		cachedHashCodeValid = false;
		
	}

	public boolean addToDbConditions(boolean markErrors, TastDbConditions dbConds)
	{
		
		boolean errors = false;
		
		Integer yearFromInt = ConversionUtils.toInteger(yearFrom);
		Integer yearToInt = ConversionUtils.toInteger(yearTo);
		
		if (yearFromInt == null || yearToInt == null || yearFromInt.compareTo(yearToInt) <= 0)
		{
			if (yearFromInt != null)
				dbConds.addCondition(
					Voyage.getAttribute("yearam"),
					yearFromInt,
					TastDbConditions.OP_GREATER_OR_EQUAL);
			
			if (yearToInt != null)
				dbConds.addCondition(
					Voyage.getAttribute("yearam"),
					yearToInt,
					TastDbConditions.OP_SMALLER_OR_EQUAL);
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
		this.cachedHashCodeValid = false;  
	}

	public String getYearFrom()
	{
		return yearFrom;
	}

	public void setYearFrom(String yearFrom)
	{
		this.yearFrom = yearFrom;
		this.cachedHashCodeValid = false;  
	}

	public String getYearTo()
	{
		return yearTo;
	}

	public void setYearTo(String yearTo)
	{
		this.yearTo = yearTo;
		this.cachedHashCodeValid = false;  
	}

	protected Object clone() throws CloneNotSupportedException
	{
		Query newQuery = new Query();
		newQuery.setYearFrom(yearFrom);
		newQuery.setYearTo(yearTo);
		newQuery.setBuilderQuery((QueryBuilderQuery) builderQuery.clone());
		newQuery.cachedHashCodeValid = this.cachedHashCodeValid;
		newQuery.cachedHashCode = this.cachedHashCode;
		return newQuery;
	}
	
	private int computeHashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((builderQuery == null) ? 0 : builderQuery.hashCode());
		result = prime * result + ((yearFrom == null) ? 0 : yearFrom.hashCode());
		result = prime * result + ((yearTo == null) ? 0 : yearTo.hashCode());
		return result;
	}
	
	/*
	 * An attempt to make cache the number of voyages for each query across all users.
	 * It's not fully finished yet. This hash function was supposed to be used as a
	 * lookup key in the cache. To make it really meaningful, it would be good
	 * to make the hash functions of all query conditions cached as well. Also, it
	 * would be good to make the hash of a query independent on the order of conditions
	 * in it. 
	 */
	public int hashCode()
	{
		if (!cachedHashCodeValid)
		{
			synchronized (this)
			{
				cachedHashCode = computeHashCode();
				cachedHashCodeValid = true;
			}
		}
		return cachedHashCode;
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
		
		if (that.builderQuery == null && this.builderQuery != null)
			return false;

		if (that.builderQuery != null && this.builderQuery == null)
			return false;
		
		if (that.builderQuery == null && this.builderQuery == null)
			return false;

		if (!that.builderQuery.equals(this.builderQuery))
			return false;

		return true;

	}

}
