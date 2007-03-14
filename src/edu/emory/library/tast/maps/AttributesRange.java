package edu.emory.library.tast.maps;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;

/**
 * Range of attribute. Used in Attributes map.
 * 
 * @author Pawel Jurczyk
 */
public class AttributesRange {
	
	/**
	 * Attribute.
	 */
	private VisibleAttributeInterface attribute;
	
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
	public AttributesRange(VisibleAttributeInterface attr, int start, int end) {
		this.attribute = attr;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Gets attribute assigned to range.
	 * @return Attribute object
	 */
	public VisibleAttributeInterface getAttribute() {
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
