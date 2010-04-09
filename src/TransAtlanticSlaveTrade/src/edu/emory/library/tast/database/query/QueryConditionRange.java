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


public abstract class QueryConditionRange extends QueryCondition
{
	
	public static final int TYPE_BETWEEN = 0;
	public static final int TYPE_LE = 1;
	public static final int TYPE_GE = 2;
	public static final int TYPE_EQ = 3;
	
	protected int type = TYPE_BETWEEN;

	public QueryConditionRange(String searchableAttributeId)
	{
		super(searchableAttributeId);
		this.type = TYPE_BETWEEN;
	}

	public QueryConditionRange(String searchableAttributeId, int type)
	{
		super(searchableAttributeId);
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	public abstract String getFromForDisplay();
	public abstract String getToForDisplay();
	public abstract String getLeForDisplay();
	public abstract String getGeForDisplay();
	public abstract String getEqForDisplay();

	protected abstract Object clone();
	
	public boolean equals(Object obj)
	{
		return 
			super.equals(obj) &&
			obj instanceof QueryConditionRange;
	}

}
