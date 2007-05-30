package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.admin.VoyageBean;
import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.common.TabChangedEvent;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerValue;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Revision;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * The bean that is responsible for requests administration/user administration and new revisions publishingl.
 * The main methods that are executed (as actions) from user interface are:
 *  - publish
 * 
 *  - 
 *
 */
public class AdminSubmissionBean {

	private static final String COPY_LABEL = "Copy";

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

	public static final String ORYGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	public static final String REQUEST_ALL = "all request types";

	public static final String REQUEST_NEW = "new voyage requests";

	public static final String REQUEST_EDIT = "edit voyage requests";

	public static final String REQUEST_MERGE = "merge voyages requests";

	public static final int TYPE_NEW = 1;

	public static final int TYPE_EDIT = 2;

	public static final int TYPE_MERGE = 3;

	public static final SelectItem[] REQUESTS_TYPES = new SelectItem[] { 
			new SelectItem("1", REQUEST_ALL), 
			new SelectItem("2", REQUEST_NEW), 
			new SelectItem("3", REQUEST_EDIT),
			new SelectItem("4", REQUEST_MERGE), 
		};
	
	public static final GridColumn[] REQUESTS_LIST_COLS = new GridColumn[] { 
			new GridColumn("Type"), 
			new GridColumn("User"), 
			new GridColumn("Date"),
			new GridColumn("Involved voyages ID"), 
			new GridColumn("Reviewed"), 
			new GridColumn("Status") 
		};
	
	/**
	 * Attributes that are available for administrator/editor.
	 */
	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getSubmissionAttributes();

	/**
	 * Reference to voyage bean - to provide way of showing voyage details.
	 */
	private VoyageBean voyageBean = null;

	/**
	 * Indicator is error occured (if wrong value was entered into any field).
	 */
	private boolean wasError = false;

	/**
	 * Values for DataGrid component.
	 */
	private Values vals = null;

	/**
	 * Rows for DataGrid component.
	 */
	private RowGroup[] rowGroups;

	/**
	 * Indication of chosen tab.
	 */
	private String chosenTab = "voyages";

	/**
	 * Chosen request type (1 - all, 2 - new, 3 - edit, 4 - merge). 
	 */
	private String requestType = "1";

	/**
	 * Status of request (1 - all).
	 */
	private String requestStatus = "1";

	/**
	 * Id of submission that is expanded.
	 */
	private Long submissionId = null;

	/**
	 * In case of edit request - one can have more than one submission solved at once.
	 */
	private Long[] editRequests = null;

	/**
	 * Main voyage id in merge request.
	 */
	private Long mergeMainVoyage = null;

	/**
	 * When deletion is approved, this is set to true
	 */
	private boolean deleteApproved = false;

	/**
	 * Loggenin user - if null - no user was logged in
	 */
	private User authenticateduser = null;

	/**
	 * Name of new, published revision
	 */
	private String revisionName;

	/**
	 * Message that is shown after the publish button was pressed.
	 */
	private String message;

	/**
	 * Constructor.
	 * Creartes some basic structures like rows and row groups (for DataGrid component)
	 *
	 */
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

