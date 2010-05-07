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
package edu.emory.library.tast.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.database.SourceInformationLookup;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEdit;
import edu.emory.library.tast.dm.SubmissionEditor;
import edu.emory.library.tast.dm.SubmissionMerge;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;
import edu.emory.library.tast.submission.SubmissionAttribute;
import edu.emory.library.tast.submission.SubmissionAttributes;
import edu.emory.library.tast.submission.SubmissionDictionaries;
import edu.emory.library.tast.submission.VoyagesApplier;

/**
 * 
 *
 */
public class VoyageBean {

	private static final String COLUMN_NEW_LABEL = "New values";

	private static final String COLUMN_OLD_LABEL = "Current values";

	private static final String COLUMN_NEW = "new";

	private static final String COLUMN_OLD = "old";

	private static SubmissionAttribute attrs[] = SubmissionAttributes.getConfiguration().getSubmissionAttributes();

	private VoyagesListBean listBean;

	private Long rowId;

	private Values values;
	
	private Values valuesSlave;
	
	private Values valuesSlave3;

	private RowGroup[] rowGroups;


	
	public VoyageBean() {
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

	public void openVoyage(GridOpenRowEvent event) {
		this.rowId = new Long(event.getRowId());
		this.values = null;
		this.valuesSlave = null;
		this.valuesSlave3 = null;
	}

	public String openVoyageAction() {
		return "edit";
	}

	public Column[] getColumns() {
		Column[] cols = new Column[1];
//		cols[0] = new Column(COLUMN_OLD, COLUMN_OLD_LABEL, true);
		cols[0] = new Column(COLUMN_NEW, COLUMN_NEW_LABEL, false);
		return cols;
	}

	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i].getUserLabel(), null, attrs[i].getGroupName(), false);
			rows[i].setNoteEnabled(false);
		}
		return rows;
	}
	
	
	public Row[] getRowsSlave() {

		String[] SLAVE_CHAR_COLS = VoyagesApplier.SLAVE_CHAR_COLS_A;
		String[] SLAVE_CHAR_COLS_LABELS = VoyagesApplier.SLAVE_CHAR_COLS_LABELS_A;
		String[] SLAVE_CHAR_ROWS = VoyagesApplier.SLAVE_CHAR_ROWS_A;
		String[] SLAVE_CHAR_ROWS_LABELS = VoyagesApplier.SLAVE_CHAR_ROWS_LABELS_A;

		Row[] rows = null;

		rows = new Row[SLAVE_CHAR_ROWS.length];
		for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
			rows[i] = new Row(TextboxIntegerAdapter.TYPE, SLAVE_CHAR_ROWS[i],
					SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics-0", false);
			rows[i].setNoteEnabled(false);
		}

		return rows;

	}

	public Row[] getRowsSlave3() {
		// Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = VoyagesApplier.SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = VoyagesApplier.SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = VoyagesApplier.SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = VoyagesApplier.SLAVE_CHAR_ROWS_LABELS_S;

		Row[] rows = null;

		rows = new Row[SLAVE_CHAR_ROWS.length];
		for (int i = 0; i < SLAVE_CHAR_ROWS.length; i++) {
			rows[i] = new Row(TextboxDoubleAdapter.TYPE, SLAVE_CHAR_ROWS[i],
					SLAVE_CHAR_ROWS_LABELS[i], null, "characteristics-0", false);
			rows[i].setNoteEnabled(false);
		}
		return rows;
	}

	public Column[] getColumnsSlave() {
		// Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = null;
		String[] SLAVE_CHAR_COLS_LABELS = null;
		String[] SLAVE_CHAR_ROWS = null;
		String[] SLAVE_CHAR_ROWS_LABELS = null;

		SLAVE_CHAR_COLS = VoyagesApplier.SLAVE_CHAR_COLS_A;
		SLAVE_CHAR_COLS_LABELS = VoyagesApplier.SLAVE_CHAR_COLS_LABELS_A;
		SLAVE_CHAR_ROWS = VoyagesApplier.SLAVE_CHAR_ROWS_A;
		SLAVE_CHAR_ROWS_LABELS = VoyagesApplier.SLAVE_CHAR_ROWS_LABELS_A;

		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}
	
	public Column[] getColumnsSlave3()
	{
		//Choose Admin or Editor Setup for Slave grids
		String[] SLAVE_CHAR_COLS = VoyagesApplier.SLAVE_CHAR_COLS_S;
		String[] SLAVE_CHAR_COLS_LABELS = VoyagesApplier.SLAVE_CHAR_COLS_LABELS_S;
		String[] SLAVE_CHAR_ROWS = VoyagesApplier.SLAVE_CHAR_ROWS_S;
		String[] SLAVE_CHAR_ROWS_LABELS = VoyagesApplier.SLAVE_CHAR_ROWS_LABELS_S;

		
		
		Column[] cols = new Column[SLAVE_CHAR_COLS.length];
		for (int i = 0; i < cols.length; i++)
		{
			cols[i] = new Column(SLAVE_CHAR_COLS[i], SLAVE_CHAR_COLS_LABELS[i]);
		}
		return cols;
	}

	
	public RowGroup[] getRowGroupsSlave() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		RowGroup[] response = new RowGroup[1];
		try {

				response[0] = new RowGroup("characteristics-0",
						"Slaves (characteristics)");	

		} finally {
			t.commit();
			session.close();
		}

		return response;
	}
	
	public RowGroup[] getRowGroupsSlave3() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		RowGroup[] response = new RowGroup[1];
		try {

			response[0] = new RowGroup("characteristics-0",
					"Slaves (age and sex)");

		} finally {
			t.commit();
			session.close();
		}
		return response;
	}

	public void setValues(Values vals) {
		this.values = vals;
	}
	
	public void setValuesSlave(Values values)
	{
		this.valuesSlave = values;
	}
	
	public void setValuesSlave3(Values values)
	{
		this.valuesSlave3 = values;
	}

	
	public Values getValuesSlave()
	{
		return this.valuesSlave;
	}
	
	public Values getValuesSlave3()
	{
		return this.valuesSlave3;
	}

	public Map getFieldTypes() {
		return SubmissionDictionaries.fieldTypes;
	}
	
	

	public RowGroup[] getRowGroups() {
		return this.rowGroups;
	}

	public Values getValues() {
		if (values == null) {
			Session session = HibernateConn.getSession();
			Transaction t = session.beginTransaction();

			SourceInformationLookup sourceInfoUtils = SourceInformationLookup
					.createSourceInformationUtils(session);

			values = new Values();
			valuesSlave = new Values();
			valuesSlave3 = new Values();
			Voyage voyage = Voyage.loadById(session, this.rowId);
			for (int i = 0; i < attrs.length; i++) {
				SubmissionAttribute attribute = attrs[i];
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++) {
					toBeFormatted[j] = voyage.getAttrValue(attribute
							.getAttribute()[j].getName());
				}
				// Value valueOld = attrs[i].getValue(session, toBeFormatted,
				// sourceInfoUtils);
				Value valueNew = attrs[i].getValue(session, toBeFormatted,
						sourceInfoUtils);
				// values.setValue(COLUMN_OLD, attrs[i].getName(), valueOld);
				values.setValue(COLUMN_NEW, attrs[i].getName(), valueNew);
			}

			for (int i = 0; i < VoyagesApplier.SLAVE_CHAR_COLS_A.length; i++) {
				for (int j = 0; j < VoyagesApplier.SLAVE_CHAR_ROWS_A.length; j++) {
					SubmissionAttribute attribute = SubmissionAttributes
							.getConfiguration()
							.getAttribute(
									VoyagesApplier.SLAVE_CHAR_ROWS_A[j]
											+ ","
											+ VoyagesApplier.SLAVE_CHAR_COLS_A[i]);
					if (attribute == null) {

						continue;
						/*
						 * throw new RuntimeException(
						 * "SubmissionAttribute not found: " +
						 * SLAVE_CHAR_ROWS[j] + "," + SLAVE_CHAR_COLS[i]);
						 */
					}
					Object[] toBeFormatted = new Object[attribute
							.getAttribute().length];
					for (int k = 0; k < toBeFormatted.length; k++) {
						toBeFormatted[k] = voyage.getAttrValue(attribute
								.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted,
							sourceInfoUtils);

					valuesSlave.setValue(VoyagesApplier.SLAVE_CHAR_COLS_A[i],
							VoyagesApplier.SLAVE_CHAR_ROWS_A[j], value);
				}
			}
			
			for (int i = 0; i < VoyagesApplier.SLAVE_CHAR_COLS_S.length; i++)
			{
				for (int j = 0; j < VoyagesApplier.SLAVE_CHAR_ROWS_S.length; j++)
				{
					SubmissionAttribute attribute = SubmissionAttributes
							.getConfiguration().getAttribute(
									VoyagesApplier.SLAVE_CHAR_ROWS_S[j] + ","
											+ VoyagesApplier.SLAVE_CHAR_COLS_S[i]);
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
						toBeFormatted[k] = voyage.getAttrValue(attribute
								.getAttribute()[k].getName());
					}
					Value value = attribute.getValue(session, toBeFormatted,
							sourceInfoUtils);
//					value.setNote((String) attributeNotes[n].get(attribute
//							.getName()));
					valuesSlave3.setValue(VoyagesApplier.SLAVE_CHAR_COLS_S[i], VoyagesApplier.SLAVE_CHAR_ROWS_S[j]
							, value);
				}
			}

			t.commit();
			session.close();

		}
		return values;
	}
	
	public Map getFieldTypesSlave() {
		return SubmissionDictionaries.simpleFieldTypes;
	}
	
	public Map getFieldTypesSlave3() {
		return SubmissionDictionaries.simpleFieldTypes;
	}
	

	public VoyagesListBean getListBean() {
		return listBean;
	}

	public void setListBean(VoyagesListBean listBean) {
		this.listBean = listBean;
	}
	
	public String save() {		
		boolean wasError = false;		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Voyage vNew = Voyage.loadById(session, this.rowId);		
		if (vNew == null) {
			return "save";
		}
		
		Map newValues = values.getColumnValues(COLUMN_NEW);
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
			if (attrs[i].getName().equals("voyageid") && 
					vNew.getVoyageid() == null) {
				wasError = true;
				val.setErrorMessage("This field is required");
			}
		}
		
		for (int i = 0; i < VoyagesApplier.SLAVE_CHAR_COLS_A.length; i++) {
			for (int j = 0; j < VoyagesApplier.SLAVE_CHAR_ROWS_A.length; j++) {
				SubmissionAttribute attribute = SubmissionAttributes
						.getConfiguration().getAttribute(
								VoyagesApplier.SLAVE_CHAR_ROWS_A[j] + ","
										+ VoyagesApplier.SLAVE_CHAR_COLS_A[i]);
				if (attribute == null) {
					continue;
					/*
					 * throw new RuntimeException(
					 * "SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] +
					 * "," + SLAVE_CHAR_COLS[i]);
					 */
				}
				Value value = valuesSlave.getValue(
						VoyagesApplier.SLAVE_CHAR_COLS_A[i],
						VoyagesApplier.SLAVE_CHAR_ROWS_A[j]);
				if (value != null) {
					if (!value.isCorrectValue()) {
						value.setErrorMessage("Value incorrect");
						wasError = true;
					}

					Object[] vals = attribute.getValues(session, value);
					for (int k = 0; k < vals.length; k++) {
						vNew.setAttrValue(
								attribute.getAttribute()[k].getName(), vals[k]);
					}
				}
			}
		}
		
		for (int i = 0; i < VoyagesApplier.SLAVE_CHAR_COLS_S.length; i++) {
			for (int j = 0; j < VoyagesApplier.SLAVE_CHAR_ROWS_S.length; j++) {
				SubmissionAttribute attribute = SubmissionAttributes
						.getConfiguration().getAttribute(
								VoyagesApplier.SLAVE_CHAR_ROWS_S[j] + ","
										+ VoyagesApplier.SLAVE_CHAR_COLS_S[i]);
				if (attribute == null) {
					continue;
					/*
					 * throw new RuntimeException(
					 * "SubmissionAttribute not found: " + SLAVE_CHAR_ROWS[j] +
					 * "," + SLAVE_CHAR_COLS[i]);
					 */
				}
				Value value = valuesSlave3.getValue(
						VoyagesApplier.SLAVE_CHAR_COLS_S[i],
						VoyagesApplier.SLAVE_CHAR_ROWS_S[j]);
				if (value != null) {
					if (!value.isCorrectValue()) {
						value.setErrorMessage("Value incorrect");
						wasError = true;
					}

					Object[] vals = attribute.getValues(session, value);
					for (int k = 0; k < vals.length; k++) {
						vNew.setAttrValue(
								attribute.getAttribute()[k].getName(), vals[k]);
					}
				}
			}
		}

		if (!wasError) {
			vNew.saveOrUpdate();
		}

		t.commit();
		session.close();

		return "save";
	}
	
	public String delete() {
		
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Voyage vNew = Voyage.loadById(session, this.rowId);	
		if (vNew == null) {
			return "main-menu";
		}
		session.delete(vNew);
		t.commit();
		session.close();
		
		return "main-menu";
	}

	public boolean isEditValid() {
		return this.rowId != null;
	}

	public boolean isDeleteValid() {
		return this.rowId != null;
	}

}