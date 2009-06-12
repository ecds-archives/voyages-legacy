package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.database.SourceInformationLookup;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.SubmissionSource;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.JsfUtils;

public class SubmissionBean
{

	public static final String ORIGINAL_VOYAGE_LABEL = TastResource.getText("submissions_oryginal_voyage");
	public static final String CHANGED_VOYAGE_LABEL = TastResource.getText("submissions_changed_voyage");

	public static final String ORIGINAL_VOYAGE = "old";
	public static final String MERGED_VOYAGE_PREFIX = "merged:";
	public static final String CHANGED_VOYAGE = "new";

	public static final String LOCATIONS = "locations";	
	public static final String PORTS = "ports";	

	public static final int SUBMISSION_TYPE_NOT_SELECTED = 0;	
	public static final int SUBMISSION_TYPE_NEW = 1;	
	public static final int SUBMISSION_TYPE_EDIT = 2;	
	public static final int SUBMISSION_TYPE_MERGE = 3;
	public static final String SOURCES_STATE = "sources-restore";
	public static final String SUBMISSION_STATE = "submission-restore";

	public static final String[] SLAVE_CHAR_COLS = {"men", "women", "boy", "girl", "male", "female", "adult", "child"};
	public static final String[] SLAVE_CHAR_COLS_LABELS = {"Men", "Women", "Boys", "Girls", "Males", "Females", "Adults", "Children"};
	public static final String[] SLAVE_CHAR_ROWS = {"e1", "e2", "e3", "died", "d1", "d2", "i1"};
	//public static final String[] SLAVE_CHAR_ROWS = {"e1", "e2", "e3", "died", "d1", "d2"};
	public static final String[] SLAVE_CHAR_ROWS_LABELS = {
			"Embarked slaves (first port)",			
			"Embarked slaves (second port)",
			"Embarked slaves (third port)",
			"Died on voyage", 
			"Disembarked slaves (first port)",
			"Disembarked slaves (second port)",
			"Slaves on arrival or departure*"
	};
	
	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getPublicAttributes();
	
	private SourcesBean sourcesBean = null;
	
	private int submissionType = SUBMISSION_TYPE_NOT_SELECTED;
	
	private Values gridValues = null;
	private Values slaveValues = null;
	
	private SubmissionVerifyBean verifyBean;
	
	private RowGroup[] rowGroups;

	private Integer lookupVoyageId = null;
	private boolean lookupPerformed = false;
	private SelectedVoyageInfo lookedUpVoyage = null;

	private SelectedVoyageInfo selectedVoyageForEdit = null;
	private List selectedVoyagesForMerge = new ArrayList();
	
	private long toRemovedVoyageId;
	
	private boolean errorSelectAtLeastTwo = false;
	
	private Set expandedGridRows;

	private User authenticatedUser = null;
	
	private Submission submission;
	
