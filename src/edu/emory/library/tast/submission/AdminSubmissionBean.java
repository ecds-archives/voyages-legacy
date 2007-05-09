package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.common.TabChangedEvent;
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
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class AdminSubmissionBean {

	private static final String REQUEST_MERGE_PREFIX = "merge_";

	private static final String REQUEST_EDIT_PREFIX = "edit_";

	private static final String REQUEST_NEW_PREFIX = "new_";

	public static final String ORYGINAL_VOYAGE_LABEL = "Original voyage";

	public static final String CHANGED_VOYAGE_LABEL = "Suggested change";

	public static final String NEW_VOYAGE_LABEL = "Suggested voyage";

	public static final String CORRECTED_VOYAGE_LABEL = "Approved voyage";

	private static final String MERGE_VOYAGE_LABEL = "Voyage indicated to merge";

	private static final String DECIDED_VOYAGE_LABEL = "Final decision";

	private static final String DECIDED_VOYAGE = "decided";

	public static final String ORIGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	public static final String REQUEST_ALL = "all request types";

	public static final String REQUEST_NEW = "new voyage requests";

	public static final String REQUEST_EDIT = "edit voyage requests";

	public static final String REQUEST_MERGE = "merge voyages requests";

	public static final int TYPE_NEW = 1;

	public static final int TYPE_EDIT = 2;

	public static final int TYPE_MERGE = 3;

	private static SubmissionAttribute[] attrs = SubmissionAttributes
			.getConfiguration().getSubmissionAttributes();

	private long voyageId = -1;

	private Values valsToSubmit = null;

	private boolean wasError = false;

	private Values vals = null;

	private RowGroup[] rowGroups;

	private String chosenTab = "voyages";

	private String requestType = "1";

	private Submission submission = null;
	
	private Object[] editRequests = null;

	private Long[] selectedRequestsIds = null;
	
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
		//this.suggestionsCount = ((Long) res[0]).intValue();
	}

	public Values getValues() {

		if (!wasError || vals == null) {

			//			
			//
			// if (this.voyageId == -1) {
			// throw new RuntimeException("Voyage ID was not set!");
			// }
			//
			// Session session = HibernateUtil.getSession();
			// Transaction t = session.beginTransaction();
			// vals = new Values();
			//
			// Conditions c = new Conditions();
			// c.addCondition(Voyage.getAttribute("voyageid"), new
			// Long(voyageId),
			// Conditions.OP_EQUALS);
			// c.addCondition(Voyage.getAttribute("revision"), new Integer(1),
			// Conditions.OP_EQUALS);
			// QueryValue qValue = new QueryValue("Voyage", c);
			// for (int i = 0; i < attrs.length; i++) {
			// Attribute[] attributes = attrs[i].getAttribute();
			// for (int j = 0; j < attributes.length; j++) {
			// qValue.addPopulatedAttribute(attributes[j]);
			// }
			// }
			// Object[] res = qValue.executeQuery(session);
			// if (res.length == 0) {
			// return null;
			// }
			//
			// c = new Conditions();
			// c.addCondition(Voyage.getAttribute("voyageid"), new
			// Long(voyageId),
			// Conditions.OP_EQUALS);
			// c.addCondition(Voyage.getAttribute("suggestion"),
			// new Boolean(true), Conditions.OP_EQUALS);
			// c.addCondition(Voyage.getAttribute("approved"), new
			// Boolean(false),
			// Conditions.OP_EQUALS);
			// qValue = new QueryValue("Voyage", c);
			// for (int i = 0; i < attrs.length; i++) {
			// Attribute[] attributes = attrs[i].getAttribute();
			// for (int j = 0; j < attributes.length; j++) {
			// qValue.addPopulatedAttribute(attributes[j]);
			// }
			// }
			// Object[] suggestions = qValue.executeQuery(session);
			Session session = HibernateUtil.getSession();
			Transaction t = session.beginTransaction();
			Voyage[] toVals = null;
			String[] cols = null;
			if (this.submission instanceof SubmissionNew) {
				toVals = new Voyage[2];
				cols = new String[2];
				toVals[0] = Voyage.loadById(session, ((SubmissionNew) this.submission).getNewVoyage().getIid());
				toVals[1] = new Voyage();
				cols[0] = ORIGINAL_VOYAGE;
				cols[1] = DECIDED_VOYAGE;
			} else if (this.submission instanceof SubmissionEdit) {
				toVals = new Voyage[2 + this.editRequests.length];
				cols = new String[2 + this.editRequests.length];
				toVals[0] = Voyage.loadById(session, ((SubmissionEdit) this.submission).getOldVoyage().getIid());
				for (int i = 1; i < toVals.length - 1; i++) {
					toVals[i] = Voyage.loadById(session, (Long)this.editRequests[i - 1]);
				}
				toVals[toVals.length - 1] = new Voyage();
				cols[0] = ORIGINAL_VOYAGE;
				for (int i = 1; i < cols.length - 1; i++) {
					cols[i] = CHANGED_VOYAGE + "_" + i;
				}
				cols[cols.length - 1] = DECIDED_VOYAGE;
			} else {

			}
			vals = new Values();
			for (int n = 0; n < toVals.length; n++) {
				for (int i = 0; i < attrs.length; i++) {
					SubmissionAttribute attribute = attrs[i];
					System.out.println("Attr: " + attrs[i]);
					Object[] toBeFormatted = new Object[attribute
							.getAttribute().length];
					for (int j = 0; j < toBeFormatted.length; j++) {
						toBeFormatted[j] = toVals[n].getAttrValue(attribute.getAttribute()[j].getName());
					}
					vals.setValue(cols[n], attrs[i].getName(), attrs[i].getValue(toBeFormatted));
				}
			}
			
			t.commit();
			session.close();

		}

		return vals;

	}

	public Column[] getColumns() {
		Column[] cols = null;
		if (this.submission instanceof SubmissionNew) {
			cols = new Column[2];
			cols[0] = new Column(ORIGINAL_VOYAGE, NEW_VOYAGE_LABEL, true);
			cols[1] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false);
		} else if (this.submission instanceof SubmissionEdit) {
			cols = new Column[2 + this.editRequests.length];
			cols[0] = new Column(ORIGINAL_VOYAGE, ORYGINAL_VOYAGE_LABEL, true);
			for (int i = 1; i < cols.length - 1; i++) {
				cols[i] = new Column(CHANGED_VOYAGE + "_" + i, CHANGED_VOYAGE_LABEL + " #" + i, true);
			}
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false);
		} else {
			cols = new Column[2 + ((SubmissionMerge) this.submission)
					.getMergedVoyages().size()];
			cols[0] = new Column(ORIGINAL_VOYAGE, NEW_VOYAGE_LABEL, false);
			for (int i = 1; i < cols.length - 1; i++) {
				cols[0] = new Column(CHANGED_VOYAGE + " #" + i,
						MERGE_VOYAGE_LABEL + " #" + i, false);
			}
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE,
					DECIDED_VOYAGE_LABEL, true);
		}
		return cols;
	}

	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), 
					attrs[i].getUserLabel(), null, attrs[i].getGroupName());
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
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(null, val);
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

	public void onTabChanged(TabChangedEvent e) {
		this.chosenTab = e.getTabId();
		System.out.println("Chosen tab: " + e.getTabId());
	}
	
	public String getSelectedTab() {
		return this.chosenTab;
	}
	
	public void setSelectedTab(String tab) {
		this.chosenTab = tab;
	}

	public Boolean getVoyagesListSelected() {
		return new Boolean(this.chosenTab.equals("voyages"));
	}

	public Boolean getRequestsListSelected() {
		return new Boolean(this.chosenTab.equals("requests"));
	}

	public SelectItem[] getRequestTypes() {
		return new SelectItem[] { new SelectItem("1", REQUEST_ALL),
				new SelectItem("2", REQUEST_NEW),
				new SelectItem("3", REQUEST_EDIT),
				new SelectItem("4", REQUEST_MERGE), };
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String type) {
		this.requestType = type;
	}

	public GridRow[] getRequestRows() {

		List l = new ArrayList();
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		if (this.requestType.equals("1") || this.requestType.equals("2")) {
			Conditions c = new Conditions();
			c.addCondition(SubmissionNew.getAttribute("solved"), 
					new Boolean(false), Conditions.OP_EQUALS);
			QueryValue q = new QueryValue("SubmissionNew", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionNew submission = (SubmissionNew) subs[i];
				l.add(new GridRow(REQUEST_NEW_PREFIX + submission.getId(),
						new String[] { "New voyage request", "Unknown",
								submission.getTime().toString(),
								"New voyage - ID not yet assigned " + submission.getId() }));
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("3")) {
			Conditions c = new Conditions();
			c.addCondition(SubmissionEdit.getAttribute("solved"), 
					new Boolean(false), Conditions.OP_EQUALS);
			QueryValue q = new QueryValue("SubmissionEdit", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionEdit submission = (SubmissionEdit) subs[i];
				l.add(new GridRow(REQUEST_EDIT_PREFIX + submission.getId(),
						new String[] {
								"Voyage edit request",
								"Unknown",
								submission.getTime().toString(),
								submission.getOldVoyage().getVoyageid()
										.toString() }));
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("4")) {
			Conditions c = new Conditions();
			c.addCondition(SubmissionMerge.getAttribute("solved"), 
					new Boolean(false), Conditions.OP_EQUALS);
			QueryValue q = new QueryValue("SubmissionMerge", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionMerge submission = (SubmissionMerge) subs[i];
				String involvedStr = "";
				Set involved = submission.getMergedVoyages();
				boolean first = true;
				for (Iterator iter = involved.iterator(); iter.hasNext();) {
					Voyage element = (Voyage) iter.next();
					if (!first) {
						involvedStr += ", ";
					}
					involvedStr += element.getVoyageid();
					first = false;
				}
				l.add(new GridRow(REQUEST_MERGE_PREFIX
								+ submission.getId(), new String[] {
								"Voyages merge request", "Unknown",
								submission.getTime().toString(), involvedStr }));
			}
		}
		t.commit();
		session.close();

		return (GridRow[]) l.toArray(new GridRow[] {});
	}

	public GridColumn[] getRequestColumns() {
		return new GridColumn[] { new GridColumn("Type"),
				new GridColumn("User"), new GridColumn("Date"),
				new GridColumn("Involved voyages ID") };
	}

	public void newRequestId(GridOpenRowEvent e) {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		Conditions c = new Conditions();
		c.addCondition(Submission.getAttribute("id"), new Long(e.getRowId()
				.split("_")[1]), Conditions.OP_EQUALS);
		QueryValue q = new QueryValue("Submission", c);
		
		Object[] res = q.executeQuery(session);
		if (res.length != 0) {
			this.submission = (Submission) res[0];
			if (this.submission instanceof SubmissionEdit) {
				Long vid = ((SubmissionEdit)this.submission).getOldVoyage().getVoyageid();
				c = new Conditions();
				c.addCondition(new SequenceAttribute(new Attribute[] {SubmissionEdit.getAttribute("oldVoyage"), Voyage.getAttribute("voyageid")}), vid, Conditions.OP_EQUALS);
				c.addCondition(SubmissionEdit.getAttribute("solved"), new Boolean(false), Conditions.OP_EQUALS);
				q = new QueryValue("SubmissionEdit", c);
				Object[] rets = q.executeQuery(session);
				editRequests = new Object[rets.length];
				for (int i = 0; i < editRequests.length; i++) {
					editRequests[i] = ((SubmissionEdit)rets[i]).getNewVoyage().getIid(); 
				}
			} else {
				
			}
		} else {
			this.submission = null;
		}
		t.commit();
		session.close();
	}
	
	public String resolveRequest() {
		return "resolve-request";
	}

}
