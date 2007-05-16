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
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

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

	private int submissionType = SUBMISSION_TYPE_NOT_SELECTED;

	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getPublicAttributes();
	
	private Values gridValues = null;
	
	private RowGroup[] rowGroups;

	private Long lookupVoyageId = null;
	private boolean lookupPerformed = false;
	private SelectedVoyageInfo lookedUpVoyage = null;

	private SelectedVoyageInfo selectedVoyageForEdit = null;
	private List selectedVoyagesForMerge = new ArrayList();
	
	private long toRemovedVoyageId;
	
	private boolean errorSelectAtLeastTwo = false;
	
	private Set expandedGridRows;

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
	
	public String selectTypeNew()
	{
		if (submissionType != SUBMISSION_TYPE_NEW)
		{
			submissionType = SUBMISSION_TYPE_NEW;
			expandedGridRows = null;
			initNewVoyage();
		}
		return "form";
	}
	
	public String selectTypeEdit()
	{
		if (submissionType != SUBMISSION_TYPE_EDIT)
		{
			submissionType = SUBMISSION_TYPE_EDIT;
			lookupVoyageId = null;
			lookupPerformed = false;
			expandedGridRows = null;
		}
		else
		{
			if (selectedVoyageForEdit != null)
			{
				lookedUpVoyage = selectedVoyageForEdit;
				lookupVoyageId = new Long(selectedVoyageForEdit.getVoyageId());
			}
		}
		return "edit";
	}

	public String selectTypeMerge()
	{
		lookupPerformed = false;
		lookedUpVoyage = null;
		errorSelectAtLeastTwo = false;
		if (submissionType != SUBMISSION_TYPE_MERGE)
		{
			submissionType = SUBMISSION_TYPE_MERGE;
			expandedGridRows = null;
		}
		return "merge";
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
	
	private boolean loadVoyagesForMerge()
	{
		
		gridValues = new Values();

		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();

		int i = 0;
		for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();)
		{
			SelectedVoyageInfo voyage = (SelectedVoyageInfo) iter.next();
			loadVoyageToColumn(session, voyage.getVoyageId(), gridValues, MERGED_VOYAGE_PREFIX + i);
			i++;
		}

		initColumnForNewVoyaye(gridValues, CHANGED_VOYAGE);

		trans.commit();
		session.close();

		return true;

	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// universal methods for loading data into a grid
	/////////////////////////////////////////////////////////////////////////////////
	
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
	
	/////////////////////////////////////////////////////////////////////////////////
	// main submit
	/////////////////////////////////////////////////////////////////////////////////
	
	public String submit()
	{

		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();

		Map newValues = gridValues.getColumnValues(CHANGED_VOYAGE);
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
		voyage.setRevision(-1);
		//voyage.setApproved(false);
		
		Map notes = new HashMap(); 

		// boolean wasError = false;
		for (int i = 0; i < attrs.length; i++) {
			Value val = (Value) newValues.get(attrs[i].getName());
			if (val.isError()) {
				val.setErrorMessage("Error in value!");
				// wasError = true;
			}
			if (val.hasNote())
			{
				notes.put(attrs[i].getName(), val.getNote().trim());
			}
			Object[] vals = attrs[i].getValues(sess, val);
			for (int j = 0; j < vals.length; j++)
			{
				voyage.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
			}
		}
		Submission submission = null;
		
		sess.save(voyage);
		if (submissionType == SUBMISSION_TYPE_NEW)
		{

			SubmissionNew submissionNew = new SubmissionNew();
			submission = submissionNew;

			EditedVoyage eVoyage = new EditedVoyage(voyage, notes);
			sess.save(eVoyage);
			
			submissionNew.setNewVoyage(eVoyage);

		}

		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{

			SubmissionEdit submissionEdit = new SubmissionEdit();
			submission = submissionEdit;

			EditedVoyage eNewVoyage = new EditedVoyage(voyage, notes);
			EditedVoyage eOldVoyage = new EditedVoyage(Voyage.loadCurrentRevision(sess, lookupVoyageId), null);
			sess.save(eOldVoyage);
			sess.save(eNewVoyage);
			
			submissionEdit.setNewVoyage(eNewVoyage);
			submissionEdit.setOldVoyage(eOldVoyage);

		}

		else if (submissionType == SUBMISSION_TYPE_MERGE)
		{

			SubmissionMerge submissionMerge = new SubmissionMerge();
			submission = submissionMerge;

			EditedVoyage eNewVoyage = new EditedVoyage(voyage, notes);
			sess.save(eNewVoyage);
			
			submissionMerge.setProposedVoyage(eNewVoyage);

			Set mergedVoyages = new HashSet();
			submissionMerge.setMergedVoyages(mergedVoyages);
			for (Iterator iter = selectedVoyagesForMerge.iterator(); iter.hasNext();)
			{
				SelectedVoyageInfo voyageInfo = (SelectedVoyageInfo) iter.next();
				EditedVoyage eVoyage = new EditedVoyage((Voyage.loadCurrentRevision(sess, voyageInfo.getVoyageId())), null);
				mergedVoyages.add(eVoyage);
				sess.save(eVoyage);				
			}
			

		}
		
		submission.setTime(new Date());
		
		sess.save(submission);
		
		trans.commit();
		sess.close();
		
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
		
		Session sess = HibernateUtil.getSession();
		Transaction trans = sess.beginTransaction();
		
		Conditions cond = new Conditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), lookupVoyageId, Conditions.OP_EQUALS);
		
		QueryValue query = new QueryValue("Voyage", cond);
		query.setLimit(1);
		
		query.addPopulatedAttribute(Voyage.getAttribute("shipname"));
		query.addPopulatedAttribute(Voyage.getAttribute("captaina"));
		query.addPopulatedAttribute(Voyage.getAttribute("yearam"));
		
		List voyages = query.executeQueryList(sess);
		
		if (voyages.size() != 0)
		{
			Object[] voyageInfoDb = (Object[]) voyages.get(0);
			lookedUpVoyage = new SelectedVoyageInfo(
					lookupVoyageId.longValue(),
					(String) voyageInfoDb[0],
					(String) voyageInfoDb[1],
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
			
			for (int i = 0; i < selectedVoyagesForMerge.size(); i++)
				columns[i] = new Column(
						MERGED_VOYAGE_PREFIX + i,
						ORIGINAL_VOYAGE_LABEL,
						true,
						CHANGED_VOYAGE,
						"Copy >");
			
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
					null,
					attrs[i].getGroupName());
			
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

	public Long getLookupVoyageId()
	{
		return lookupVoyageId;
	}

	public void setLookupVoyageId(Long voyageId)
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

}