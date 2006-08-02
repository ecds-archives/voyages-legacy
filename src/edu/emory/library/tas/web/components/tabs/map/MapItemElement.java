package edu.emory.library.tas.web.components.tabs.map;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tas.attrGroups.Attribute;

public class MapItemElement {
	private Attribute attribute;
	private List elements = new ArrayList();
	private Element max = null;
	
	public MapItemElement(Attribute attribute) {
		this.attribute = attribute;
	}
	
	public void addElement(Element element) {
		this.elements.add(element);
		if (this.max == null || this.max.getValue().compareTo(element.getValue()) == -1) {
			this.max = element;
		}
	}
	
	public Element[] getElements() {
		return (Element[])this.elements.toArray(new Element[] {});
	}
	
	public Element getMaxElement() {
		return max;
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
}
