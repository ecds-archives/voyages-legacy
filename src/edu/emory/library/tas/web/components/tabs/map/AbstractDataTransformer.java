package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.attrGroups.Attribute;

public abstract class AbstractDataTransformer {
	
	private AttributesMap attributesMap;
	
	public AbstractDataTransformer(AttributesMap map) {
		this.attributesMap = map;
	}
	
	public abstract TransformerResponse transformData(Object[] data, double min, double max);
	
	protected Attribute getAttribute(int i, int j) {
		return attributesMap.getAttribute(i, j);
	}
}
