package edu.emory.library.tas.web.components.tabs.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.attrGroups.Attribute;

public class AttributesMap {

	private List columns = new ArrayList();
	
	public AttributesMap() {
	}
	
	public void addColumn(List list) {
		this.columns.add(list);
	}
	
	public Attribute getAttribute(int i, int j) {
		List list = (List)this.columns.get(j);
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			AttributesRange element = (AttributesRange) iter.next();
			if (element.getStart() >= i && element.getEnd() <= i) {
				return element.getAttribute();
			}
		}
		return null;
	}

}
