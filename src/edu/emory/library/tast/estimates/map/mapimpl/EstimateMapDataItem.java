package edu.emory.library.tast.estimates.map.mapimpl;

import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.component.PointOfInterest;

public class EstimateMapDataItem extends AbstractMapItem {

	/**
	 * Used symbol prefix.
	 */
	public static final String SYMBOL_NAME_PREFIX = "circle-";

	/**
	 * Name of symbol.
	 */
	private String symbolName;

	/**
	 * Used color.
	 */
	private int color;

	private int size;

	public EstimateMapDataItem(double x, double y, String mainLabel) {
		super(x, y, mainLabel);
	}

	public String[] getLegendSymbolNames() {
		return new String[] {this.symbolName};
	}

	public String[] getSymbolNames() {
		return new String[] {this.symbolName};
	}

	public PointOfInterest getTooltipText() {
		
		PointOfInterest point = new PointOfInterest(this.getX(), this.getY());
		point.setText(this.buildToolTipInfo());
		return point;
		
	}
	
	private String buildToolTipInfo() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<div style=\"white-space: nowrap\">");
		buffer.append("<b>");
		buffer.append("Location : ").append(this.getMainLabel());
		buffer.append("</b><br/>");

		Element[] elements = this.getMapItemElements()[0].getElements();
		for (int i = 0; i < elements.length; i++) {
			Element element = elements[i];
			buffer.append(element.getAttribute().getUserLabelOrName()).append(": ");
			buffer.append(element.getValue()).append("<br/>");
		}
		buffer.append("</div>");

		return buffer.toString();

	}



	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		StringBuffer buf = new StringBuffer();
		buf.append(SYMBOL_NAME_PREFIX);
		buf.append(this.color);
		buf.append("-").append(this.size);
		this.symbolName = buf.toString();
	}

	
	
}
