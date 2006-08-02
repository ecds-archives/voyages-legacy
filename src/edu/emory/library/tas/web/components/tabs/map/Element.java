package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.attrGroups.Attribute;

public class Element {
	private Attribute attribute;
	private Comparable value;
	
	public Element(Attribute attribute, Comparable value) {
		this.attribute = attribute;
		this.value = value;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Comparable getValue() {
		return value;
	}

	public void setValue(Comparable value) {
		this.value = value;
	}
	
}
