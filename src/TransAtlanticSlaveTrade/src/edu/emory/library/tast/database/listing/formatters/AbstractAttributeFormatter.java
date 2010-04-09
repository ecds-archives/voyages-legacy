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
package edu.emory.library.tast.database.listing.formatters;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Formatter for any attribute. 
 * Formatters are used whenever there is generated string 
 * from attribute value that will be shown to user.
 * @author Pawel Jurczyk
 *
 */
public abstract class AbstractAttributeFormatter {
	
	/**
	 * Single object formatter.
	 * @param object
	 * @return
	 */
	public abstract String format(VisibleAttributeInterface attr, Object object);
	
	/**
	 * Array of objects formatter.
	 * @param object
	 * @return
	 */
	public String[] format(VisibleAttributeInterface attr, Object[] object)
	{
		String[] ret = new String[object.length];
		for (int i = 0; i < object.length; i++) {
			ret[i] = format(attr, object[i]);
		}
		return ret;
	}
	
}
