package edu.emory.library.tast.database.table.mapimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.maps.AbstractDataTransformer;
import edu.emory.library.tast.maps.AbstractMapItem;
import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.Element;
import edu.emory.library.tast.maps.LegendItem;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapItemElement;
import edu.emory.library.tast.maps.TransformerResponse;

/**
 * Transformer used to transform mapping data for map of single voyage. It
 * assumes that data passed to transformData has one row with desired
 * attributes.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class DetailVoyageDataTransformer extends AbstractDataTransformer {

	/**
	 * Constructs transformer.
	 * 
	 * @param map
	 *            Attributes mapping in data passed to transformData.
	 */
	public DetailVoyageDataTransformer(AttributesMap map) {
		super(map);
	}

	/**
	 * Transformes data for detail voyage info.
	 */
	public TransformerResponse transformData(Session session, AbstractTransformerQueryHolder data) {

		// Retrieve first row
		Object[] row = (Object[]) data.getRawQueryResponse()[0];
		List rowList = Arrays.asList(row);
		
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

		// toMap.addAll(Arrays.asList(parseAfrica(row)));
		// toMap.addAll(Arrays.asList(parseAmericas(row)));
		// toMap.addAll(Arrays.asList(parseArrival(row)));
		
		List items = new ArrayList();

		int symbolNumber = 1;
		Object[][] response = parseDeparture(row);
		if (response.length != 0) {
			DetailVoyageMapItem item = addItemToMap(session, 
					(Dictionary) response[0][0], items, rowList, symbolNumber);
			if (item != null) {
				symbolNumber++;

				if (response[1][0] != null) {
					MapItemElement dateElement = item.getMapItemElements()[item
							.getMapItemElements().length - 1];
					dateElement.addElement(new Element(this.getAttribute(0,
							rowList.indexOf(response[1][0])), SimpleDateFormat
							.getDateInstance(SimpleDateFormat.LONG).format(
									(Date) response[1][0])));
				}
			}
		}

		response = parseAfrica(row);
		if (response.length != 0) {
			for (int i = 0; i < response[0].length; i++) {
				DetailVoyageMapItem item = addItemToMap(session,
						(Dictionary) response[0][i], items, rowList,
						symbolNumber);
				if (item != null) {
					symbolNumber++;

					if (response[1][0] != null && i == 0) {
						MapItemElement dateElement = item.getMapItemElements()[item
								.getMapItemElements().length - 1];
						dateElement.addElement(new Element(this.getAttribute(0,
								rowList.indexOf(response[1][0])),
								SimpleDateFormat.getDateInstance(
										SimpleDateFormat.LONG).format(
										(Date) response[1][0])));
					}
					if (response[1][1] != null && i == response[0].length - 1) {
						MapItemElement dateElement = item.getMapItemElements()[item
								.getMapItemElements().length - 1];
						dateElement.addElement(new Element(this.getAttribute(0,
								rowList.indexOf(response[1][1])),
								SimpleDateFormat.getDateInstance(
										SimpleDateFormat.LONG).format(
										(Date) response[1][1])));
					}
				}
			}
		}

		response = parseAmericas(row);
		if (response.length != 0) {
			for (int i = 0; i < response[0].length; i++) {
				DetailVoyageMapItem item = addItemToMap(session,
						(Dictionary) response[0][i], items, rowList,
						symbolNumber);
				if (item != null) {
					symbolNumber++;

					if (response[1][i] != null) {
						MapItemElement dateElement = item.getMapItemElements()[item
								.getMapItemElements().length - 1];
						dateElement.addElement(new Element(this.getAttribute(0,
								rowList.indexOf(response[1][i])),
								SimpleDateFormat.getDateInstance(
										SimpleDateFormat.LONG).format(
										(Date) response[1][i])));
					}
				}
			}
		}

		response = parseArrival(row);
		if (response.length != 0) {
			DetailVoyageMapItem item = addItemToMap(session,
					(Dictionary) response[0][0], items, rowList, symbolNumber);
			if (item != null) {
				symbolNumber++;

				if (response[1][0] != null) {
					MapItemElement dateElement = item.getMapItemElements()[item
							.getMapItemElements().length - 1];
					dateElement.addElement(new Element(this.getAttribute(0,
							rowList.indexOf(response[1][0])), SimpleDateFormat
							.getDateInstance(SimpleDateFormat.LONG).format(
									(Date) response[1][0])));
				}
			}
		}

		// Prepare legend of map
		// Legend will have only one group with no name
		LegendItemsGroup legend = new LegendItemsGroup("");
		int i = 0;
		
		// Legend for different symbols
		for (Iterator iter = items.iterator(); iter.hasNext(); i++) {
			DetailVoyageMapItem element = (DetailVoyageMapItem) iter.next();
			// element.setNumber(i + 1);
			String[] legendSymbols = element.getLegendSymbolNames();
			for (int j = 0; j < legendSymbols.length; j++) {
				LegendItem legendItem = new LegendItem(legendSymbols[j],
						contextPath + "/tast-map-assets/symbols/" + legendSymbols[j] + ".png",
						element.getLegendTexts()[j]);
				legend.addItemToGroup(legendItem);
				element.addLegendItem(legendItem);
			}
		}

		// Return result of transformation
		return new TransformerResponse((AbstractMapItem[]) items
				.toArray(new AbstractMapItem[] {}),
				new LegendItemsGroup[] { legend });
	}

	private DetailVoyageMapItem addItemToMap(Session session, Dictionary dict, List items,
			List rowList, int symbolNumber) {

		// Get GIS port
		Location gisLoc = Port.loadById(session, dict.getId().longValue());
		if (gisLoc == null) {
			gisLoc = Region.loadById(session, dict.getId().longValue());
		}
		if (gisLoc != null) {
			// Prepare test item
			DetailVoyageMapItem testItem = new DetailVoyageMapItem(gisLoc
					.getX(), gisLoc.getY(), gisLoc.getName(), 1);
			int index;
			if ((index = items.indexOf(testItem)) != -1) {
				// Item with given coordinates already in map - will add
				// additional element to
				// existing map item
				DetailVoyageMapItem item = (DetailVoyageMapItem) items
						.get(index);
				MapItemElement itemElement = new MapItemElement(getAttribute(0,
						rowList.lastIndexOf(dict)), new Object[] { new Integer(
						symbolNumber++) });
				itemElement.addElement(new Element(getAttribute(0, rowList
						.indexOf(dict)), ""));
				item.addMapItemElement(itemElement);
				return item;
			} else {
				// No item in map - will create new one
				MapItemElement itemElement = new MapItemElement(getAttribute(0,
						rowList.indexOf(dict)), new Object[] { new Integer(
						symbolNumber++) });
				itemElement.addElement(new Element(getAttribute(0, rowList
						.indexOf(dict)), ""));
				testItem.addMapItemElement(itemElement);
				// Set prijected coordinates as well
				testItem.setProjXY(gisLoc.getX(), gisLoc.getY());
				items.add(testItem);
				return testItem;
			}
		}
		return null;
	}

	private Object[][] parseDeparture(Object[] data) {
		if (data[0] != null) {
			return new Object[][] { { data[0] }, { data[19] } };
		} else if (data[1] != null) {
			return new Object[][] { { data[1] }, { data[19] } };
		} else {
			// Unknown port - add '?' mark
			return new Object[][] {};
		}
	}

	private Object[][] parseAfrica(Object[] data) {
		if (data[2] != null) {
			List ports = new ArrayList();
			ports.add(data[2]);
			if (data[3] != null) {
				ports.add(data[3]);
			}
			if (data[4] != null) {
				ports.add(data[4]);
			}
			return new Object[][] { ports.toArray(), { data[20], data[21] } };
		} else if (data[5] != null) {
			List regions = new ArrayList();
			regions.add(data[5]);
			if (data[6] != null) {
				regions.add(data[6]);
			}
			if (data[7] != null) {
				regions.add(data[7]);
			}
			return new Object[][] { regions.toArray(), { data[20], data[21] } };
		} else if (data[8] != null) {
			return new Object[][] {{data[8]}, {data[20], data[21]}};
		} else if (data[9] != null) {
			return new Object[][] {{data[9]}, {data[20], data[21]}};
		} else {
			return new Object[][] {};
		}
//		else if (data[8] != null) {
//			List ports = new ArrayList();
//			ports.add(data[8]);
//			if (data[9] != null) {
//				ports.add(data[9]);
//			}
//			return new Object[][] { ports.toArray(), { data[28], data[29] } };
//		} else if (data[10] != null) {
//			List regions = new ArrayList();
//			regions.add(data[10]);
//			if (data[11] != null) {
//				regions.add(data[11]);
//			}
//			return new Object[][] { regions.toArray(), { data[28], data[29] } };
//		} else if (data[12] != null) {
//			return new Object[][] { { data[12] }, { data[28], data[29] } };
//		} else if (data[13] != null) {
//			return new Object[][] { { data[13] }, { data[28], data[29] } };
//		} else {
//			return new Object[][] {};
//		}
	}

	private Object[][] parseAmericas(Object[] data) {
		if (data[10] != null) {
			List ports = new ArrayList();
			ports.add(data[10]);
			if (data[11] != null) {
				ports.add(data[11]);
			}
			if (data[12] != null) {
				ports.add(data[12]);
			}
			return new Object[][] { ports.toArray(),
					{ data[22], data[23], data[24], data[25] } };
		} else if (data[13] != null) {
			List regions = new ArrayList();
			regions.add(data[13]);
			if (data[14] != null) {
				regions.add(data[14]);
			}
			if (data[15] != null) {
				regions.add(data[15]);
			}
			return new Object[][] { regions.toArray(),
					{ data[22], data[23], data[24], data[25] } };
		} else if (data[16] != null) {
			List ports = new ArrayList();
			ports.add(data[16]);
			return new Object[][] { ports.toArray(),
					{ data[22], data[23], data[24], data[25] } };
		} else {
			return new Object[][] {};
		}
	}

	private Object[][] parseArrival(Object[] data) {
		if (data[17] != null) {
			return new Object[][] { { data[17] }, { data[26] } };
		} else if (data[18] != null) {
			return new Object[][] { { data[18] }, { data[26] } };
		} else {
			// Unknown port - add '?' mark
			return new Object[][] {};
		}
	}

}
