package edu.emory.library.tas.web.components.tabs.map;

import edu.emory.library.tas.attrGroups.Attribute;

/**
 * Range of attribute. Used in Attributes map.
 * 
 * @author Pawel Jurczyk
 */
public class AttributesRange {
	
	/**
	 * Attribute.
	 */
	private Attribute attribute;
	
	/**
	 * Start index of range of Attribute coverage.
	 */
	private int start;
	
	/**
	 * End index of range of Attribute coverage.
	 */
	private int end;
	
	/**
	 * Constructor.
	 * @param attr Attribute object
	 * @param start start index of range of Attribute
	 * @param end end index of range of Attribute
	 */
	public AttributesRange(Attribute attr, int start, int end) {
		this.attribute = attr;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Gets attribute assigned to range.
	 * @return Attribute object
	 */
	public Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Gets end of attribute range.
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Gets start of attribute range
	 * @return
	 */
	public int getStart() {
		return start;
	}
	
}
