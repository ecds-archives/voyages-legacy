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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.ColumnAction;
import edu.emory.library.tast.common.grideditor.ColumnActionEvent;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.common.grideditor.date.DateValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerValue;
import edu.emory.library.tast.database.SourceInformationLookup;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionEditor;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.util.StringUtils;

public class VoyagesApplier
{

	//Admin Use
	public static final String[] SLAVE_CHAR_COLS_A = {"men", "women", "boy", "girl", "male", "female", "adult", "child", "infant"};
	public static final String[] SLAVE_CHAR_COLS_LABELS_A = {"Men", "Women", "Boys", "Girls", "Males", "Females", "Adults", "Children", "Infants"};
	public static final String[] SLAVE_CHAR_ROWS_A = {"e1", "e2", "e3", "died", "d1", "d2", "i1", "i2", "i3", "i4"};
	public static final String[] SLAVE_CHAR_ROWS_LABELS_A = {
			"Embarked slaves (first port)",			
			"Embarked slaves (second port)",
			"Embarked slaves (third port)",
			"Died on voyage", 
			"Disembarked slaves (first port)",
			"Disembarked slaves (second port)",
			"Imputed number at ports of purchase*",
			"Imputed number at ports of landing*",
			"Imputed number at departure or arrival*",
			"Imputed deaths on middle passage*"
	};
	
	
	//Slave Age Sex Summary  grid
	public static final String[] SLAVE_CHAR_COLS_S = {"pur", "land", "doa"};
	public static final String[] SLAVE_CHAR_COLS_LABELS_S = {"At ports of purchase", "At ports of landing", "At departure or on arrival"};
	public static final String[] SLAVE_CHAR_ROWS_S = {"s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9",};
	public static final String[] SLAVE_CHAR_ROWS_LABELS_S = {
		"Total identified by age*",
		"Total identified by gender*",
		"Total identified by age and gender*",
		"Percentage of men*",
		"Percentage of women*",
		"Percentage of boys*",
		"Percentage of girls*",
		"Child ratio*",
		"Male ratio*"
	};
	
	
	//Editor Use
	public static final String[] SLAVE_CHAR_COLS_E = {"men", "women", "boy", "girl", "male", "female", "adult", "child", "infant"};
	public static final String[] SLAVE_CHAR_COLS_LABELS_E = {"Men", "Women", "Boys", "Girls", "Males", "Females", "Adults", "Children", "Infants"};
	public static final String[] SLAVE_CHAR_ROWS_E = {"e1", "e2", "e3", "died", "d1", "d2"};
	public static final String[] SLAVE_CHAR_ROWS_LABELS_E = {
			"Embarked slaves (first port)",			
			"Embarked slaves (second port)",
			"Embarked slaves (third port)",
			"Died on voyage", 
			"Disembarked slaves (first port)",
			"Disembarked slaves (second port)"
	};
			

	private static final String REMOVE_EDITOR_ACTION = "removeEditor";

	public static final String COPY_LABEL = "Copy";

	public static final String ORYGINAL_VOYAGE_LABEL = "Original voyage";

	public static final String CHANGED_VOYAGE_LABEL = "Suggested change";

	public static final String NEW_VOYAGE_LABEL = "Contributor";

	public static final String CORRECTED_VOYAGE_LABEL = "Approved voyage";

	public static final String MERGE_VOYAGE_LABEL = "Voyage indicated to merge";

	public static final String DECIDED_VOYAGE_LABEL = "Editor";

	public static final String DECIDED_VOYAGE = "decided";

	public static final String ORYGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	private static final String EDITOR_CHOICE = "editor_suggestion";

	private static final String EDITOR_CHOICE_LABEL = "Suggestion of";

	/**
	 * Attributes that are available for administrator/reviewer.
	 */
	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getAttributes(); //Admin
	private static SubmissionAttribute[] pAttrs = SubmissionAttributes.getConfiguration().getPublicAttributes(); //Reviewer

	/**
	 * Indicator is error occured (if wrong value was entered into any field).
	 */
	private boolean wasError = false;

	/**
	 * Values for DataGrid component.
	 */
	private Values vals = null;

	private Values valuesSlave;
	private Values valuesSlave2;
	private Values valuesSlave3;

	/**
	 * Rows for DataGrid component.
	 */
	private RowGroup[] rowGroups;

	/**
	 * Id of submission that is expanded.
	 */
	private Long submissionId = null;

	/**
	 * In case of edit request - one can have more than one submission solved at
	 * once.
	 */
	private Long[] editRequests = null;
	
	
	private Long[] relatedSubmissionsId = null;

	/**
	 * Main voyage id in merge request.
	 */
	private Long mergeMainVoyage = null;

	/**
	 * Reference to admin bean.
	 */
	private AdminSubmissionBean adminBean;

	private boolean requiredReload = false;
	//private boolean requiredReloadSlave = false;

	private static Object publishMonitor = new Object();
	
	public Long[] getRelatedSubmissionsId() {
		return relatedSubmissionsId;
	}

	public void setRelatedSubmissionsId(Long[] relatedSubmissionsId) {
		this.relatedSubmissionsId = relatedSubmissionsId;
	}


	private boolean revising = false;
	
	public VoyagesApplier(AdminSubmissionBean bean)
	{
		this.adminBean = bean;
		List rowGroupsList = new ArrayList();
		for (int i = 0; i < attrs.length; i++)
		{
			if (!rowGroupsList.contains(attrs[i].getGroupName()))
			{
				rowGroupsList.add(attrs[i].getGroupName());
			}
		}
		this.rowGroups = new RowGroup[rowGroupsList.size()];
		for (int i = 0; i < this.rowGroups.length; i++)
		{
			String rowGroup = (String) rowGroupsList.get(i);
			this.rowGroups[i] = new RowGroup(rowGroup, rowGroup);
		}
	}

	/**
	 * Gets values for grid component. If required - queries DB to fill in
	 * values.
	 * 
	 * @return
	 */
	public Values getValues()
	{
		if (vals == null || requiredReload)
		{
			requiredReload = false;
			fillInValues();
		}
		return vals;

	}

	private void fillInValues()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = null;
		String[] SLAVE_CHAR_COLS_LABELS = null;
		String[] SLAVE_CHAR_ROWS = null;
		String[] SLAVE_CHAR_ROWS_LABELS = null;
		
