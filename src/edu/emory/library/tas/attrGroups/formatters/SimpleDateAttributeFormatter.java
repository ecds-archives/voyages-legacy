package edu.emory.library.tas.attrGroups.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	public String format(Object object) {
		if (object == null) {
			return "";
		} else {
			return dateFormat.format((Date)object);
		}
	}

	/**
	 * Formats array of Date values.
	 */
	public String format(Object[] object) {
		StringBuffer buf = new StringBuffer();
		boolean added = false;
		buf.append("[");		
		for (int i = 0; i < object.length; i++) {
			if (object[i] != null) {
				if (i > 0 && added) {
					buf.append(", ");
				}
				buf.append("'");
				buf.append(dateFormat.format((Date)object[i]));
				buf.append("'");
				added = true;
			}

		}
		buf.append("]");
		return buf.toString();
	}

}
