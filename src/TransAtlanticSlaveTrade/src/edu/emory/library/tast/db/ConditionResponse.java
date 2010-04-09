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
/**
 * 
 */
package edu.emory.library.tast.db;

import java.util.HashMap;

/**
 * Class that keeps query string buffer ald map of attributes used in the string query.
 * StringBuffer keeps string like "voyageId = :id_11 and shipName like (:name_33)
 * Map then contains entries for id_11 and name_33. 
 * @author Pawel Jurczyk
 *
 */
public class ConditionResponse {
	
	/**
	 * Conditions string.
	 */
	public StringBuffer conditionString;
	
	/**
	 * Map of mappings between parameters in string above and their exact values.
	 */
	public HashMap parameters = new HashMap();
}