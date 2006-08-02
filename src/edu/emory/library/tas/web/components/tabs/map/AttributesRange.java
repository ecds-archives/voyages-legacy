package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.attrGroups.Attribute;

public class AttributesRange {
	private Attribute attribute;
	private int start;
	private int end;
	
	public AttributesRange(Attribute attr, int start, int end) {
		this.attribute = attr;
		this.start = start;
		this.end = end;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	public int getEnd() {
		return end;
	}
	public int getStart() {
		return start;
	}
	
}
