package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tas.util.HibernateConnector;
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

	private int currentSubmissionType = SUBMISSION_TYPE_NOT_SELECTED;
	private int selectedSubmissionType = SUBMISSION_TYPE_NOT_SELECTED;
	private Submission submission = null;

	private int submissionType = 0;

	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getPublicAttributes();
	private long voyageId = 0;
	private Values valsToSubmit = null;
	private boolean wasError = false;
	private Values gridValues = null;
	
	private RowGroup[] rowGroups;
	
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
		if (currentSubmissionType != SUBMISSION_TYPE_NEW)
		{
			currentSubmissionType = SUBMISSION_TYPE_NEW;
			submission = new SubmissionNew();
			initNewVoyage();
		}
		return "form";
	}
	
	public String selectTypeEdit()
	{
		if (currentSubmissionType != SUBMISSION_TYPE_EDIT)
		{
			currentSubmissionType = SUBMISSION_TYPE_EDIT;
			submission = new SubmissionEdit();
		}
		return "select-voyage";
	}

	public String selectTypeMerge()
	{
		if (currentSubmissionType != SUBMISSION_TYPE_MERGE)
		{
			currentSubmissionType = SUBMISSION_TYPE_MERGE;
			submission = new SubmissionMerge();
		}
		return null;
	}
	
	public String loadEdit()
	{
		loadCurrentVoyage();
		return "form";
	}
	
	private void initNewVoyage()
	{
		gridValues = new Values();
		for (int i = 0; i < attrs.length; i++)
			gridValues.setValue(
					CHANGED_VOYAGE,
					attrs[i].getName(),
					attrs[i].getEmptyValue());
	}
	
	private boolean loadCurrentVoyage()
	{
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		gridValues = new Values();

		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("voyageid"), new Long(voyageId), Conditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", c);

		for (int i = 0; i < attrs.length; i++) {
			Attribute[] attributes = attrs[i].getAttribute();
			for (int j = 0; j < attributes.length; j++) {
				qValue.addPopulatedAttribute(attributes[j]);
			}
		}
		Object[] res = qValue.executeQuery(session);
		if (res.length == 0) return false;

		Object[] voyageAttrs = (Object[]) res[0];
		int index = 0;
		for (int i = 0; i < attrs.length; i++) {
			SubmissionAttribute attribute = attrs[i];
			System.out.println("Attr: " + attrs[i]);
			Object[] toBeFormatted = new Object[attribute.getAttribute().length];
			for (int j = 0; j < toBeFormatted.length; j++) {
				toBeFormatted[j] = voyageAttrs[j + index];
			}
			gridValues.setValue(ORIGINAL_VOYAGE, attrs[i].getName(), attrs[i].getValue(toBeFormatted));
			gridValues.setValue(CHANGED_VOYAGE, attrs[i].getName(), attrs[i].getEmptyValue());
			index += attribute.getAttribute().length;
		}
		
		t.commit();
		session.close();
		
		return true;
		
	}
	
	public String submitVoyage()
	{

		Session sess = HibernateUtil.getSession();
		Transaction t = sess.beginTransaction();

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
			Object[] vals = attrs[i].getValues(sess, val);
			for (int j = 0; j < vals.length; j++)
			{
				vNew.setAttrValue(attrs[i].getAttribute()[j].getName(), vals[j]);
			}
		}


		Submission submission = null;
		if (submissionType == SUBMISSION_TYPE_NEW)
		{
			SubmissionNew submissionNew = new SubmissionNew();
			submissionNew.setNewVoyage(vNew);
			submission = submissionNew;
		}
		else if (submissionType == SUBMISSION_TYPE_EDIT)
		{
			SubmissionEdit submissionEdit = new SubmissionEdit();
			submissionEdit.setNewVoyage(vNew);
			submissionEdit.setOldVoyage(Voyage.loadCurrentRevision(sess, voyageId));
			submission = submissionEdit;
		}		
		submission.setTime(new Date());

		sess.save(vNew);
		sess.save(submission);
		
		t.commit();
		sess.close();

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
		if (currentSubmissionType == SUBMISSION_TYPE_NEW)
		{
			return new Column[] {
					new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false)};
		}
		else if (currentSubmissionType == SUBMISSION_TYPE_EDIT)
		{
			return new Column[] {
					new Column(ORIGINAL_VOYAGE, ORIGINAL_VOYAGE_LABEL, true),
					new Column(CHANGED_VOYAGE, CHANGED_VOYAGE_LABEL, false)};
		}
		else if (currentSubmissionType == SUBMISSION_TYPE_MERGE)
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

	public boolean isSubmissionTypeEdit()
	{
		return selectedSubmissionType == SUBMISSION_TYPE_EDIT;
	}

	public void setSubmissionTypeEdit(boolean submissionTypeEdit)
	{
		selectedSubmissionType = SUBMISSION_TYPE_EDIT;
	}

	public boolean isSubmissionTypeMerge()
	{
		return selectedSubmissionType == SUBMISSION_TYPE_MERGE;
	}

	public void setSubmissionTypeMerge(boolean submissionTypeMerge)
	{
		selectedSubmissionType = SUBMISSION_TYPE_MERGE;
	}

	public boolean isSubmissionTypeNew()
	{
		return selectedSubmissionType == SUBMISSION_TYPE_MERGE;
	}

	public void setSubmissionTypeNew(boolean submissionTypeNew)
	{
		selectedSubmissionType = SUBMISSION_TYPE_NEW;
	}

	public long getVoyageId()
	{
		return voyageId;
	}

	public void setVoyageId(long voyageId)
	{
		this.voyageId = voyageId;
	}

}