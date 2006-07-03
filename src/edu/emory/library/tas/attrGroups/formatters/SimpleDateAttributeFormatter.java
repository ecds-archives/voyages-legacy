package edu.emory.library.tas.attrGroups.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateAttributeFormatter extends AbstractAttributeFormatter {

	private SimpleDateFormat dateFormat;
	
	public SimpleDateAttributeFormatter(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String format(Object object) {
		if (object == null) {
			return "";
		} else {
			return dateFormat.format((Date)object);
		}
	}

	public String format(Object[] object) {
		return "";
	}

}
