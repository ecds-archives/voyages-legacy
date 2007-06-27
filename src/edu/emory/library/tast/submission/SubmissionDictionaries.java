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
import edu.emory.library.tast.common.grideditor.textbox.TextboxFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongFieldType;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.util.HibernateUtil;

public class SubmissionDictionaries {

	public static final String LOCATIONS = "locations";	
	public static final String PORTS = "ports";	
	public static final String RIGS = "rigs";
	public static final String FATES = "fates";
	public static final String TONTYPES = "tontypes";
	public static final String NATIONALS = "nationals";
	
	public static final String FATE2 = "fate2";
	public static final String FATE3 = "fate3";
	public static final String FATE4 = "fate4";
	public static final String BOOLEAN = "boolean";
	
	public static Map fieldTypes = new HashMap();
	public static ListItem[] areas;	
	public static ListItem[] ports;
	public static ListItem[] rigs;
	public static ListItem[] fates;
	public static ListItem[] tontypes;
	public static ListItem[] nationals;
	
	public static ListItem[] fate2;
	public static ListItem[] fate3;
	public static ListItem[] fate4;
	public static ListItem[] boolItems;
	
	static {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction(); 
		List areasL = Area.loadAll(session);
		areas = new ListItem[areasL.size() + 1];
		Iterator areasI = areasL.iterator();
		int i = 1;
		while (areasI.hasNext()) {
			Area a = (Area) areasI.next();
			
			Set regions = a.getRegions();
			ListItem[] regionsArray = new ListItem[regions.size()];
			//regionsArray[0] = new ListItem("-1", "Unknown");
			int j = 0;
			for (Iterator iter = regions.iterator(); iter.hasNext();) {
				Region region = (Region) iter.next();
				Set ports = region.getPorts();
				ListItem[] portsArray = new ListItem[ports.size()];
				//portsArray[0] = new ListItem("-1", "Unknown");
				int k = 0;
				for (Iterator iterator = ports.iterator(); iterator.hasNext();) {
					Port element = (Port) iterator.next();
					portsArray[k++] = new ListItem(element.getId().toString(), element.getName());
				}
				regionsArray[j++] = new ListItem(region.getId().toString(), region.getName(), portsArray);
			}
			areas[i++] = new ListItem(a.getId().toString(), a.getName(), regionsArray);
		}
		areas[0] = new ListItem("-1", "Unknown");
		
		List allPorts = Port.loadAll(session);
		ports = new ListItem[allPorts.size() + 1];
		ports[0] = new ListItem("-1", "Unknown");
		for (i = 1; i < ports.length; i++) {
			Port port = (Port) allPorts.get(i-1);
			ports[i] = new ListItem(port.getId().toString(), port.getName());
		}
		
		
		fieldTypes.put(TextboxAdapter.TYPE, new TextboxFieldType(TextboxAdapter.TYPE));
		fieldTypes.put(TextboxIntegerAdapter.TYPE, new TextboxIntegerFieldType(TextboxIntegerAdapter.TYPE));
		fieldTypes.put(TextboxLongAdapter.TYPE, new TextboxLongFieldType(TextboxLongAdapter.TYPE));
		fieldTypes.put(TextboxFloatAdapter.TYPE, new TextboxFloatFieldType(TextboxFloatAdapter.TYPE));
		fieldTypes.put(DateAdapter.TYPE, new DateFieldType(DateAdapter.TYPE));
		fieldTypes.put(TextareaAdapter.TYPE, new TextareaFieldType(TextareaAdapter.TYPE, 10));
		fieldTypes.put(LOCATIONS, new ListFieldType(LOCATIONS, areas));
		fieldTypes.put(PORTS, new ListFieldType(PORTS, ports));
		
		
		rigs = fillIn(session, VesselRig.class);
		fieldTypes.put(RIGS, new ListFieldType(RIGS, rigs));
		fates = fillIn(session, Fate.class);
		fieldTypes.put(FATES, new ListFieldType(FATES, fates));
		nationals = fillIn(session, Nation.class);
		fieldTypes.put(NATIONALS, new ListFieldType(NATIONALS, nationals));
		
		fate2 = fillIn(session, FateSlaves.class);
		fieldTypes.put(FATE2, new ListFieldType(FATE2, fate2));
		fate3 = fillIn(session, FateVessel.class);
		fieldTypes.put(FATE3, new ListFieldType(FATE3, fate3));
		fate4 = fillIn(session, FateOwner.class);
		fieldTypes.put(FATE4, new ListFieldType(FATE4, fate4));
		
		boolItems = new ListItem[3];
		boolItems[0] = new ListItem("-1", "Unknown");
		boolItems[1] = new ListItem("true", "Yes");
		boolItems[2] = new ListItem("false", "No");
		fieldTypes.put(BOOLEAN, new ListFieldType(BOOLEAN, boolItems));
		
		t.commit();
		session.close();
	}
	
	
	private static ListItem[] fillIn(Session sessios, Class clazz) {
		List dics = Dictionary.loadAll(clazz, sessios, "name");
		ListItem[] items = new ListItem[dics.size() + 1];
		items[0] = new ListItem("-1", "Not selected");
		int i = 1;
		for (Iterator iter = dics.iterator(); iter.hasNext();) {
			Dictionary element = (Dictionary) iter.next();
			items[i++] = new ListItem(element.getId().toString(), element.getName().toString());
		}
		return items;
	}
}
