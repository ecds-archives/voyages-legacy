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

import java.text.MessageFormat;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Formatter used to format simple attributes.
 * It uses two methods of formatting: first is for Object - toString method called.
 * The second is for Object array - ['val a' , ... 'val n'] string is generated.
 * @author Pawel Jurczyk
 *
 */
public class SimpleAttributeFormatter extends AbstractAttributeFormatter {

	private MessageFormat formatter = new MessageFormat("{0,number, #,###,###}");
	
	/**
	 * Formats output that will be shown to user for single Object.
	 */
	public String format(VisibleAttributeInterface attr, Object object) {
		if (object == null) {
			return "";
		} else {
			if (attr.getFormat() != null) {
				if (attr.getFormat().endsWith("%")) {
					double d = ((Number)object).doubleValue();
					d *= 100;
					object = new Double(d);
				}
				return new MessageFormat(attr.getFormat()).format(new Object[] {object});
			}
			if (!attr.isDate()) {
				if (object instanceof Number) {
					return formatter.format(new Object[] {object}); 
				} else {
					return object.toString();
				}
			} else {
				return object.toString();
			}
		}
	}

}
