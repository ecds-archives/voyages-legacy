package edu.emory.library.tast.submission;

import java.util.ArrayList;
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
import edu.emory.library.tast.common.grideditor.RowGroup;
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
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.dm.attributes.NationAttribute;
import edu.emory.library.tast.dm.attributes.VesselRigAttribute;
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
	

	private static SubmissionAttribute[] attrs = SubmissionAttributes
			.getConfiguration().getPublicAttributes();

	private long voyageId = -1;

	private Values valsToSubmit = null;

	private boolean wasError = false;

	private Values vals = null;
	
	private RowGroup[] rowGroups;
	
	public SubmissionBean() {
		List rowGroupsList = new ArrayList();
		for (int i = 0; i < attrs.length; i++) {
			if (!rowGroupsList.contains(attrs[i].getGroupName())) {
				rowGroupsList.add(attrs[i].getGroupName());
			}
		}
		this.rowGroups = new RowGroup[rowGroupsList.size()];
		for (int i = 0; i < this.rowGroups.length; i++) {
			String rowGroup = (String) rowGroupsList.get(i);
			this.rowGroups[i] = new RowGroup(rowGroup, rowGroup);
		}
	}
	
	

	private ListItem[] fillIn(Session sessios, Class clazz) {
		List dics = Dictionary.loadAll(clazz, sessios);
		ListItem[] items = new ListItem[dics.size() + 1];
		items[0] = new ListItem("-1", "Unknown");
		int i = 1;
		for (Iterator iter = dics.iterator(); iter.hasNext();) {
			Dictionary element = (Dictionary) iter.next();
			items[i++] = new ListItem(element.getId().toString(), element.getName().toString());
		}
		
		return items;
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
				System.out.println("Attr: " + attrs[i]);
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
					.getUserLabel(), null, attrs[i].getGroupName());
		}
		return rows;
	}

	public void setValues(Values vals) {
		this.valsToSubmit = vals;
	}
	
	public Map getFieldTypes() {
		return SubmissionDictionaries.fieldTypes;
	}
	
	public RowGroup[] getRowGroups() {
		return this.rowGroups;
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
