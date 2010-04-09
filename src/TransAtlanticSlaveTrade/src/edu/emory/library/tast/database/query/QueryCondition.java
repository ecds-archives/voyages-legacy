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

import java.io.Serializable;

import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.Searchables;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.util.StringUtils;

/**
 * This class represents one condition in the list of conditions, represented by
 * {@link edu.emory.library.tast.database.query.QueryBuilderQuery}, in the currently built query in the
 * search UI. It is abstract. It provides only the necessary methods and
 * properties for specialized conditions. It is important that this class and,
 * in particular, its descendants implement {@link #clone()} and
 * {@link #equals(Object)} since is the two methods are used when storing and
 * restoring the query in the history list.
 * 
 * @author Jan Zich
 * 
 */
public abstract class QueryCondition implements Serializable
{
	
	private String searchableAttributeId;
	private transient boolean errorFlag;

	protected abstract Object clone();

	public boolean addToConditions(TastDbConditions conditions, boolean markErrors)
	{
		return getSearchableAttribute().addToConditions(markErrors, conditions, this);
	}

	public QueryCondition(String searchableAttributeId)
	{
		this.searchableAttributeId = searchableAttributeId;
		this.errorFlag = false;
	}

	public boolean isErrorFlag()
	{
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag)
	{
		this.errorFlag = errorFlag;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryCondition)) return false;
		QueryCondition that = (QueryCondition) obj;
		return StringUtils.compareStrings(this.searchableAttributeId, that.searchableAttributeId);
	}
	
	public String getSearchableAttributeId()
	{
		return searchableAttributeId;
	}
	
	public SearchableAttribute getSearchableAttribute()
	{
		return Searchables.getCurrent().getSearchableAttributeById(searchableAttributeId);
	}

	public abstract UrlParam[] createUrlParamValue();
	
}