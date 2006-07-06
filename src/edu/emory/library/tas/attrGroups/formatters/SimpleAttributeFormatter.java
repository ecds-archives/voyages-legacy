package edu.emory.library.tas.attrGroups.formatters;

public class SimpleAttributeFormatter extends AbstractAttributeFormatter {

	public String format(Object object) {
		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}

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
				buf.append(object[i].toString());
				buf.append("'");
				added = true;
			}

		}
		buf.append("]");
		return buf.toString();
	}

}
