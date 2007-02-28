package edu.emory.library.tast.ui.search.table;

import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;

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