	/**
	 * Gets values for grid component.
	 * If required - queries DB to fill in values.
	 * @return
	 */
	public Values getValues() {

		if (!wasError || vals == null) {
			Session session = HibernateUtil.getSession();
			Transaction t = session.beginTransaction();
			Voyage[] toVals = null;
			String[] cols = null;
			Map[] attributeNotes = null;
			EditedVoyage vNew = null;
			Submission lSubmisssion = Submission.loadById(session, this.submissionId);

			if (lSubmisssion instanceof SubmissionNew) {
				vNew = ((SubmissionNew) lSubmisssion).getEditorVoyage();
			} else if (lSubmisssion instanceof SubmissionEdit) {
				vNew = ((SubmissionEdit) lSubmisssion).getEditorVoyage();
			} else {
				vNew = ((SubmissionMerge) lSubmisssion).getEditorVoyage();
			}
			if (lSubmisssion instanceof SubmissionNew) {
				toVals = new Voyage[2];
				cols = new String[2];
				attributeNotes = new Map[2];
				toVals[0] = Voyage.loadById(session, ((SubmissionNew) lSubmisssion).getNewVoyage().getVoyage().getIid());
				attributeNotes[0] = (((SubmissionNew) lSubmisssion).getNewVoyage().getAttributeNotes());
				if (vNew == null) {
					toVals[1] = new Voyage();
					attributeNotes[1] = new HashMap();
				} else {
					toVals[1] = vNew.getVoyage();
					attributeNotes[1] = vNew.getAttributeNotes();
				}
				cols[0] = ORYGINAL_VOYAGE;
				cols[1] = DECIDED_VOYAGE;
			} else if (lSubmisssion instanceof SubmissionEdit) {
				toVals = new Voyage[2 + this.editRequests.length];
				cols = new String[2 + this.editRequests.length];
				attributeNotes = new Map[2 + this.editRequests.length];
				cols[0] = ORYGINAL_VOYAGE;
				toVals[0] = Voyage.loadById(session, ((SubmissionEdit) lSubmisssion).getOldVoyage().getVoyage().getIid());
				attributeNotes[0] = ((SubmissionEdit) lSubmisssion).getOldVoyage().getAttributeNotes();
				for (int i = 1; i < toVals.length - 1; i++) {
					EditedVoyage eV = EditedVoyage.loadById(session, this.editRequests[i - 1]);
					toVals[i] = eV.getVoyage();
					cols[i] = CHANGED_VOYAGE + "_" + i;
					attributeNotes[i] = eV.getAttributeNotes();
				}
				if (vNew == null) {
					toVals[toVals.length - 1] = getCurrentlyPreparedVoyage(session, toVals[0].getVoyageid());
					attributeNotes[toVals.length - 1] = new HashMap();
				} else {
					toVals[toVals.length - 1] = vNew.getVoyage();
					attributeNotes[toVals.length - 1] = vNew.getAttributeNotes();
				}
				cols[cols.length - 1] = DECIDED_VOYAGE;
			} else {
				toVals = new Voyage[2 + this.editRequests.length];
				cols = new String[2 + this.editRequests.length];
				attributeNotes = new Map[2 + this.editRequests.length];
				for (int i = 0; i < toVals.length - 2; i++) {
					EditedVoyage eV = EditedVoyage.loadById(session, this.editRequests[i]);
					toVals[i] = eV.getVoyage();
					cols[i] = CHANGED_VOYAGE + "_" + i;
					attributeNotes[i] = eV.getAttributeNotes();
				}
				EditedVoyage eV = EditedVoyage.loadById(session, this.mergeMainVoyage);
				toVals[toVals.length - 2] = eV.getVoyage();
				cols[cols.length - 2] = ORYGINAL_VOYAGE;
				attributeNotes[toVals.length - 2] = eV.getAttributeNotes();
				if (vNew == null) {
					toVals[toVals.length - 1] = new Voyage();
					attributeNotes[toVals.length - 1] = new HashMap();
				} else {
					toVals[toVals.length - 1] = vNew.getVoyage();
					attributeNotes[toVals.length - 1] = vNew.getAttributeNotes();
				}
				cols[cols.length - 1] = DECIDED_VOYAGE;
			}
			vals = new Values();
			for (int n = 0; n < toVals.length; n++) {
				for (int i = 0; i < attrs.length; i++) {
					SubmissionAttribute attribute = attrs[i];
					System.out.println("Attr: " + attrs[i]);
					Object[] toBeFormatted = new Object[attribute.getAttribute().length];
					for (int j = 0; j < toBeFormatted.length; j++) {
						toBeFormatted[j] = toVals[n].getAttrValue(attribute.getAttribute()[j].getName());
					}
					Value value = attrs[i].getValue(toBeFormatted);
					value.setNote((String) attributeNotes[n].get(attrs[i].getName()));
					vals.setValue(cols[n], attrs[i].getName(), value);
				}
			}

			t.commit();
			session.close();

		}

		return vals;

	}
	
