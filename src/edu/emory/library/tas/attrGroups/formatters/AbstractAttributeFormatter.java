package edu.emory.library.tas.attrGroups.formatters;

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
	public abstract String format(Object object);
	
	/**
	 * Array of objects formatter.
	 * @param object
	 * @return
	 */
	public abstract String format(Object[] object);
	
}
