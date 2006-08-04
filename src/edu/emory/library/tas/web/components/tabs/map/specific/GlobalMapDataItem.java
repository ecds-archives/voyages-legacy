package edu.emory.library.tas.web.components.tabs.map.specific;

import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;
import edu.emory.library.tas.web.components.tabs.map.Element;
import edu.emory.library.tas.web.maps.PointOfInterest;


public class GlobalMapDataItem extends AbstractMapItem {

	public static final String SYMBOL_NAME_PREFIX = "circle-";
	
	private String symbolName;
	
	private int color;
	
	public GlobalMapDataItem(double x, double y, String mailLabel, int color) {
		super(x, y, mailLabel);
		this.color = color;
	}
	
	public void setSymbolColor(int color) {
		this.color = color;
	}

	public String getSymbolName() {
		return symbolName;
	}
	
	public void setSymbolSize(int size) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(SYMBOL_NAME_PREFIX).append(this.color);
		buffer.append("-").append(size + 1);
		this.symbolName =  buffer.toString();
	}

	public PointOfInterest getTooltipText() {
		PointOfInterest point = new PointOfInterest(this.getProjectedX(), this.getProjectedY());
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


}
