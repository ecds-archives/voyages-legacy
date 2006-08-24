package edu.emory.library.tast.ui;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.StringUtils;

public class LookupBean
{
	
	private String souceId;
	private String searchForValue;
	private List items = null;
	
	public void setSouceId(String lookupSourceId)
	{
		this.souceId = lookupSourceId;
	}
	
	public String getSouceId()
	{
		Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String newSourceId = (String) params.get("sourceId");
		if (!StringUtils.isNullOrEmpty(newSourceId) && !newSourceId.equals(souceId))
		{
			items = null;
			souceId = newSourceId;
		}
		return souceId;
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
		if (items == null) loadAllItems();
		return items;
	}
	
	private void loadAllItems()
	{
		LookupSource source = LookupSources.getLookupSource(getSouceId());
		if (source != null && source.canReturnAllItems()) items = source.allItems();
	}

	public String searchFor()
	{
		LookupSource source = LookupSources.getLookupSource(getSouceId());
		if (source != null) items = source.search(searchForValue);
		return null;
	}

	public String showAll()
	{
		loadAllItems();
		return null;
	}

}
