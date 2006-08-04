package edu.emory.library.tas.web.components.tabs.map.specific;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.GISPortLocation;
import edu.emory.library.tas.web.components.tabs.map.AbstractDataTransformer;
import edu.emory.library.tas.web.components.tabs.map.AbstractMapItem;
import edu.emory.library.tas.web.components.tabs.map.AttributesMap;
import edu.emory.library.tas.web.components.tabs.map.Element;
import edu.emory.library.tas.web.components.tabs.map.LegendItem;
import edu.emory.library.tas.web.components.tabs.map.LegendItemsGroup;
import edu.emory.library.tas.web.components.tabs.map.MapItemElement;
import edu.emory.library.tas.web.components.tabs.map.TransformerResponse;

public class DetailVoyageDataTransformer extends AbstractDataTransformer {

	public DetailVoyageDataTransformer(AttributesMap map) {
		super(map);
	}

	public TransformerResponse transformData(Object[] data, double min, double max) {
		
		Object[] row = (Object[]) data[0];
		List rowList = Arrays.asList(row);
		
		List toMap = new ArrayList();
		if (row[0] == null) {
			toMap.add(row[1]);
		} else {
			toMap.add(row[0]);
		}
		if (row[2] == null) {
			toMap.add(row[3]);
		} else {
			toMap.add(row[2]);
		}
		if (row[4] == null) {
			toMap.add(row[5]);
		} else {
			toMap.add(row[4]);
		}
		if (row[6] == null) {
			toMap.add(row[7]);
		} else {
			toMap.add(row[6]);
		}
		toMap.add(row[8]);
		
		List items = new ArrayList();
		
		for (Iterator iter = toMap.iterator(); iter.hasNext();) {				
			Dictionary dict = (Dictionary) iter.next();
			GISPortLocation gisLoc = GISPortLocation.getGISPortLocation(dict);
			if (gisLoc != null) {
				DetailVoyageMapItem testItem = new DetailVoyageMapItem(gisLoc.getX(), gisLoc.getY(), gisLoc.getPortName(), 1);
				int index;
				if ((index = items.indexOf(testItem)) != -1) {
					DetailVoyageMapItem item = (DetailVoyageMapItem) items.get(index);
					double [] projXY = gisLoc.getXYProjected();
					item.setProjXY(projXY[0], projXY[1]);
					item.getMapItemElements()[0].addElement(new Element(this.getAttribute(0, rowList.indexOf(dict)), ""));
				} else {
					MapItemElement itemElement = new MapItemElement(getAttribute(0, rowList.indexOf(dict)));
					itemElement.addElement(new Element(getAttribute(0, rowList.indexOf(dict)), null));
					testItem.addMapItemElement(itemElement);
					items.add(testItem);
				}
			}
		}

		LegendItemsGroup legend = new LegendItemsGroup("");
		int i = 0;
		for (Iterator iter = items.iterator(); iter.hasNext(); i++) {
			DetailVoyageMapItem element = (DetailVoyageMapItem) iter.next();
			element.setNumber(i + 1);
			LegendItem legengItem = new LegendItem("/symbols/number-1-" + (i + 1) + ".png", element.getLegendText());
			legend.addItemToGroup(legengItem);
		}
		
		return new TransformerResponse((AbstractMapItem[])items.toArray(new AbstractMapItem[] {}), new LegendItemsGroup[] {legend});
	}

}