	/**
	 * Sets values when user returns form with GridComponent.
	 * @param vals
	 */
	public void setValues(Values vals) {
		this.vals = vals;
	}

	/**
	 * Returns voyage that will be published in next publish process.
	 * @param sess
	 * @param voyageid2
	 * @return
	 */
	private Voyage getCurrentlyPreparedVoyage(Session sess, Integer voyageid2) {
		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("voyageid"), voyageid2, Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(-1), Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), Conditions.OP_EQUALS);
		QueryValue q = new QueryValue("Voyage", c);
		Object[] rets = q.executeQuery(sess);
		if (rets.length == 0) {
			throw new RuntimeException("Voyage not found! Contact support!");
		} else {
			return (Voyage) rets[0];
		}
	}

	/**
	 * Gets columns for GridComponent. The result depends on type of submission that is handled. 
	 * @return
	 */
	public Column[] getColumns() {
		Column[] cols = null;
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session, this.submissionId);
		if (lSubmisssion instanceof SubmissionNew) {
			cols = new Column[2];
			cols[0] = new Column(ORYGINAL_VOYAGE, NEW_VOYAGE_LABEL, true, DECIDED_VOYAGE, COPY_LABEL);
			cols[1] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false);
		} else if (lSubmisssion instanceof SubmissionEdit) {
			cols = new Column[2 + this.editRequests.length];
			cols[0] = new Column(ORYGINAL_VOYAGE, ORYGINAL_VOYAGE_LABEL, true, DECIDED_VOYAGE, COPY_LABEL);
			for (int i = 1; i < cols.length - 1; i++) {
				cols[i] = new Column(CHANGED_VOYAGE + "_" + i, CHANGED_VOYAGE_LABEL + " #" + i, true, DECIDED_VOYAGE, COPY_LABEL);
			}
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false);
		} else {
			cols = new Column[2 + this.editRequests.length];
			for (int i = 0; i < cols.length - 2; i++) {
				cols[i] = new Column(CHANGED_VOYAGE + "_" + i, MERGE_VOYAGE_LABEL + " #" + (i + 1), true, DECIDED_VOYAGE, COPY_LABEL);
			}
			cols[cols.length - 2] = new Column(ORYGINAL_VOYAGE, NEW_VOYAGE_LABEL, true, DECIDED_VOYAGE, COPY_LABEL);
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false);
		}
		t.commit();
		session.close();
		return cols;
	}

	/**
	 * Returns rows for GridComponent.
	 * @return
	 */
	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i].getUserLabel(), null, attrs[i].getGroupName());
			rows[i].setNoteEnabled(true);
		}
		return rows;
	}

	/**
	 * Gets supported field types (items in lists etc) (for GridComponent).
	 * @return
	 */
	public Map getFieldTypes() {
		return SubmissionDictionaries.fieldTypes;
	}

	/**
	 * Gets groups for rows - GridComponent requirement.
	 * @return
	 */
	public RowGroup[] getRowGroups() {
		return this.rowGroups;
	}

	/**
	 * Called when active tab is changed.
	 * @param e
	 */
	public void onTabChanged(TabChangedEvent e) {
		this.chosenTab = e.getTabId();
	}

	/**
	 * Gets currently selected tab name.
	 * @return
	 */
	public String getSelectedTab() {
		return this.chosenTab;
	}

	/**
	 * Sets selected tab.
	 * @param tab
	 */
	public void setSelectedTab(String tab) {
		this.chosenTab = tab;
	}

	/**
	 * Checks if list of voyages is selected.
	 * @return
	 */
	public Boolean getVoyagesListSelected() {
		return new Boolean(this.chosenTab.equals("voyages"));
	}

	/**
	 * Checks if list of requests is selected.
	 * @return
	 */
	public Boolean getRequestsListSelected() {
		return new Boolean(this.chosenTab.equals("requests"));
	}

	/**
	 * Checks if list of users is selected.
	 * @return
	 */
	public Boolean getUsersListSelected() {
		return new Boolean(this.chosenTab.equals("users"));
	}
	
	/**
	 * Checks if publish is selected.
	 * @return
	 */
	public Boolean getPublishSelected() {
		return new Boolean(this.chosenTab.equals("publish"));
	}

	/**
	 * Returns types of available requests.
	 * @return
	 */
	public SelectItem[] getRequestTypes() {
		return REQUESTS_TYPES;
	}

	/**
	 * Gets currently chosen request type (requests of this type will be visible).
	 * @return
	 */
	public String getRequestType() {
		return this.requestType;
	}

	/**
	 * Sets requests type filter .
	 * @param type
	 */
	public void setRequestType(String type) {
		this.requestType = type;
	}

	/**
	 * Sets request status filter.
	 * @param status
	 */
	public void setRequestStatus(String status) {
		this.requestStatus = status;
	}

	/**
	 * Gets request status filter.
	 * @return
	 */
	public String getRequestStatus() {
		return this.requestStatus;
	}

	/**
	 * Gets rows which represent requests that correspond to current filter setting.
	 * @return
	 */
	public GridRow[] getRequestRows() {

		List l = new ArrayList();
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		Conditions c = new Conditions();
		if (this.requestStatus.equals("2")) {
			c.addCondition(Submission.getAttribute("editorVoyage"), null, Conditions.OP_IS_NOT);
			c.addCondition(Submission.getAttribute("solved"), new Boolean(false), Conditions.OP_EQUALS);
		} else if (this.requestStatus.equals("3")) {
			c.addCondition(Submission.getAttribute("editorVoyage"), null, Conditions.OP_IS);
			c.addCondition(Submission.getAttribute("solved"), new Boolean(false), Conditions.OP_EQUALS);
		} else if (this.requestStatus.equals("4")) {
			c.addCondition(Submission.getAttribute("solved"), new Boolean(true), Conditions.OP_EQUALS);
		}
		if (this.requestType.equals("1") || this.requestType.equals("2")) {
			QueryValue q = new QueryValue("SubmissionNew", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionNew submission = (SubmissionNew) subs[i];
				String lastCol = submission.isSolved() ? "Solved" : "Not solved";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol += "/Accepted";
					} else {
						lastCol += "/Rejected";
					}
				}
				l.add(new GridRow(REQUEST_NEW_PREFIX + submission.getId(), new String[] { "New voyage request",
						submission.getUser().getUserName(), submission.getTime().toString(), "New voyage - ID not yet assigned",
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("3")) {
			QueryValue q = new QueryValue("SubmissionEdit", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionEdit submission = (SubmissionEdit) subs[i];
				String lastCol = submission.isSolved() ? "Solved" : "Not solved";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol += "/Accepted";
					} else {
						lastCol += "/Rejected";
					}
				}
				l.add(new GridRow(REQUEST_EDIT_PREFIX + submission.getId(), new String[] { "Voyage edit request",
						submission.getUser().getUserName(), submission.getTime().toString(),
						submission.getOldVoyage().getVoyage().getVoyageid().toString(),
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("4")) {
			QueryValue q = new QueryValue("SubmissionMerge", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionMerge submission = (SubmissionMerge) subs[i];
				String involvedStr = "";
				Set involved = submission.getMergedVoyages();
				boolean first = true;
				for (Iterator iter = involved.iterator(); iter.hasNext();) {
					EditedVoyage element = (EditedVoyage) iter.next();
					if (!first) {
						involvedStr += ", ";
					}
					involvedStr += element.getVoyage().getVoyageid();
					first = false;
				}
				String lastCol = submission.isSolved() ? "Solved" : "Not solved";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol += "/Accepted";
					} else {
						lastCol += "/Rejected";
					}
				}
				l.add(new GridRow(REQUEST_MERGE_PREFIX + submission.getId(), new String[] { "Voyages merge request",
						submission.getUser().getUserName(), submission.getTime().toString(), involvedStr,
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
			}
		}
		t.commit();
		session.close();

		return (GridRow[]) l.toArray(new GridRow[] {});
	}

	/**
	 * Gets columns for requests list.
	 * @return
	 */
	public GridColumn[] getRequestColumns() {
		return REQUESTS_LIST_COLS;
	}

	/**
	 * Invoked when user clicks on any row of list showing requests.
	 * Prepares data for request handling.
	 * @param e
	 */
	public void newRequestId(GridOpenRowEvent e) {

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		Conditions c = new Conditions();
		c.addCondition(Submission.getAttribute("id"), new Long(e.getRowId().split("_")[1]), Conditions.OP_EQUALS);
		QueryValue q = new QueryValue("Submission", c);

		Object[] res = q.executeQuery(session);
		if (res.length != 0) {
			Submission lSubmission = (Submission) res[0];
			this.submissionId = lSubmission.getId();
			if (lSubmission instanceof SubmissionEdit) {
				Integer vid = ((SubmissionEdit) lSubmission).getOldVoyage().getVoyage().getVoyageid();
				c = new Conditions();
				c.addCondition(new SequenceAttribute(new Attribute[] { SubmissionEdit.getAttribute("oldVoyage"),
						EditedVoyage.getAttribute("voyage"), Voyage.getAttribute("voyageid") }), vid, Conditions.OP_EQUALS);
				c.addCondition(SubmissionEdit.getAttribute("solved"), new Boolean(false), Conditions.OP_EQUALS);
				q = new QueryValue("SubmissionEdit", c);
				Object[] rets = q.executeQuery(session);
				editRequests = new Long[rets.length];
				for (int i = 0; i < editRequests.length; i++) {
					editRequests[i] = ((SubmissionEdit) rets[i]).getNewVoyage().getId();
				}
			} else if (lSubmission instanceof SubmissionMerge) {
				Set voyagesToMerge = ((SubmissionMerge) lSubmission).getMergedVoyages();
				editRequests = new Long[voyagesToMerge.size()];
				Iterator iter = voyagesToMerge.iterator();
				int i = 0;
				while (iter.hasNext()) {
					editRequests[i++] = ((EditedVoyage) iter.next()).getId();
				}
				this.mergeMainVoyage = ((SubmissionMerge) lSubmission).getProposedVoyage().getId();
			}
		} else {
			this.submissionId = null;
		}
		t.commit();
		session.close();
	}

	public String resolveRequest() {
		return "resolve-request";
	}

	public Boolean getRejectAvailable() {
		Boolean avail = null;
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session, this.submissionId);
		avail = new Boolean(!lSubmisssion.isSolved());
		t.commit();
		session.close();
		return avail;

	}

	/**
	 * Action fired when reject button was pressed
	 */
	public String rejectSubmission() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session, this.submissionId);
		if (lSubmisssion instanceof SubmissionEdit) {
			Conditions c = new Conditions();
			c.addCondition(SubmissionEdit.getAttribute("oldVoyage"), ((SubmissionEdit) lSubmisssion).getOldVoyage(), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("SubmissionEdit", c);
			Object[] toUpdate = qValue.executeQuery(session);
			for (int i = 0; i < toUpdate.length; i++) {
				SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
				edit.setSolved(true);
				edit.setAccepted(false);
				session.update(edit);
			}
		} else {
			lSubmisssion.setSolved(true);
			lSubmisssion.setAccepted(false);
			session.update(lSubmisssion);
		}
		t.commit();
		session.close();
		return "back";
	}

	public String submit() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Map newValues = vals.getColumnValues(DECIDED_VOYAGE);
		Submission lSubmission = Submission.loadById(session, this.submissionId);

		if (lSubmission instanceof SubmissionMerge && !deleteApproved) {
			t.commit();
			session.close();
			return "approve-delete";
		}
		deleteApproved = false;

		Voyage vNew = null;
		Voyage mergedVoyage = updateMergedVoyage(session, lSubmission, newValues);

		if (mergedVoyage == null) {
			t.commit();
			session.close();
			return null;
		}

		if (lSubmission instanceof SubmissionMerge) {
			System.out.println("Will delete all merged voyages");
			Set voyagesToDelete = ((SubmissionMerge) lSubmission).getMergedVoyages();
			for (Iterator iter = voyagesToDelete.iterator(); iter.hasNext();) {
				EditedVoyage element = (EditedVoyage) iter.next();
				Integer voyageId = element.getVoyage().getVoyageid();
				Voyage voyage = Voyage.loadFutureRevision(session, voyageId.intValue());
				if (voyage != null) {
					session.delete(voyage);
				}
			}
		}
		Conditions cond = new Conditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), mergedVoyage.getVoyageid(), Conditions.OP_EQUALS);
		cond.addCondition(Voyage.getAttribute("revision"), new Integer(-1), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", cond);
		Object[] voyage = qValue.executeQuery(session);
		if (voyage.length != 0) {
			vNew = (Voyage) voyage[0];
		} else {
			vNew = new Voyage();
			vNew.setRevision(-1);
		}

		wasError = false;
		for (int i = 0; i < attrs.length; i++) {
			Value val = (Value) newValues.get(attrs[i].getName());

			if (val.isError()) {
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(null, val);

			if (attrs[i].getName().equals("voyageid") && vals[0] == null) {
				val.setErrorMessage("This field is required!");
				wasError = true;
			}

			for (int j = 0; j < vals.length; j++) {
				vNew.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
			}
		}
		if (!wasError) {
			vNew.saveOrUpdate();
		}

		if (lSubmission instanceof SubmissionEdit) {
			Conditions c = new Conditions();
			c
					.addCondition(new SequenceAttribute(new Attribute[] { SubmissionEdit.getAttribute("oldVoyage"),
							EditedVoyage.getAttribute("voyage") }), ((SubmissionEdit) lSubmission).getOldVoyage().getVoyage(),
							Conditions.OP_EQUALS);
			qValue = new QueryValue("SubmissionEdit", c);
			Object[] toUpdate = qValue.executeQuery(session);
			for (int i = 0; i < toUpdate.length; i++) {
				SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
				edit.setSolved(true);
				edit.setAccepted(true);
				session.update(edit);
			}
		} else {
			lSubmission.setSolved(true);
			lSubmission.setAccepted(true);
			session.update(lSubmission);
		}

		System.out.println("Voyage submission saved");
		t.commit();
		session.close();
		return "back";
	}

	/**
	 * Action fired when accept button was pressed
	 */
	public String save() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session, this.submissionId);
		Map newValues = vals.getColumnValues(DECIDED_VOYAGE);

		if (updateMergedVoyage(session, lSubmisssion, newValues) == null) {
			t.commit();
			session.close();
			return null;
		}

		t.commit();
		session.close();
		return "back";
	}

	private Voyage updateMergedVoyage(Session session, Submission lSubmisssion, Map newValues) {

		this.wasError = false;
		Voyage vNew = null;
		EditedVoyage editedVoyage = null;
		if (lSubmisssion instanceof SubmissionNew) {
			editedVoyage = ((SubmissionNew) lSubmisssion).getEditorVoyage();
			if (editedVoyage != null) {
				vNew = editedVoyage.getVoyage();
			}
		} else if (lSubmisssion instanceof SubmissionEdit) {
			editedVoyage = ((SubmissionEdit) lSubmisssion).getEditorVoyage();
			if (editedVoyage != null) {
				vNew = editedVoyage.getVoyage();
			}
		} else {
			editedVoyage = ((SubmissionMerge) lSubmisssion).getEditorVoyage();
			if (editedVoyage != null) {
				vNew = editedVoyage.getVoyage();
			}
		}
		if (vNew == null) {
			vNew = new Voyage();
		}
		Map notes = new HashMap();
		for (int i = 0; i < attrs.length; i++) {
			Value val = (Value) newValues.get(attrs[i].getName());
			if (!val.isCorrectValue()) {
				val.setErrorMessage("Value incorrect");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(session, val);
			for (int j = 0; j < vals.length; j++) {
				vNew.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
			}
			if (!StringUtils.isNullOrEmpty(val.getNote())) {
				notes.put(attrs[i].getName(), val.getNote());
			}
			if (attrs[i].getName().equals("voyageid") && vNew.getVoyageid() == null) {
				wasError = true;
				val.setErrorMessage("This field is required");
			}
		}
		vNew.setSuggestion(true);
		if (!wasError) {
			session.saveOrUpdate(vNew);
			if (editedVoyage == null) {
				editedVoyage = new EditedVoyage(vNew, null);
				session.save(editedVoyage);
			}
			editedVoyage.setAttributeNotes(notes);
			if (lSubmisssion instanceof SubmissionNew) {
				((SubmissionNew) lSubmisssion).setEditorVoyage(editedVoyage);
				session.update(lSubmisssion);
			} else if (lSubmisssion instanceof SubmissionEdit) {
				Conditions c = new Conditions();
				c.addCondition(new SequenceAttribute(new Attribute[] { SubmissionEdit.getAttribute("oldVoyage"),
						EditedVoyage.getAttribute("voyage") }), ((SubmissionEdit) lSubmisssion).getOldVoyage().getVoyage(),
						Conditions.OP_EQUALS);
				QueryValue qValue = new QueryValue("SubmissionEdit", c);
				Object[] toUpdate = qValue.executeQuery(session);
				for (int i = 0; i < toUpdate.length; i++) {
					SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
					edit.setEditorVoyage(editedVoyage);
					session.update(edit);
				}
			} else {
				((SubmissionMerge) lSubmisssion).setEditorVoyage(editedVoyage);
				session.update(lSubmisssion);
			}

			return vNew;
		} else {
			return null;
		}
	}

	public String approveDelete() {
		this.deleteApproved = true;
		if (this.submit() == null) {
			return "back";
		}
		return "main-menu";
	}

	public String rejectDelete() {
		return "main-menu";
	}

	public String openVoyageAction() {
		return "edit";
	}

	public String logout() {
		this.authenticateduser = null;
		return null;
	}

	public User getAuthenticateduser() {
		return authenticateduser;
	}

	public void setAuthenticateduser(User authenticateduser) {
		this.authenticateduser = authenticateduser;
	}

	public Boolean getIsAdmin() {
		return new Boolean(this.authenticateduser.isAdmin());
	}

	public boolean isDeleteMergeValid() {
		return this.submissionId != null;
	}

	public boolean isEditValid() {
		return this.voyageBean.isEditValid();
	}

	public boolean isDeleteValid() {
		return this.voyageBean.isDeleteValid();
	}

	public boolean isResolveValid() {
		return this.submissionId != null;
	}

	public VoyageBean getVoyageBean() {
		return voyageBean;
	}

	public void setVoyageBean(VoyageBean voyageBean) {
		this.voyageBean = voyageBean;
	}

	public String publish() {

		if (this.revisionName == null || "".equals(this.revisionName)) {
			this.message = "Revision name cannot be empty!";
			return null;
		}
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();

		try {
			System.out.println("publish");
			SQLQuery query = session.createSQLQuery("select publish('" + this.revisionName.replaceAll("'", "''") + "');");
			query.list();
			this.message = "New revision named '" + this.revisionName + "' has just been published.";
			this.revisionName = null;
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "Something was wrong... Contact system administrator!";
		}finally {
			t.commit();
			session.close();
		}
		return null;
	}

	public String getRevisionName() {
		return revisionName;
	}

	public void setRevisionName(String revisionName) {
		this.revisionName = revisionName;
	}

	public String getMessage() {
		return message;
	}
}
