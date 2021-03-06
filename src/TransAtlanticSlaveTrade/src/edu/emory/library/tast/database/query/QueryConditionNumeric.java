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

import edu.emory.library.tast.util.StringUtils;

public class QueryConditionNumeric extends QueryConditionRange
{
	
	private static final long serialVersionUID = -7863875106659949813L;

	public static final String TYPE = "numeric";
	
	private String from;
	private String to;
	private String ge;
	private String le;
	private String eq;
	
	public QueryConditionNumeric(String searchableAttributeId)
	{
		super(searchableAttributeId);
	}

	public QueryConditionNumeric(String searchableAttributeId, int type)
	{
		super(searchableAttributeId);
		this.type = type;
	}

	public String getEq()
	{
		return eq;
	}

	public void setEq(String eq)
	{
		this.eq = eq;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getGe()
	{
		return ge;
	}

	public void setGe(String ge)
	{
		this.ge = ge;
	}

	public String getLe()
	{
		return le;
	}

	public void setLe(String le)
	{
		this.le = le;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getFromForDisplay()
	{
		return from;
	}

	public String getToForDisplay()
	{
		return to;
	}

	public String getLeForDisplay()
	{
		return le;
	}

	public String getGeForDisplay()
	{
		return ge;
	}

	public String getEqForDisplay()
	{
		return eq;
	}
	
	public UrlParam[] createUrlParamValue()
	{
		String attrId = getSearchableAttributeId();
		switch (getType())
		{
			case QueryConditionRange.TYPE_BETWEEN:
				return new UrlParam[] {
						new UrlParam(attrId + "From",String.valueOf(from)),
						new UrlParam(attrId + "To", String.valueOf(to))};
			
			case QueryConditionRange.TYPE_EQ:
				return new UrlParam[] {
						new UrlParam(attrId, String.valueOf(eq))};
			
			case QueryConditionRange.TYPE_LE:
				return new UrlParam[] {
						new UrlParam(attrId + "To", String.valueOf(le))};
			
			case QueryConditionRange.TYPE_GE:
				return new UrlParam[] {
						new UrlParam(attrId + "From", String.valueOf(ge))};
				
			default:
				throw new RuntimeException("unexpected type");
		}
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionNumeric))
			return false;

		QueryConditionNumeric queryConditionRange = (QueryConditionNumeric) obj;
		return
			type == queryConditionRange.getType() &&
			StringUtils.compareStrings(from, queryConditionRange.getFrom()) &&
			StringUtils.compareStrings(to, queryConditionRange.getTo()) &&
			StringUtils.compareStrings(le, queryConditionRange.getLe()) &&
			StringUtils.compareStrings(ge, queryConditionRange.getGe()) &&
			StringUtils.compareStrings(eq, queryConditionRange.getEq());
	}
	
	protected Object clone()
	{
		QueryConditionNumeric newQueryCondition = new QueryConditionNumeric(getSearchableAttributeId(), getType());
		newQueryCondition.setType(type);
		newQueryCondition.setFrom(from);
		newQueryCondition.setTo(to);
		newQueryCondition.setLe(le);
		newQueryCondition.setGe(ge);
		newQueryCondition.setEq(eq);
		return newQueryCondition;
	}

	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((le == null) ? 0 : le.hashCode());
		result = prime * result + ((ge == null) ? 0 : ge.hashCode());
		result = prime * result + ((eq == null) ? 0 : eq.hashCode());
		return result;
	}
	
}
