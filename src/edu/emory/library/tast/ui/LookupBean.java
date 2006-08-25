package edu.emory.library.tast.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.StringUtils;

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
		if (!StringUtils.isNullOrEmpty(newSourceId) && !newSourceId.equals(sourceId))
		{
			sourceId = newSourceId;
			searchForValue = null;
			loadAllItems();
		}
		if (!StringUtils.isNullOrEmpty(newlookupSelectId))
		{
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

	public List getItems()
	{
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
