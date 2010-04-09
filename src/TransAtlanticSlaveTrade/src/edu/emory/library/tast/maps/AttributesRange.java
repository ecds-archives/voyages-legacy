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

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Range of attribute. Used in Attributes map.
 * 
 * @author Pawel Jurczyk
 */
public class AttributesRange {
	
	/**
	 * Attribute.
	 */
	private VisibleAttributeInterface attribute;
	
	/**
	 * Start index of range of Attribute coverage.
	 */
	private int start;
	
	/**
	 * End index of range of Attribute coverage.
	 */
	private int end;
	
	/**
	 * Constructor.
	 * @param attr Attribute object
	 * @param start start index of range of Attribute
	 * @param end end index of range of Attribute
	 */
	public AttributesRange(VisibleAttributeInterface attr, int start, int end) {
		this.attribute = attr;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Gets attribute assigned to range.
	 * @return Attribute object
	 */
	public VisibleAttributeInterface getAttribute() {
		return attribute;
	}
	
	/**
	 * Gets end of attribute range.
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Gets start of attribute range
	 * @return
	 */
	public int getStart() {
		return start;
	}
	
}
