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
package edu.emory.library.tast.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Map of attributes that helps to decide about attribute in given column and row in data
 * from database. Used in AbstractDataTransformer.
 * 
 * Columns keep information about ranges of attributes - list of AttributesRanges.
 * 
 * @author Pawel Jurczyk
 *
 */
public class AttributesMap {

	/**
	 * List of columns.
	 */
	private List columns = new ArrayList();
	
	/**
	 * Default constructor.
	 *
	 */
	public AttributesMap() {
	}
	
	/**
	 * Adds column with attributes range.
	 * @param list list of attributes range
	 */
	public void addColumn(List list) {
		this.columns.add(list);
	}
	
	/**
	 * Gets Attribute that corresponds to i,j location in response data from DB.
	 * @param i i index
	 * @param j j index
	 * @return Attribute, null if there is no such an attribute
	 */
	public VisibleAttributeInterface getAttribute(int i, int j) {
		
		//Get desired column
		List list = (List)this.columns.get(j);
		
		//Find Attribute that cover range having i inside
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			AttributesRange element = (AttributesRange) iter.next();
			if (element.getStart() <= i && element.getEnd() >= i) {
				return element.getAttribute();
			}
		}
		return null;
	}

}
