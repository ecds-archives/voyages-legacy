package edu.emory.library.tast.submission;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.admin.VoyageBean;
import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.common.TabChangedEvent;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.ColumnActionEvent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Values;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionEditor;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.SubmissionSource;
import edu.emory.library.tast.dm.SubmissionSourceBook;
import edu.emory.library.tast.dm.SubmissionSourceOther;
import edu.emory.library.tast.dm.SubmissionSourcePaper;
import edu.emory.library.tast.dm.SubmissionSourcePrimary;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.CSVUtils;

/**
 * The bean that is responsible for requests administration/user administration and new revisions publishingl.
 * The main methods that are executed (as actions) from user interface are:
 *  - publish
 * 
 *  - 
 *
 */
public class AdminSubmissionBean {
	
	private static final DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	
	private static final String REQUEST_MERGE_PREFIX = "merge_";

	private static final String REQUEST_EDIT_PREFIX = "edit_";

	private static final String REQUEST_NEW_PREFIX = "new_";

	public static final String REQUEST_ALL = "All request types";

	public static final String REQUEST_NEW = "New";

	public static final String REQUEST_EDIT = "Edit";

	public static final String REQUEST_MERGE = "Merge";

	public static final int TYPE_NEW = 1;

	public static final int TYPE_EDIT = 2;

	public static final int TYPE_MERGE = 3;

	public static final SelectItem[] REQUESTS_TYPES = new SelectItem[] { 
			new SelectItem("1", REQUEST_ALL), 
			new SelectItem("2", REQUEST_NEW), 
			new SelectItem("3", REQUEST_EDIT),
			new SelectItem("4", REQUEST_MERGE), 
		};
	
	private GridColumn[] REQUESTS_LIST_COLS = null;
	
	/**
	 * Reference to voyage bean - to provide way of showing voyage details.
	 */
	private VoyageBean voyageBean = null;

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

	private VoyagesApplier applier;

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
	
	private Boolean addingEditor = new Boolean(false);
	private String newEditorUser = null;

	/**
	 * ID of source that is presented in details
	 */
	private Long sourceId;
	
	/**
	 * Indication of final decision about submission by editor
	 */
	private boolean finished = false;

	private static Object publishMonitor = new Object();

	private boolean publishing = false;

	/**
	 * Constructor.
	 * Creartes some basic structures like rows and row groups (for DataGrid component)
	 *
	 */
	public AdminSubmissionBean() {
		this.applier = new VoyagesApplier(this);
	}

	/**
	 * Gets values for grid component.
	 * If required - queries DB to fill in values.
	 * @return
	 */
	public Values getValues() {
		return applier.getValues();
	}
	
	/**
	 * Sets values when user returns form with GridComponent.
	 * @param vals
	 */
	public void setValues(Values vals) {
		applier.setValues(vals);
	}

	/**
	 * Gets columns for GridComponent. The result depends on type of submission that is handled. 
	 * @return
	 */
	public Column[] getColumns() {
		return this.applier.getColumns();
	}

