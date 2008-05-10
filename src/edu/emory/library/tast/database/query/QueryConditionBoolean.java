package edu.emory.library.tast.database.query;

public class QueryConditionBoolean extends QueryCondition
{
	
	private static final long serialVersionUID = 940301730134650051L;

	public static final String TYPE = "boolean";
	
	private boolean yesChecked = true;
	private boolean noChecked = true;
	
	public QueryConditionBoolean(String searchableAttributeId)
	{
		super(searchableAttributeId);
		this.yesChecked = false;
		this.noChecked = false;
	}

	public QueryConditionBoolean(String searchableAttributeId, boolean checked)
	{
		super(searchableAttributeId);
		this.yesChecked = checked;
	}
	
	public boolean isYesChecked()
	{
		return yesChecked;
	}

	public void setYesChecked(boolean checked)
	{
		this.yesChecked = checked;
	}

	public boolean isNoChecked()
	{
		return noChecked;
	}

	public void setNoChecked(boolean noChecked)
	{
		this.noChecked = noChecked;
	}

	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionBoolean)) return false;
		QueryConditionBoolean queryConditionBoolean = (QueryConditionBoolean) obj;
		return 
			queryConditionBoolean.yesChecked == this.yesChecked &&
			queryConditionBoolean.noChecked == this.noChecked;
	}
	
	protected Object clone()
	{
		QueryConditionBoolean newQueryCondition = new QueryConditionBoolean(getSearchableAttributeId());
		newQueryCondition.setYesChecked(yesChecked);
		newQueryCondition.setNoChecked(noChecked);
		return newQueryCondition;
	}

	public UrlParam[] createUrlParamValue()
	{
		if (!noChecked && !yesChecked)
			return null;
		
		return new UrlParam[] {new UrlParam(
				getSearchableAttributeId(),
				yesChecked && noChecked ? "present" :
					yesChecked ? "true" :
						noChecked ? "false" : null)};
	}
	
}