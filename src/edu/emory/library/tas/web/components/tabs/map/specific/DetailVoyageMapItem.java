package edu.emory.library.tas.web.components.tabs.map.specific;

import java.util.Iterator;

import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;
import edu.emory.library.tas.web.components.tabs.map.Element;
import edu.emory.library.tas.web.maps.PointOfInterest;

public class DetailVoyageMapItem extends AbstractMapItem {

	public static final String SYMBOL_NAME_PREFIX = "number-";
	
	String symbolName;
	
	private int color;
	
	private int number;
	
	public DetailVoyageMapItem(double x, double y, String mailLabel, int color) {
		super(x, y, mailLabel);
		this.color = color;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public PointOfInterest getTooltipText() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<b>").append("Location: ");
		buffer.append(this.getMainLabel()).append("</b><br/>");
		Element[] elements = this.getMapItemElements()[0].getElements();
		boolean first = true;
		for (int i = 0; i < elements.length; i++) {
			if (!first) {
				buffer.append(", ");
			}
			Element element = elements[i];
			buffer.append(element.getAttribute());
			first = false;
		}
		
		PointOfInterest point = new PointOfInterest(this.getX(), this.getY());
		point.setText(buffer.toString());
		return point;
	}
	
	public void setNumber(int number) {
		this.number = number;
		StringBuffer buffer = new StringBuffer();
		buffer.append(SYMBOL_NAME_PREFIX);
		buffer.append(color).append("-");
		buffer.append(this.number);
		this.symbolName = buffer.toString();
	}

	public String getLegendText() {
		StringBuffer buffer = new StringBuffer();
		Element[] elements = this.getMapItemElements()[0].getElements();
		boolean first = true;
		for (int i = 0; i < elements.length; i++) {
			if (!first) {
				buffer.append(", ");
			}
			Element element = elements[i];
			buffer.append(element.getAttribute());
			first = false;
		}
		return buffer.toString();
	}

}
