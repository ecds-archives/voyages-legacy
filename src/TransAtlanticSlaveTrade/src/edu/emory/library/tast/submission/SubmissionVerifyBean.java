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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.util.JsfUtils;

public class SubmissionVerifyBean {

	private static final SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getPublicAttributes();

	private SubmissionBean submissionBean;

	private RowGroup[] rowGroups;

	private Set expandedRows = new HashSet();

	private Column[] columns;

	public SubmissionVerifyBean() {
		List rowGroupsList = new ArrayList();
		int j = 0;
		for (int i = 0; i < attrs.length; i++) {
			if (!rowGroupsList.contains(attrs[i].getGroupName())) {
				rowGroupsList.add(attrs[i].getGroupName());
				expandedRows.add(new Integer(j++));
			}
		}
		this.rowGroups = new RowGroup[rowGroupsList.size()];
		for (int i = 0; i < this.rowGroups.length; i++) {
			String rowGroup = (String) rowGroupsList.get(i);
			this.rowGroups[i] = new RowGroup(rowGroup, rowGroup);
		}

	}

	public Column[] getVerifyColumns() {
		if (this.submissionBean.getSubmissionType() == SubmissionBean.SUBMISSION_TYPE_NEW) {

			columns = new Column[] { new Column(SubmissionBean.CHANGED_VOYAGE,
					SubmissionBean.CONTRIBUTOR_LABEL, false) };

		} else if (this.submissionBean.getSubmissionType() == SubmissionBean.SUBMISSION_TYPE_EDIT) {

			columns = new Column[] {

					new Column(SubmissionBean.ORIGINAL_VOYAGE, "Voyageid "
							+ String.valueOf(this.submissionBean
									.getSelectedVoyageForEdit().getVoyageId()),
							true, false),

					new Column(SubmissionBean.CHANGED_VOYAGE,
							SubmissionBean.CONTRIBUTOR_LABEL, true, false) };

		} else if (this.submissionBean.getSubmissionType() == SubmissionBean.SUBMISSION_TYPE_MERGE) {

			Column columnstemp[] = new Column[this.submissionBean
					.getSelectedVoyagesForMerge().size() + 1];

			for (int i = 0; i < this.submissionBean
					.getSelectedVoyagesForMerge().size(); i++) {
				SelectedVoyageInfo info = (SelectedVoyageInfo) this.submissionBean
						.getSelectedVoyagesForMerge().get(i);
				columnstemp[i] = new Column(SubmissionBean.MERGED_VOYAGE_PREFIX
						+ i, "Voyageid " + String.valueOf(info.getVoyageId()),
						true, false);
			}

			columnstemp[this.submissionBean.getSelectedVoyagesForMerge().size()] = new Column(
					SubmissionBean.CHANGED_VOYAGE,
					SubmissionBean.CONTRIBUTOR_LABEL, true);

			columns = columnstemp;

		} else {
			JsfUtils.navigateTo("start");
			columns = null;
		}

		Column updatedColumn = new Column(
				columns[columns.length - 1].getName(),
				columns[columns.length - 1].getLabel(), true,
				columns[columns.length - 1].getCopyToColumn(),
				columns[columns.length - 1].getCopyToLabel());
		columns[columns.length - 1] = updatedColumn;
		return this.columns;
	}

	public Row[] getVerifyRows() {

		Row[] rows = new Row[attrs.length];

		for (int i = 0; i < rows.length; i++) {

			Row row = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i].getUserLabel(), attrs[i].getComment(),
					attrs[i].getGroupName(), false);