		if (this.adminBean.getAuthenticateduser().isAdmin() || this.adminBean.getAuthenticateduser().isChiefEditor())
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_A;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_A;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_A;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_A;
		}
		else
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_E;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_E;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_E;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_E;
		}
		
		
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		SourceInformationLookup sourceInformationUtils = SourceInformationLookup.createSourceInformationUtils(session);
		
		Voyage[] toVals = null;
		String[] cols = null;
		Map[] attributeNotes = null;
		EditedVoyage vNew = null;
		Submission submission = Submission.loadById(session, this.submissionId);

		if (submission instanceof SubmissionNew)
		{
			if (this.adminBean.getAuthenticateduser().isEditor())
			{
				vNew = this.getSubmissionEditor(submission,
						this.adminBean.getAuthenticateduser())
						.getEditedVoyage();
				toVals = new Voyage[2];
				cols = new String[2];
				attributeNotes = new Map[2];
			} else
			{
				vNew = ((SubmissionNew) submission).getEditorVoyage();
				toVals = new Voyage[2 + ((SubmissionNew) submission)
						.getSubmissionEditors().size()];
				cols = new String[2 + ((SubmissionNew) submission)
						.getSubmissionEditors().size()];
				attributeNotes = new Map[2 + ((SubmissionNew) submission)
						.getSubmissionEditors().size()];
			}
			toVals[0] = Voyage.loadById(session, ((SubmissionNew) submission)
					.getNewVoyage().getVoyage().getIid());
			attributeNotes[0] = (((SubmissionNew) submission).getNewVoyage()
					.getAttributeNotes());
			cols[0] = ORYGINAL_VOYAGE;

			if (!this.adminBean.getAuthenticateduser().isEditor())
			{
				Iterator iter = ((SubmissionNew) submission)
						.getSubmissionEditors().iterator();
				for (int i = 0; i < toVals.length - 2; i++)
				{
					SubmissionEditor edit = (SubmissionEditor) iter.next();
					if (edit.getEditedVoyage() != null)
					{
						toVals[i + 1] = edit.getEditedVoyage().getVoyage();
						attributeNotes[i + 1] = edit.getEditedVoyage()
								.getAttributeNotes();
					} else
					{
						attributeNotes[i + 1] = new HashMap();
					}
					if (toVals[i + 1] == null)
					{
						toVals[i + 1] = new Voyage();
					}
					cols[i + 1] = EDITOR_CHOICE + "_" + i;
				}
			}

			if (vNew == null)
			{
				toVals[toVals.length - 1] = new Voyage();
				attributeNotes[attributeNotes.length - 1] = new HashMap();
			} else
			{
				toVals[toVals.length - 1] = vNew.getVoyage();
				if (toVals[toVals.length - 1] == null)
				{
					toVals[toVals.length - 1] = new Voyage();
				}
				attributeNotes[attributeNotes.length - 1] = vNew
						.getAttributeNotes();
			}
			cols[cols.length - 1] = DECIDED_VOYAGE;

		} else if (submission instanceof SubmissionEdit)
		{
			if (this.adminBean.getAuthenticateduser().isEditor())
			{
				vNew = this.getSubmissionEditor(submission,
						this.adminBean.getAuthenticateduser())
						.getEditedVoyage();
				toVals = new Voyage[2 + this.editRequests.length];
				cols = new String[2 + this.editRequests.length];
				attributeNotes = new Map[2 + this.editRequests.length];
			} else
			{
				vNew = ((SubmissionEdit) submission).getEditorVoyage();
				toVals = new Voyage[2 + this.editRequests.length
						+ submission.getSubmissionEditors().size()];
				cols = new String[2 + this.editRequests.length
						+ submission.getSubmissionEditors().size()];
				attributeNotes = new Map[2 + this.editRequests.length
						+ submission.getSubmissionEditors().size()];
			}
			cols[0] = ORYGINAL_VOYAGE;
			toVals[0] = Voyage.loadById(session, ((SubmissionEdit) submission)
					.getOldVoyage().getVoyage().getIid());
			attributeNotes[0] = ((SubmissionEdit) submission).getOldVoyage()
					.getAttributeNotes();
			for (int i = 1; i <= this.editRequests.length; i++)
			{
				EditedVoyage eV = EditedVoyage.loadById(session,
						this.editRequests[i - 1]);
				toVals[i] = eV.getVoyage();
				cols[i] = CHANGED_VOYAGE + "_" + i;
				attributeNotes[i] = eV.getAttributeNotes();
			}

			if (!this.adminBean.getAuthenticateduser().isEditor())
			{
				Iterator iter = submission.getSubmissionEditors().iterator();
				for (int i = 0; i < submission.getSubmissionEditors().size(); i++)
				{
					EditedVoyage eV = ((SubmissionEditor) iter.next())
							.getEditedVoyage();
					if (eV != null)
					{
						toVals[i + this.editRequests.length + 1] = eV
								.getVoyage();
						attributeNotes[i + this.editRequests.length + 1] = eV
								.getAttributeNotes();
					}
					if (toVals[i + this.editRequests.length + 1] == null)
					{
						toVals[i + this.editRequests.length + 1] = new Voyage();
					}
					if (attributeNotes[i + this.editRequests.length + 1] == null)
					{
						attributeNotes[i + this.editRequests.length + 1] = new HashMap();
					}
					cols[i + this.editRequests.length + 1] = EDITOR_CHOICE
							+ "_" + i;
				}
			}

			if (vNew == null)
			{
				toVals[toVals.length - 1] = new Voyage();
				attributeNotes[toVals.length - 1] = new HashMap();
			} else
			{
				toVals[toVals.length - 1] = vNew.getVoyage();
				if (toVals[toVals.length - 1] == null)
				{
					toVals[toVals.length - 1] = new Voyage();
				}
				attributeNotes[toVals.length - 1] = vNew.getAttributeNotes();
			}
			cols[cols.length - 1] = DECIDED_VOYAGE;
		} else
		{
			if (this.adminBean.getAuthenticateduser().isEditor())
			{
				vNew = this.getSubmissionEditor(submission,
						this.adminBean.getAuthenticateduser())
						.getEditedVoyage();
				toVals = new Voyage[2 + this.editRequests.length];
				cols = new String[2 + this.editRequests.length];
				attributeNotes = new Map[2 + this.editRequests.length];
			} else
			{
				vNew = ((SubmissionMerge) submission).getEditorVoyage();
				toVals = new Voyage[2
						+ this.editRequests.length
						+ ((SubmissionMerge) submission).getSubmissionEditors()
								.size()];
				cols = new String[2
						+ this.editRequests.length
						+ ((SubmissionMerge) submission).getSubmissionEditors()
								.size()];
				attributeNotes = new Map[2
						+ this.editRequests.length
						+ ((SubmissionMerge) submission).getSubmissionEditors()
								.size()];
			}
			for (int i = 0; i < this.editRequests.length; i++)
			{
				EditedVoyage eV = EditedVoyage.loadById(session,
						this.editRequests[i]);
				toVals[i] = eV.getVoyage();
				cols[i] = CHANGED_VOYAGE + "_" + i;
				attributeNotes[i] = eV.getAttributeNotes();
			}

			EditedVoyage eV = EditedVoyage.loadById(session,
					this.mergeMainVoyage);
			toVals[this.editRequests.length] = eV.getVoyage();
			cols[this.editRequests.length] = ORYGINAL_VOYAGE;
			attributeNotes[this.editRequests.length] = eV.getAttributeNotes();
			if (!this.adminBean.getAuthenticateduser().isEditor())
			{
				Iterator iter = ((SubmissionMerge) submission)
						.getSubmissionEditors().iterator();
				for (int i = 1; i <= ((SubmissionMerge) submission)
						.getSubmissionEditors().size(); i++)
				{
					SubmissionEditor editor = (SubmissionEditor) iter.next();
					if (editor.getEditedVoyage() != null)
					{
						toVals[this.editRequests.length + i] = editor
								.getEditedVoyage().getVoyage();
						attributeNotes[this.editRequests.length + i] = editor
								.getEditedVoyage().getAttributeNotes();
					}
					if (toVals[i + this.editRequests.length] == null)
					{
						toVals[i + this.editRequests.length] = new Voyage();
					}
					if (attributeNotes[i + this.editRequests.length] == null)
					{
						attributeNotes[i + this.editRequests.length] = new HashMap();
					}
					cols[this.editRequests.length + i] = EDITOR_CHOICE + "_"
							+ (i - 1);

				}
			}

			if (vNew == null)
			{
				toVals[toVals.length - 1] = new Voyage();
				attributeNotes[toVals.length - 1] = new HashMap();
			} else
			{
				toVals[toVals.length - 1] = vNew.getVoyage();
				if (toVals[toVals.length - 1] == null)
				{
					toVals[toVals.length - 1] = new Voyage();
				}
				attributeNotes[toVals.length - 1] = vNew.getAttributeNotes();
			}
			cols[cols.length - 1] = DECIDED_VOYAGE;
		}
		vals = new Values();
		valuesSlave = new Values();
		valuesSlave2 = new Values();
		valuesSlave3 = new Values();
		for (int n = 0; n < toVals.length; n++)
		{
			for (int i = 0; i < attrs.length; i++)
			{
				SubmissionAttribute attribute = attrs[i];
				if (attrs[i].getName().equals("saild2")){
					//System.out.println("attr:" + attrs[i].getName());
				}
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++)
				{
					toBeFormatted[j] = toVals[n].getAttrValue(attribute
							.getAttribute()[j].getName());
				}
				Value value = attrs[i].getValue(session, toBeFormatted,
						sourceInformationUtils);
				//System.out.println("attr:" + attrs[i].getName());
				value.setNote((String) attributeNotes[n]
						.get(attrs[i].getName()));
				vals.setValue(cols[n], attrs[i].getName(), value);
			}
			for (int i = 0; i < SLAVE_CHAR_COLS.length; i++)
			{
				for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++)
				{
					SubmissionAttribute attribute = SubmissionAttributes
							.getConfiguration().getAttribute(
									SLAVE_CHAR_ROWS[j] + ","
											+ SLAVE_CHAR_COLS[i]);
					if (attribute == null)
					{
						
						continue;
						/*throw new RuntimeException(
								"SubmissionAttribute not found: "
										+ SLAVE_CHAR_ROWS[j] + ","
										+ SLAVE_CHAR_COLS[i]);*/
					}
					Object[] toBeFormatted = new Object[attribute
							.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++)
					{
						toBeFormatted[k] = toVals[n].getAttrValue(attribute
								.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted,
							sourceInformationUtils);
					value.setNote((String) attributeNotes[n].get(attribute
							.getName()));
					valuesSlave.setValue(SLAVE_CHAR_COLS[i], SLAVE_CHAR_ROWS[j]
							+ (n != toVals.length - 1 ? "-" + n : ""), value);
				}
			}
			
			for (int i = 0; i < SLAVE_CHAR_COLS_S.length; i++)
			{
				for (int j = 0; j < SLAVE_CHAR_ROWS_S.length; j++)
				{
					SubmissionAttribute attribute = SubmissionAttributes
							.getConfiguration().getAttribute(
									SLAVE_CHAR_ROWS_S[j] + ","
											+ SLAVE_CHAR_COLS_S[i]);
					if (attribute == null)
					{
						
						continue;
						/*throw new RuntimeException(
								"SubmissionAttribute not found: "
										+ SLAVE_CHAR_ROWS[j] + ","
										+ SLAVE_CHAR_COLS[i]);*/
					}
					Object[] toBeFormatted = new Object[attribute
							.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++)
					{
						toBeFormatted[k] = toVals[n].getAttrValue(attribute
								.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted,
							sourceInformationUtils);
					value.setNote((String) attributeNotes[n].get(attribute
							.getName()));
					valuesSlave3.setValue(SLAVE_CHAR_COLS_S[i], SLAVE_CHAR_ROWS_S[j]
							+ (n != toVals.length - 1 ? "-" + n : ""), value);
				}
			}
			//Start Slave2 HERE
			/*for (int i = 0; i < SLAVE_CHAR_COLS_S.length; i++)
			{
				for (int j = 0; j < SLAVE_CHAR_ROWS_S.length; j++)
				{
					SubmissionAttribute attribute = SubmissionAttributes
							.getConfiguration().getAttribute(
									SLAVE_CHAR_ROWS_S[j] + ","
											+ SLAVE_CHAR_COLS_S[i]);
					if (attribute == null)
					{
						
						continue;
						
					}
					Object[] toBeFormatted = new Object[attribute
							.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++)
					{
						toBeFormatted[k] = toVals[n].getAttrValue(attribute
								.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted,
							sourceInformationUtils);
					value.setNote((String) attributeNotes[n].get(attribute
							.getName()));
					valuesSlave2.setValue(SLAVE_CHAR_COLS_S[i], SLAVE_CHAR_ROWS_S[j]
							+ (n != toVals.length - 1 ? "-" + n : ""), value);
				}
			}*/
		}

		t.commit();
		session.close();
	}
	
