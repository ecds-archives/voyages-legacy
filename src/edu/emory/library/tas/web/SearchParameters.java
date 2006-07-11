package edu.emory.library.tas.web;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.util.query.Conditions;

public class SearchParameters
{
	
	private Conditions conditions;
	private VisibleColumn[] columns = new VisibleColumn[0]; 
	private int category = AbstractAttribute.CATEGORY_GENERAL;
	
	public VisibleColumn[] getColumns()
	{
		return columns;
	}
	
	public void setColumns(VisibleColumn[] columns)
	{
		this.columns = columns;
	}
	
	public Conditions getConditions()
	{
		return conditions;
	}
	
	public void setConditions(Conditions conditions)
	{
		this.conditions = conditions;
	}

	public int getCategory()
	{
		return category;
	}

	public void setCategory(int category)
	{
		this.category = category;
	}

}
