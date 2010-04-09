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
package edu.emory.library.tast.database.query.searchables;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.db.TastDbConditions;

public abstract class SearchableAttribute
{
	
	private String userLabel;
	private String id;
	private UserCategories userCategories;
	private String spssName;
	private String listDescription;
	private boolean inEstimates;
	
	public SearchableAttribute(String id, String userLabel, UserCategories userCategories, String spssName, String listDescription, boolean inEstimates)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.userCategories = userCategories;
		this.inEstimates = inEstimates;
		this.listDescription = listDescription;
		this.spssName = spssName;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public String getId()
	{
		return id;
	}

	public UserCategories getUserCategories()
	{
		return userCategories;
	}

	public boolean isInUserCategory(UserCategory category)
	{
		return userCategories.isIn(category);
	}

	public String getSpssName()
	{
		return spssName;
	}

	public String getListDescription()
	{
		return listDescription;
	}

	public boolean isInEstimates()
	{
		return inEstimates;
	}

	public abstract QueryCondition createQueryCondition();
	public abstract boolean addToConditions(boolean markErrors, TastDbConditions conditions, QueryCondition queryCondition);
	public abstract QueryCondition restoreFromUrl(Session session, Map params);
	public abstract String getNonNullSqlQuerySelectPart(String voyagePrefix);

}