	public SubmissionBean()
	{
		
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
	
	/////////////////////////////////////////////////////////////////////////////////
	// wizard logic
	/////////////////////////////////////////////////////////////////////////////////
	
	public String selectTypeNew() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			cleanSubmission(session);
			if (submissionType != SUBMISSION_TYPE_NEW) {
				submissionType = SUBMISSION_TYPE_NEW;
				expandedGridRows = null;
				initNewVoyage();
			}
		} finally {
			t.commit();
			session.close();
		}
		return "form";
	}

	public String selectTypeEdit() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			cleanSubmission(session);
			if (submissionType != SUBMISSION_TYPE_EDIT) {
				submissionType = SUBMISSION_TYPE_EDIT;
				lookupVoyageId = null;
				lookupPerformed = false;
				expandedGridRows = null;
				cleanSubmission(session);
			} else {
				if (selectedVoyageForEdit != null) {
					lookedUpVoyage = selectedVoyageForEdit;
					lookupVoyageId = new Integer(selectedVoyageForEdit.getVoyageId());
				}
			}
		} finally {
			t.commit();
			session.close();
		}
		return "edit";
	}

	public String selectTypeMerge() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			cleanSubmission(session);
			lookupPerformed = false;
			lookedUpVoyage = null;
			errorSelectAtLeastTwo = false;
			if (submissionType != SUBMISSION_TYPE_MERGE) {
				submissionType = SUBMISSION_TYPE_MERGE;
				expandedGridRows = null;
				cleanSubmission(session);
			}
		} finally {
			t.commit();
			session.close();
		}
		return "merge";
	}
	
	private void cleanSubmission(Session session) {
		if (this.submission != null && !this.submission.isSubmitted()) {
			Submission localCopy = Submission.loadById(session, this.submission.getId());
				Set sources = localCopy.getSources();
				for (Iterator iter = sources.iterator(); iter.hasNext();) {
					SubmissionSource element = (SubmissionSource) iter.next();
					session.delete(element);
				}
				session.delete(localCopy);
		}
		this.submission = null;
	}

	public String removeSelectedVoyageForMerge()
	{
		
		for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();)
		{
			SelectedVoyageInfo voyage = (SelectedVoyageInfo) iter.next();
			if (voyage.getVoyageId() == toRemovedVoyageId)
			{
				iter.remove();
			}
		}
		
		return null;

	}
	
	public String editVoyage()
	{
		selectedVoyageForEdit = lookedUpVoyage;
		loadVoyageForEdit();
		return "form";
	}
	
	public String addVoyageForMerge()
	{
		selectedVoyagesForMerge.add(lookedUpVoyage);
		lookupVoyageId = null;
		lookupPerformed = false;
		lookedUpVoyage = null;
		return null;
	}
	
	public Boolean getLessThanFive() {
		return new Boolean(this.selectedVoyagesForMerge.size() < 5);
	}
	
	public String mergeVoyages()
	{
		if (selectedVoyagesForMerge == null || selectedVoyagesForMerge.size() < 2)
		{
			errorSelectAtLeastTwo = true;
			return null;
		}
		else
		{
			errorSelectAtLeastTwo = false;
			loadVoyagesForMerge();
			return "form";
		}
	}

	public String goBackFromForm()
	{
		if (submissionType == SUBMISSION_TYPE_NEW)
		{
			return "back-new";
		}
		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			return "back-edit";
		}
		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{
			return "back-merge";
		}
		else
		{
			return "start";
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// particular methods for loading data into a grid
	/////////////////////////////////////////////////////////////////////////////////
	
	private void initNewVoyage()
	{
		gridValues = new Values();
		slaveValues = new Values();
		initColumnForNewVoyaye(CHANGED_VOYAGE);
	}

	private boolean loadVoyageForEdit()
	{
		
		gridValues = new Values();
		slaveValues = new Values();
		
		Session session = HibernateConn.getSession();
		Transaction trans = session.beginTransaction();
		
		SourceInformationLookup sourceInformationUtils =
			SourceInformationLookup.createSourceInformationUtils(session);
		
		loadVoyageToColumn(session, selectedVoyageForEdit.getVoyageId(), gridValues, ORIGINAL_VOYAGE, sourceInformationUtils);
		initColumnForNewVoyaye(CHANGED_VOYAGE);
		
		Voyage old = Voyage.loadCurrentRevision(session, selectedVoyageForEdit.getVoyageId());
		for (int i = 0; i < SLAVE_CHAR_COLS.length; i++) {
			for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
				SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(
						SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
				if (attribute == null) {
					throw new RuntimeException("SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] + ","
							+ SLAVE_CHAR_COLS[i]);
				}
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int k = 0; k < toBeFormatted.length; k++) {
					toBeFormatted[k] = old.getAttrValue(attribute.getAttribute()[k].getName());
				}
				Value value = attribute.getValue(session, toBeFormatted, sourceInformationUtils);
				slaveValues.setValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j] + "_old", value);
			}
		}
		
		trans.commit();
		session.close();
		
		return true;
		
	}
	
	private boolean loadVoyagesForMerge()
	{
		
		gridValues = new Values();
		slaveValues = new Values();
		
		Session session = HibernateConn.getSession();
		Transaction trans = session.beginTransaction();
		
		SourceInformationLookup sourceInformationUtils =
			SourceInformationLookup.createSourceInformationUtils(session);

		int i = 0;
		for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();)
		{
			SelectedVoyageInfo voyage = (SelectedVoyageInfo) iter.next();
			loadVoyageToColumn(session, voyage.getVoyageId(), gridValues, MERGED_VOYAGE_PREFIX + i, sourceInformationUtils);
			i++;
		}

		initColumnForNewVoyaye(CHANGED_VOYAGE);

		for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();) {
			SelectedVoyageInfo element = (SelectedVoyageInfo) iter.next();
			Voyage old = Voyage.loadCurrentRevision(session, element.getVoyageId());
			for (i = 0; i < SLAVE_CHAR_COLS.length; i++) {
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
					SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(
							SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
					if (attribute == null) {
						throw new RuntimeException("SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] + ","
								+ SLAVE_CHAR_COLS[i]);
					}
					Object[] toBeFormatted = new Object[attribute.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++) {
						toBeFormatted[k] = old.getAttrValue(attribute.getAttribute()[k].getName());
					}
//					Value value = attribute.getValue(session, toBeFormatted, sourceInformationUtils);
//					slaveValues.setValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j] + "_" + element.getVoyageId(), value);
				}
			}
		}
		
		
		trans.commit();
		session.close();

		return true;

	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// universal methods for loading data into a grid
	/////////////////////////////////////////////////////////////////////////////////
	
	private void initColumnForNewVoyaye(String columnName)
	{
		if (!prefillSuccess(columnName)) {
			for (int i = 0; i < attrs.length; i++) {
				gridValues.setValue(
					columnName,
					attrs[i].getName(),
					attrs[i].getEmptyValue());
			}
			for (int i = 0; i < SLAVE_CHAR_COLS.length; i++) {
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
					SubmissionAttribute attr = SubmissionAttributes.getConfiguration().getAttribute(
							SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
					if (attr == null) {
						throw new RuntimeException("SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] + ","
								+ SLAVE_CHAR_COLS[i]);
					}
					slaveValues.setValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j], attr.getEmptyValue());
				}
			}
		}
	}
	
	private boolean prefillSuccess(String columnName) {
		if (this.submission == null) {
			return false;
		}
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		SourceInformationLookup sourceInformationUtils =
			SourceInformationLookup.createSourceInformationUtils(session);
		
		try {
			Submission submission = Submission.loadById(session, this.submission.getId());
			EditedVoyage storedEditedVoyage = null;
			if (submission instanceof SubmissionEdit) {
				storedEditedVoyage = ((SubmissionEdit) submission).getNewVoyage();
			} else if (submission instanceof SubmissionMerge) {
				storedEditedVoyage = ((SubmissionMerge) submission).getProposedVoyage();
			} else if (submission instanceof SubmissionNew) {
				storedEditedVoyage = ((SubmissionNew) submission).getNewVoyage();
			}
			if (storedEditedVoyage == null && storedEditedVoyage.getVoyage() != null) {
				return false;
			}
			Voyage voyage = storedEditedVoyage.getVoyage();
			Map attributeNotes = storedEditedVoyage.getAttributeNotes();
			for (int i = 0; i < attrs.length; i++) {
				SubmissionAttribute attribute = attrs[i];
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++) {
					toBeFormatted[j] = voyage.getAttrValue(attribute.getAttribute()[j].getName());
				}
				Value value = attribute.getValue(session, toBeFormatted, sourceInformationUtils);
				value.setNote((String) attributeNotes.get(attrs[i].getName()));
				gridValues.setValue(columnName, attribute.getName(), value);
			}
			
			for (int i = 0; i < SLAVE_CHAR_COLS.length; i++) {
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
					SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(
							SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
					if (attribute == null) {
						throw new RuntimeException("SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] + ","
								+ SLAVE_CHAR_COLS[i]);
					}
					Object[] toBeFormatted = new Object[attribute.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++) {
						toBeFormatted[k] = voyage.getAttrValue(attribute.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted, sourceInformationUtils);
					value.setNote((String) attributeNotes.get(attribute.getName()));
					slaveValues.setValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j], value);
				}
			}
		} finally {
			t.commit();
			session.close();
		}
		return true;
	}
	
	private boolean loadVoyageToColumn(Session session, int voyageId, Values values, String columnName, SourceInformationLookup sourceInformationUtils)
	{
		
		TastDbConditions cond = new TastDbConditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), new Integer(voyageId), TastDbConditions.OP_EQUALS);
		cond.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", cond);

		for (int i = 0; i < attrs.length; i++)
		{
			Attribute[] attributes = attrs[i].getAttribute();
			for (int j = 0; j < attributes.length; j++)
			{
				qValue.addPopulatedAttribute(attributes[j]);
			}
		}
		Object[] res = qValue.executeQuery(session);
		if (res.length == 0) return false;

		Object[] voyageAttrs = (Object[]) res[0];
		int index = 0;
		for (int i = 0; i < attrs.length; i++)
		{
			SubmissionAttribute attribute = attrs[i];
			Object[] toBeFormatted = new Object[attribute.getAttribute().length];
			for (int j = 0; j < toBeFormatted.length; j++)
			{
				toBeFormatted[j] = voyageAttrs[j + index];
			}
			gridValues.setValue(columnName, attrs[i].getName(), attrs[i].getValue(session, toBeFormatted, sourceInformationUtils));
			index += attribute.getAttribute().length;
		}
		
		return true;
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// main submit
	/////////////////////////////////////////////////////////////////////////////////
	
	public String submit()
	{
		this.sourcesBean.submit();
		
		lookupPerformed = false;
		lookedUpVoyage = null;
		selectedVoyagesForMerge.clear();
		
		return "done";

	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// lookup (used in edit and merge)
	/////////////////////////////////////////////////////////////////////////////////
	
	public String lookupVoyage()
	{
		
		lookupPerformed = true;
		lookedUpVoyage = null;
		errorSelectAtLeastTwo = false;
		
		if (lookupVoyageId == null)
			return null;
		
		Session sess = HibernateConn.getSession();
		Transaction trans = sess.beginTransaction();
		
		TastDbConditions cond = new TastDbConditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), lookupVoyageId, TastDbConditions.OP_EQUALS);
		
		TastDbQuery query = new TastDbQuery("Voyage", cond);
		query.setLimit(1);
		
		query.addPopulatedAttribute(Voyage.getAttribute("shipname"));
		query.addPopulatedAttribute(Voyage.getAttribute("captaina"));
		query.addPopulatedAttribute(Voyage.getAttribute("yearam"));
		
		List voyages = query.executeQueryList(sess);
		
		if (voyages.size() != 0)
		{
			Object[] voyageInfoDb = (Object[]) voyages.get(0);
			lookedUpVoyage = new SelectedVoyageInfo(
					lookupVoyageId.intValue(),
					(String) voyageInfoDb[1],
					(String) voyageInfoDb[0],
					voyageInfoDb[2] != null ? voyageInfoDb[2].toString() : "not known");
		}
		
		trans.commit();
		sess.close();
		
		return null;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// for phase listener
	/////////////////////////////////////////////////////////////////////////////////

	public boolean isStateValidForSelectVoyageForEdit()
	{
		return submissionType == SUBMISSION_TYPE_EDIT;
	}

	public boolean isStateValidForSelectVoyagesForMerge()
	{
		return submissionType == SUBMISSION_TYPE_MERGE;
	}

	public boolean isStateValidForForm()
	{
		return submissionType != SUBMISSION_TYPE_NOT_SELECTED && gridValues != null;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// getters and setters
	/////////////////////////////////////////////////////////////////////////////////

	public Values getValues()
	{
		return gridValues;
	}
	
	public void setValues(Values vals)
	{
		this.gridValues = vals;
	}

	public Column[] getColumns()
	{
		if (submissionType == SUBMISSION_TYPE_NEW)
		{
			
			return new Column[] {
					new Column(
							CHANGED_VOYAGE,
							CHANGED_VOYAGE_LABEL,
							false)};

		}
		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			
			return new Column[] {

					new Column(
							ORIGINAL_VOYAGE,
							ORIGINAL_VOYAGE_LABEL,
							true,
							CHANGED_VOYAGE,
							"Copy >"),
					
					new Column(
							CHANGED_VOYAGE,
							CHANGED_VOYAGE_LABEL,
							false,
							true)};

		}
		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{
			
			Column columns[] = new Column[selectedVoyagesForMerge.size() + 1];
			
			for (int i = 0; i < selectedVoyagesForMerge.size(); i++) {
				SelectedVoyageInfo info = (SelectedVoyageInfo) selectedVoyagesForMerge.get(i);
				columns[i] = new Column(
						MERGED_VOYAGE_PREFIX + i,
						"VoyageID " + String.valueOf(info.getVoyageId()),
						true,
						CHANGED_VOYAGE,
						"Copy >");
			}
			
			columns[selectedVoyagesForMerge.size()] = new Column(
					CHANGED_VOYAGE,
					CHANGED_VOYAGE_LABEL,
					false);
			
			return columns;

		}
		else
		{
			JsfUtils.navigateTo("start");
			return null;
		}
	}

	public Row[] getRows()
	{
		
		Row[] rows = new Row[attrs.length];
		
		for (int i = 0; i < rows.length; i++)
		{
			
			Row row = new Row(
					attrs[i].getType(),
					attrs[i].getName(),
					attrs[i].getUserLabel(),
					attrs[i].getComment(),
					attrs[i].getGroupName(),
					false);
			
			row.setNoteEnabled(true);
			rows[i] = row;

		}
		
		return rows;
	}
	
	public RowGroup[] getRowGroups() {
		return this.rowGroups;
	}

	public Map getFieldTypes()
	{
		return SubmissionDictionaries.fieldTypes;
	}

	public Integer getLookupVoyageId()
	{
		return lookupVoyageId;
	}

	public void setLookupVoyageId(Integer voyageId)
	{
		this.lookupVoyageId = voyageId;
	}

	public boolean isShowLookedUpVoyage()
	{
		return lookupPerformed && lookedUpVoyage != null;
	}

	public boolean isShowLookupFailed()
	{
		return lookupPerformed && lookedUpVoyage == null;
	}

	public SelectedVoyageInfo getLookedUpVoyage()
	{
		return lookedUpVoyage;
	}

	public List getSelectedVoyagesForMerge()
	{
		return selectedVoyagesForMerge;
	}

	public boolean isSomeSelectedVoyagesForMerge()
	{
		return selectedVoyagesForMerge != null && selectedVoyagesForMerge.size() != 0;
	}

	public boolean isAtLeastTwoSelectedVoyagesForMerge()
	{
		return selectedVoyagesForMerge != null && selectedVoyagesForMerge.size() > 1;
	}

	public long getToRemovedVoyageId()
	{
		return toRemovedVoyageId;
	}

	public void setToRemovedVoyageId(long removedVoyageId)
	{
		toRemovedVoyageId = removedVoyageId;
	}

	public boolean isErrorSelectAtLeastTwo()
	{
		return errorSelectAtLeastTwo;
	}

	public Set getExpandedGridRows()
	{
		return expandedGridRows;
	}

	public void setExpandedGridRows(Set expandedGridRows)
	{
		this.expandedGridRows = expandedGridRows;
	}

	public User getAuthenticatedUser() {
		return authenticatedUser;
	}

	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
	
	public String logout() {
		this.authenticatedUser = null;
		return "logout";
	}
	
	public String submitNext() {
		return "new-submission";
	}
	
	private Submission createSubmission(String phase) {
		Session sess = HibernateConn.getSession();
		Transaction trans = sess.beginTransaction();
		Submission submission = null;
		
		try {
			Map newValues = gridValues.getColumnValues(CHANGED_VOYAGE);
			Voyage voyage = new Voyage();

			if (submissionType == SUBMISSION_TYPE_NEW) {
				voyage.setVoyageid(null);
			}

			else if (submissionType == SUBMISSION_TYPE_EDIT) {
				voyage.setVoyageid(new Integer(selectedVoyageForEdit.getVoyageId()));
			}

			else if (submissionType == SUBMISSION_TYPE_MERGE) {
				voyage.setVoyageid(null);
			}

			voyage.setSuggestion(true);
			voyage.setRevision(-1);
			// voyage.setApproved(false);

			Map notes = new HashMap();

			// boolean wasError = false;
			for (int i = 0; i < attrs.length; i++) {
				Value val = (Value) newValues.get(attrs[i].getName());
				if (!val.isCorrectValue()) {
					val.setErrorMessage("Error in value!");
					// wasError = true;
				}
				if (val.hasEditableNote()) {
					notes.put(attrs[i].getName(), val.getNote().trim());
				}
				
				Object[] vals = attrs[i].getValues(sess, val);
				for (int j = 0; j < vals.length; j++) {
					voyage.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);			
				}				
			}
			
			for (int i = 0; i < SLAVE_CHAR_COLS.length; i++) {
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
					SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(
							SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
					if (attribute == null) {
						throw new RuntimeException("SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] + ","
								+ SLAVE_CHAR_COLS[i]);
					}
					Value value = slaveValues.getValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j]);
					if (value.hasEditableNote()) {
						notes.put(attribute.getName(), value.getNote().trim());
					}
					Object[] vals = attribute.getValues(sess, value);
					for (int k = 0; k < vals.length; k++) {
						voyage.setAttrValue(attribute.getAttribute()[k].getName(), vals[k]);
					}
				}
			}

			if (!doErrorChecking()) {
				return null;
			}
			
			imputeVariables(voyage);
			
			sess.save(voyage);
			if (submissionType == SUBMISSION_TYPE_NEW) {

				SubmissionNew submissionNew = new SubmissionNew();
				submission = submissionNew;

				EditedVoyage eVoyage = new EditedVoyage(voyage, notes);
				sess.save(eVoyage);

				submissionNew.setNewVoyage(eVoyage);

			}

			else if (submissionType == SUBMISSION_TYPE_EDIT) {

				SubmissionEdit submissionEdit = new SubmissionEdit();
				submission = submissionEdit;

				EditedVoyage eNewVoyage = new EditedVoyage(voyage, notes);
				EditedVoyage eOldVoyage = new EditedVoyage(Voyage.loadCurrentRevision(sess, lookupVoyageId), null);
				sess.save(eOldVoyage);
				sess.save(eNewVoyage);

				submissionEdit.setNewVoyage(eNewVoyage);
				submissionEdit.setOldVoyage(eOldVoyage);

			}

			else if (submissionType == SUBMISSION_TYPE_MERGE) {

				SubmissionMerge submissionMerge = new SubmissionMerge();
				submission = submissionMerge;

				EditedVoyage eNewVoyage = new EditedVoyage(voyage, notes);
				sess.save(eNewVoyage);

				submissionMerge.setProposedVoyage(eNewVoyage);

				Set mergedVoyages = new HashSet();
				submissionMerge.setMergedVoyages(mergedVoyages);
				for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();) {
					SelectedVoyageInfo voyageInfo = (SelectedVoyageInfo) iter.next();
					EditedVoyage eVoyage = new EditedVoyage(
							(Voyage.loadCurrentRevision(sess, voyageInfo.getVoyageId())), null);
					mergedVoyages.add(eVoyage);
					sess.save(eVoyage);
				}

			}

			submission.setUser(this.authenticatedUser);
			submission.setTime(new Date());
			submission.setSavedState(phase);

			sess.save(submission);
			this.cleanSubmission(sess);

		} finally {
			trans.commit();
			sess.close();
		}
		return submission;
	}
	
	public String toSources() {
		if (!doErrorChecking()) {
			return null;
		}
		if (this.submission == null) {
			this.submission = this.createSubmission(SOURCES_STATE);
			if (this.submission == null) {
				return null;
			}
			this.sourcesBean.setSubmission(this.submission);
		}
		return "sources";
	}

	private boolean doErrorChecking() {
		boolean wasError = false;
		Map newValues = gridValues.getColumnValues(CHANGED_VOYAGE);
		for (Iterator iter = newValues.values().iterator(); iter.hasNext();)
		{
			Value val = (Value) iter.next();

			if (!val.isCorrectValue()) {
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
		}
		for (Iterator iter = slaveValues.getValues().iterator(); iter.hasNext();) {
			Value val = (Value) iter.next();
			if (!val.isCorrectValue()) {
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
		}
		
		if (wasError) {
			return false;
		}

		return true;
	}

	public String bookSource() {
		return null;
	}

	public SourcesBean getSourcesBean() {
		return sourcesBean;
	}

	public void setSourcesBean(SourcesBean sourcesBean) {
		this.sourcesBean = sourcesBean;
	}
	
	public String saveStateSubmission() {
		if (this.submission == null) {
			if (this.createSubmission(SUBMISSION_STATE) == null) {
				return null;
			}
		} else {
			if (!this.updateSubmission(SUBMISSION_STATE)) {
				return null;
			}
		}
		this.authenticatedUser = null;
		return null;
	}
	
	public String logoutOnly() {
		this.authenticatedUser = null;
		return null;
	}
	
	public String saveStateSources() {
		if (this.submission == null) {
			this.createSubmission(SOURCES_STATE);
		} else {
			if (!this.updateSubmission(SOURCES_STATE)) {
				return null;
			}
		}
		this.authenticatedUser = null;
		return null;
	}

	
	private boolean updateSubmission(String phase) {
		Session sess = HibernateConn.getSession();
		Transaction trans = sess.beginTransaction();

		try {

			Submission submission = Submission.loadById(sess, this.submission.getId());
			EditedVoyage storedEditedVoyage = null;
			if (submission instanceof SubmissionEdit) {
				storedEditedVoyage = ((SubmissionEdit) submission).getNewVoyage();
			} else if (submission instanceof SubmissionMerge) {
				storedEditedVoyage = ((SubmissionMerge) submission).getProposedVoyage();
			} else if (submission instanceof SubmissionNew) {
				storedEditedVoyage = ((SubmissionNew) submission).getNewVoyage();
			}
			if (storedEditedVoyage == null && storedEditedVoyage.getVoyage() != null) {
				return false;
			}
			Voyage voyage = storedEditedVoyage.getVoyage();
			
			Map newValues = gridValues.getColumnValues(CHANGED_VOYAGE);

			Map notes = new HashMap();

			// boolean wasError = false;
			for (int i = 0; i < attrs.length; i++) {
				Value val = (Value) newValues.get(attrs[i].getName());
				if (!val.isCorrectValue()) {
					val.setErrorMessage("Error in value!");
				}
				if (val.hasEditableNote()) {
					notes.put(attrs[i].getName(), val.getNote().trim());
				}
				Object[] vals = attrs[i].getValues(sess, val);
				for (int j = 0; j < vals.length; j++) {
					voyage.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
				}
			}
			
			for (int i = 0; i < SLAVE_CHAR_COLS.length; i++) {
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++) {
					SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(
							SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
					Map columnValues = slaveValues.getColumnValues(SLAVE_CHAR_COLS[i]);
					Value val = (Value) columnValues.get(SLAVE_CHAR_ROWS[j]);
					if (!val.isCorrectValue()) {
						val.setErrorMessage("Error in value!");
					}
					if (val.hasEditableNote()) {
						notes.put(attribute.getName(), val.getNote().trim());
					}
					Object[] vals = attribute.getValues(sess, val);
					for (int k = 0; k < vals.length; k++) {
						voyage.setAttrValue(attribute.getAttribute()[k].getName(), vals[k]);
					}
				}
			}

			if (!doErrorChecking()) {
				return false;
			}
			
			imputeVariables(voyage);
			
			submission.setSavedState(phase);
			storedEditedVoyage.setAttributeNotes(notes);
			sess.update(voyage);
			sess.update(storedEditedVoyage);
			sess.update(submission);
			
		} finally {
			trans.commit();
			sess.close();
		}
		return true;
	}
	 
	private void imputeVariables(Voyage voyage) {
		VoyagesCalculation voyagesCalc = new VoyagesCalculation(voyage);
		voyage = voyagesCalc.calculateImputedVariables();
	}
	
	public void setSubmission(Submission submission) {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
		this.submission = submission;
		this.sourcesBean.setSubmission(submission);
		if (this.submission instanceof SubmissionNew) {
			this.submissionType = SUBMISSION_TYPE_NEW;
			this.initNewVoyage();
		} else if (this.submission instanceof SubmissionEdit) {
			this.submissionType = SUBMISSION_TYPE_EDIT;
			SubmissionEdit s = (SubmissionEdit) Submission.loadById(session, this.submission.getId());
			Voyage v = Voyage.loadCurrentRevision(session, s.getOldVoyage().getVoyage().getVoyageid());
			if (v != null) {
				lookedUpVoyage = new SelectedVoyageInfo(
						v.getVoyageid().intValue(),
						v.getShipname(),
						v.getCaptaina(),
						v.getYearam() != null ? v.getYearam().toString() : "not known");
				selectedVoyageForEdit = lookedUpVoyage;
			}
			this.loadVoyageForEdit();
		} else if (this.submission instanceof SubmissionMerge) {
			this.submissionType = SUBMISSION_TYPE_MERGE;			
			selectedVoyagesForMerge = new ArrayList();
			SubmissionMerge s = (SubmissionMerge) Submission.loadById(session, this.submission.getId());
			Set voyages = s.getMergedVoyages();
			for (Iterator iter = voyages.iterator(); iter.hasNext();) {
				EditedVoyage element = (EditedVoyage) iter.next();
				Voyage v = element.getVoyage();
				selectedVoyagesForMerge.add(new SelectedVoyageInfo(
							v.getVoyageid().intValue(),
							v.getShipname(),
							v.getCaptaina(),
							v.getYearam() != null ? v.getYearam().toString() : "not known"));
			}
			this.loadVoyagesForMerge();
		}
		} finally {
			t.commit();
			session.close();
		}
		this.getColumns();
	}
	
	public Row[] getRowsSlave() {
		Row[] rows = null;
		int groupsNumber;
		if (this.submissionType == SUBMISSION_TYPE_EDIT) {
			groupsNumber = 2;
			rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
			for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
				rows[i] = new Row(TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i] + "_old", SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics-old", true, SLAVE_CHAR_ROWS[i], "Copy");
			}
		} else if (this.submissionType == SUBMISSION_TYPE_MERGE) {
			groupsNumber = selectedVoyagesForMerge.size() + 1;
			rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
			int j = 0;
			for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();) {
				SelectedVoyageInfo element = (SelectedVoyageInfo) iter.next();
				for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
					rows[j++] = new Row(TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i] + "_" + element.getVoyageId(), SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics-" + element.getVoyageId(), true, SLAVE_CHAR_ROWS[i], "Copy");
				}
			}
		} else {
			groupsNumber = 1;
			rows = new Row[SLAVE_CHAR_ROWS.length];
		}
		for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
			rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length] = new Row(TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i], SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics", true);
			rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length].setNoteEnabled(true);
		} 
		return rows;
	}
	
	public Column[] getColumnsSlave() {
		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}
	
	public void setValuesSlave(Values values) {
		this.slaveValues = values;
	}
	
	public Values getValuesSlave() {
		return slaveValues;
	}
	
	public RowGroup[] getRowGroupsSlave() {
		if (this.submissionType == SUBMISSION_TYPE_NEW) {
			return new RowGroup[] {new RowGroup("characteristics", "Slaves (characteristics)")};
		} else if (this.submissionType == SUBMISSION_TYPE_MERGE) {
			RowGroup[] groups = new RowGroup[selectedVoyagesForMerge.size() + 1];
			for (int i = 0; i < groups.length - 1; i++) {
				SelectedVoyageInfo info = (SelectedVoyageInfo) selectedVoyagesForMerge.get(i);
				groups[i] = new RowGroup("characteristics-" + info.getVoyageId(), "Slaves (characteristics) [VoyageID: " + info.getVoyageId() + "]");
			}
			groups[groups.length - 1] = new RowGroup("characteristics", "Slaves (characteristics) [New values]");
			return groups;
		} else {
			RowGroup[] groups = new RowGroup[2];
			groups[0] = new RowGroup("characteristics-old", "Slaves (characteristics) [Current voyage]");
			groups[1] = new RowGroup("characteristics", "Slaves (characteristics) [Edited information]");
			return groups;
		}
	}
	
	public Set getExpandedGridRowsSlave() {
		return new HashSet();
	}
	
	public void setExpandedGridRowsSlave(Set set) {
	}
	
	public Map getFieldTypesSlave() {
		return SubmissionDictionaries.simpleFieldTypes;
	}
	
	public SubmissionVerifyBean getVerifyBean() {
		return verifyBean;
	}

	public void setVerifyBean(SubmissionVerifyBean verifyBean) {
		this.verifyBean = verifyBean;
		this.verifyBean.setSubmissionBean(this);
	}
		
	
}