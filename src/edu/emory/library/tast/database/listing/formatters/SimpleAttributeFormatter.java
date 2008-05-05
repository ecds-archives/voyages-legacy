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

	/**
	 * Formats output that will be shown to user for Object array.
	 */
	public String[] format(VisibleAttributeInterface attr, Object[] object) {
		
		String[] ret = new String[object.length];
		for (int i = 0; i < object.length; i++) {
			ret[i] = format(attr, object[i]);
		}
		return ret;
	}

}
