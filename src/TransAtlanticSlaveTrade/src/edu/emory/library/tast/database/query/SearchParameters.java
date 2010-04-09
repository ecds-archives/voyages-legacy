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


import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.db.TastDbConditions;

/**
 * Used by the bean {@link edu.emory.library.tast.database.query.SearchBean} in order to
 * pass search parameters collected from
 * {@link edu.emory.library.tast.database.query.QueryBuilderComponent}. It contains the
 * current query, the list of attributes (since we need the columns to the
 * results) and the type of the user category.
 * 
 * @author Jan Zich
 * 
 */
public class SearchParameters
{
	
	private TastDbConditions conditions;
	private int numberOfResults;
	private UserCategory category = UserCategory.Beginners;
	
	public SearchParameters()
	{
		this.conditions = null;
		this.numberOfResults = 0;
	}

	public TastDbConditions getConditions()
	{
		return conditions;
	}
	
	public void setConditions(TastDbConditions conditions)
	{
		this.conditions = conditions;
	}

	public UserCategory getCategory()
	{
		return category;
	}

	public void setCategory(UserCategory category)
	{
		this.category = category;
	}

	public int getNumberOfResults()
	{
		return numberOfResults;
	}

	public void setNumberOfResults(int numberOfResults)
	{
		this.numberOfResults = numberOfResults;
	}

	public Object clone()
	{
		SearchParameters params = new SearchParameters();
		params.setConditions((TastDbConditions) this.conditions.clone());
		params.setNumberOfResults(numberOfResults);
		params.setCategory(category);
		return params;
	}

}
