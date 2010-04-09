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
package edu.emory.library.tast.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

public class LookupBean
{
	
	private String sourceId;
	private String searchForValue;
	private String lookupSelectId;
	private List items = null;
	
	private void retriveParamsFromQueryString()
	{
		Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String newSourceId = (String) params.get("sourceId");
		String newlookupSelectId = (String) params.get("lookupSelectId");
		if (newSourceId != null && newlookupSelectId != null)
		{
			if (!newSourceId.equals(sourceId))
			{
				items = null;
				searchForValue = null;
			}
			sourceId = newSourceId;
			lookupSelectId = newlookupSelectId;
		}
	}
	
	public String getLookupSelectId()
	{
		retriveParamsFromQueryString();
		return lookupSelectId;
	}

	public void setLookupSelectId(String lookupSelectId)
	{
		this.lookupSelectId = lookupSelectId;
	}

	public String getSourceId()
	{
		retriveParamsFromQueryString();
		return sourceId;
	}

	public void setSourceId(String sourceId)
	{
		this.sourceId = sourceId;
	}
	
	public String getSearchForValue()
	{
		return searchForValue;
	}

	public void setSearchForValue(String searchForValue)
	{
		this.searchForValue = searchForValue;
	}
	
	public String getInfoLine()
	{
		LookupSource source = LookupSources.getLookupSource(getSourceId());
		if (!source.canReturnAllItems())
		{
			if (items == null)
			{
				return "No items selected. Search first please.";
			}
			else
			{
				return "First " + source.getMaxLimit() + " items:";
			}
		}
		else
		{
			return "Items:";
		}
	}

	public List getItems()
	{
		LookupSource source = LookupSources.getLookupSource(getSourceId());
		if (items == null && source.canReturnAllItems()) loadAllItems();
		return items == null ? new ArrayList() : items;
	}
	
	private void loadAllItems()
	{
		LookupSource source = LookupSources.getLookupSource(getSourceId());
		if (source != null && source.canReturnAllItems()) items = source.allItems();
	}

	public String search()
	{
		LookupSource source = LookupSources.getLookupSource(getSourceId());
		if (source != null) items = source.search(searchForValue);
		return null;
	}

	public String showAll()
	{
		loadAllItems();
		return null;
	}

}
