package edu.emory.library.tast.ui.search.table.formatters;

import java.text.MessageFormat;

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
	public String format(Object object) {
		if (object == null) {
			return "";
		} else {
//			if (object instanceof Number) {
//				return formatter.format(new Object[] {object}); 
//			}
			return object.toString();
		}
	}

	/**
	 * Formats output that will be shown to user for Object array.
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
//				if (object[i] instanceof Number) {
//					buf.append(formatter.format(new Object[] {object[i]}));
//				} else {
					buf.append(object[i].toString());
//				}
				buf.append("'");
				added = true;
			}

		}
		buf.append("]");
		return buf.toString();
	}

}
