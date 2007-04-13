package edu.emory.library.tast.submissions;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.grideditor.Column;
import edu.emory.library.tast.common.grideditor.Row;
import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.Values;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SubmissionBean {
	
	public static final String ORYGINAL_VOYAGE = TastResource.getText("submissions_oryginal_voyage");
	public static final String CHANGED_VOYAGE = TastResource.getText("submissions_changed_voyage");
	
	private static SubmissionAttribute[] attrs = SubmissionAttributes.getConfiguration().getSubmissionAttributes(); 
	private long voyageId = -1;
	
	
	public void setVoyageId(long voyageId) {
		this.voyageId = voyageId;
	}
	
	public Values getOriginalValues() {
		if (this.voyageId == -1) {
			throw new RuntimeException("Voyage ID was not set!");
		}
		
		Values vals = new Values();
		
		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("voyageId"), new Long(voyageId), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Voyage", c);
		for (int i = 0; i < attrs.length; i++) {
			Attribute[] attributes = attrs[i].getAttribute();
			for (int j = 0; j < attributes.length; j++) {
				qValue.addPopulatedAttribute(attributes[j]);
			}
		}
		Object[] res = qValue.executeQuery();
		if (res.length == 0) {
			return null;
		}
		
		Object[] voyageAttrs = (Object[])res[0];
		int index = 0;
		for (int i = 0; i < attrs.length; i++) {
			SubmissionAttribute attribute = attrs[i];
			Object[] toBeFormatted = new Object[attribute.getAttribute().length];
			for (int j = 0; j < toBeFormatted.length; j++) {
				toBeFormatted[j] = voyageAttrs[j + index];
			}
			vals.setValue(ORYGINAL_VOYAGE, attrs[i].getUserLabel(), attrs[i].getValue(toBeFormatted));
			
			index += attribute.getAttribute().length;
		}
		
		return null;
		
	}
		
	public Values getChangedValues() {
		if (this.voyageId == -1) {
			throw new RuntimeException("Voyage ID was not set!");
		}
		return null;
	}
	
	public Column[] getColumns() {
		Column[] cols = new Column[2];
		cols[0] = new Column(ORYGINAL_VOYAGE, ORYGINAL_VOYAGE, false);
		cols[1] = new Column(CHANGED_VOYAGE, CHANGED_VOYAGE, true);
		return cols;
	}
	
	public Row[] getRows() {
		Row[] rows = new Row[attrs.length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(TextboxAdapter.TYPE, attrs[i].getName(), attrs[i].getUserLabel());
		}
		return rows;
	}
		
		
	public static void main(String[] args) {
		SubmissionBean bean = new SubmissionBean();
		bean.setVoyageId(1000);
		System.out.println(bean.getOriginalValues());
	}
}
