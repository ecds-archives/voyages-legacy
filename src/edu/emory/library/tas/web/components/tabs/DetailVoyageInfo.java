package edu.emory.library.tas.web.components.tabs;

import java.util.Date;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.Attribute;

public class DetailVoyageInfo {
	
	private Attribute attribute;
	private Object value;
	
	public DetailVoyageInfo(Attribute attr, Object value) {
		this.attribute = attr;
		this.value = value;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
