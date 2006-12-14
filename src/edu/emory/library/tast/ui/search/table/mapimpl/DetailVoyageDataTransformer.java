package edu.emory.library.tast.ui.search.table.mapimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Location;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.maps.AbstractDataTransformer;
import edu.emory.library.tast.ui.maps.AbstractMapItem;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.Element;
import edu.emory.library.tast.ui.maps.LegendItem;
import edu.emory.library.tast.ui.maps.LegendItemsGroup;
import edu.emory.library.tast.ui.maps.MapItemElement;
import edu.emory.library.tast.ui.maps.TransformerResponse;

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
	public TransformerResponse transformData(AbstractTransformerQueryHolder data) {

		// Retrieve first row
		Object[] row = (Object[]) data.getRawQueryResponse()[0];
		List rowList = Arrays.asList(row);

		// toMap.addAll(Arrays.asList(parseAfrica(row)));
		// toMap.addAll(Arrays.asList(parseAmericas(row)));
		// toMap.addAll(Arrays.asList(parseArrival(row)));
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
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
						"symbols/" + legendSymbols[j] + ".png", element
								.getLegendTexts()[j]);
				legend.addItemToGroup(legendItem);
				element.addLegendItem(legendItem);
			}
		}
		t.commit();
		session.close();

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
			return new Object[][] { { data[0] }, { data[27] } };
		} else if (data[1] != null) {
			return new Object[][] { { data[1] }, { data[27] } };
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
			return new Object[][] { ports.toArray(), { data[28], data[29] } };
		} else if (data[5] != null) {
			List regions = new ArrayList();
			regions.add(data[5]);
			if (data[6] != null) {
				regions.add(data[6]);
			}
			if (data[7] != null) {
				regions.add(data[7]);
			}
			return new Object[][] { regions.toArray(), { data[28], data[29] } };
		} else if (data[8] != null) {
			List ports = new ArrayList();
			ports.add(data[8]);
			if (data[9] != null) {
				ports.add(data[9]);
			}
			return new Object[][] { ports.toArray(), { data[28], data[29] } };
		} else if (data[10] != null) {
			List regions = new ArrayList();
			regions.add(data[10]);
			if (data[11] != null) {
				regions.add(data[11]);
			}
			return new Object[][] { regions.toArray(), { data[28], data[29] } };
		} else if (data[12] != null) {
			return new Object[][] { { data[12] }, { data[28], data[29] } };
		} else if (data[13] != null) {
			return new Object[][] { { data[13] }, { data[28], data[29] } };
		} else {
			return new Object[][] {};
		}
	}

	private Object[][] parseAmericas(Object[] data) {
		if (data[14] != null) {
			List ports = new ArrayList();
			ports.add(data[14]);
			if (data[15] != null) {
				ports.add(data[3]);
			}
			if (data[16] != null) {
				ports.add(data[4]);
			}
			return new Object[][] { ports.toArray(),
					{ data[30], data[31], data[32], data[33] } };
		} else if (data[17] != null) {
			List regions = new ArrayList();
			regions.add(data[17]);
			if (data[18] != null) {
				regions.add(data[18]);
			}
			if (data[19] != null) {
				regions.add(data[19]);
			}
			return new Object[][] { regions.toArray(),
					{ data[30], data[31], data[32], data[33] } };
		} else if (data[20] != null) {
			List ports = new ArrayList();
			ports.add(data[20]);
			if (data[21] != null) {
				ports.add(data[21]);
			}
			return new Object[][] { ports.toArray(),
					{ data[30], data[31], data[32], data[33] } };
		} else if (data[22] != null) {
			List regions = new ArrayList();
			regions.add(data[22]);
			if (data[23] != null) {
				regions.add(data[23]);
			}
			return new Object[][] { regions.toArray(),
					{ data[30], data[31], data[32], data[33] } };
		} else if (data[24] != null) {
			return new Object[][] { { data[24] },
					{ data[30], data[31], data[32], data[33] } };
		} else {
			return new Object[][] {};
		}
	}

	private Object[][] parseArrival(Object[] data) {
		if (data[25] != null) {
			return new Object[][] { { data[25] }, { data[34] } };
		} else if (data[26] != null) {
			return new Object[][] { { data[26] }, { data[34] } };
		} else {
			// Unknown port - add '?' mark
			return new Object[][] {};
		}
	}

}
