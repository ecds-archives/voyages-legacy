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
