package edu.emory.library.tast.ui.search.table.mapimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.GISPortLocation;
import edu.emory.library.tast.ui.maps.AbstractDataTransformer;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.LegendItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapItemElement;
import edu.emory.library.tast.ui.maps.TransformerResponse;

/**
 * Transformer used to transform mapping data for map of single voyage.
 * It assumes that data passed to transformData has one row with desired attributes. 
 * @author Pawel Jurczyk
 *
 */
public class DetailVoyageDataTransformer extends AbstractDataTransformer {

	/**
	 * Constructs transformer.
	 * @param map Attributes mapping in data passed to transformData.
	 */
	public DetailVoyageDataTransformer(AttributesMap map) {
		super(map);
	}

	/**
	 * Transformes data for detail voyage info.
	 */
	public TransformerResponse transformData(Object[] data, double min, double max) {
		
		//Retrieve first row
		Object[] row = (Object[]) data[0];
		List rowList = Arrays.asList(row);
		
		//Prepare attributes that will be shown on map
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

		//Prepare map items list
		for (Iterator iter = toMap.iterator(); iter.hasNext();) {
			
			//Get attribute from DB data
			Dictionary dict = (Dictionary) iter.next();
			
			//Get GIS port
			GISPortLocation gisLoc = GISPortLocation.getGISPortLocation(dict);
			if (gisLoc != null) {
				//Prepare test item
				DetailVoyageMapItem testItem = new DetailVoyageMapItem(gisLoc.getX(), gisLoc.getY(), gisLoc.getPortName(), 1);
				int index;
				if ((index = items.indexOf(testItem)) != -1) {
					//Item with given coordinates already in map - will add additional element to
					//existing map item
					DetailVoyageMapItem item = (DetailVoyageMapItem) items.get(index);
					item.getMapItemElements()[0].addElement(new Element(this.getAttribute(0, rowList.indexOf(dict)), ""));
				} else {
					//No item in map - will create new one
					MapItemElement itemElement = new MapItemElement(getAttribute(0, rowList.indexOf(dict)));
					itemElement.addElement(new Element(getAttribute(0, rowList.indexOf(dict)), null));
					testItem.addMapItemElement(itemElement);
					//Set prijected coordinates as well
					double [] projXY = gisLoc.getXYProjected();
					testItem.setProjXY(projXY[0], projXY[1]);
					items.add(testItem);
				}
			}
		}

		//Prepare legend of map
		//Legend will have only one group with no name
		LegendItemsGroup legend = new LegendItemsGroup("");
		int i = 0;
		
		//Legend for different symbols
		for (Iterator iter = items.iterator(); iter.hasNext(); i++) {
			DetailVoyageMapItem element = (DetailVoyageMapItem) iter.next();
			element.setNumber(i + 1);
			LegendItem legengItem = new LegendItem("/symbols/number-1-" + (i + 1) + ".png", element.getLegendText());
			legend.addItemToGroup(legengItem);
		}
		
		//Return result of transformation
		return new TransformerResponse((AbstractMapItem[])items.toArray(new AbstractMapItem[] {}), new LegendItemsGroup[] {legend});
	}

}
