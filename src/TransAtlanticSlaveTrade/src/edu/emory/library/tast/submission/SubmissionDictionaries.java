/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.submission;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.date.DateFieldType;
import edu.emory.library.tast.common.grideditor.list.ListFieldType;
import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.common.grideditor.textbox.TextareaAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextareaFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongFieldType;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.TonType;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Resistance;

public class SubmissionDictionaries {

	// NOTE: The area list is used to derive other types
	// The AREAS type is used for broad region

	public static final String LOCATIONS = "locations";
	public static final String PORTS = "ports";
	public static final String RIGS = "rigs";
	public static final String FATES = "fates";
	public static final String TONTYPES = "tontypes";
	public static final String NATIONALS = "nationals";
	public static final String RESISTANCE = "resistance";
	public static final String REGIONS = "regions";

	public static final String FATE2 = "fate2";
	public static final String FATE3 = "fate3";
	public static final String FATE4 = "fate4";
	public static final String BOOLEAN = "boolean";
	public static final String AREAS = "areas";

	public static Map fieldTypes = new HashMap();
	public static Map simpleFieldTypes = new HashMap();
	public static ListItem[] areas;
	public static ListItem[] ports;
	public static ListItem[] rigs;
	public static ListItem[] fates;
	public static ListItem[] tontypes;
	public static ListItem[] nationals;
	public static ListItem[] regions;

	public static ListItem[] fate2;
	public static ListItem[] fate3;
	public static ListItem[] fate4;
	public static ListItem[] boolItems;
	public static ListItem[] resistance;

