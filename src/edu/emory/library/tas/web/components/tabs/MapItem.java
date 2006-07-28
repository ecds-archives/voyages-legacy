/**
 * 
 */
package edu.emory.library.tas.web.components.tabs;

import java.util.Iterator;
import java.util.List;

public class MapItem {

	public String label;

	public double x;

	public double y;

	public List size;

	public int shape;
	
	public int color;
	
	public int maxVal = Integer.MIN_VALUE;
	
	public boolean autoSized;
	
	List usedAttrs;

	public MapItem(String label, double x, double y, List size, List usedAttrs, int shapeIndex, int color, boolean autoSized) {
		this.label = label;
		this.x = x;
		this.y = y;
		this.size = size;
		this.shape = shapeIndex;
		this.color = color;
		this.usedAttrs = usedAttrs;
		this.autoSized = autoSized;
		for (Iterator iter = size.iterator(); iter.hasNext();) {
			Number element = (Number) iter.next();
			if (maxVal < element.intValue()) {
				maxVal = element.intValue();
			}
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public List getData() {
		return size;
	}

	public void setData(List data) {
		this.size = data;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof MapItem)) {
			return false;
		}
		MapItem that = (MapItem)o;
		return this.x == that.x && this.y == that.y;
	}

	public boolean isAutoSized() {
		return autoSized;
	}

	public void setAutoSized(boolean autoSized) {
		this.autoSized = autoSized;
	}

	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public List getUsedAttrs() {
		return usedAttrs;
	}

	public void setUsedAttrs(List list) {
		this.usedAttrs = list;
	}
	
	public int getMaxVal() {
		return maxVal;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public List getSize() {
		return size;
	}

	public void setSize(List size) {
		this.size = size;
	}
}