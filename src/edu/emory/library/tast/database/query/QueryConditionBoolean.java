package edu.emory.library.tast.database.query;


public class QueryConditionBoolean extends QueryCondition
{
	
	private static final long serialVersionUID = 940301730134650051L;

	private boolean checked = true;

	public QueryConditionBoolean(String searchableAttributeId)
	{
		super(searchableAttributeId);
		this.checked = false;
	}

	public QueryConditionBoolean(String searchableAttributeId, boolean checked)
	{
		super(searchableAttributeId);
		this.checked = checked;
	}
	
	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionBoolean)) return false;
		QueryConditionBoolean queryConditionBoolean = (QueryConditionBoolean) obj;
		return queryConditionBoolean.checked == this.checked;
	}
	
	protected Object clone()
	{
		QueryConditionBoolean newQueryCondition = new QueryConditionBoolean(getSearchableAttributeId());
		newQueryCondition.setChecked(checked);
		return newQueryCondition;
	}

}