			row.setNoteEnabled(true);
			rows[i] = row;

		}

		return rows;
	}

	public Values getVerifyValues() {
		return submissionBean.getValues();
	}

	public Map getFieldTypes() {
		return SubmissionDictionaries.fieldTypes;
	}

	public RowGroup[] getVerifyRowGroups() {
		return this.rowGroups;
	}

	public Set getVerifyExpandedGridRows() {
		return this.expandedRows;
	}

	public SubmissionBean getSubmissionBean() {
		return submissionBean;
	}

	public void setSubmissionBean(SubmissionBean submissionBean) {
		this.submissionBean = submissionBean;
	}
	
	
	
	public Column[] getVerifyColumnsSlave() {
		return this.submissionBean.getColumnsSlave();
	}

	public Row[] getVerifyRowsSlave() {
		Row[] rows = null;
		int groupsNumber;
		if (this.submissionBean.getSubmissionType() == SubmissionBean.SUBMISSION_TYPE_EDIT) {
			groupsNumber = 2;
			rows = new Row[groupsNumber * SubmissionBean.SLAVE_CHAR_ROWS.length];
			for (int i = 0; i < SubmissionBean.SLAVE_CHAR_ROWS.length; i++) {
				rows[i] = new Row(TextboxIntegerAdapter.TYPE,
						SubmissionBean.SLAVE_CHAR_ROWS[i] + "_old",
						SubmissionBean.SLAVE_CHAR_ROWS_LABELS[i], null,true,
						"characteristics-old");
			}
		} else if (this.submissionBean.getSubmissionType() == SubmissionBean.SUBMISSION_TYPE_MERGE) {
			groupsNumber = this.submissionBean.getSelectedVoyagesForMerge()
					.size() + 1;
			rows = new Row[groupsNumber * SubmissionBean.SLAVE_CHAR_ROWS.length];
			int j = 0;
			for (Iterator iter = this.submissionBean
					.getSelectedVoyagesForMerge().iterator(); iter.hasNext();) {
				SelectedVoyageInfo element = (SelectedVoyageInfo) iter.next();
				for (int i = 0; i < SubmissionBean.SLAVE_CHAR_ROWS.length; i++) {
					rows[j++] = new Row(TextboxIntegerAdapter.TYPE,
							SubmissionBean.SLAVE_CHAR_ROWS[i] + "_"
									+ element.getVoyageId(),
							SubmissionBean.SLAVE_CHAR_ROWS_LABELS[i], null,true,
							"characteristics-" + element.getVoyageId());
				}
			}
		} else {
			groupsNumber = 1;
			rows = new Row[SubmissionBean.SLAVE_CHAR_ROWS.length];
		}
		for (int i = 0; i < SubmissionBean.SLAVE_CHAR_ROWS.length; i++) {
			rows[i + (groupsNumber - 1) * SubmissionBean.SLAVE_CHAR_ROWS.length] = new Row(
					TextboxIntegerAdapter.TYPE,
					SubmissionBean.SLAVE_CHAR_ROWS[i],
					SubmissionBean.SLAVE_CHAR_ROWS_LABELS[i], null,true,
					"characteristics");
			rows[i + (groupsNumber - 1) * SubmissionBean.SLAVE_CHAR_ROWS.length]
					.setNoteEnabled(true);
		}
		
		for (int i = 0; i < SubmissionBean.SLAVE_CHAR_ROWS.length; i++) {
			Row newRow = new Row(
					rows[rows.length - 1 - i].getType(), 
					rows[rows.length - 1 - i].getName(),
					rows[rows.length - 1 - i].getLabel(),
					rows[rows.length - 1 - i].getDescription(),
					true,
					rows[rows.length - 1 - i].getGroupName());
			newRow.setNoteEnabled(true);
			rows[rows.length - 1 - i] = newRow;
		}
		return rows;
	}

	public Values getVerifyValuesSlave() {
		return submissionBean.getValuesSlave();
	}

	public Map getFieldTypesSlave() {
		return SubmissionDictionaries.fieldTypes;
	}

	public RowGroup[] getVerifyRowGroupsSlave() {
		return this.submissionBean.getRowGroupsSlave();
	}

	public Set getVerifyExpandedGridRowsSlave() {
		Set expanded = new HashSet();
		for (int i = 0; i < this.submissionBean.getRowGroupsSlave().length; i++) {
			expanded.add(new Integer(i));
		}
		return expanded;
	}
	
	public void setVerifyValues(Values vals) {
	}
	
	public void setVerifyValuesSlave(Values vals) {		
	}
	
	public void setVerifyExpandedGridRowsSlave(Set vals) {
	}
	
	public void setVerifyExpandedGridRows(Set vals) {
	}
	
}
