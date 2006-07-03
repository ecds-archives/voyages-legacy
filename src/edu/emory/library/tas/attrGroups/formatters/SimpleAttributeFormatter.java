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
		buf.append("[");
		for (int i = 0; i < object.length; i++) {

			if (object[i] != null) {
				buf.append("'");
				buf.append(object[i].toString());
				buf.append("'");
				if (i < object.length - 1) {
					buf.append(", ");
				}
			}

		}
		buf.append("]");
		return buf.toString();
	}

}
