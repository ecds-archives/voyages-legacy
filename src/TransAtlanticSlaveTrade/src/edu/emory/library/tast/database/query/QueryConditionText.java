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



public class QueryConditionText extends QueryCondition
{
	
	private static final long serialVersionUID = -650415782530711623L;
	
	public static final String TYPE = "text";
	
	private String value = "";
	
	public QueryConditionText(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	public boolean isNonEmpty()
	{
		return value != null && value.trim().length() > 0;
	}
	
	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionText)) return false;
		QueryConditionText queryConditionText = (QueryConditionText) obj;
		return compareTextFields(queryConditionText.getValue(), value);
	}
	
	protected Object clone()
	{
		QueryConditionText newQueryCondition = new QueryConditionText(getSearchableAttributeId());
		newQueryCondition.setValue(value);
		return newQueryCondition;
	}

	public UrlParam[] createUrlParamValue()
	{
		return new UrlParam[] {new UrlParam(getSearchableAttributeId(), value)};
	}

	public int hashCode()
	{
		return (value == null) ? 0 : value.hashCode();
	}
	
}
