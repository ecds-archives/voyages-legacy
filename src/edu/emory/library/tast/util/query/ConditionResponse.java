/**
 * 
 */
package edu.emory.library.tast.util.query;

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
	public HashMap properties = new HashMap();
}