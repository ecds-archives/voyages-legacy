package edu.emory.library.tast.database.table.formatters;

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
	public abstract String[] format(VisibleAttributeInterface attr, Object[] object);
	
}
