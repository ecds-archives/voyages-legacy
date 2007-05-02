package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SubmissionBean
{

	public static final String ORIGINAL_VOYAGE_LABEL = TastResource.getText("submissions_oryginal_voyage");
	public static final String CHANGED_VOYAGE_LABEL = TastResource.getText("submissions_changed_voyage");
	public static final String ORIGINAL_VOYAGE = "old";
	public static final String CHANGED_VOYAGE = "new";
	public static final String LOCATIONS = "locations";	
	public static final String PORTS = "ports";	

	public static final int SUBMISSION_TYPE_NOT_SELECTED = 0;	
	public static final int SUBMISSION_TYPE_NEW = 1;	
	public static final int SUBMISSION_TYPE_EDIT = 2;	
	public static final int SUBMISSION_TYPE_MERGE = 3;	

	private int submissionType = SUBMISSION_TYPE_NOT_SELECTED;

	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getPublicAttributes();
	
	private long[] mergedVoyageIds = new long[] {};
	
	private Values valsToSubmit = null;
	private Values gridValues = null;
	
	private RowGroup[] rowGroups;

	private long lookupVoyageId = 0;
	private boolean lookupPerformed = false;
	private SelectedVoyageInfo lookedUpVoyage = null;

	private SelectedVoyageInfo selectedVoyageForEdit = null;
	private List selectedVoyageForMerge = new ArrayList();

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
	
	/*
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
	*/
	
	public String selectTypeNew()
	{
		if (submissionType != SUBMISSION_TYPE_NEW)
		{
			submissionType = SUBMISSION_TYPE_NEW;
			initNewVoyage();
		}
		return "form";
	}
	
	public String selectTypeEdit()
	{
		if (submissionType != SUBMISSION_TYPE_EDIT)
		{
			submissionType = SUBMISSION_TYPE_EDIT;
			lookupPerformed = false;
		}
		else
		{
			if (selectedVoyageForEdit != null)
			{
				lookedUpVoyage = selectedVoyageForEdit;
				lookupVoyageId = selectedVoyageForEdit.getVoyageId();
			}
		}
		lookupPerformed = true;
		return "select-voyage";
	}

	public String selectTypeMerge()
	{
		lookupPerformed = false;
		lookedUpVoyage = null;
		if (submissionType != SUBMISSION_TYPE_MERGE)
		{
			submissionType = SUBMISSION_TYPE_MERGE;
		}
		return null;
	}
	
	public String editVoyage()
	{
		selectedVoyageForEdit = lookedUpVoyage;
		loadVoyageForEdit();
		return "form";
	}
	
	public String goBackFromSelectVoyage()
	{
		return "back";
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
		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			return "back-merge";
		}
		else
		{
			throw new RuntimeException("unspecified submission type, session probably expired");
		}
	}

	private void initNewVoyage()
	{
		gridValues = new Values();
		initColumnForNewVoyaye(gridValues, CHANGED_VOYAGE);
	}

	private boolean loadVoyageForEdit()
	{
		
		gridValues = new Values();

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		
		loadVoyageToColumn(session, selectedVoyageForEdit.getVoyageId(), gridValues, ORIGINAL_VOYAGE);
		initColumnForNewVoyaye(gridValues, CHANGED_VOYAGE);
		
		trans.commit();
		session.close();
		
		return true;
		
	}
	
