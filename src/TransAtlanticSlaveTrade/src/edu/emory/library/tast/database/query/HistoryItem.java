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

/**
 * This class represents a sinle item in the histoty list in the search UI. In
 * fact, it only contains a query of the type
 * {@link edu.emory.library.tast.database.query.QueryBuilderQuery} and a history ID. The history ID is
 * used to access, reffer and manipulate the history items in the history list.
 * 
 * @author Jan Zich
 * 
 */
public class HistoryItem
{

	private String id;
	private Query query;
	private int timeFrameFrom;
	private int timeFrameTo;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Query getQuery()
	{
		return query;
	}

	public void setQuery(Query query)
	{
		this.query = query;
	}

	public int getTimeFrameFrom()
	{
		return timeFrameFrom;
	}

	public void setTimeFrameFrom(int timeFrameFrom)
	{
		this.timeFrameFrom = timeFrameFrom;
	}

	public int getTimeFrameTo()
	{
		return timeFrameTo;
	}

	public void setTimeFrameTo(int timeFrameTo)
	{
		this.timeFrameTo = timeFrameTo;
	}

}