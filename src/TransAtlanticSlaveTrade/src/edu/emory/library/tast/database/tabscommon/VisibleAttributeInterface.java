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
package edu.emory.library.tast.database.tabscommon;

import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Instances of this interface are used in components.
 * Provide mapping from datamodel attribute to properties presented to users in web interface.
 * Each visible attribute has user label which is visible
 * in the header of table component. It also has name, which 
 * is optional. VisibleAttributeInterface should keep list of attributes
 * which are attributes of objects presented in table. 
 *
 */
public interface VisibleAttributeInterface {

	public static final String DATE_ATTRIBUTE = "DateAttribute";

	public static final String DICTIONARY_ATTRIBUTE = "DictionaryAttribute";

	public static final String STRING_ATTRIBUTE = "StringAttribute";

	public static final String NUMERIC_ATTRIBUTE = "NumericAttribute";
	
	public static final String BOOLEAN_ATTRIBUTE = "BooleanAttribute";
	
	public abstract Attribute[] getAttributes();

	public abstract String getUserLabel();

	public abstract String getName();

	public abstract String getUserLabelOrName();

	public abstract String encodeToString();

	public abstract String getType();
	
	public boolean isDate();

	public abstract boolean isInUserCategory(UserCategory category);

	public abstract Attribute getQueryAttribute();
	
	public String getFormat();

}