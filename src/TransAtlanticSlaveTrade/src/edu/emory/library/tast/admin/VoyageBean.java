package edu.emory.library.tast.admin;

import java.util.ArrayList;
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
import edu.emory.library.tast.database.SourceInformationLookup;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.SubmissionAttribute;
import edu.emory.library.tast.submission.SubmissionAttributes;
import edu.emory.library.tast.submission.SubmissionDictionaries;

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
	}

	public String openVoyageAction() {
		return "edit";
	}

	public Column[] getColumns() {
		Column[] cols = new Column[2];
		cols[0] = new Column(COLUMN_OLD, COLUMN_OLD_LABEL, true);
		cols[1] = new Column(COLUMN_NEW, COLUMN_NEW_LABEL, false);
		return cols;
	}

	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(attrs[i].getType(), attrs[i].getName(), attrs[i].getUserLabel(), null, attrs[i].getGroupName(), false);
			rows[i].setNoteEnabled(true);
		}
		return rows;
	}

	public void setValues(Values vals) {
		this.values = vals;
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
			
			SourceInformationLookup sourceInfoUtils = SourceInformationLookup.createSourceInformationUtils(session);
			
			values = new Values();

			Voyage voyage = Voyage.loadById(session, this.rowId);
			for (int i = 0; i < attrs.length; i++) {
				SubmissionAttribute attribute = attrs[i];
				Object[] toBeFormatted = new Object[attribute.getAttribute().length];
				for (int j = 0; j < toBeFormatted.length; j++) {
					toBeFormatted[j] = voyage.getAttrValue(attribute.getAttribute()[j].getName());
				}
				Value valueOld = attrs[i].getValue(session, toBeFormatted, sourceInfoUtils);
				Value valueNew = attrs[i].getValue(session, toBeFormatted, sourceInfoUtils);
				values.setValue(COLUMN_OLD, attrs[i].getName(), valueOld);
				values.setValue(COLUMN_NEW, attrs[i].getName(), valueNew);
			}

			t.commit();
			session.close();

		}
		return values;
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
		};
		
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