	/**
	 * Returns rows for GridComponent.
	 * @return
	 */
	public Row[] getRows() {
		return this.applier.getRows();
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
		return this.applier.getRowGroups();
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
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();

		TastDbConditions c = new TastDbConditions();
		c.addCondition(Submission.getAttribute("submitted"), new Boolean(true), TastDbConditions.OP_EQUALS);		
		if (this.requestStatus.equals("2")) {
			c.addCondition(Submission.getAttribute("editorVoyage"), null, TastDbConditions.OP_IS_NOT);
			c.addCondition(Submission.getAttribute("solved"), new Boolean(false), TastDbConditions.OP_EQUALS);
		} else if (this.requestStatus.equals("3")) {
			c.addCondition(Submission.getAttribute("editorVoyage"), null, TastDbConditions.OP_IS);
			c.addCondition(Submission.getAttribute("solved"), new Boolean(false), TastDbConditions.OP_EQUALS);
		} else if (this.requestStatus.equals("4")) {
			c.addCondition(Submission.getAttribute("solved"), new Boolean(true), TastDbConditions.OP_EQUALS);
		}
		if (this.requestType.equals("1") || this.requestType.equals("2")) {
			TastDbQuery q = new TastDbQuery("SubmissionNew", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionNew submission = (SubmissionNew) subs[i];
				if (!valid(submission)) {
					continue;
				}
				String lastCol = submission.isSolved() ? "Solved" : "Under review";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol = "Accepted";
					} else {
						lastCol = "Rejected";
					}
				}
				
				//Use for display in request list
				Integer adminVId = getIdForList(submission.getId());
				String adminVId_str = (adminVId==null ? "New voyage - ID not yet assigned" : adminVId.toString());
				
				if (!this.authenticateduser.isEditor()) {
					l.add(new GridRow(REQUEST_NEW_PREFIX + submission.getId(), new String[] { "New voyage",
						submission.getUser().getUserName(), formatter.format(submission.getTime()), adminVId_str,
						this.getEditors(submission), this.reviewedByEditor(submission, null) ? "Yes" + this.infoString(submission) : "No",
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
				} else {
					l.add(new GridRow(REQUEST_NEW_PREFIX + submission.getId(), new String[] { "New voyage request",
						submission.getUser().getUserName(), formatter.format(submission.getTime()), adminVId_str,
						this.reviewedByEditor(submission, this.authenticateduser) ? "Yes" : "No", 
						this.getSubmissionEditor(submission, this.authenticateduser).isFinished() ? "Finished" : "Not finished", lastCol }));
				}
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("3")) {
			TastDbQuery q = new TastDbQuery("SubmissionEdit", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionEdit submission = (SubmissionEdit) subs[i];
				if (!valid(submission)) {
					continue;
				}
				String lastCol = submission.isSolved() ? "Solved" : "Under review";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol = "Accepted";
					} else {
						lastCol = "Rejected";
					}
				}
				if (!this.authenticateduser.isEditor()) {
					l.add(new GridRow(REQUEST_EDIT_PREFIX + submission.getId(), new String[] { "Edit voyage",
						submission.getUser().getUserName(), formatter.format(submission.getTime()),
						submission.getOldVoyage().getVoyage().getVoyageid().toString(),
						this.getEditors(submission), this.reviewedByEditor(submission, null) ? "Yes" + this.infoString(submission) : "No",
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
				} else {
					l.add(new GridRow(REQUEST_EDIT_PREFIX + submission.getId(), new String[] { "Voyage edit request",
						submission.getUser().getUserName(), formatter.format(submission.getTime()),
						submission.getOldVoyage().getVoyage().getVoyageid().toString(),
						this.reviewedByEditor(submission, this.authenticateduser) ? "Yes" : "No", 
						this.getSubmissionEditor(submission, this.authenticateduser).isFinished() ? "Finished" : "Not finished", lastCol }));
				}
			}
		}

		if (this.requestType.equals("1") || this.requestType.equals("4")) {
			TastDbQuery q = new TastDbQuery("SubmissionMerge", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionMerge submission = (SubmissionMerge) subs[i];
				if (!valid(submission)) {
					continue;
				}
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
				String lastCol = submission.isSolved() ? "Solved" : "Under review";
				if (submission.isSolved()) {
					if (submission.isAccepted()) {
						lastCol = "Accepted";
					} else {
						lastCol = "Rejected";
					}
				}
				if (!this.authenticateduser.isEditor()) {
					l.add(new GridRow(REQUEST_MERGE_PREFIX + submission.getId(), new String[] { "Merge voyages",
						submission.getUser().getUserName(), formatter.format(submission.getTime()), involvedStr,
						this.getEditors(submission), this.reviewedByEditor(submission, null) ? "Yes" + this.infoString(submission) : "No",
						submission.getEditorVoyage() != null ? "Yes" : "No", lastCol }));
				} else {
					l.add(new GridRow(REQUEST_MERGE_PREFIX + submission.getId(), new String[] { "Voyages merge request",
						submission.getUser().getUserName(), formatter.format(submission.getTime()), involvedStr,
						this.reviewedByEditor(submission, this.authenticateduser) ? "Yes" : "No", 
						this.getSubmissionEditor(submission, this.authenticateduser).isFinished() ? "Finished" : "Not finished", lastCol }));
				}
			}
		}
		t.commit();
		session.close();

		return (GridRow[]) l.toArray(new GridRow[] {});
	}

	private String infoString(Submission submission) {
		
		int all = 0;
		int done = 0;
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			all++;
			if (element.isFinished()) {
				done++;
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(" (");
		buffer.append(done).append(" out of ").append(all);
		buffer.append(")");
		return buffer.toString();
	}

	private boolean reviewedByEditor(Submission submission, User user) {
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			if ((user == null || user.getId().equals(element.getUser().getId())) && element.getEditedVoyage() != null && element.getEditedVoyage().getVoyage() != null) {
				return true;
			}
		}
		return false;
	}
	
	private SubmissionEditor getSubmissionEditor(Submission submission, User user) {
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			if (element.getUser().getId().equals(user.getId())) {
				return element;
			}
		}
		return null;
	}

	private String getEditors(Submission submission) {
		StringBuffer editors = new StringBuffer();
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			editors.append(element.getUser().getUserName()).append(", ");
		}
		if (editors.length() != 0) {
			String tmp = editors.substring(0, editors.length() - 2);
			if (tmp.length() > 100) {
				return tmp.substring(0, 100) + " ...";
			}
			return tmp;
		}
		return "not yet assigned";
	}

	private boolean valid(Submission submission) {
		if (this.authenticateduser.isAdmin() || this.authenticateduser.isChiefEditor()) {
			return true;
		}
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			if (element.getUser().getId().equals(this.authenticateduser.getId())) {
				return true;
			}
		}
		return false;
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

		this.applier.newRequestId(e);
	}

