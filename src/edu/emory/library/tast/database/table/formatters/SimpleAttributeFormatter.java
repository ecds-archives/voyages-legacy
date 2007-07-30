package edu.emory.library.tast.database.table.formatters;

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
//		StringBuffer buf = new StringBuffer();
//		boolean added = false;		
//		buf.append("<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" class=\"multiline-attr-table\">");
//		for (int i = 0; i < object.length; i++) {
//			if (object[i] != null && !String.valueOf(object[i]).trim().equals("")) {
//				
//				buf.append("<tr><td>");
//				added = true;
//				if (!attr.isDate()) {
//					if (object[i] instanceof Number) {
//						buf.append(formatter.format(new Object[] {object[i]}));
//					} else {
//						buf.append(object[i].toString());
//					}
//				} else {
//					buf.append(object[i].toString());
//				}
//
//				buf.append("</td></tr>\n");
//				added = true;
//			}
//
//		}
//		if (!added) {
//			buf.append("<tr><td> \n");
//			buf.append("</td></tr>\n");
//		}
//		buf.append("</table>");
		
		String[] ret = new String[object.length];
		for (int i = 0; i < object.length; i++) {
			ret[i] = format(attr, object[i]);
		}
		return ret;
	}

	
//	/**
//	 * Formats output that will be shown to user for Object array.
//	 */
//	public String format(VisibleAttributeInterface attr, Object[] object) {
//		StringBuffer buf = new StringBuffer();
//		boolean added = false;
//		buf.append("[");		
//		for (int i = 0; i < object.length; i++) {
//			if (object[i] != null) {
//				if (i > 0 && added) {
//					buf.append(", ");
//				}
//				buf.append("'");
////				if (object[i] instanceof Number) {
////					buf.append(formatter.format(new Object[] {object[i]}));
////				} else {
////				}
//				if (!attr.isDate()) {
//					if (object[i] instanceof Number) {
//						buf.append(formatter.format(new Object[] {object[i]}));
//					} else {
//						buf.append(object[i].toString());
//					}
//				} else {
//					buf.append(object[i].toString());
//				}
//
//				buf.append("'");
//				added = true;
//			}
//
//		}
//		buf.append("]");
//		return buf.toString();
//	}
}
