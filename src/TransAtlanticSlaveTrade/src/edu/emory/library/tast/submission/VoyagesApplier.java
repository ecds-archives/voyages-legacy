package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
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

	public static final String[] SLAVE_CHAR_COLS = { "men", "women", "boy",
			"girl", "male", "female", "adult", "child", "infant" };
	public static final String[] SLAVE_CHAR_COLS_LABELS = { "Men", "Women",
			"Boys", "Girls", "Males", "Females", "Adults", "Children", "Infants" };
	public static final String[] SLAVE_CHAR_ROWS = { "e1", "e2", "e3", "died",
			"d1", "d2", "i1" };
	public static final String[] SLAVE_CHAR_ROWS_LABELS = {
			"Embarked slaves (first port)", "Embarked slaves (second port)",
			"Embarked slaves (third port)", "Died on voyage",
			"Disembarked slaves (first port)",
			"Disembarked slaves (second port)",
			"Slaves on arrival or departure*"};
			

	private static final String REMOVE_EDITOR_ACTION = "removeEditor";

	public static final String COPY_LABEL = "Copy";

	public static final String ORYGINAL_VOYAGE_LABEL = "Original voyage";

	public static final String CHANGED_VOYAGE_LABEL = "Suggested change";

	public static final String NEW_VOYAGE_LABEL = "Suggested voyage";

	public static final String CORRECTED_VOYAGE_LABEL = "Approved voyage";

	public static final String MERGE_VOYAGE_LABEL = "Voyage indicated to merge";

	public static final String DECIDED_VOYAGE_LABEL = "Final decision";

	public static final String DECIDED_VOYAGE = "decided";

	public static final String ORYGINAL_VOYAGE = "old";

	public static final String CHANGED_VOYAGE = "new";

	private static final String EDITOR_CHOICE = "editor_suggestion";

	private static final String EDITOR_CHOICE_LABEL = "Suggestion of";

	/**
	 * Attributes that are available for administrator/editor.
	 */
	private static SubmissionAttribute[] attrs = SubmissionAttributes
			.getConfiguration().getSubmissionAttributes();

	/**
	 * Indicator is error occured (if wrong value was entered into any field).
	 */
	private boolean wasError = false;

	/**
	 * Values for DataGrid component.
	 */
	private Values vals = null;

	private Values valuesSlave;

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
		for (int n = 0; n < toVals.length; n++)
		{
			for (int i = 0; i < attrs.length; i++)
			{
				SubmissionAttribute attribute = attrs[i];
				if (attrs[i].getName().equals("saild2")){
					System.out.println("attr:" + attrs[i].getName());
				}
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++)
				{
					toBeFormatted[j] = toVals[n].getAttrValue(attribute
							.getAttribute()[j].getName());
				}
				Value value = attrs[i].getValue(session, toBeFormatted,
						sourceInformationUtils);
				System.out.println("attr:" + attrs[i].getName());
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
						throw new RuntimeException(
								"SubmissionAttribute not found: "
										+ SLAVE_CHAR_ROWS[j] + ","
										+ SLAVE_CHAR_COLS[i]);
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
		}

		t.commit();
		session.close();
	}

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
							EDITOR_CHOICE_LABEL
									+ " "
									+ submissionEditor.getUser().getUserName()
									+ "("
									+ (submissionEditor.isFinished() ? "Finished"
											: "Not finished") + ")", true,
							DECIDED_VOYAGE, COPY_LABEL, false);
					cols[i++].setActions(new ColumnAction[] { new ColumnAction(
							REMOVE_EDITOR_ACTION + "_"
									+ submissionEditor.getId(), "Remove") });
				}
			}
			cols[i++] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false,
					true);
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
		cols[0] = new Column(ORYGINAL_VOYAGE, ORYGINAL_VOYAGE_LABEL, true,
				DECIDED_VOYAGE, COPY_LABEL, false);
		int i;
		for (i = 1; i <= this.editRequests.length; i++)
		{
			cols[i] = new Column(CHANGED_VOYAGE + "_" + i, CHANGED_VOYAGE_LABEL
					+ " #" + i, true, DECIDED_VOYAGE, COPY_LABEL, false);
		}
		if (!editor)
		{
			Iterator iter = submission.getSubmissionEditors().iterator();
			for (int j = 0; j < submission.getSubmissionEditors().size(); j++)
			{
				SubmissionEditor submissionEditor = (SubmissionEditor) iter
						.next();
				cols[i] = new Column(EDITOR_CHOICE + "_" + j,
						EDITOR_CHOICE_LABEL
								+ " "
								+ submissionEditor.getUser().getUserName()
								+ "("
								+ (submissionEditor.isFinished() ? "Finished"
										: "Not finished") + ")", true,
						DECIDED_VOYAGE, COPY_LABEL, false);
				cols[i++].setActions(new ColumnAction[] { new ColumnAction(
						REMOVE_EDITOR_ACTION + "_" + submissionEditor.getId(),
						"Remove") });
			}
		}
		cols[i++] = new Column(DECIDED_VOYAGE, DECIDED_VOYAGE_LABEL, false,
				true);
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
		if (!editor)
		{
			Iterator iter = submission.getSubmissionEditors().iterator();
			for (int i = 0; i < submission.getSubmissionEditors().size(); i++)
			{
				SubmissionEditor submissionEditor = (SubmissionEditor) iter
						.next();
				cols[i + 1] = new Column(EDITOR_CHOICE + "_" + i,
						EDITOR_CHOICE_LABEL
								+ " "
								+ submissionEditor.getUser().getUserName()
								+ "("
								+ (submissionEditor.isFinished() ? "Finished"
										: "Not finished") + ")", true,
						DECIDED_VOYAGE, COPY_LABEL, false);
				cols[i + 1].setActions(new ColumnAction[] { new ColumnAction(
						REMOVE_EDITOR_ACTION + "_" + submissionEditor.getId(),
						"Remove") });
			}
		}
		cols[cols.length - 1] = new Column(DECIDED_VOYAGE,
				DECIDED_VOYAGE_LABEL, false, true);
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
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++)
		{
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i]
					.getUserLabel(), null, attrs[i].getGroupName(), false);
			rows[i].setNoteEnabled(true);
		}
		return rows;
	}

	public Row[] getRowsSlave()
	{
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
					rows[i] = new Row(TextboxIntegerAdapter.TYPE,
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
							TextboxIntegerAdapter.TYPE,
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
						TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i],
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
									"Slaves (characteristics) [Suggested voyage]"),
							new RowGroup("characteristics",
									"Slaves (characteristics) [Final decision]") };
				} else
				{
					RowGroup[] response = new RowGroup[2 + submission
							.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Suggested voyage]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = 1; i < response.length - 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Final decision]");
					return response;
				}
			} else if (submission instanceof SubmissionEdit)
			{
				if (this.adminBean.getAuthenticateduser().isEditor())
				{
					RowGroup[] response = new RowGroup[2 + this.editRequests.length];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Original voyage]");
					for (int i = 1; i < response.length - 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Suggested change #"
										+ i + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Final decision]");
					return response;
				} else
				{
					RowGroup[] response = new RowGroup[2
							+ this.editRequests.length
							+ submission.getSubmissionEditors().size()];
					response[0] = new RowGroup("characteristics-0",
							"Slaves (characteristics) [Original voyage]");
					for (int i = 1; i < this.editRequests.length + 1; i++)
					{
						response[i] = new RowGroup("characteristics-" + i,
								"Slaves (characteristics) [Suggested change #"
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
								"Slaves (characteristics) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Final decision]");
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
							"Slaves (characteristics) [Suggested voyage]");
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Final decision]");
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
							"Slaves (characteristics) [Suggested voyage]");
					Iterator iter = submission.getSubmissionEditors()
							.iterator();
					for (int i = this.editRequests.length + 1; i < this.editRequests.length
							+ submission.getSubmissionEditors().size() + 1; i++)
					{
						SubmissionEditor editor = (SubmissionEditor) iter
								.next();
						response[i] = new RowGroup("characteristics-" + (i),
								"Slaves (characteristics) [Suggestion of "
										+ editor.getUser().getUserName() + "]");
					}
					response[response.length - 1] = new RowGroup(
							"characteristics",
							"Slaves (characteristics) [Final decision]");
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
				editRequests = new Long[rets.length];
				for (int i = 0; i < editRequests.length; i++)
				{
					editRequests[i] = ((SubmissionEdit) rets[i]).getNewVoyage()
							.getId();
				}
			} else if (lSubmission instanceof SubmissionMerge)
			{
				Set voyagesToMerge = ((SubmissionMerge) lSubmission)
						.getMergedVoyages();
				editRequests = new Long[voyagesToMerge.size()];
				Iterator iter = voyagesToMerge.iterator();
				int i = 0;
				while (iter.hasNext())
				{
					editRequests[i++] = ((EditedVoyage) iter.next()).getId();
				}
				this.mergeMainVoyage = ((SubmissionMerge) lSubmission)
						.getProposedVoyage().getId();
			}
		} else
		{
			this.submissionId = null;
		}
		this.vals = null;
		this.valuesSlave = null;
		t.commit();
		session.close();
	}

	public Boolean getRejectAvailable()
	{
		Boolean avail = null;
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Submission lSubmisssion = Submission.loadById(session,
				this.submissionId);
		avail = new Boolean(!lSubmisssion.isSolved());
		t.commit();
		session.close();
		return avail;

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

	public String submit()
	{
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Map newValues = vals.getColumnValues(DECIDED_VOYAGE);
		Submission lSubmission = Submission
				.loadById(session, this.submissionId);

		Voyage vNew = null;
		Voyage mergedVoyage = updateMergedVoyage(session, lSubmission,
				newValues);

		if (mergedVoyage == null)
		{
			t.commit();
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
		for (int i = 0; i < attrs.length; i++)
		{
			Value val = (Value) newValues.get(attrs[i].getName());

			if (!val.isCorrectValue())
			{
				val.setErrorMessage("Error in value!");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(null, val);

			if (attrs[i].getName().equals("voyageid") && vals[0] == null)
			{
				val.setErrorMessage("This field is required!");
				wasError = true;
			}

			for (int j = 0; j < vals.length; j++)
			{
				vNew
						.setAttrValue(attrs[i].getAttribute()[j].getName(),
								vals[j]);
			}
		}
		if (!wasError)
		{
			vNew.saveOrUpdate();
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

		if (updateMergedVoyage(session, lSubmisssion, newValues) == null)
		{
			t.commit();
			session.close();
			return null;
		}

		t.commit();
		session.close();
		this.vals = null;
		return "back";
	}

	private Voyage updateMergedVoyage(Session session, Submission submission,
			Map newValues)
	{

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
			Value val = (Value) newValues.get(attrs[i].getName());
			if (!val.isCorrectValue())
			{
				val.setErrorMessage("Value incorrect");
				wasError = true;
			}
			Object[] vals = attrs[i].getValues(session, val);

			if(vals!=null){
			for (int j = 0; j < vals.length; j++)
			{
				vNew
						.setAttrValue(attrs[i].getAttribute()[j].getName(),
								vals[j]);
			}
			}
			if (!StringUtils.isNullOrEmpty(val.getNote()))
			{
				notes.put(attrs[i].getName(), val.getNote());
			}
			if (attrs[i].getName().equals("voyageid")
					&& vNew.getVoyageid() == null)
			{
				wasError = true;
				val
						.setErrorMessage("This field is required and has to be a number");
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
					throw new RuntimeException(
							"SubmissionAttribute not found: "
									+ SLAVE_CHAR_ROWS[j] + ","
									+ SLAVE_CHAR_COLS[i]);
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

		vNew.setSuggestion(true);
		if (!wasError)
		{
			session.saveOrUpdate(vNew);
			if (editedVoyage == null)
			{
				editedVoyage = new EditedVoyage(vNew, null);
				editedVoyage.setAttributeNotes(notes);
				session.save(editedVoyage);
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

}
