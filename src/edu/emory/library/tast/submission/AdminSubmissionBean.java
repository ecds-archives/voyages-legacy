package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class AdminSubmissionBean {

	public static final String ORIGINAL_VOYAGE_LABEL = TastResource
			.getText("submissions_oryginal_voyage");

	public static final String CHANGED_VOYAGE_LABEL = TastResource
			.getText("submissions_suggested_voyage");

	public static final String ORIGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	public static final int TYPE_NEW = 1;

	public static final int TYPE_EDIT = 2;

	public static final int TYPE_MERGE = 3;

	private static SubmissionAttribute[] attrs = SubmissionAttributes
			.getConfiguration().getSubmissionAttributes();

	private long voyageId = -1;

	private Values valsToSubmit = null;

	private boolean wasError = false;

	private Values vals = null;

	private int suggestionsCount = 0;

	private RowGroup[] rowGroups;

	private SubmissionNew selectedNew;

	private SubmissionEdit selectedEdit;

	private SubmissionMerge selectedMerge;

	private int chosenType = 0;

	public AdminSubmissionBean() {
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

	public void setVoyageId(long voyageId) {
		this.voyageId = voyageId;
		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId),
				Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(true),
				Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("approved"), new Boolean(false),
				Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", c);
		qValue.addPopulatedAttribute(new FunctionAttribute("count",
				new Attribute[] { Voyage.getAttribute("voyageid") }));
		Object[] res = qValue.executeQuery();
		this.suggestionsCount = ((Long) res[0]).intValue();
	}

	public Values getValues() {

		if (!wasError || vals == null) {

			this.setVoyageId(43);

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

			c = new Conditions();
			c.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId),
					Conditions.OP_EQUALS);
			c.addCondition(Voyage.getAttribute("suggestion"),
					new Boolean(true), Conditions.OP_EQUALS);
			c.addCondition(Voyage.getAttribute("approved"), new Boolean(false),
					Conditions.OP_EQUALS);
			qValue = new QueryValue("Voyage", c);
			for (int i = 0; i < attrs.length; i++) {
				Attribute[] attributes = attrs[i].getAttribute();
				for (int j = 0; j < attributes.length; j++) {
					qValue.addPopulatedAttribute(attributes[j]);
				}
			}
			Object[] suggestions = qValue.executeQuery(session);

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
				for (int j = 0; j < suggestions.length; j++) {
					Object[] suggestionAttrs = (Object[]) suggestions[j];
					String label = CHANGED_VOYAGE + "-" + (j + 1);
					Object[] suggestionAttrsToEdit = new Object[attribute
							.getAttribute().length];
					for (int k = 0; k < suggestionAttrsToEdit.length; k++) {
						suggestionAttrsToEdit[k] = suggestionAttrs[k + index];
					}
					vals.setValue(label, attrs[i].getName(), attrs[i]
							.getValue(suggestionAttrsToEdit));
				}
				index += attribute.getAttribute().length;
			}

			t.commit();
			session.close();
		}

		return vals;

	}

	public Column[] getColumns() {
		this.setVoyageId(43);
		Column[] cols = new Column[1 + this.suggestionsCount];
		cols[0] = new Column(ORIGINAL_VOYAGE, ORIGINAL_VOYAGE_LABEL, false);
		for (int i = 0; i < this.suggestionsCount; i++) {
			cols[i + 1] = new Column(CHANGED_VOYAGE + "-" + (i + 1),
					CHANGED_VOYAGE_LABEL + " " + (i + 1), true);
		}
		return cols;
	}

	public Row[] getRows() {
		this.setVoyageId(43);
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
		return SubmissionDictionaries.fieldTypes;
	}

	public String submit() {
		System.out.println("Voyage correction saved");
		Map newValues = valsToSubmit.getColumnValues(ORIGINAL_VOYAGE);
		Voyage vNew = new Voyage();
		vNew.setVoyageid(new Long(this.voyageId));
		vNew.setSuggestion(false);
		vNew.setRevision(-1);
		vNew.setApproved(true);
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

		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId),
				Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(true),
				Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("approved"), new Boolean(false),
				Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", c);
		Object[] voyages = qValue.executeQuery();
		for (int i = 0; i < voyages.length; i++) {
			Voyage v = (Voyage) voyages[i];
			v.setApproved(true);
			v.saveOrUpdate();
		}

		System.out.println("Voyage submission saved");
		return null;
	}

	public RowGroup[] getRowGroups() {
		return this.rowGroups;
	}

	public List getNewRequests() {
		List l = new ArrayList();
		Conditions c = new Conditions();
		QueryValue q = new QueryValue("SubmissionNew", c);
		Object[] subs = q.executeQuery();
		for (int i = 0; i < subs.length; i++) {
			SubmissionNew submission = (SubmissionNew) subs[i];
			l.add(new SelectItem(submission.getId().toString(), "Date: "
					+ submission.getTime()));
		}
		return l;
	}

	public List getEditRequests() {
		List l = new ArrayList();
		Conditions c = new Conditions();
		QueryValue q = new QueryValue("SubmissionEdit", c);
		Object[] subs = q.executeQuery();
		for (int i = 0; i < subs.length; i++) {
			SubmissionEdit submission = (SubmissionEdit) subs[i];
			l.add(new SelectItem(submission.getId().toString(), "Date: "
					+ submission.getTime()));
		}
		return l;
	}

	public List getMergeRequests() {
		List l = new ArrayList();
		Conditions c = new Conditions();
		QueryValue q = new QueryValue("SubmissionMerge", c);
		Object[] subs = q.executeQuery();
		for (int i = 0; i < subs.length; i++) {
			SubmissionMerge submission = (SubmissionMerge) subs[i];
			l.add(new SelectItem(submission.getId().toString(), "Date: "
					+ submission.getTime()));
		}
		return l;
	}

	public void setNewRequest(String value) {
		if (value == null) {
			return;
		}
		try {
			Conditions c = new Conditions();
			c.addCondition(Submission.getAttribute("id"), new Long(value),
					Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("SubmissionNew", c);
			Object[] res = qValue.executeQuery();
			this.selectedNew = (SubmissionNew) res[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNewRequest() {
		if (this.selectedNew == null) {
			return null;
		}
		return this.selectedNew.getId().toString();
	}

	public void setEditRequest(String value) {
		if (value == null) {
			return;
		}
		try {
			Conditions c = new Conditions();
			c.addCondition(Submission.getAttribute("id"), new Long(value),
					Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("SubmissionEdit", c);
			Object[] res = qValue.executeQuery();
			this.selectedEdit = (SubmissionEdit) res[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEditRequest() {
		if (this.selectedEdit == null) {
			return null;
		}
		return this.selectedEdit.getId().toString();
	}

	public void setMergeRequest(String value) {
		if (value == null) {
			return;
		}
		try {
			Conditions c = new Conditions();
			c.addCondition(Submission.getAttribute("id"), new Long(value),
					Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("SubmissionMerge", c);
			Object[] res = qValue.executeQuery();
			this.selectedMerge = (SubmissionMerge) res[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMergeRequest() {
		if (this.selectedMerge == null) {
			return null;
		}
		return this.selectedMerge.getId().toString();
	}

	public String resolveNew() {
		this.chosenType = TYPE_NEW;
		return "admin-step-1";
	}

	public String resolveEdit() {
		this.chosenType = TYPE_EDIT;
		return "admin-step-1";
	}

	public String resolveMerge() {
		this.chosenType = TYPE_MERGE;
		return "admin-step-1";
	}
}