	public String resolveRequest() {
		return "resolve-request";
	}

	public Boolean getRejectAvailable() {
		return this.applier.getRejectAvailable();

	}

	/**
	 * Action fired when reject button was pressed
	 */
	public String rejectSubmission() {
		return this.applier.rejectSubmission();
	}

	public String submit() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmission = Submission.loadById(session, this.applier.getSubmissionId());
		if (lSubmission instanceof SubmissionMerge && !deleteApproved) {
			t.commit();
			session.close();
			return "approve-delete";
		}
		t.commit();
		session.close();
		deleteApproved = false;
		return this.applier.submit();		
	}

	/**
	 * Action fired when accept button was pressed
	 */
	public String save() {
		return this.applier.save();
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
		this.chosenTab = "voyages";
		this.authenticateduser = authenticateduser;
		if (this.authenticateduser.isEditor()) {
			this.REQUESTS_LIST_COLS = new GridColumn[] { 
					new GridColumn("Type"), 
					new GridColumn("User"), 
					new GridColumn("Date"),
					new GridColumn("Involved voyages ID"),
					new GridColumn("Reviewed"),
					new GridColumn("Review finished"),
					new GridColumn("Status"),
				};
		} else {
			this.REQUESTS_LIST_COLS = new GridColumn[] { 
					new GridColumn("Type"), 
					new GridColumn("User"), 
					new GridColumn("Date"),
					new GridColumn("Voyages ID"),
					new GridColumn("Assigned reviewers"),
					new GridColumn("Review completed"),
					new GridColumn("Reviewed"), 
					new GridColumn("Status"),
				};
		}
	}

	public Boolean getIsAdmin() {
		return new Boolean(this.authenticateduser.isAdmin());
	}
	
	public Boolean getIsChiefEditor() {
		return new Boolean(this.authenticateduser.isAdmin() || this.authenticateduser.isChiefEditor());
	}

	public boolean isEditValid() {
		return this.voyageBean.isEditValid();
	}

	public boolean isDeleteValid() {
		return this.voyageBean.isDeleteValid();
	}

	public boolean isResolveValid() {
		return this.applier.getSubmissionId() != null;
	}

	public VoyageBean getVoyageBean() {
		return voyageBean;
	}

	public void setVoyageBean(VoyageBean voyageBean) {
		this.voyageBean = voyageBean;
	}

	public String publish() {

		synchronized (publishMonitor) {
			if (publishing) {
				this.message = "Publish operation is currently being processed - please be patient.";
				return null;
			}
			this.publishing = true;
		}
		
		if (this.revisionName == null || "".equals(this.revisionName)) {
			this.message = "Revision name cannot be empty!";
			return null;
		}
		
		Session session = HibernateConn.getSession();
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
		synchronized (publishMonitor) {
			publishing = false;
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
		
	public GridColumn[] getSourcesColumns() {
		return new GridColumn[] {
				new GridColumn("Source type"),
				new GridColumn("Source details")
		};
	}
	
	public GridRow[] getSourcesRows() {
		
		int i = 0;
		
		TastDbConditions c = new TastDbConditions();
		c.addCondition(new SequenceAttribute(new Attribute[] {SubmissionSource.getAttribute("submission"), Submission.getAttribute("id")}), this.applier.getSubmissionId(), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("SubmissionSource", c);
		List list = qValue.executeQueryList();
		Iterator iter = list.iterator();
		
		GridRow[] rows = new GridRow[list.size()];
		
		while (iter.hasNext()) {
			SubmissionSource source = (SubmissionSource)iter.next();
			if (source instanceof SubmissionSourceBook) {
				SubmissionSourceBook book = (SubmissionSourceBook)source;
				String desc = "Title: " + this.shortenIfNecessary(book.getTitle()) + 
				"; Authors: " + this.shortenIfNecessary(book.getAuthors());
				rows[i++] = new GridRow(book.getId().toString(), new String[] {
					"Book",
					desc
				});
			} else if (source instanceof SubmissionSourcePaper) {
				SubmissionSourcePaper paper = (SubmissionSourcePaper)source;
				String desc = "Title: " + this.shortenIfNecessary(paper.getTitle()) + 
					"; Authors: " + this.shortenIfNecessary(paper.getAuthors());
				rows[i++] = new GridRow(paper.getId().toString(), new String[] {
					"Article",
					desc
				});
			} else if (source instanceof SubmissionSourceOther) {
				SubmissionSourceOther other = (SubmissionSourceOther)source;
				String desc = "Title: " + this.shortenIfNecessary(other.getTitle()) + 
				"; Location: " + this.shortenIfNecessary(other.getLocation());
				rows[i++] = new GridRow(other.getId().toString(), new String[] {
					"Other",
					desc
				});
			} else if (source instanceof SubmissionSourcePrimary) {
				SubmissionSourcePrimary primary = (SubmissionSourcePrimary)source;
				String desc = "Name: " + this.shortenIfNecessary(primary.getName()) + 
				"; Location: " + this.shortenIfNecessary(primary.getLocation());
				rows[i++] = new GridRow(primary.getId().toString(), new String[] {
					"Primary source",
					desc
				});
			}
		}
		
		return rows;
	}
	
	private String shortenIfNecessary(String string) {
		if (string == null) {
			return "none";
		}
		if (string.length() > 40) {
			return string.substring(0, 40) + "...";
		} else {
			return string;
		}
	}
	
	public String addEditor() {
		this.addingEditor = new Boolean(true);
		return null;
	}
	
	public String applyAddEditor() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();

		try {
			this.addingEditor = new Boolean(false);
			User user = User.loadById(session, new Long(this.newEditorUser));
			EditedVoyage eVoyage = new EditedVoyage();
			SubmissionEditor editor = new SubmissionEditor();
			editor.setSubmission(Submission.loadById(session, this.applier.getSubmissionId()));
			editor.setUser(user);
			editor.setEditedVoyage(eVoyage);
			session.save(eVoyage);
			session.save(editor);
			this.applier.setRequiredReload(true);
		} finally {
			t.commit();
			session.close();
		}
		return null;
	}
	
	public String cancelAddEditor() {
		this.addingEditor = new Boolean(false);
		this.applier.setRequiredReload(true);
		return null;
	}
	
	public Boolean getAddingEditor() {
		return this.addingEditor;
	}

	public String getNewEditorUser() {
		return newEditorUser;
	}

	public void setNewEditorUser(String newEditorUser) {
		this.newEditorUser = newEditorUser;
	}
	
	public void openSourcesRow(GridOpenRowEvent e) {
		this.sourceId = new Long(e.getRowId());
	}
	
	public String closeDetails() {
		this.sourceId = null;
		return null;
	}
	
	public Boolean getIsSourceDetailsVisible() {
		return new Boolean(this.sourceId != null);
	}
	
	public SourceData[] getSourceData() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		List dataItems = new ArrayList();
		try {
			SubmissionSource source = SubmissionSource.loadById(session, this.sourceId);
			if (source instanceof SubmissionSourcePrimary) {
				SubmissionSourcePrimary primary = (SubmissionSourcePrimary)source;
				dataItems.add(new SourceData("Source type", "Primary source"));
				dataItems.add(new SourceData("Name of  library or archive", primary.getName()));
				dataItems.add(new SourceData("Location of library/archive", primary.getLocation()));
				dataItems.add(new SourceData("Series or collection", primary.getSeries()));
				dataItems.add(new SourceData("Volume or box or bundle", primary.getVolume()));
				dataItems.add(new SourceData("Document details (page or folio, and/or date of document)", primary.getDetails()));
				dataItems.add(new SourceData("Additional information", primary.getNote()));
			} else if (source instanceof SubmissionSourcePaper) {
				SubmissionSourcePaper primary = (SubmissionSourcePaper)source;
				dataItems.add(new SourceData("Source type", "Publication"));
				dataItems.add(new SourceData("Title", primary.getTitle()));
				dataItems.add(new SourceData("Authors", primary.getAuthors()));
				dataItems.add(new SourceData("Journal", primary.getJournal()));
				dataItems.add(new SourceData("Year/Volume", primary.getYear() + "/" + primary.getVolume()));
				dataItems.add(new SourceData("Pages", primary.getPageFrom() + " - " + primary.getPageTo()));
				dataItems.add(new SourceData("Additional information", primary.getNote()));
			} else if (source instanceof SubmissionSourceBook) {
				SubmissionSourceBook primary = (SubmissionSourceBook)source;
				dataItems.add(new SourceData("Source type", "Book"));
				dataItems.add(new SourceData("Title", primary.getTitle()));
				dataItems.add(new SourceData("Authors", primary.getAuthors()));
				dataItems.add(new SourceData("Publisher", primary.getPublisher()));
				dataItems.add(new SourceData("Place of publication", primary.getPlaceOfPublication()));
				dataItems.add(new SourceData("Year", String.valueOf(primary.getYear())));
				dataItems.add(new SourceData("Pages", primary.getPageFrom() + " - " + primary.getPageTo()));
				dataItems.add(new SourceData("Additional information", primary.getNote()));
			} else if (source instanceof SubmissionSourceOther) {
				SubmissionSourceOther primary = (SubmissionSourceOther)source;
				dataItems.add(new SourceData("Source type", "Other source"));
				dataItems.add(new SourceData("Title", primary.getTitle()));
				dataItems.add(new SourceData("Location", primary.getLocation()));
				dataItems.add(new SourceData("Page or folio", primary.getFolioOrPage()));
				dataItems.add(new SourceData("Additional information", primary.getNote()));
			}
		} finally {
			t.commit();
			session.close();
		}
		return (SourceData[]) dataItems.toArray(new SourceData[] {});
	}
	
	public class SourceData {
		private String name;
		private String value;
		public SourceData(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		
	}
	
	public Long getSubmissionId() {
		return this.applier.getSubmissionId();
	}
	
	public void columnAction(ColumnActionEvent event) {
		this.applier.columnAction(event);
	}

	public Boolean getFinished() {
		return new Boolean(finished);
	}

	public void setFinished(Boolean finished) {
		this.finished = finished.booleanValue();
	}
	
	public Map getFieldTypesSlave() {
		return SubmissionDictionaries.simpleFieldTypes;
	}
	
	public Row[] getRowsSlave() {
		return applier.getRowsSlave();
	}
	
	public Row[] getRowsSlave2() {
		return applier.getRowsSlave2();
	}
	
	public Column[] getColumnsSlave() {
		return applier.getColumnsSlave();
	}
	
	public Column[] getColumnsSlave2() {
		return applier.getColumnsSlave2();
	}
	
	public void setValuesSlave(Values values) {
		this.applier.setValuesSlave(values);
	}
	
	public Values getValuesSlave() {
		return this.applier.getValuesSlave();
	}
	
	
	public void setValuesSlave2(Values values) {
		this.applier.setValuesSlave2(values);
	}
	
	public Values getValuesSlave2() {
		return this.applier.getValuesSlave2();
	}
	
	
	public RowGroup[] getRowGroupsSlave() {
		return applier.getRowGroupsSlave();
	}
	
	public RowGroup[] getRowGroupsSlave2() {
		return applier.getRowGroupsSlave2();
	}
	
	public String logoutOnly() {
		this.authenticateduser = null;
		return null;
	}
	
	public String getFileAllData()	{		
		boolean useSQL = AppConfig.getConfiguration().getBoolean(AppConfig.DATABASE_USE_SQL);
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
				
		TastDbQuery query = this.getQueryFileAllData();		
		CSVUtils.writeResponse(session, query, false, true);
		t.commit();
		session.close();
		return null;		
	}
	
	private TastDbQuery getQueryFileAllData() {		
		Integer[] revArr = {1, -1};
		TastDbConditions cond = new TastDbConditions();
		cond.addCondition(Voyage.getAttribute("revision"), revArr, TastDbConditions.OP_IN);
		cond.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), TastDbConditions.OP_EQUALS);
		
		TastDbQuery q = new TastDbQuery(new String[] {"Voyage"}, new String[] {}, cond);	
		q.setOrderBy(new Attribute[] {Voyage.getAttribute("voyageid")});
		q.setOrder(TastDbQuery.ORDER_ASC);
		String[] str = 
			{"voyageid", "suggestion", "revision", "iid", "adlt1imp", "adlt2imp", "adlt3imp", "adpsale1", "adpsale2", "adult1", "adult2", "adult3", "adult4", "adult5", "adult6", "adult7", "arrport", "arrport2", "boy1", "boy2", "boy3", "boy4", "boy5", "boy6", "boy7", "boyrat1", "boyrat3", "boyrat7", "captaina", "captainb", "captainc", "chil1imp", "chil2imp", "chil3imp", "child1", "child2", "child3", "child4", "child5", "child6", "child7", "chilrat1", "chilrat3", "chilrat7", "constreg", "crew", "crew1", "crew2", "crew3", "crew4", "crew5", "crewdied", "datedepa", "datedepb", "datedepc", "d1slatra", "d1slatrb", "d1slatrc", "dlslatra", "dlslatrb", "dlslatrc", "ddepam", "ddepamb", "ddepamc", "datarr32", "datarr33", "datarr34", "datarr36", "datarr37", "datarr38", "datarr39", "datarr40", "datarr41", "datarr43", "datarr44", "datarr45", "datedep", "datebuy", "dateleftafr", "dateland1", "dateland2", "dateland3", "datedepam", "dateend", "deptregimp", "deptregimp1", "embport", "embport2", "embreg", "embreg2", "evgreen", "fate", "fate2", "fate3", "fate4", "female1", "female2", "female3", "female4", "female5", "female6", "female7", "feml1imp", "feml2imp", "feml3imp", "girl1", "girl2", "girl3", "girl4", "girl5", "girl6", "girl7", "girlrat1", "girlrat3", "girlrat7", "guns", "infant1", "infant3", "infant4", "jamcaspr", "majbuypt", "majbyimp", "majbyimp1", "majselpt", "male1", "male1imp", "male2", "male2imp", "male3", "male3imp", "male4", "male5", "male6", "male7", "malrat1", "malrat3", "malrat7", "men1", "men2", "men3", "men4", "men5", "men6", "men7", "menrat1", "menrat3", "menrat7", "mjbyptimp", "mjselimp", "mjselimp1", "mjslptimp", "natinimp", "national", "ncar13", "ncar15", "ncar17", "ndesert", "npafttra", "nppretra", "npprior", "ownera", "ownerb", "ownerc", "ownerd", "ownere", "ownerf", "ownerg", "ownerh", "owneri", "ownerj", "ownerk", "ownerl", "ownerm", "ownern", "ownero", "ownerp", "plac1tra", "plac2tra", "plac3tra", "placcons", "placreg", "portdep", "portret", "ptdepimp", "regarr", "regarr2", "regdis1", "regdis2", "regdis3", "regem1", "regem2", "regem3", "regisreg", "resistance", "retrnreg", "retrnreg1", "rig", "saild1", "saild2", "saild3", "saild4", "saild5", "shipname", "sla1port", "slaarriv", "sladafri", "sladamer", "sladvoy", "slamimp", "slas32", "slas36", "slas39", "slavema1", "slavema3", "slavema7", "slavemx1", "slavemx3", "slavemx7", "slavmax1", "slavmax3", "slavmax7", "slaximp", "slinten2", "slintend", "sourcea", "sourceb", "sourcec", "sourced", "sourcee", "sourcef", "sourceg", "sourceh", "sourcei", "sourcej", "sourcek", "sourcel", "sourcem", "sourcen", "sourceo", "sourcep", "sourceq", "sourcer", "tonmod", "tonnage", "tontype", "tslavesd", "tslavesp", "tslmtimp", "voy1imp", "voy2imp", "voyage", "vymrtimp", "vymrtrat", "women1", "women2", "women3", "women4", "women5", "women6", "women7", "womrat1", "womrat3", "womrat7", "xmimpflag", "year10", "year100", "year25", "year5", "yearaf", "yearam", "yeardep", "yrcons", "yrreg"};
		List<String> arrList = Arrays.asList(str);  
				
		for (Iterator iterator = arrList.iterator(); iterator.hasNext();) {
			String attr  = (String) iterator.next();
			q.addPopulatedAttribute(Voyage.getAttribute(attr));
		}
		
		return q;	
	}
	
	
	
	//look up voyageid assigned by Admin based on submisisonId
	public Integer getIdForList(Long subId)
	{
		//There should be a better way of doing this!!!
		Session sess = HibernateConn.getSession(); 
		Transaction	transaction = sess.beginTransaction();
	
		//Link to new table
		String Q = " FROM SubmissionNew WHERE submission_id= " + subId;
		Query query = sess.createQuery(Q);
		List voyIdList = query.list();
		
		if (voyIdList==null || voyIdList.size()==0) {return null;}
		
		SubmissionNew sn = (SubmissionNew) voyIdList.get(0);
		
		if(sn==null || sn.getEditorVoyage()==null) {return null;}
		
		Long id = sn.getEditorVoyage().getId();
		
		if(id==null) {return null;}
		
		//Get Edited voyage -> voyage -> voyageid
		EditedVoyage ev = EditedVoyage.loadById(sess, id);
		Voyage v = ev.getVoyage();
		
		if(v==null) {return null;}
		
		Integer ret = v.getVoyageid();
		
		        
		transaction.commit();
		sess.close();
		
		return ret;
	}
	
}