	static {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		List areasL = Area.loadAll(session);
		areas = new ListItem[areasL.size() + 1];
		Iterator areasI = areasL.iterator();
		int i = 1;
		while (areasI.hasNext()) {
			Area a = (Area) areasI.next();

			Set regions = a.getRegions();
			ListItem[] regionsArray = new ListItem[regions.size()];
			// regionsArray[0] = new ListItem("-1", "Unknown");
			int j = 0;
			for (Iterator iter = regions.iterator(); iter.hasNext();) {
				Region region = (Region) iter.next();
				Set ports = region.getPorts();
				ListItem[] portsArray = new ListItem[ports.size()];
				// portsArray[0] = new ListItem("-1", "Unknown");
				int k = 0;
				for (Iterator iterator = ports.iterator(); iterator.hasNext();) {
					Port element = (Port) iterator.next();
					portsArray[k++] = new ListItem(element.getId().toString(),
							element.getName());
				}
				regionsArray[j++] = new ListItem(region.getId().toString(),
						region.getName(), portsArray);
			}
			areas[i++] = new ListItem(a.getId().toString(), a.getName(),
					regionsArray);
		}
		areas[0] = new ListItem("-1", "Unknown");

		List allPorts = Port.loadAll(session);
		ports = new ListItem[allPorts.size() + 1];
		ports[0] = new ListItem("-1", "Unknown");
		for (i = 1; i < ports.length; i++) {
			Port port = (Port) allPorts.get(i - 1);
			ports[i] = new ListItem(port.getId().toString(), port.getName());
		}

		// Strip out sub elements of Area to create broad area
		ListItem[] bAreas = new ListItem[areas.length];

		for (int s = 0; s < areas.length; s++) {
			if (areas[s] != null) {
				ListItem ba = new ListItem(areas[s].getValue(), areas[s]
						.getText());
				bAreas[s] = ba;
			}
		}

		fieldTypes.put(TextboxAdapter.TYPE, new TextboxFieldType(
				TextboxAdapter.TYPE));
		fieldTypes.put(TextboxIntegerAdapter.TYPE, new TextboxIntegerFieldType(
				TextboxIntegerAdapter.TYPE));
		fieldTypes.put(TextboxLongAdapter.TYPE, new TextboxLongFieldType(
				TextboxLongAdapter.TYPE));
		fieldTypes.put(TextboxFloatAdapter.TYPE, new TextboxFloatFieldType(
				TextboxFloatAdapter.TYPE));
		fieldTypes.put(TextboxDoubleAdapter.TYPE, new TextboxDoubleFieldType(
				TextboxDoubleAdapter.TYPE));
		fieldTypes.put(DateAdapter.TYPE, new DateFieldType(DateAdapter.TYPE));
		fieldTypes.put(TextareaAdapter.TYPE, new TextareaFieldType(
				TextareaAdapter.TYPE, 10));
		fieldTypes.put(LOCATIONS, new ListFieldType(LOCATIONS, areas));
		fieldTypes.put(AREAS, new ListFieldType(AREAS, bAreas));
		fieldTypes.put(PORTS, new ListFieldType(PORTS, ports));

		rigs = fillIn(session, VesselRig.class);
		fieldTypes.put(RIGS, new ListFieldType(RIGS, rigs));
		fates = fillIn(session, Fate.class);
		fieldTypes.put(FATES, new ListFieldType(FATES, fates));
		nationals = fillIn(session, Nation.class);
		fieldTypes.put(NATIONALS, new ListFieldType(NATIONALS, nationals));

		tontypes = fillIn(session, TonType.class);
		fieldTypes.put(TONTYPES, new ListFieldType(TONTYPES, tontypes));

		fate2 = fillIn(session, FateSlaves.class);
		fieldTypes.put(FATE2, new ListFieldType(FATE2, fate2));
		fate3 = fillIn(session, FateVessel.class);
		fieldTypes.put(FATE3, new ListFieldType(FATE3, fate3));
		fate4 = fillIn(session, FateOwner.class);
		fieldTypes.put(FATE4, new ListFieldType(FATE4, fate4));

		boolItems = new ListItem[3];
		/*
		 * boolItems[0] = new ListItem("-1", "Unknown"); boolItems[1] = new
		 * ListItem("true", "Yes"); boolItems[2] = new ListItem("false", "No");
		 */
		boolItems[1] = new ListItem("false", "No");
		boolItems[0] = new ListItem("true", "Yes");
		boolItems[2] = new ListItem("-1", "Unknown");
		fieldTypes.put(BOOLEAN, new ListFieldType(BOOLEAN, boolItems));
		regions = fillIn(session, Region.class);
		fieldTypes.put(REGIONS, new ListFieldType(REGIONS, regions));

		resistance = fillIn(session, Resistance.class);
		fieldTypes.put(RESISTANCE, new ListFieldType(RESISTANCE, resistance));

		t.commit();
		session.close();

		simpleFieldTypes.put(TextboxAdapter.TYPE, new TextboxFieldType(
				TextboxAdapter.TYPE));
		simpleFieldTypes.put(TextboxIntegerAdapter.TYPE,
				new TextboxIntegerFieldType(TextboxIntegerAdapter.TYPE));
		simpleFieldTypes.put(TextboxLongAdapter.TYPE, new TextboxLongFieldType(
				TextboxLongAdapter.TYPE));
		simpleFieldTypes.put(TextboxFloatAdapter.TYPE,
				new TextboxFloatFieldType(TextboxFloatAdapter.TYPE));
		simpleFieldTypes.put(TextboxDoubleAdapter.TYPE,
				new TextboxDoubleFieldType(TextboxDoubleAdapter.TYPE));
		simpleFieldTypes.put(DateAdapter.TYPE, new DateFieldType(
				DateAdapter.TYPE));
		simpleFieldTypes.put(TextareaAdapter.TYPE, new TextareaFieldType(
				TextareaAdapter.TYPE, 10));
	}

	private static ListItem[] fillIn(Session sessios, Class clazz) {
		List dics = Dictionary.loadAll(clazz, sessios, "name");
		ListItem[] items = new ListItem[dics.size() + 1];
		items[0] = new ListItem("-1", "Unknown");
		int i = 1;
		for (Iterator iter = dics.iterator(); iter.hasNext();) {
			Dictionary element = (Dictionary) iter.next();
			items[i++] = new ListItem(element.getId().toString(), element
					.getName().toString());
		}
		return items;
	}
}