/*	public void fillInValuesSlaves2()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		SourceInformationLookup sourceInformationUtils = SourceInformationLookup.createSourceInformationUtils(session);
		

		for (int i = 0; i < SLAVE_CHAR_COLS_S.length; i++)
		{
			for (int j = 0; j < SLAVE_CHAR_ROWS_S.length; j++)
			{
				SubmissionAttribute attribute = SubmissionAttributes.getConfiguration().getAttribute(SLAVE_CHAR_ROWS_S[j] + ","	+ SLAVE_CHAR_COLS_S[i]);
				
				if (attribute == null)
				{
					continue;
								
				}
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
							for (int k = 0; k < toBeFormatted.length; k++)
							{
								toBeFormatted[k] = toVals[n].getAttrValue(attribute.getAttribute()[k].getName());
							}
							Value value = attribute.getValue(session, toBeFormatted, sourceInformationUtils);
							value.setNote((String) attributeNotes[n].get(attribute.getName()));
							valuesSlave.setValue(SLAVE_CHAR_COLS_S[i], SLAVE_CHAR_ROWS_S[j]+ (n != toVals.length - 1 ? "-" + n : ""), value);
						}
					}
		t.commit();
		session.close();
		
	}*/
	

	/**
	 * Sets values when user returns form with GridComponent.
	 * 
	 * @param vals
	 */
	public void setValues(Values vals)
	{
		this.vals = vals;
	}

	/**
	 * Gets columns for GridComponent. The result depends on type of submission
	 * that is handled.
	 * 
	 * @return
	 */
	public Column[] getColumns()
	{
		Column[] cols = null;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session,
				this.submissionId);
		if (lSubmisssion instanceof SubmissionNew)
		{
			cols = this.getNewColumns((SubmissionNew) lSubmisssion,
					this.adminBean.getAuthenticateduser().isEditor());
		} else if (lSubmisssion instanceof SubmissionEdit)
		{
			cols = getEditColumns((SubmissionEdit) lSubmisssion, this.adminBean
					.getAuthenticateduser().isEditor());
		} else
		{
			cols = getMergeColumns((SubmissionMerge) lSubmisssion,
					this.adminBean.getAuthenticateduser().isEditor());
		}
		t.commit();
		session.close();
		return cols;
	}

	public Column[] getColumnsSlave()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = null;
		String[] SLAVE_CHAR_COLS_LABELS = null;
		String[] SLAVE_CHAR_ROWS = null;
		String[] SLAVE_CHAR_ROWS_LABELS = null;
		
		if (this.adminBean.getAuthenticateduser().isAdmin() || this.adminBean.getAuthenticateduser().isChiefEditor())
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_A;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_A;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_A;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_A;
		}
		else
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_E;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_E;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_E;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_E;
		}
		
		
		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++)
		{
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}
	
	public Column[] getColumnsSlave2()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_S;
		
		
		
		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++)
		{
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}
	

	public Column[] getColumnsSlave3()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_S;
		
		
		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++)
		{
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}
	
	

	private Column[] getMergeColumns(SubmissionMerge submission, boolean editor)
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Column[] cols;
		try
		{
			if (editor)
			{
				cols = new Column[2 + this.editRequests.length];
			} else
			{
				cols = new Column[2 + this.editRequests.length
						+ submission.getSubmissionEditors().size()];
			}
			int i;
			for (i = 0; i < this.editRequests.length; i++)
			{
				EditedVoyage eV = EditedVoyage.loadById(session,
						this.editRequests[i]);
				cols[i] = new Column(CHANGED_VOYAGE + "_" + i, "Voyageid "
						+ eV.getVoyage().getVoyageid(), true, DECIDED_VOYAGE,
						COPY_LABEL, false);
			}
			cols[i++] = new Column(ORYGINAL_VOYAGE, NEW_VOYAGE_LABEL, true,
					DECIDED_VOYAGE, COPY_LABEL, false);
			if (!editor)
			{
				Iterator iter = submission.getSubmissionEditors().iterator();
				for (int j = 0; j < submission.getSubmissionEditors().size(); j++)
				{
					SubmissionEditor submissionEditor = (SubmissionEditor) iter
							.next();
					cols[i] = new Column(
							EDITOR_CHOICE + "_" + j,
							submissionEditor.getUser().getUserName()
									+ "("
									+ (submissionEditor.isFinished() ? "finished"
											: "not finished") + ")", true,
							DECIDED_VOYAGE, COPY_LABEL, false);
					cols[i++].setActions(new ColumnAction[] { new ColumnAction(
							REMOVE_EDITOR_ACTION + "_"
									+ submissionEditor.getId(), "remove") });
				}
				cols[i++] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false,
						true);
			}else{
				cols[i++] = new Column(DECIDED_VOYAGE, this.adminBean
						.getAuthenticateduser().getUserName(), false, true);
			}

		} finally
		{
			t.commit();
			session.close();
		}
		return cols;
	}

	private Column[] getEditColumns(SubmissionEdit submission, boolean editor)
	{
		Column[] cols;
		if (editor)
		{
			cols = new Column[2 + this.editRequests.length];
		} else
		{
			cols = new Column[2 + this.editRequests.length
					+ submission.getSubmissionEditors().size()];
		}
		EditedVoyage eV =((SubmissionEdit) submission).getOldVoyage();
		Voyage v =eV.getVoyage();
		

		
		cols[0] = new Column(ORYGINAL_VOYAGE, "Voyageid "
				+ v.getVoyageid(), true, DECIDED_VOYAGE,
				COPY_LABEL, false);
		
		int i;
		for (i = 1; i <= this.editRequests.length; i++)
		{
			cols[i] = new Column(CHANGED_VOYAGE + "_" + i, NEW_VOYAGE_LABEL
					, true, DECIDED_VOYAGE, COPY_LABEL, false);
		}

		
		if (!editor)
		{
			Iterator iter = submission.getSubmissionEditors().iterator();
			for (int j = 0; j < submission.getSubmissionEditors().size(); j++)
			{
				SubmissionEditor submissionEditor = (SubmissionEditor) iter
						.next();
				cols[i] = new Column(EDITOR_CHOICE + "_" + j, submissionEditor
						.getUser().getUserName()
						+ "("
						+ (submissionEditor.isFinished() ? "finished"
								: "not finished") + ")", true, DECIDED_VOYAGE,
						COPY_LABEL, false);
				cols[i++].setActions(new ColumnAction[] { new ColumnAction(
						REMOVE_EDITOR_ACTION + "_" + submissionEditor.getId(),
						"remove") });
			}
			cols[i++] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false,
					true);
		}else{
			cols[i++] = new Column(DECIDED_VOYAGE, this.adminBean
					.getAuthenticateduser().getUserName(), false, true);
		}

		return cols;
	}

	private Column[] getNewColumns(SubmissionNew submission, boolean editor)
	{
		Column[] cols;
		if (editor)
		{
			cols = new Column[2];
		} else
		{
			cols = new Column[2 + submission.getSubmissionEditors().size()];
		}
		cols[0] = new Column(ORYGINAL_VOYAGE, NEW_VOYAGE_LABEL, true,
				DECIDED_VOYAGE, COPY_LABEL, false);
		if (!editor) {
			Iterator iter = submission.getSubmissionEditors().iterator();

			for (int i = 0; i < submission.getSubmissionEditors().size(); i++) {
				SubmissionEditor submissionEditor = (SubmissionEditor) iter
						.next();
				cols[i + 1] = new Column(EDITOR_CHOICE + "_" + i,
						submissionEditor.getUser().getUserName()
								+ "("
								+ (submissionEditor.isFinished() ? "finished"
										: "not finished") + ")", true,
						DECIDED_VOYAGE, COPY_LABEL, false);
				cols[i + 1].setActions(new ColumnAction[] { new ColumnAction(
						REMOVE_EDITOR_ACTION + "_" + submissionEditor.getId(),
						"remove") });
			}
			
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE,
					DECIDED_VOYAGE_LABEL, false, true);


		}else{
			cols[cols.length - 1] = new Column(DECIDED_VOYAGE, this.adminBean
					.getAuthenticateduser().getUserName(), false, true);
		}

		return cols;
	}

	public void columnAction(ColumnActionEvent event)
	{
		if (event.getActionName().startsWith(REMOVE_EDITOR_ACTION)
				&& event.getColumnName().startsWith(EDITOR_CHOICE))
		{
			Long editorId = new Long(event.getActionName().substring(
					REMOVE_EDITOR_ACTION.length() + 1));
			Session session = HibernateConn.getSession();
			Transaction t = session.beginTransaction();
			try
			{
				SubmissionEditor editor = SubmissionEditor.loadById(session,
						editorId);
				session.delete(editor);
			} finally
			{
				t.commit();
				session.close();
			}
		}
	}

	/**
	 * Returns rows for GridComponent.
	 * 
	 * @return
	 */
	public Row[] getRows()
	{
		SubmissionAttribute[] dispAttrs= null;
		
		if (this.adminBean.getAuthenticateduser().isAdmin() || this.adminBean.getAuthenticateduser().isChiefEditor()) //Get all attributes
		{
			dispAttrs = attrs;
		}
		else if (this.adminBean.getAuthenticateduser().isEditor()) //Get only public attributes
		{
			dispAttrs = pAttrs;
		}
		
		Row[] rows = new Row[dispAttrs.length];
		for (int i = 0; i < rows.length; i++)
		{
			rows[i] = new Row(dispAttrs[i].getType(), 
					          dispAttrs[i].getName(), 
					          dispAttrs[i].getUserLabel(), 
					          dispAttrs[i].getComment(), 
					          dispAttrs[i].getGroupName(), 
					          false);
			
			rows[i].setNoteEnabled(true);
		}
		return rows;
	}

	public Row[] getRowsSlave() {
		// Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = null;
		String[] SLAVE_CHAR_COLS_LABELS = null;
		String[] SLAVE_CHAR_ROWS = null;
		String[] SLAVE_CHAR_ROWS_LABELS = null;

		if (this.adminBean.getAuthenticateduser().isAdmin()
				|| this.adminBean.getAuthenticateduser().isChiefEditor()) {
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_A;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_A;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_A;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_A;
		} else {
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_E;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_E;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_E;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_E;
		}

		Row[] rows = null;
		int groupsNumber;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionEdit
					|| submission instanceof SubmissionMerge) {
				if (this.adminBean.getAuthenticateduser().isEditor()) {
					groupsNumber = 2 + this.editRequests.length;
					rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				} else {
					groupsNumber = 2 + this.editRequests.length
							+ submission.getSubmissionEditors().size();

					rows = new Row[SLAVE_CHAR_ROWS.length + (groupsNumber - 1)
							* SLAVE_CHAR_ROWS_E.length];
				}
				// rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				// for (int i = 0; i < (groupsNumber - 1) *
				// SLAVE_CHAR_ROWS.length; i++)
				// {
				// rows[i] = new Row(TextboxIntegerAdapter.TYPE,
				// SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length] + "-"
				// + (i / SLAVE_CHAR_ROWS.length),
				// SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
				// null, "characteristics-"
				// + (i / SLAVE_CHAR_ROWS.length), true,
				// SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
				// rows[i].setNoteEnabled(true);
				// }

				for (int i = 0; i < (groupsNumber - 1)
						* SLAVE_CHAR_ROWS_E.length; i++) {
					rows[i] = new Row(
							TextboxIntegerAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS_E.length] + "-"
									+ (i / SLAVE_CHAR_ROWS_E.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS_E.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS_E.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS_E.length],
							"Copy");
					rows[i].setNoteEnabled(true);
				}
			} else {
				if (this.adminBean.getAuthenticateduser().isEditor()) {
					groupsNumber = 2;
					rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				} else {
					groupsNumber = 2 + submission.getSubmissionEditors().size();
					rows = new Row[SLAVE_CHAR_ROWS.length + (groupsNumber - 1)
							* SLAVE_CHAR_ROWS_E.length];
				}
				// rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				// for (int i = 0; i < (groupsNumber - 1) *
				// SLAVE_CHAR_ROWS.length; i++)
				// {
				// rows[i] = new Row(
				// TextboxIntegerAdapter.TYPE,
				// SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length]
				// + "-"
				// + (int) (i / (double) SLAVE_CHAR_ROWS.length),
				// SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
				// null, "characteristics-"
				// + (i / SLAVE_CHAR_ROWS.length), true,
				// SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
				// rows[i].setNoteEnabled(true);
				// }

				for (int i = 0; i < (groupsNumber - 1)
						* SLAVE_CHAR_ROWS_E.length; i++) {
					rows[i] = new Row(
							TextboxIntegerAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS_E.length]
									+ "-"
									+ (int) (i / (double) SLAVE_CHAR_ROWS_E.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS_E.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS_E.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS_E.length],
							"Copy");
					rows[i].setNoteEnabled(true);
				}
			}
			// for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++)
			// {
			// rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length] = new Row(
			// TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i],
			// SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics",
			// true);
			// rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length]
			// .setNoteEnabled(true);
			// }

			for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS_E.length] = new Row(
						TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i],
						SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics",
						true);
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS_E.length]
						.setNoteEnabled(true);
			}
		} finally {
			t.commit();
			session.close();
		}
		return rows;
	}
	
	public Row[] getRowsSlave2()
	{
		//Summary configuration
		String[] SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_S;
			
		Row[] rows = null;
		int groupsNumber;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try
		{
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionEdit
					|| submission instanceof SubmissionMerge)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					groupsNumber = 2 + this.editRequests.length;
				} else
				{
					groupsNumber = 2 + this.editRequests.length
							+ submission.getSubmissionEditors().size();
				}
				rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				for (int i = 0; i < (groupsNumber - 1) * SLAVE_CHAR_ROWS.length; i++)
				{
					rows[i] = new Row(TextboxDoubleAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length] + "-"
									+ (i / SLAVE_CHAR_ROWS.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
					rows[i].setNoteEnabled(true);
				}
			} else
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					groupsNumber = 2;
				} else
				{
					groupsNumber = 2 + submission.getSubmissionEditors().size();
				}
				rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				for (int i = 0; i < (groupsNumber - 1) * SLAVE_CHAR_ROWS.length; i++)
				{
					rows[i] = new Row(
							TextboxDoubleAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length]
									+ "-"
									+ (int) (i / (double) SLAVE_CHAR_ROWS.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
					rows[i].setNoteEnabled(true);
				}
			}
			for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++)
			{
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length] = new Row(
						TextboxDoubleAdapter.TYPE, SLAVE_CHAR_ROWS[i],
						SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics",
						true);
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length]
						.setNoteEnabled(true);
			}
		} finally
		{
			t.commit();
			session.close();
		}
		return rows;
	}
	
	public Row[] getRowsSlave3()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_S;
		
		Row[] rows = null;
		int groupsNumber;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try
		{
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionEdit
					|| submission instanceof SubmissionMerge)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
