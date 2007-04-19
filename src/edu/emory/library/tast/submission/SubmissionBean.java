package edu.emory.library.tast.submission;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
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
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SubmissionBean {

	public static final String ORIGINAL_VOYAGE_LABEL = TastResource
			.getText("submissions_oryginal_voyage");

	public static final String CHANGED_VOYAGE_LABEL = TastResource
			.getText("submissions_changed_voyage");

	public static final String ORIGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	public static final String LOCATIONS = "locations";	
	public static final String PORTS = "ports";	
	public static final String FATES = "fates";
	public static final String RIGS = "rigs";
	public static final String COUNTRIES = "countries";
	public static final String RESISTANCES = "resistances";

	private static SubmissionAttribute[] attrs = SubmissionAttributes
			.getConfiguration().getPublicAttributes();

	private long voyageId = -1;

	private Values valsToSubmit = null;

	private boolean wasError = false;

	private Values vals = null;

	private ListItem[] areas;	
	private ListItem[] ports;
	private ListItem[] fates;
	private ListItem[] rigs;
	private ListItem[] countries;
	private ListItem[] resistances;
	
	
	public SubmissionBean() {
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
		
		fates = fillIn(session, FateAttribute.class);
		
		t.commit();
		session.close();
		
	}
	
	

	private ListItem[] fillIn(Session sessios, Class clazz) {
		List dics = Dictionary.loadAll(clazz, session);
		ListItem[] items = new ListItem[dics.size() + 1];
		int i = 1;
		for (Iterator iter = dics.iterator(); iter.hasNext();) {
			type element = (type) iter.next();
			
		}
	}



	public void setVoyageId(long voyageId) {
		this.voyageId = voyageId;
	}

	public Values getValues() {

		if (!wasError || vals == null) {
			
			this.voyageId = 43;

			if (this.voyageId == -1) {
				throw new RuntimeException("Voyage ID was not set!");
			}

			
			Session session = HibernateUtil.getSession();
			Transaction t = session.beginTransaction();
			vals = new Values();

			Conditions c = new Conditions();
			c.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId),
					Conditions.OP_EQUALS);
			c.addCondition(Voyage.getAttribute("revision"), new Integer(1),
					Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("Voyage", c);
			for (int i = 0; i < attrs.length; i++) {
				Attribute[] attributes = attrs[i].getAttribute();
				for (int j = 0; j < attributes.length; j++) {
					qValue.addPopulatedAttribute(attributes[j]);
				}
			}
			Object[] res = qValue.executeQuery(session);
			if (res.length == 0) {
				return null;
			}

			Object[] voyageAttrs = (Object[]) res[0];
			int index = 0;
			for (int i = 0; i < attrs.length; i++) {
				SubmissionAttribute attribute = attrs[i];
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++) {
					toBeFormatted[j] = voyageAttrs[j + index];
				}
				vals.setValue(ORIGINAL_VOYAGE, attrs[i].getName(), attrs[i]
						.getValue(toBeFormatted));
				vals.setValue(CHANGED_VOYAGE, attrs[i].getName(), attrs[i]
						.getEmptyValue());
				index += attribute.getAttribute().length;
			}
			
			t.commit();
			session.close();
		}

		return vals;

	}

	public Column[] getColumns() {
		Column[] cols = new Column[2];
		cols[0] = new Column(ORIGINAL_VOYAGE, ORIGINAL_VOYAGE_LABEL, true);
		cols[1] = new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false);
		return cols;
	}

	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i]
					.getUserLabel());
		}
		return rows;
	}

	public void setValues(Values vals) {
		this.valsToSubmit = vals;
	}
	
	public Map getFieldTypes() {
		
		Map map = new HashMap();
		map.put(TextboxAdapter.TYPE, new TextboxFieldType(TextboxAdapter.TYPE));
		map.put(TextboxIntegerAdapter.TYPE, new TextboxIntegerFieldType(TextboxIntegerAdapter.TYPE));
		map.put(TextboxFloatAdapter.TYPE, new TextboxFloatFieldType(TextboxFloatAdapter.TYPE));
		map.put(DateAdapter.TYPE, new DateFieldType(DateAdapter.TYPE));
		map.put(TextareaAdapter.TYPE, new TextareaFieldType(TextareaAdapter.TYPE, 10));
		map.put(LOCATIONS, new ListFieldType(LOCATIONS, areas));
		map.put(PORTS, new ListFieldType(PORTS, ports));
		return map;
		
	}

	public String submit() {
		System.out.println("Voyage submission saved");
		Map newValues = valsToSubmit.getColumnValues(CHANGED_VOYAGE);
		Voyage vNew = new Voyage();
		vNew.setVoyageid(new Long(this.voyageId));
		vNew.setSuggestion(true);
		vNew.setRevision(-1);
		vNew.setApproved(false);
		wasError = false;
		for (int i = 0; i < attrs.length; i++) {
			Value val = (Value) newValues.get(attrs[i].getName());
			if (val.isError()) {
				System.out.println("ERROR!!!!");
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(val);
			for (int j = 0; j < vals.length; j++) {
				vNew
						.setAttrValue(attrs[i].getAttribute()[j].getName(),
								vals[j]);
			}
		}
		if (!wasError) {
			vNew.save();
		}
		System.out.println("Voyage submission saved");
		return null;
	}

}
