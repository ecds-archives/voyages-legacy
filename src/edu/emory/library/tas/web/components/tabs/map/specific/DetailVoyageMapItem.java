package edu.emory.library.tas.web.components.tabs.map.specific;

import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;
import edu.emory.library.tas.web.maps.PointOfInterest;

public class DetailVoyageMapItem extends AbstractMapItem {

	public static final String SYMBOL_NAME_PREFIX = "number-";
	
	private String symbolName;
	
	
	
	public DetailVoyageMapItem(double x, double y, String mailLabel, String symbolName) {
		super(x, y, mailLabel);
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public PointOfInterest getTooltipText() {
		// TODO Auto-generated method stub
		return null;
	}

}
