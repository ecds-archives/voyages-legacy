package edu.emory.library.tast.ui.search.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.util.StringUtils;

public class QueryConditionListItem implements Serializable
{
	
	private static final long serialVersionUID = -2205785794349321757L;

	private String id;
	private String text;
	private boolean selectable = true;
	private boolean selectChildren = true;
	private transient Map childrenById = null;
	private QueryConditionListItem[] children;
	
	public QueryConditionListItem()
	{
	}
	
	public QueryConditionListItem(String id)
	{
		this.id = id;
	}

	public QueryConditionListItem(String id, String text)
	{
		this.id = id;
		this.text = text;
	}
	
	private void ensureIdMap()
	{
		if (childrenById == null)
		{
			Map newMap = new HashMap();
			for (int i = 0; i < children.length; i++)
			{
				QueryConditionListItem child = children[i];
				newMap.put(child.getId(), child);
			}
			childrenById = newMap; 
		}
	}
	
	public QueryConditionListItem getChildById(String id)
	{
		ensureIdMap();
		return (QueryConditionListItem) childrenById.get(id);
	}

	public int getChildrenCount()
	{
		if (children == null) return 0;
		return children.length;
	}

	public QueryConditionListItem[] getChildren()
	{
		return children;
	}
	
	public void setChildren(QueryConditionListItem[] children)
	{
		childrenById = null;
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
	
	public boolean isSelectable()
	{
		return selectable;
	}
	
	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}
	
	public boolean isSelectChildren()
	{
		return selectChildren;
	}
	
	public void setSelectChildren(boolean selectedChildren)
	{
		this.selectChildren = selectedChildren;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof QueryConditionListItem))
			return false;
		
		QueryConditionListItem that = (QueryConditionListItem) obj;
		
		if (!StringUtils.compareStrings(this.getId(), that.getId()))
			return false;
		
		if (this.getChildrenCount() != that.getChildrenCount())
			return false;
		
		if (this.getChildrenCount() == 0)
			return true;
		
		QueryConditionListItem[] thisChildren = this.getChildren();
		QueryConditionListItem[] thatChildren = that.getChildren();
		for (int i = 0; i < thatChildren.length; i++)
			if (thisChildren.equals(thatChildren))
				return false;
		
		return true;
		
	}
	
	protected Object clone()
	{
		
		QueryConditionListItem[] newChildren = new QueryConditionListItem[getChildrenCount()];
		for (int i = 0; i < children.length; i++)
			newChildren[i] = (QueryConditionListItem) children[i].clone();

		QueryConditionListItem newItem = new QueryConditionListItem();
		newItem.setId(id);
		newItem.setText(text);
		newItem.setSelectable(selectable);
		newItem.setSelectChildren(selectChildren);
		newItem.setChildren(children);
		
		return newItem;
		
	}

}