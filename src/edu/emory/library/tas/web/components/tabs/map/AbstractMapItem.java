package edu.emory.library.tas.web.components.tabs.map;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tas.web.maps.PointOfInterest;

public abstract class AbstractMapItem {
	
	private double x;
	
	private double y;
	
	private String mailLabel;
	
	private List elements = new ArrayList();
	
	public AbstractMapItem(double x, double y, String mailLabel) {
		this.x = x;
		this.y = y;
		this.mailLabel = mailLabel;
	}
	
	public MapItemElement[] getMapItemElements() {
		return (MapItemElement[])elements.toArray(new MapItemElement[] {});
	}
	
	public void addMapItemElement(MapItemElement element) {
		this.elements.add(element);
	}
	
	public abstract String getSymbolName();
	
	public abstract PointOfInterest getTooltipText();

	public String getMainLabel() {
		return mailLabel;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof AbstractMapItem)) {
			return false;
		}
		AbstractMapItem that = (AbstractMapItem)o;
		return this.x == that.x && this.y == that.y;
	}
}
