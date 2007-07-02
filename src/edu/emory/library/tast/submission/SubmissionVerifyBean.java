package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.RowGroup;
import edu.emory.library.tast.common.grideditor.Values;

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
		columns = this.submissionBean.getColumns();
		Column updatedColumn = new Column(columns[columns.length - 1].getName(),
				columns[columns.length - 1].getLabel(), true, columns[columns.length - 1].getCopyToColumn(),
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
		Row[] rows = this.submissionBean.getRowsSlave();
		for (int i = 0; i < this.submissionBean.SLAVE_CHAR_ROWS.length; i++) {
			Row newRow = new Row(
					rows[rows.length - 1 - i].getType(), 
					rows[rows.length - 1 - i].getName(),
					rows[rows.length - 1 - i].getLabel(),
					rows[rows.length - 1 - i].getDescription(),
					rows[rows.length - 1 - i].getGroupName(),
					true,
					rows[rows.length - 1 - i].getCopyToLabel(),
					rows[rows.length - 1 - i].getCopyToRow());
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
