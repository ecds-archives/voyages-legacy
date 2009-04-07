package edu.emory.library.tast.common;

import java.util.HashMap;
import java.util.Map;

public class LookupCheckboxItem
{
	
	private String id;
	private String text;
	private transient Map childrenById = null;
	private LookupCheckboxItem[] children;
	
	public LookupCheckboxItem()
	{
	}
	
	public LookupCheckboxItem(String id)
	{
		this.id = id;
	}

	public LookupCheckboxItem(String id, String text)
	{
		this.id = id;
		this.text = text;
	}
	
	public LookupCheckboxItem(String id, String text, LookupCheckboxItem[] children)
	{
		this.id = id;
		this.text = text;
		this.children = children;
	}
	
	private void ensureIdMap()
	{
		if (childrenById == null)
		{
			Map newMap = new HashMap();
			for (int i = 0; i < children.length; i++)
			{
				LookupCheckboxItem child = children[i];
				newMap.put(child.getId(), child);
			}
			childrenById = newMap; 
		}
	}

	public LookupCheckboxItem getChildById(String id)
	{
		ensureIdMap();
		return (LookupCheckboxItem) childrenById.get(id);
	}
	
	public boolean hasChildren()
	{
		return children != null && children.length > 0;
	}

	public LookupCheckboxItem[] getChildren()
	{
		return children;
	}
	
	public void setChildren(LookupCheckboxItem[] children)
	{
		this.children = children;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

}
