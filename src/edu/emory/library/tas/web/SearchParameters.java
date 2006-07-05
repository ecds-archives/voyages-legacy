package edu.emory.library.tas.web;

import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.util.query.Conditions;

public class SearchParameters
{
	
	private Conditions conditions;
	private VisibleColumn[] columns;
	
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

}
