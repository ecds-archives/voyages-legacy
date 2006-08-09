package edu.emory.library.tast.ui.search.query.searchables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group
{
	
	private String id;
	private String userLabel;
	private SearchableAttribute[] searchableAttributesAll;
	private Map searchableAttributesbyUserCategories = new HashMap();
	
	public Group(String id, String userLabel, SearchableAttribute[] all)
	{
		this.id = id;
		this.userLabel = userLabel;
		this.searchableAttributesAll = all;
		splitByUserCategories();
	}
	
	private void splitByUserCategories()
	{
		
		if (searchableAttributesbyUserCategories == null)
			searchableAttributesbyUserCategories = new HashMap();
		else
			searchableAttributesbyUserCategories.clear();
		
		UserCategory allCategories[] = UserCategory.getAllCategories();
		List selected = new ArrayList();
		
		for (int i = 0; i < allCategories.length; i++)
		{
			
			UserCategory category = allCategories[i];
			selected.clear();

			for (int j = 0; j < searchableAttributesAll.length; j++)
			{
				SearchableAttribute attr = searchableAttributesAll[j];
				if (attr.getUserCategory().equals(category)) selected.add(attr);
			}
			
			SearchableAttribute[] selectedArr = new SearchableAttribute[selected.size()];
			selected.toArray(selectedArr);
			
			searchableAttributesbyUserCategories.put(category, selectedArr);
			
		}
		
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getUserLabel()
	{
		return userLabel;
	}

	public SearchableAttribute[] getAllSearchableAttributes()
	{
		return searchableAttributesAll;
	}
	
	public SearchableAttribute[] getSearchableAttributesInUserCategory(UserCategory category)
	{
		return (SearchableAttribute[]) searchableAttributesbyUserCategories.get(category);
	}

}