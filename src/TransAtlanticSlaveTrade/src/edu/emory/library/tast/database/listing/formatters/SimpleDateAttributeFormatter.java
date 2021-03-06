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

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Formatter used to format dates.
 * @author Pawel Jurczyk
 *
 */
public class SimpleDateAttributeFormatter extends AbstractAttributeFormatter {

	/**
	 * Output date format.
	 */
	private SimpleDateFormat dateFormat;
	
	/**
	 * Default constructor.
	 * @param dateFormat desired output date format
	 */
	public SimpleDateAttributeFormatter(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Formats single Date value
	 */
	public String format(VisibleAttributeInterface attr, Object object) {
		if (object == null) {
			return "";
		} else {
			if (object instanceof Date) {
				return dateFormat.format((Date)object);
			} else {
				return object.toString();
			}
		}
	}

}