//					groupsNumber = 2 + this.editRequests.length;
					groupsNumber = 1;
				} else
				{
//					groupsNumber = 2 + this.editRequests.length
//							+ submission.getSubmissionEditors().size();
					groupsNumber = 1;
				}
				rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				for (int i = 0; i < (groupsNumber - 1) * SLAVE_CHAR_ROWS.length; i++)
				{
					rows[i] = new Row(TextboxDoubleAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length] + "-"
									+ (i / SLAVE_CHAR_ROWS.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
					rows[i].setNoteEnabled(true);
				}
			} else
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					groupsNumber = 2;
				} else
				{
//					groupsNumber = 2 + submission.getSubmissionEditors().size();
					groupsNumber = 1;
				}
				rows = new Row[groupsNumber * SLAVE_CHAR_ROWS.length];
				for (int i = 0; i < (groupsNumber - 1) * SLAVE_CHAR_ROWS.length; i++)
				{
					rows[i] = new Row(
							TextboxDoubleAdapter.TYPE,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length]
									+ "-"
									+ (int) (i / (double) SLAVE_CHAR_ROWS.length),
							SLAVE_CHAR_ROWS_LABELS[i % SLAVE_CHAR_ROWS.length],
							null, "characteristics-"
									+ (i / SLAVE_CHAR_ROWS.length), true,
							SLAVE_CHAR_ROWS[i % SLAVE_CHAR_ROWS.length], "Copy");
					rows[i].setNoteEnabled(true);
				}
			}
			for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++)
			{
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length] = new Row(
						TextboxDoubleAdapter.TYPE, SLAVE_CHAR_ROWS[i],
						SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics",
						true);
				rows[i + (groupsNumber - 1) * SLAVE_CHAR_ROWS.length]
						.setNoteEnabled(true);
			}
		} finally
		{
			t.commit();
			session.close();
		}
		return rows;
	}

	/**
	 * Gets groups for rows - GridComponent requirement.
	 * 
	 * @return
	 */
	public RowGroup[] getRowGroups()
	{
		return this.rowGroups;
	}

	public RowGroup[] getRowGroupsSlave()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try
		{
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionNew)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					return new RowGroup[] {
							new RowGroup("characteristics-0",
									"Slaves (characteristics) [Contributor]"),
							new RowGroup("characteristics",
									"Slaves (characteristics) ["+this.adminBean
									.getAuthenticateduser().getUserName()+"]") };
				} else
				{
					RowGroup[] response = new RowGroup[2 + submission
							.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Contributor]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = 1; i < response.length - 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) ["
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Editor]");
					return response;
				}
			} else if (submission instanceof SubmissionEdit)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					EditedVoyage eV =((SubmissionEdit) submission).getOldVoyage();
					Voyage v =eV.getVoyage();
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Voyageid "+v.getVoyageid()+"]");
					for (int i = 1; i < response.length - 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Contributor]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) ["+this.adminBean
									.getAuthenticateduser().getUserName()+"]");
					return response;
				} else
				{
					EditedVoyage eV =((SubmissionEdit) submission).getOldVoyage();
					Voyage v =eV.getVoyage();
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Voyageid "+v.getVoyageid()+"]");
					for (int i = 1; i < this.editRequests.length + 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Contributor]");
					}
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length; i < this.editRequests.length
							+ submission.getSubmissionEditors().size(); i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i + 1] = new RowGroup("characteristics-"
								+ (i + 1),
								"Slaves (characteristics) ["
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Editor]");
					return response;
				}
			} else
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[response.length - 2] = new RowGroup(
							"characteristics-" + (response.length - 2),
							"Slaves (characteristics) [Contributor]");
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) ["+this.adminBean
							.getAuthenticateduser().getUserName()+"]");
					return response;
				} else
				{
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[this.editRequests.length] = new RowGroup(
							"characteristics-" + (this.editRequests.length),
							"Slaves (characteristics) [Contributor]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length + 1; i < this.editRequests.length
							+ submission.getSubmissionEditors().size() + 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + (i),
								"Slaves (characteristics) ["
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Editor]");
					return response;
				}
			}
		} finally
		{
			t.commit();
			session.close();
		}
	}
	
	
	public RowGroup[] getRowGroupsSlave2()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try
		{
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionNew)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					return new RowGroup[] {
							new RowGroup("characteristics-0",
									"Slaves (Age and Sex Summary) [Suggested voyage]"),
							new RowGroup("characteristics",
									"Slaves (Age and Sex Summary) [Final decision]") };
				} else
				{
					RowGroup[] response = new RowGroup[2 + submission
							.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (Age and Sex Summary) [Suggested voyage]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = 1; i < response.length - 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (Age and Sex Summary) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (Age and Sex Summary) [Final decision]");
					return response;
				}
			} else if (submission instanceof SubmissionEdit)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (Age and Sex Summary) [Original voyage]");
					for (int i = 1; i < response.length - 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (Age and Sex Summary) [Suggested change #"
										+ i + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (Age and Sex Summary) [Final decision]");
					return response;
				} else
				{
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (Age and Sex Summary) [Original voyage]");
					for (int i = 1; i < this.editRequests.length + 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (Age and Sex Summary) [Suggested change #"
										+ i + "]");
					}
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length; i < this.editRequests.length
							+ submission.getSubmissionEditors().size(); i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i + 1] = new RowGroup("characteristics-"
								+ (i + 1),
								"Slaves (Age and Sex Summary) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (Age and Sex Summary) [Final decision]");
					return response;
				}
			} else
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (Age and Sex Summary) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[response.length - 2] = new RowGroup(
							"characteristics-" + (response.length - 2),
							"Slaves (Age and Sex Summary) [Suggested voyage]");
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (Age and Sex Summary) [Final decision]");
					return response;
				} else
				{
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (Age and Sex Summary) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[this.editRequests.length] = new RowGroup(
							"characteristics-" + (this.editRequests.length),
							"Slaves (Age and Sex Summary) [Suggested voyage]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length + 1; i < this.editRequests.length
							+ submission.getSubmissionEditors().size() + 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + (i),
								"Slaves (Age and Sex Summary) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (Age and Sex Summary) [Final decision]");
					return response;
				}
			}
		} finally
		{
			t.commit();
			session.close();
		}
	}
	
	
	public RowGroup[] getRowGroupsSlave3()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try
		{
			Submission submission = Submission.loadById(session,
					getSubmissionId());
			if (submission instanceof SubmissionNew)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
//					return new RowGroup[] {
//							new RowGroup("characteristics-0",
//									"Slaves (age and sex) [Contributor]"),
//							new RowGroup("characteristics",
//									"Slaves (age and sex) ["+this.adminBean
//									.getAuthenticateduser().getUserName()+"]") };
					
					return new RowGroup[] {
							new RowGroup("characteristics",
									"Slaves (age and sex) ["+this.adminBean
									.getAuthenticateduser().getUserName()+"]") };
				} else
				{
					RowGroup[] response = new RowGroup[1];
//					RowGroup[] response = new RowGroup[2 + submission
//							.getSubmissionEditors().size()];
//					response[0] = new RowGroup("characteristics-0",
//							"Slaves (age and sex) [Contributor]");
//					Iterator iter = submission.getSubmissionEditors()
//							.iterator();
//					for (int i = 1; i < response.length - 1; i++)
//					{
//						SubmissionEditor editor = (SubmissionEditor) iter
//								.next();
//						response[i] = new RowGroup("characteristics-" + i,
//								"Slaves (age and sex) ["
//										+ editor.getUser().getUserName() + "]");
//					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (age and sex) [Editor]");
					return response;
				}
			} else if (submission instanceof SubmissionEdit)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					EditedVoyage eV =((SubmissionEdit) submission).getOldVoyage();
					Voyage v =eV.getVoyage();
					response[0] = new RowGroup("characteristics-0",
							"Slaves (age and sex) [Voyageid "+v.getVoyageid()+"]");
					for (int i = 1; i < response.length - 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (age and sex) [Contributor]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (age and sex) ["+this.adminBean
									.getAuthenticateduser().getUserName()+"]");
					return response;
				} else
				{
					EditedVoyage eV =((SubmissionEdit) submission).getOldVoyage();
					Voyage v =eV.getVoyage();
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (age and sex) [Voyageid "+v.getVoyageid()+"]");
					for (int i = 1; i < this.editRequests.length + 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (age and sex) [Contributor]");
					}
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length; i < this.editRequests.length
							+ submission.getSubmissionEditors().size(); i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i + 1] = new RowGroup("characteristics-"
								+ (i + 1),
								"Slaves (age and sex) ["
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (age and sex) [Editor]");
					return response;
				}
			} else
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (age and sex) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[response.length - 2] = new RowGroup(
							"characteristics-" + (response.length - 2),
							"Slaves (age and sex) [Contributor]");
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (age and sex) ["+this.adminBean
							.getAuthenticateduser().getUserName()+"]");
					return response;
				} else
				{
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					for (int i = 0; i < this.editRequests.length; i++)
					{
						EditedVoyage eV = EditedVoyage.loadById(session,
								this.editRequests[i]);
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (age and sex) [Voyageid "
										+ eV.getVoyage().getVoyageid() + "]");
					}
					response[this.editRequests.length] = new RowGroup(
							"characteristics-" + (this.editRequests.length),
							"Slaves (age and sex) [Contributor]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length + 1; i < this.editRequests.length
							+ submission.getSubmissionEditors().size() + 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + (i),
								"Slaves (age and sex) ["
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (age and sex) [Editor]");
					return response;
				}
			}
		} finally
		{
			t.commit();
			session.close();
		}
	}

	/**
	 * Invoked when user clicks on any row of list showing requests. Prepares
	 * data for request handling.
	 * 
	 * @param e
	 */
	public void newRequestId(GridOpenRowEvent e)
	{

		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();

		TastDbConditions c = new TastDbConditions();
		c.addCondition(Submission.getAttribute("id"), new Long(e.getRowId()
				.split("_")[1]), TastDbConditions.OP_EQUALS);
		TastDbQuery q = new TastDbQuery("Submission", c);

		Object[] res = q.executeQuery(session);
		if (res.length != 0)
		{
			Submission lSubmission = (Submission) res[0];
			this.submissionId = lSubmission.getId();
			if (this.adminBean.getAuthenticateduser().isEditor())
			{
				SubmissionEditor editor = this.getSubmissionEditor(lSubmission,
						this.adminBean.getAuthenticateduser());
				this.adminBean.setFinished(new Boolean(editor.isFinished()));
			}
			if (lSubmission instanceof SubmissionEdit)
			{
				Integer vid = ((SubmissionEdit) lSubmission).getOldVoyage()
						.getVoyage().getVoyageid();
				c = new TastDbConditions();
				c.addCondition(new SequenceAttribute(new Attribute[] {
						SubmissionEdit.getAttribute("oldVoyage"),
						EditedVoyage.getAttribute("voyage"),
						Voyage.getAttribute("voyageid") }), vid,
						TastDbConditions.OP_EQUALS);
				c.addCondition(SubmissionEdit.getAttribute("solved"),
						new Boolean(false), TastDbConditions.OP_EQUALS);
				q = new TastDbQuery("SubmissionEdit", c);
				Object[] rets = q.executeQuery(session);
				this.editRequests = new Long[rets.length];
				this.relatedSubmissionsId = new Long[rets.length];
				for (int i = 0; i < editRequests.length; i++)
				{
					editRequests[i] = ((SubmissionEdit) rets[i]).getNewVoyage()
							.getId();
					relatedSubmissionsId[i] = ((SubmissionEdit) rets[i]).getId();
				}
			} else if (lSubmission instanceof SubmissionMerge)
			{
				Set voyagesToMerge = ((SubmissionMerge) lSubmission)
						.getMergedVoyages();
				editRequests = new Long[voyagesToMerge.size()];
				this.relatedSubmissionsId = new Long[1];
				this.relatedSubmissionsId[0] = this.submissionId;
				Iterator iter = voyagesToMerge.iterator();
				int i = 0;
				while (iter.hasNext())
				{
					editRequests[i++] = ((EditedVoyage) iter.next()).getId();
				}
				this.mergeMainVoyage = ((SubmissionMerge) lSubmission)
						.getProposedVoyage().getId();
			} else if (lSubmission instanceof Submission){
				this.relatedSubmissionsId = new Long[1];
				this.relatedSubmissionsId[0] = this.submissionId;
			}
		} else
		{
			this.submissionId = null;
		}
		this.vals = null;
		this.valuesSlave = null;
		this.valuesSlave3 = null;
		t.commit();
		session.close();
	}

	public Boolean getRejectAvailable()
	{
		try {
			Boolean avail = null;
			Session session = HibernateConn.getSession();
			Transaction t = session.beginTransaction();
			Submission lSubmisssion = Submission.loadById(session,
					this.submissionId);
			avail = new Boolean(!lSubmisssion.isSolved());
			t.commit();
			session.close();
			return avail;
		} catch (Exception e) {
			return false;
		}

	}
	
	public Boolean getAccepted()
	{
		Boolean accepted = null;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session,
				this.submissionId);
		accepted = new Boolean(lSubmisssion.isAccepted());
		t.commit();
		session.close();
		return accepted;

	}

	/**
	 * Action fired when reject button was pressed
	 */
	public String rejectSubmission()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session,
				this.submissionId);
		if (lSubmisssion instanceof SubmissionEdit)
		{
			TastDbConditions c = new TastDbConditions();
			c.addCondition(SubmissionEdit.getAttribute("oldVoyage"),
					((SubmissionEdit) lSubmisssion).getOldVoyage(),
					TastDbConditions.OP_EQUALS);
			TastDbQuery qValue = new TastDbQuery("SubmissionEdit", c);
			Object[] toUpdate = qValue.executeQuery(session);
			for (int i = 0; i < toUpdate.length; i++)
			{
				SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
				edit.setSolved(true);
				edit.setAccepted(false);
				session.update(edit);
			}
		} else
		{
			lSubmisssion.setSolved(true);
			lSubmisssion.setAccepted(false);
			session.update(lSubmisssion);
		}
		t.commit();
		session.close();
		return "back";
	}
	
	public String deleteSubmission() {
	

		synchronized (publishMonitor) {
			if (revising) {
				return null;
			}
			this.revising = true;
		}
		
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission submission = Submission.loadById(session, this.submissionId);
		try {
			System.out.println("revising");
			if(submission instanceof SubmissionNew){
				SQLQuery query = session.createSQLQuery("select delete_new("+this.submissionId+");");
				query.list();
			} else if(submission instanceof SubmissionEdit) {
				SQLQuery query = session.createSQLQuery("select delete_edit("+this.submissionId+");");
				query.list();
			} else if (submission instanceof SubmissionMerge){
				SQLQuery query = session.createSQLQuery("select delete_merge("+this.submissionId+");");
				query.list();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally {
			t.commit();
			session.close();
		}
		synchronized (publishMonitor) {
			revising = false;
		}
		
		return "back";
	}
	

	
	public String deleteVoyage() {

		synchronized (publishMonitor) {
			if (revising) {
				return null;
			}
			this.revising = true;
		}
		
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission submission = Submission.loadById(session, this.submissionId);
		try {
			System.out.println("deleting");
			if(submission instanceof SubmissionNew){
				SQLQuery query = session.createSQLQuery("select delete_newvoyage("+this.submissionId+");");
				query.list();
			} else if(submission instanceof SubmissionEdit) {
				SQLQuery query = session.createSQLQuery("select delete_editvoyage("+this.submissionId+");");
				query.list();
			} else if (submission instanceof SubmissionMerge){
				SQLQuery query = session.createSQLQuery("select delete_mergevoyage("+this.submissionId+");");
				query.list();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally {
			t.commit();
			session.close();
		}
		synchronized (publishMonitor) {
			revising = false;
		}
		
		return "back";
	}

	public String submit()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Map newValues = vals.getColumnValues(DECIDED_VOYAGE);
		Submission lSubmission = Submission
				.loadById(session, this.submissionId);
		if (this.adminBean.getAuthenticateduser().isAdmin()) {
			for (Iterator iter = lSubmission.getSubmissionEditors().iterator(); iter
					.hasNext();) {
				SubmissionEditor editor = (SubmissionEditor) iter.next();
				editor.setFinished(true);
				session.update(editor);
			}
		}

		Voyage vNew = null;
		Voyage mergedVoyage = updateMergedVoyage(session, lSubmission,
				newValues);

		if (mergedVoyage == null)
		{
			t.rollback();
			session.close();
			return null;
		}

		if (lSubmission instanceof SubmissionMerge)
		{
			System.out.println("Will delete all merged voyages");
			Set voyagesToDelete = ((SubmissionMerge) lSubmission)
					.getMergedVoyages();
			for (Iterator iter = voyagesToDelete.iterator(); iter.hasNext();)
			{
				EditedVoyage element = (EditedVoyage) iter.next();
				Integer voyageId = element.getVoyage().getVoyageid();
				Voyage voyage = Voyage.loadFutureRevision(session, voyageId);
				if (voyage != null)
				{
					session.delete(voyage);
				}
			}
		}
		TastDbConditions cond = new TastDbConditions();
		cond.addCondition(Voyage.getAttribute("voyageid"), mergedVoyage
				.getVoyageid(), TastDbConditions.OP_EQUALS);
		cond.addCondition(Voyage.getAttribute("revision"), new Integer(-1),
				TastDbConditions.OP_EQUALS);
		cond.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false),
				TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", cond);
		Object[] voyage = qValue.executeQuery(session);
		if (voyage.length != 0)
		{
			vNew = (Voyage) voyage[0];
		} else
		{
			vNew = new Voyage();
			vNew.setRevision(-1);
		}

		wasError = false;
		String[] fullAttributes = Voyage.getAllAttrNames();

		for (int i = 0; i < fullAttributes.length; i++) {
			Object val = mergedVoyage.getAttrValue(fullAttributes[i]);

			if (val == null) {
				if (fullAttributes[i].equals("voyageid")) {
					Value idVal = (Value) newValues.get("voyageid");
					idVal.setErrorMessage("This field is required!");
					wasError = true;
				}
				continue;
			}

			vNew.setAttrValue(fullAttributes[i], val);

		}
		if (!wasError)
		{
			vNew.saveOrUpdate();
		} else{
			return null;
		}

		if (lSubmission instanceof SubmissionEdit)
		{
			TastDbConditions c = new TastDbConditions();
			c.addCondition(new SequenceAttribute(new Attribute[] {
					SubmissionEdit.getAttribute("oldVoyage"),
					EditedVoyage.getAttribute("voyage") }),
					((SubmissionEdit) lSubmission).getOldVoyage().getVoyage(),
					TastDbConditions.OP_EQUALS);
			qValue = new TastDbQuery("SubmissionEdit", c);
			Object[] toUpdate = qValue.executeQuery(session);
			for (int i = 0; i < toUpdate.length; i++)
			{
				SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
				edit.setSolved(true);
				edit.setAccepted(true);
				vNew.setSuggestion(false);
				session.update(edit);
				session.update(vNew);
			}
		} else
		{
			lSubmission.setSolved(true);
			lSubmission.setAccepted(true);
			vNew.setSuggestion(false);
			session.update(lSubmission);
			session.update(vNew);
		}
		this.vals = null;

		// TODO implement
		this.valuesSlave = null;
		
		this.valuesSlave2 = null;
		
		this.valuesSlave3 = null;

		//Update related voyages when admn applies changes
		TextboxIntegerValue voyId = (TextboxIntegerValue) newValues.get("voyageid");
		updateVoyageId(session, lSubmission.getId(), voyId.getInteger());
		
		t.commit();
		session.close();
		return "back";
	}

	/**
	 * Action fired when accept button was pressed
	 */
	public String save()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session,
				this.submissionId);
		Map newValues = vals.getColumnValues(DECIDED_VOYAGE);
		
		
		//Editor updates the voyageid with the id assigned by admin user
		if (this.adminBean.getAuthenticateduser().isEditor() )
		{
			try {
				Voyage v = getAdminVoyage(session, this.submissionId);
				Integer voyid = v.getVoyageid();
				newValues.put("voyageid", new TextboxIntegerValue(voyid));
			} catch (Exception e) {}
						
		}
			


		if (updateMergedVoyage(session, lSubmisssion, newValues) == null)
		{
			t.rollback();
			session.close();
			return null;
		}

		if (this.adminBean.getAuthenticateduser().isAdmin() || this.adminBean.getAuthenticateduser().isChiefEditor())
		{
			TextboxIntegerValue voyageid= (TextboxIntegerValue)newValues.get("voyageid");
			Integer voyId = voyageid.getInteger();
			//Admin updates the other related versions of the submission (contrib, editor) with the selected voyageid
			updateVoyageId(session, this.submissionId, voyId);
		}
		
		
		t.commit();
		session.close();
		this.vals = null;
		return "back";
	}

	private Voyage updateMergedVoyage(Session session, Submission submission,
			Map newValues)
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = null;
		String[] SLAVE_CHAR_COLS_LABELS = null;
		String[] SLAVE_CHAR_ROWS = null;
		String[] SLAVE_CHAR_ROWS_LABELS = null;
		
		if (this.adminBean.getAuthenticateduser().isAdmin() || this.adminBean.getAuthenticateduser().isChiefEditor())
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_A;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_A;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_A;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_A;
		}
		else
		{
			SLAVE_CHAR_COLS = SLAVE_CHAR_COLS_E;
			SLAVE_CHAR_COLS_LABELS = SLAVE_CHAR_COLS_LABELS_E;
			SLAVE_CHAR_ROWS = SLAVE_CHAR_ROWS_E;
			SLAVE_CHAR_ROWS_LABELS = SLAVE_CHAR_ROWS_LABELS_E;
		}
		

		this.wasError = false;
		Voyage vNew = null;
		EditedVoyage editedVoyage = null;
		SubmissionEditor editor = null;
		if (this.adminBean.getAuthenticateduser().isEditor())
		{
			// we need to use editor's edited voyage
			editor = getSubmissionEditor(submission, this.adminBean
					.getAuthenticateduser());
			editor.setFinished(this.adminBean.getFinished().booleanValue());
			editedVoyage = editor.getEditedVoyage();
			if (editedVoyage != null)
			{
				vNew = editedVoyage.getVoyage();
			}
		} else if (submission instanceof SubmissionNew)
		{
			editedVoyage = ((SubmissionNew) submission).getEditorVoyage();
			if (editedVoyage != null)
			{
				vNew = editedVoyage.getVoyage();
			}
		} else if (submission instanceof SubmissionEdit)
		{
			editedVoyage = ((SubmissionEdit) submission).getEditorVoyage();
			if (editedVoyage != null)
			{
				vNew = editedVoyage.getVoyage();
			}
		} else
		{
			editedVoyage = ((SubmissionMerge) submission).getEditorVoyage();
			if (editedVoyage != null)
			{
				vNew = editedVoyage.getVoyage();
			}
		}
		if (vNew == null)
		{
			vNew = new Voyage();
		}
		Map notes = new HashMap();
		for (int i = 0; i < attrs.length; i++)
		{
			if(attrs[i] == null) {continue;}
			
			Value val = (Value) newValues.get(attrs[i].getName());
			
			if(val== null) {continue;}
			
			if (!val.isCorrectValue())
			{
				val.setErrorMessage("Value incorrect");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(session, val);

			if (vals != null) {
				for (int j = 0; j < vals.length; j++) {
					if (val instanceof DateValue) {
						if (!(vals[j] == (Object) 0))
							vNew.setAttrValue(attrs[i].getAttribute()[j]
									.getName(), vals[j]);
					} else {
						//Resize array to max attrs size for field
						//so it can handle deleting values if the list shrinks
						String firstAttrib = attrs[i].getAttribute()[0].getName();
						if (firstAttrib.equals("sourcea") || firstAttrib.equals("ownera")){
							vals = StringUtils.resizeArray(vals, attrs[i].getAttribute().length);
						}
						 
						vNew.setAttrValue(attrs[i].getAttribute()[j].getName(),
								vals[j]);
					}

				}
			}
			if (!StringUtils.isNullOrEmpty(val.getNote()))
			{
				notes.put(attrs[i].getName(), val.getNote());
			}
			
			//Validate voyageid
			if (this.adminBean.getAuthenticateduser().isAdmin() && attrs[i].getName().equals("voyageid"))
			{
				TextboxIntegerValue voyId = (TextboxIntegerValue) newValues.get("voyageid");
				
				if (submission instanceof SubmissionNew)
				{
					//Check for published voyages
					Voyage v1 = Voyage.loadByVoyageId(session, voyId.getIntValue());
										
					if(v1!=null)
					{
						wasError = true;
						val.setErrorMessage("This value has already been used by another voyage");
					}
					
					//Check for other new voyages that have not been published
					String Q = " FROM Voyage WHERE voyageid = " + voyId.getIntValue() +  " AND revision != 1";
					org.hibernate.Query query = session.createQuery(Q);
					List voyIdList = query.list();
					
					for(int v=0; v < voyIdList.size(); v++)
					{
						Voyage voy = (Voyage) voyIdList.get(v);
						
						Q = " FROM EditedVoyage where voyage_iid = " + voy.getIid();
						org.hibernate.Query query2 = session.createQuery(Q);
						List editList = query2.list();
						if(editList == null || editList.size() == 0) {continue;}
						EditedVoyage ev = (EditedVoyage) editList.get(0);
						Long id = ev.getId();
						
						Q = " FROM SubmissionNew where new_edited_voyage_id = " + id + " OR editor_edited_voyage_id = " + id ;
						org.hibernate.Query query3 = session.createQuery(Q);
						List newList = query3.list();
						if(newList == null || newList.size() == 0) {continue;}
						SubmissionNew sn = (SubmissionNew) newList.get(0);
						Long subId = sn.getId();
						
						if(!this.submissionId.equals(subId))
						{
							wasError = true;
							val.setErrorMessage("This value has already been used by another voyage");
						}
						
					}
					
					
					
				}
									
			}
		}
		
		for (int i = 0; i < SLAVE_CHAR_COLS.length; i++)
		{
			for (int j = 0; j < SLAVE_CHAR_ROWS.length; j++)
			{
				SubmissionAttribute attribute = SubmissionAttributes
						.getConfiguration().getAttribute(
								SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
				if (attribute == null)
				{
					continue;
					/*throw new RuntimeException(
							"SubmissionAttribute not found: "
									+ SLAVE_CHAR_ROWS[j] + ","
									+ SLAVE_CHAR_COLS[i]);*/
				}
				Value value = valuesSlave.getValue(SLAVE_CHAR_COLS[i],
						SLAVE_CHAR_ROWS[j]);
				if (!value.isCorrectValue())
				{
					value.setErrorMessage("Value incorrect");
					wasError = true;
				}
				if (value.hasEditableNote())
				{
					notes.put(attribute.getName(), value.getNote().trim());
				}
				Object[] vals = attribute.getValues(session, value);
				for (int k = 0; k < vals.length; k++)
				{
					vNew.setAttrValue(attribute.getAttribute()[k].getName(),
							vals[k]);
				}
			}
		}
		
		
		//This is for the 3rd grid
		for (int i = 0; i < SLAVE_CHAR_COLS_S.length; i++)
		{
			for (int j = 0; j < SLAVE_CHAR_ROWS_S.length; j++)
			{
				SubmissionAttribute attribute = SubmissionAttributes
						.getConfiguration().getAttribute(
								SLAVE_CHAR_ROWS_S[j] + "," + SLAVE_CHAR_COLS_S[i]);
				if (attribute == null)
				{
					continue;
					/*throw new RuntimeException(
							"SubmissionAttribute not found: "
									+ SLAVE_CHAR_ROWS[j] + ","
									+ SLAVE_CHAR_COLS[i]);*/
				}
				Value value = valuesSlave3.getValue(SLAVE_CHAR_COLS_S[i],
						SLAVE_CHAR_ROWS_S[j]);
				if (!value.isCorrectValue())
				{
					value.setErrorMessage("Value incorrect");
					wasError = true;
				}
				if (value.hasEditableNote())
				{
					notes.put(attribute.getName(), value.getNote().trim());
				}
				Object[] vals = attribute.getValues(session, value);
				for (int k = 0; k < vals.length; k++)
				{
					vNew.setAttrValue(attribute.getAttribute()[k].getName(),
							vals[k]);
				}
			}
		}

		vNew.setSuggestion(true);		
		
		if (!wasError)
		{
			/*if (this.adminBean.getAuthenticateduser().isAdmin()) {
				imputeVariables(session, vNew);
			}*/
			session.saveOrUpdate(vNew);
			if (editedVoyage == null)
			{
				editedVoyage = new EditedVoyage(vNew, null);
				editedVoyage.setAttributeNotes(notes);
				try{
				 session.save(editedVoyage);
				}catch(Exception e){
					System.out.println("Could not save this record due to either invalid data or your entry for a field is too long. The system reported cause is: " + e.getCause());
				}
			} else
			{
				editedVoyage.setVoyage(vNew);
				editedVoyage.setAttributeNotes(notes);
				session.update(editedVoyage);
			}
			if (submission instanceof SubmissionNew)
			{
				if (!this.adminBean.getAuthenticateduser().isEditor())
				{
					((SubmissionNew) submission).setEditorVoyage(editedVoyage);
					session.update(submission);
				} else
				{
					editor.setEditedVoyage(editedVoyage);
					session.update(editor);
				}
			} else if (submission instanceof SubmissionEdit)
			{
				TastDbConditions c = new TastDbConditions();
				c.addCondition(new SequenceAttribute(new Attribute[] {
						SubmissionEdit.getAttribute("oldVoyage"),
						EditedVoyage.getAttribute("voyage") }),
						((SubmissionEdit) submission).getOldVoyage()
								.getVoyage(), TastDbConditions.OP_EQUALS);
				TastDbQuery qValue = new TastDbQuery("SubmissionEdit", c);
				Object[] toUpdate = qValue.executeQuery(session);
				for (int i = 0; i < toUpdate.length; i++)
				{
					if (!this.adminBean.getAuthenticateduser().isEditor())
					{
						SubmissionEdit edit = (SubmissionEdit) toUpdate[i];
						edit.setEditorVoyage(editedVoyage);
						session.update(edit);
					} else
					{
						SubmissionEditor localeditor = this
								.getSubmissionEditor((Submission) toUpdate[i],
										this.adminBean.getAuthenticateduser());
						if (localeditor != null)
						{
							localeditor.setEditedVoyage(editedVoyage);
							session.update(localeditor);
						}
					}
				}
			} else
			{
				if (!this.adminBean.getAuthenticateduser().isEditor())
				{
					((SubmissionMerge) submission)
							.setEditorVoyage(editedVoyage);
					session.update(submission);
				} else
				{
					editor.setEditedVoyage(editedVoyage);
					session.update(editor);
				}
			}			
			
			return vNew;
		} else
		{
			return null;
		}
	}
	
	public void imputeVariables(Session sess, Voyage voyage) {
		VoyagesCalculation voyagesCalc = new VoyagesCalculation(sess, voyage);
		voyagesCalc.calculateImputedVariables();		
	}

	private SubmissionEditor getSubmissionEditor(Submission submission,
			User user)
	{
		for (Iterator iter = submission.getSubmissionEditors().iterator(); iter
				.hasNext();)
		{
			SubmissionEditor element = (SubmissionEditor) iter.next();
			if (element.getUser().getId().equals(user.getId()))
			{
				return element;
			}
		}
		return null;
	}

	public Long getSubmissionId()
	{
		return this.submissionId;
	}

	public void setRequiredReload(boolean requiredReload)
	{
		this.requiredReload = requiredReload;
		//this.requiredReloadSlave = requiredReload;
	}

	public void setValuesSlave(Values values)
	{
		this.valuesSlave = values;
	}

	public Values getValuesSlave()
	{
		if (this.valuesSlave == null || requiredReload)
		{
			requiredReload = false;
			fillInValues();
		}
		return this.valuesSlave;
	}
	
	
	public void setValuesSlave2(Values values)
	{
		this.valuesSlave2 = values;
	}

	public Values getValuesSlave2()
	{
		if (this.valuesSlave2 == null || requiredReload)
		{
			requiredReload = false;
			fillInValues();
		}
		return this.valuesSlave2;
	}
	
	public void setValuesSlave3(Values values)
	{
		this.valuesSlave3 = values;
	}

	public Values getValuesSlave3()
	{
		if (this.valuesSlave3 == null || requiredReload)
		{
			requiredReload = false;
			fillInValues();
		}
		return this.valuesSlave3;
	}
	
	public void updateVoyageId(Session sess, Long sub, Integer voyid)
	{
		Voyage v = null;
		
		try {
			v = Voyage.loadById(sess, ((SubmissionNew) Submission.loadById(sess, sub)).getNewVoyage().getVoyage().getIid());
		} catch (Exception e) {}
		
		if (v == null) {
			try {
				v = Voyage.loadById(sess, ((SubmissionEdit) Submission.loadById(sess, sub)).getEditorVoyage().getVoyage().getIid());
			} catch (Exception e) {}
		}
		
		if (v == null) {
			try {
				v = Voyage.loadById(sess, ((SubmissionMerge) Submission.loadById(sess, sub)).getProposedVoyage().getVoyage().getIid());
			} catch (Exception e) {}
		}
		
		//Set voyageid for contrib user
		v.setVoyageid(voyid);
		
		
		Set se = Submission.loadById(sess, sub).getSubmissionEditors();
		Object[] eds = se.toArray();
		
		for(int i = 0; i < eds.length; i++)
		{
			SubmissionEditor e = (SubmissionEditor) eds[i];
		
		
			if(e==null || e.getEditedVoyage()==null || e.getEditedVoyage().getVoyage() == null) {return;}
		
			Long id = e.getEditedVoyage().getVoyage().getIid();
			
			v = Voyage.loadById(sess, id);
		
			if(v==null) {return;}
		
		    //Set voyageid for editors
			v.setVoyageid(voyid);
		}
	
	
	
	}
	
	
	public Voyage getAdminVoyage(Session sess, Long sub)
	{
		Voyage v = null;
		
		try {
			v = Voyage.loadById(sess, ((SubmissionNew) Submission.loadById(sess, sub)).getEditorVoyage().getVoyage().getIid());
		} catch (Exception e) {}
		
		if (v == null) {
			try {
				v = Voyage.loadById(sess, ((SubmissionEdit) Submission.loadById(sess, sub)).getEditorVoyage().getVoyage().getIid());
			} catch (Exception e) {}
		}
		
		if (v == null) {
			try {
				v = Voyage.loadById(sess, ((SubmissionMerge) Submission.loadById(sess, sub)).getEditorVoyage().getVoyage().getIid());
			} catch (Exception e) {}
		}
		
		return v;
		
	}
	

}
