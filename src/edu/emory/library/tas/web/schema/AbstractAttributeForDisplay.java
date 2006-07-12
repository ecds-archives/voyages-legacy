package edu.emory.library.tas.web.schema;

import edu.emory.library.tas.attrGroups.AbstractAttribute;

public class AbstractAttributeForDisplay extends SchemaElementForDisplay
{

	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_LABEL = 1;
	public static final int SORT_BY_TYPE = 2;
	public static final int SORT_BY_VISIBILITY = 3;
	public static final int SORT_BY_CATEGORY = 4;
	
	private String typeDisplayName;
	private String groupsHTML;
	private int category;
	private boolean visible;

	public String getTypeDisplayName()
	{
		return typeDisplayName;
	}
	
	public void setTypeDisplayName(String typeDisplayName)
	{
		this.typeDisplayName = typeDisplayName;
	}

	public String getGroupsHTML()
	{
		return groupsHTML;
	}

	public void setGroupsHTML(String groupsHTML)
	{
		this.groupsHTML = groupsHTML;
	}

	public int getCategory()
	{
		return category;
	}

	public void setCategory(int category)
	{
		this.category = category;
	}
	
	public String getCategoryIcon()
	{
		switch (category)
		{
			case AbstractAttribute.CATEGORY_BEGINNER: return "schema-icon-beginner.png";
			case AbstractAttribute.CATEGORY_GENERAL: return "schema-icon-general.png";
			default: return null;
		}
	}

	public String getCategoryText()
	{
		switch (category)
		{
			case AbstractAttribute.CATEGORY_BEGINNER: return "Beginner";
			case AbstractAttribute.CATEGORY_GENERAL: return "General";
			default: return null;
		}
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public String getVisibleIcon()
	{
		if (visible)
			return "schema-icon-visible.png";
		else
			return "schema-icon-invisible.png";
	}

	public String getVisibleText()
	{
		if (visible)
			return "";
		else
			return "hidden";
	}

}
