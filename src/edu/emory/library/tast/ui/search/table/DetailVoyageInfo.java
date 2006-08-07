package edu.emory.library.tast.ui.search.table;

import java.util.Date;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;

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
