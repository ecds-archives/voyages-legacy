package edu.emory.library.tast.database.table;

import edu.emory.library.tast.database.tabscommon.VisibleAttribute;

public class DetailVoyageInfo {
	
	private VisibleAttribute attribute;
	private Object value;
	
	public DetailVoyageInfo(VisibleAttribute attr, Object value) {
		this.attribute = attr;
		this.value = value;
	}

	public VisibleAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(VisibleAttribute attribute) {
		this.attribute = attribute;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