//	private boolean loadVoyagesForMerge()
//	{
//		
//		return false;
//
//	}
	
	private void initColumnForNewVoyaye(Values values, String columnName)
	{
		for (int i = 0; i < attrs.length; i++)
			values.setValue(
					columnName,
					attrs[i].getName(),
					attrs[i].getEmptyValue());
	}
	
	private boolean loadVoyageToColumn(Session session, long voyageId, Values values, String columnName)
	{
		
		Conditions cond = new Conditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId), Conditions.OP_EQUALS);
		cond.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", cond);

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
			gridValues.setValue(columnName, attrs[i].getName(), attrs[i].getValue(toBeFormatted));
			index += attribute.getAttribute().length;
		}
		
		return true;
		
	}
	
	public String submit()
	{

		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();

		Map newValues = valsToSubmit.getColumnValues(CHANGED_VOYAGE);
		Voyage voyage = new Voyage();
		
		if (submissionType == SUBMISSION_TYPE_NEW)
		{
			voyage.setVoyageid(null);
		}

		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			voyage.setVoyageid(new Long(selectedVoyageForEdit.getVoyageId()));
		}

		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{
			voyage.setVoyageid(null);
		}
		
		voyage.setSuggestion(true);
		voyage.setRevision(Voyage.getCurrentRevision());
		voyage.setApproved(false);

		// boolean wasError = false;
		for (int i = 0; i < attrs.length; i++) {
			Value val = (Value) newValues.get(attrs[i].getName());
			if (val.isError()) {
				val.setErrorMessage("Error in value!");
				// wasError = true;
			}
			Object[] vals = attrs[i].getValues(sess, val);
			for (int j = 0; j < vals.length; j++)
			{
				voyage.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
			}
		}

		Submission submission = null;
		
		if (submissionType == SUBMISSION_TYPE_NEW)
		{

			SubmissionNew submissionNew = new SubmissionNew();
			submission = submissionNew;

			submissionNew.setNewVoyage(voyage);

		}

		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{

			SubmissionEdit submissionEdit = new SubmissionEdit();
			submission = submissionEdit;

			submissionEdit.setNewVoyage(voyage);
			submissionEdit.setOldVoyage(Voyage.loadCurrentRevision(sess, lookupVoyageId));

		}

		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{

			SubmissionMerge submissionMerge = new SubmissionMerge();
			submission = submissionMerge;

			submissionMerge.setProposedNewVoyage(voyage);

			Set mergedVoyages = new HashSet();
			submissionMerge.setMergedVoyages(mergedVoyages);
			for (int i = 0; i < mergedVoyageIds.length; i++)
				mergedVoyages.add((Voyage.loadCurrentRevision(sess, mergedVoyageIds[i])));

		}

		submission.setTime(new Date());

		sess.save(voyage);
		sess.save(submission);
		
		trans.commit();
		sess.close();

		return null;

	}
	
	private SelectedVoyageInfo lookupVoyage(long voyageId)
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();
		
		Conditions cond = new Conditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId), Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("Voyage", cond);
		query.setLimit(1);

		query.addPopulatedAttribute(Voyage.getAttribute("shipname"));
		query.addPopulatedAttribute(Voyage.getAttribute("captaina"));
		query.addPopulatedAttribute(Voyage.getAttribute("yearam"));
		
		List voyages = query.executeQueryList(sess);

		SelectedVoyageInfo voyageInfo = null;
		if (voyages.size() != 0)
		{
			Object[] voyageInfoDb = (Object[]) voyages.get(0);
			voyageInfo = new SelectedVoyageInfo(
					lookupVoyageId,
					(String) voyageInfoDb[0],
					(String) voyageInfoDb[1],
					voyageInfoDb[2] != null ? voyageInfoDb[2].toString() : "not known");
		}
		
		trans.commit();
		sess.close();
		
		return voyageInfo;
		
	}
	
	public String lookupVoyageForEdit()
	{
		lookupPerformed = true;
		lookedUpVoyage = lookupVoyage(lookupVoyageId);
		return null;
	}

	public String lookupVoyageForMerge()
	{
		lookupPerformed = true;
		lookedUpVoyage = lookupVoyage(lookupVoyageId);
		return null;
	}

	public Values getValues()
	{
		return gridValues;
	}
	
	public void setValues(Values vals)
	{
		this.valsToSubmit = vals;
	}

	public Column[] getColumns()
	{
		if (submissionType == SUBMISSION_TYPE_NEW)
		{
			return new Column[] {
					new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false)};
		}
		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			return new Column[] {
					new Column(ORIGINAL_VOYAGE, ORIGINAL_VOYAGE_LABEL, true),
					new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false)};
		}
		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{
			return new Column[] {
					new Column(ORIGINAL_VOYAGE, ORIGINAL_VOYAGE_LABEL, true),
					new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false)};
		}
		else
		{
			throw new RuntimeException("no submission type selected");
		}
	}

	public Row[] getRows()
	{
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i]
					.getUserLabel(), null, attrs[i].getGroupName());
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

	public long getLookupVoyageId()
	{
		return lookupVoyageId;
	}

	public void setLookupVoyageId(long voyageId)
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

	public List getSelectedVoyageForMerge()
	{
		return selectedVoyageForMerge;
	}

}