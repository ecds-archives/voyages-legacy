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
package edu.emory.library.tast.database.query.searchables;

import edu.emory.library.tast.database.query.QueryConditionNumeric;
import edu.emory.library.tast.dm.attributes.Attribute;

public abstract class SearchableAttributeSimpleRange extends SearchableAttributeSimple
{
	
	protected int defaultSearchType = QueryConditionNumeric.TYPE_BETWEEN;
	
	public SearchableAttributeSimpleRange(String id, String userLabel, UserCategories userCategories, Attribute[] attributes, int defaultSearchType, String spssName, String listDescription, boolean inEstimates)
	{
		super(id, userLabel, userCategories, attributes, spssName, listDescription, inEstimates);
		this.defaultSearchType = defaultSearchType;
	}

	public abstract String getLabelFrom();
	public abstract String getLabelTo();
	public abstract String getLabelEquals();
	public abstract String getLabelBetween();

}
