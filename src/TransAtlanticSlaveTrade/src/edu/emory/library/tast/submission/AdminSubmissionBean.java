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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;


import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	
	private boolean deleteVoaygeApproved = false;

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
	 * Checks if source codes is selected.
	 * @return
	 */
	public Boolean getSourceCodesSelected() {
		return new Boolean(this.chosenTab.equals("source_codes"));
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

		/*
		 * Statuses
		 * 1 - All
		 * 2 - Under review
		 * 3 - Not assigned
		 * 4 - Final
		 * 5 - Review complete
		 */
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Submission.getAttribute("submitted"), new Boolean(true), TastDbConditions.OP_EQUALS);		
		if (this.requestStatus.equals("2")) {
			c.addCondition(Submission.getAttribute("solved"),
					new Boolean(false), TastDbConditions.OP_EQUALS);
		} else if (this.requestStatus.equals("4")) {
			c.addCondition(Submission.getAttribute("solved"),
					new Boolean(true), TastDbConditions.OP_EQUALS);
		}
		if (this.requestType.equals("1") || this.requestType.equals("2")) {
			TastDbQuery q = new TastDbQuery("SubmissionNew", c);
			Object[] subs = q.executeQuery(session);
			for (int i = 0; i < subs.length; i++) {
				SubmissionNew submission = (SubmissionNew) subs[i];
				if (!valid(submission)) {
					continue;
				}
				
				if (!validRestAttribute(this.requestStatus, submission)){
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
				Integer adminVId = getIdForList(session,  submission.getId());
				String adminVId_str = (adminVId==null ? "New voyage - ID not yet assigned" : adminVId.toString());
				Voyage voyage = new Voyage();
				voyage = Voyage.loadById(session, ((SubmissionNew) submission)
						.getNewVoyage().getVoyage().getIid());
		
				//display the rows according  to the two inputs
//				if (this.requestStatus.equals("1") || this.requestStatus.equals("3") || this.requestStatus.equals("4") || (this.requestStatus.equals("2") && !editorsFinished(submission)) || (this.requestStatus.equals("5") && editorsFinished(submission))) {
					if (!this.authenticateduser.isEditor()) {
						l.add(new GridRow(REQUEST_NEW_PREFIX
								+ submission.getId(), new String[] {
											"New voyage",
											submission.getUser().getUserName(),
											formatter.format(submission
													.getTime()),
											adminVId_str,
											this.getEditors(submission),
											this.reviewedByEditor(submission,
													null) ? "Yes"
													+ this
															.infoString(submission)
													: "No"
															+ this
																	.infoString(submission),
											lastCol }));
					} else {
						l.add(new GridRow(REQUEST_NEW_PREFIX
								+ submission.getId(),
								new String[] {
										"New request",
										formatter.format(submission.getTime()),
										adminVId_str,
										voyage.getShipname(),
										voyage.getYearam() != null ? voyage
												.getYearam().toString()
												: "Not assigned yet",
										this.reviewedByEditor(submission,
												this.authenticateduser) ? "Yes"
												: "No" }));
					}
//				}
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
				
				if (!validRestAttribute(this.requestStatus, submission)){
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
				Voyage voyage = Voyage.loadById(session, ((SubmissionEdit) submission)
						.getOldVoyage().getVoyage().getIid());
				
				//display the rows according  to the two inputs
//				if (this.requestStatus.equals("1") || this.requestStatus.equals("3")|| this.requestStatus.equals("4") || (this.requestStatus.equals("2") && !editorsFinished(submission)) || (this.requestStatus.equals("5") && editorsFinished(submission))) {
					if (!this.authenticateduser.isEditor()) {
						l.add(new GridRow(REQUEST_EDIT_PREFIX
								+ submission.getId(), new String[] {
											"Edit voyage",
											submission.getUser().getUserName(),
											formatter.format(submission
													.getTime()),
											submission.getOldVoyage()
													.getVoyage().getVoyageid()
													.toString(),
											this.getEditors(submission),
											this.reviewedByEditor(submission,
													null) ? "Yes"
													+ this
															.infoString(submission)
													: "No"
															+ this
																	.infoString(submission),
											lastCol }));
					} else {
						l.add(new GridRow(REQUEST_EDIT_PREFIX
								+ submission.getId(),
								new String[] {
										"Edit request",
										formatter.format(submission.getTime()),
										submission.getOldVoyage().getVoyage()
												.getVoyageid().toString(),
										voyage.getShipname(),
										voyage.getYearam() != null ? voyage
												.getYearam().toString()
												: "Not assigned yet",
										this.reviewedByEditor(submission,
												this.authenticateduser) ? "Yes"
												: "No" }));
					}
//				}
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
				
				if (!validRestAttribute(this.requestStatus, submission)){
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
				Long suggestedVoyageId = ((SubmissionMerge) submission)
						.getProposedVoyage().getId();
				
				EditedVoyage eV = EditedVoyage.loadById(session,
						suggestedVoyageId);
				Voyage suggestVoyage = eV.getVoyage();
				String shipName;
				if(suggestVoyage.getShipname()==null)
					shipName="None suggested name";
				else
					shipName=suggestVoyage.getShipname();
				
				//display the rows according  to the two inputs
//				if (this.requestStatus.equals("1") || this.requestStatus.equals("3") || this.requestStatus.equals("4") || (this.requestStatus.equals("2") && !editorsFinished(submission)) || (this.requestStatus.equals("5") && editorsFinished(submission))) {
					if (!this.authenticateduser.isEditor()) {
						l.add(new GridRow(REQUEST_MERGE_PREFIX
								+ submission.getId(), new String[] {
											"Merge voyages",
											submission.getUser().getUserName(),
											formatter.format(submission
													.getTime()),
											involvedStr,
											this.getEditors(submission),
											this.reviewedByEditor(submission,
													null) ? "Yes"
													+ this
															.infoString(submission)
													: "No"
															+ this
																	.infoString(submission),
											lastCol }));
					} else {

						l.add(new GridRow(
										REQUEST_MERGE_PREFIX
												+ submission.getId(),
										new String[] {
												"Merge request",
												formatter.format(submission
														.getTime()),
												involvedStr,
												shipName,
												suggestVoyage.getYearam() != null ? suggestVoyage
														.getYearam().toString()
														: "Not assigned yet",
												this.reviewedByEditor(
														submission,
														this.authenticateduser) ? "Yes"
														: "No" }));
					}
//				}
			}
		}
		t.commit();
		session.close();

		return (GridRow[]) l.toArray(new GridRow[] {});
	}

	private boolean validRestAttribute(String requestStatus,
			Submission submission) {
		if (requestStatus.equals("3")) {
			if (submission.getSubmissionEditors().size() != 0) {
				return false;
			}
		} else if (requestStatus.equals("5")) {
			if (!reviewedByEditor(submission,
					this.authenticateduser.isEditor() ? this.authenticateduser
							: null))
				return false;
		}
		return true;
	}

	private String infoString(Submission submission) {
		
		int all = 0;
		int done = 0;
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter
				.hasNext();) {
			SubmissionEditor element = (SubmissionEditor) iter.next();
			all++;
			if (element.isFinished()) {
				done++;
			}
		}
		if (all != 0) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(" (");
			buffer.append(done).append(" out of ").append(all);
			buffer.append(")");
			return buffer.toString();
		} else {
			return "";
		}
	}

	private boolean reviewedByEditor(Submission submission, User user) {
		if (user == null) {
			return editorsFinished(submission);
		} else {
			for (Iterator iter = submission.getSubmissionEditors().iterator(); iter
					.hasNext();) {
				SubmissionEditor element = (SubmissionEditor) iter.next();
				if (user.getId().equals(element.getUser().getId())) {
					if (element.isFinished() == true) {
						return true;
					} else {
						return false;
					}
				}
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
	
	public Boolean getAccepted() {
		return this.applier.getAccepted();

	}

	/**
	 * Action fired when reject button was pressed
	 */
	public String rejectSubmission() {
		return this.applier.rejectSubmission();
	}
	
	public String deleteSubmission() {
		return this.applier.deleteSubmission();
	}
	
	
	public String deleteVoyage() {
		if (!deleteVoaygeApproved) {
			return "approve-delete-voyage";
		}
		deleteVoaygeApproved = false;
		return this.applier.deleteVoyage();
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
		try{
		return this.applier.save();
		}catch(Exception e){
			e.printStackTrace();
			return "Could not save this record due to either invalid data or your entry for a field is too long. The system reported cause is: " + e.getCause();
		}
	}
	
	public String back() {
		this.sourceId=null;
		return "back";
	}

	public String approveDelete() {
		this.deleteApproved = true;
		if (this.submit() == null) {
			return "back";
		}
		return "main-menu";
	}
	
	public String deleteEditandMerge() {
		this.deleteVoaygeApproved = true;
		if (this.applier.deleteVoyage()== null) {
			return "back";
		}
		return "main-menu";
	}

	public String rejectDelete() {
		return "main-menu";
	}
	
	public String rejectDeleteVoyage() {
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
					new GridColumn("Type of submission"), 
					new GridColumn("Date of submission"),
					new GridColumn("Voyage ID"),
					new GridColumn("Ship"),
					new GridColumn("Year"),
					new GridColumn("Reviewed"),
				};
		} else {
			this.REQUESTS_LIST_COLS = new GridColumn[] { 
					new GridColumn("Type"), 
					new GridColumn("User"), 
					new GridColumn("Date"),
					new GridColumn("Voyages ID"),
					new GridColumn("Assigned reviewers"),
					new GridColumn("Review completed"),
//					new GridColumn("Reviewed"), 
					new GridColumn("Status"),
				};
		}
	}

	public Boolean getIsNew() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmission = Submission.loadById(session, this.applier.getSubmissionId());
		t.commit();
		session.close();
		return new Boolean((lSubmission instanceof SubmissionNew));
	}
	
	public Boolean getIsAdmin() {
		try{
		return new Boolean(this.authenticateduser.isAdmin());
		}catch(Exception e){
			return false; //default to false if there is an error
		}
	}
	
	public Boolean getIsChiefEditor() {
		try{
			return new Boolean(this.authenticateduser.isAdmin() || this.authenticateduser.isChiefEditor());
		}catch (Exception e) {
			return false;
		}
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
		
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();

		try {
			System.out.println("publish");
			SQLQuery query = session.createSQLQuery("select publish();");
			query.list();
			this.message = "New revision has just been published.";
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
				new GridColumn("Source details"),
				new GridColumn("Contributor")
		};
	}
	
	public GridRow[] getSourcesRows() {

		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();


		List<GridRow> rowList = new ArrayList<GridRow>();

		Long[] relatedSubmissionId = this.applier.getRelatedSubmissionsId();
//		Long[] requestlist = {(long)1795366,(long)1795371};
		for (int i = 0; i < relatedSubmissionId.length; i++) {

			
//			c.addCondition(new SequenceAttribute(new Attribute[] {SubmissionSource.getAttribute("submission"), Submission.getAttribute("id")}), this.applier.getSubmissionId(), TastDbConditions.OP_EQUALS);
			
			TastDbConditions c = new TastDbConditions();
			
			c.addCondition(new SequenceAttribute(new Attribute[] {
					SubmissionSource.getAttribute("submission"),
					Submission.getAttribute("id") }), relatedSubmissionId[i],
					TastDbConditions.OP_EQUALS);
			TastDbQuery qValue = new TastDbQuery("SubmissionSource", c);
			List list = qValue.executeQueryList();
			Iterator iter = list.iterator();

			// GridRow[] rows = new GridRow[list.size()];

			while (iter.hasNext()) {
				SubmissionSource source = (SubmissionSource) iter.next();
				String contributorName = Submission.loadById(session,
						relatedSubmissionId[i]).getUser().getUserName();
				if (source instanceof SubmissionSourceBook) {
					SubmissionSourceBook book = (SubmissionSourceBook) source;
					String desc = "Title: "
							+ this.shortenIfNecessary(book.getTitle())
							+ "; Authors: "
							+ this.shortenIfNecessary(book.getAuthors());
					rowList.add(new GridRow(book.getId().toString(),
							new String[] { "Book", desc, contributorName }));
				} else if (source instanceof SubmissionSourcePaper) {
					SubmissionSourcePaper paper = (SubmissionSourcePaper) source;
					String desc = "Title: "
							+ this.shortenIfNecessary(paper.getTitle())
							+ "; Authors: "
							+ this.shortenIfNecessary(paper.getAuthors());
					rowList.add(new GridRow(paper.getId().toString(),
							new String[] { "Article", desc, contributorName }));
				} else if (source instanceof SubmissionSourceOther) {
					SubmissionSourceOther other = (SubmissionSourceOther) source;
					String desc = "Title: "
							+ this.shortenIfNecessary(other.getTitle())
							+ "; Location: "
							+ this.shortenIfNecessary(other.getLocation());
					rowList.add(new GridRow(other.getId().toString(),
							new String[] { "Other", desc, contributorName }));
				} else if (source instanceof SubmissionSourcePrimary) {
					SubmissionSourcePrimary primary = (SubmissionSourcePrimary) source;
					String desc = "Name: "
							+ this.shortenIfNecessary(primary.getName())
							+ "; Location: "
							+ this.shortenIfNecessary(primary.getLocation());
					rowList.add(new GridRow(primary.getId().toString(),
							new String[] { "Primary source", desc,
									contributorName }));
				}
			}
		}

		GridRow[] rows = new GridRow[rowList.size()];
		
		for(int i =0; i<rowList.size(); i++){
			rows[i] = rowList.get(i);
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
			t.commit();
		} finally {
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
	
	public Map getFieldTypesSlave3() {
		return SubmissionDictionaries.simpleFieldTypes;
	}
	
	public Row[] getRowsSlave() {
		return applier.getRowsSlave();
	}
	
	public Row[] getRowsSlave2() {
		return applier.getRowsSlave2();
	}
	
	public Row[] getRowsSlave3() {
		return applier.getRowsSlave3();
	}
	
	public Column[] getColumnsSlave() {
		return applier.getColumnsSlave();
	}
	
	public Column[] getColumnsSlave2() {
		return applier.getColumnsSlave2();
	}
	
	public Column[] getColumnsSlave3() {
		return applier.getColumnsSlave3();
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
	
	public void setValuesSlave3(Values values) {
		this.applier.setValuesSlave3(values);
	}
	
	public Values getValuesSlave3() {
		return this.applier.getValuesSlave3();
	}
	
	public RowGroup[] getRowGroupsSlave() {
		return applier.getRowGroupsSlave();
	}
	
	public RowGroup[] getRowGroupsSlave2() {
		return applier.getRowGroupsSlave2();
	}
	
	public RowGroup[] getRowGroupsSlave3() {
		return applier.getRowGroupsSlave3();
	}
	
	public String logoutOnly() {
		this.authenticateduser = null;
		return null;
	}
	
	public String getFilePublishedandApprovedData()	{		
		boolean useSQL = AppConfig.getConfiguration().getBoolean(AppConfig.DATABASE_USE_SQL);
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
				
		TastDbQuery query = this.getQueryFilePublishedandApprovedData();		
		CSVUtils.writeResponse(session, query, false, true);
		t.commit();
		session.close();
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

		TastDbConditions cond = new TastDbConditions();
		
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

	private TastDbQuery getQueryFilePublishedandApprovedData() {		
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
	public Integer getIdForList(Session sess, Long subId)
	{
		//get voyageId for new submission and return result
		
		Voyage v = null;
		
		try {
			v = Voyage.loadById(sess, ((SubmissionNew) Submission.loadById(sess, subId)).getEditorVoyage().getVoyage().getIid());
		} catch (Exception e) {return null;}
				
		return v.getVoyageid();
	}
	
	//Checks to see if all editors are done
	public boolean editorsFinished(Submission sub) {
		boolean ret = true;

		Set editors = sub.getSubmissionEditors();

		Iterator itt = editors.iterator();

		if (!itt.hasNext()) {
			return false;
		}

		while (itt.hasNext()) {
			SubmissionEditor se = (SubmissionEditor) itt.next();

			if (!se.isFinished()) {
				ret = false;
			}

		}

		return ret;

	}
	
	public void impute()
	{
		try{
			this.applier.save();
		}catch(Exception e){
			System.out.println("Could not save this record due to either invalid data or your entry for a field is too long. The system reported cause is:");
			e.printStackTrace();
		}

		
		System.out.println("I feel like I'm going to impute!");
		
		Session session = HibernateConn.getSession();
		
		Voyage v = this.applier.getAdminVoyage(session, this.getSubmissionId());
		
		this.applier.imputeVariables(session, v);
		
	}
	
	public boolean getImputeAvailable()
	{
		if(this.getRejectAvailable() && this.getAuthenticateduser().isAdmin())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
}