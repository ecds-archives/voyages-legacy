/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
	
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (noChecked ? 1231 : 1237);
		result = prime * result + (yesChecked ? 1231 : 1237);
		return result;
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