package edu.emory.library.tast.common.table;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.attributes.Group;

/**
 * Component that presents daetailed information abour voyage.
 * @author Pawel Jurczyk
 *
 */
public class UIDetailComponent extends UIComponentBase {

	public String getFamily() {
		return null;
	}

	/**
	 * Encode begin of component.
	 */
	public void encodeBegin(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		
		//Encode table
		writer.startElement("table", this);
		writer.writeAttribute("class", "detail", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);

		//Get data to fill in table
		TableData data = this.getData();	
		
		//Voyages info
		TableData.DataTableItem[] objs = data.getData();
		
		//Columns of voyage
		VisibleAttributeInterface[] columns = data.getVisibleAttributes();
		
		Group[] groups = data.loadAttrGroups();
		
		List colsCollection = Arrays.asList(columns);
		for (int i = 0; i < groups.length; i++) {
			Group group = groups[i];
			
			writer.startElement("tr", this);
			writer.startElement("td", this);
			writer.writeAttribute("class", "detail-label-main", null);
			writer.writeAttribute("rowspan", String.valueOf(group.getAllVisibleAttributes().length), null);
			writer.write(group.getUserLabel());
			writer.endElement("td");
			
			for (int k = 0; k < group.getAllVisibleAttributes().length; k++) {
				VisibleAttributeInterface attr = group.getAllVisibleAttributes()[k];
				int index = colsCollection.indexOf(attr);
				
				if (k != 0) {
					writer.startElement("tr", this);
				}
				writer.startElement("td", this);
				writer.writeAttribute("class", "detail-label", null);
				writer.write(columns[index].toString());
				writer.endElement("td");
				for (int j = 0; j < objs.length; j++) {
					boolean changed = false;
					Object[] row = objs[j].dataRow;
					if (j > 0) {
						Object[] oldRow = objs[j - 1].dataRow;
						Object old = oldRow[index];
						Object newO = row[index];
						if (!old.equals(newO)) {
							changed = true;
						}
					}
					if (j < objs.length - 1) {
						Object[] oldRow = objs[j + 1].dataRow;
						Object old = oldRow[index];
						Object newO = row[index];
						if (!old.equals(newO)) {
							changed = true;
						}
					}
					
					writer.startElement("td", this);
					if (changed) {
						writer.writeAttribute("class", "detail-data-changed", null);
					} else {
						writer.writeAttribute("class", "detail-data", null);
					}
					
					if (row[i] != null) {
						writer.write(row[index].toString());
					}
					writer.endElement("td");
				}
				
			}
			
		}

		
		//Load info about voyages into table
//		if (objs != null) {
//			for (int i = 0; i < columns.length; i++) {
//				
//				writer.startElement("tr", this);
//				writer.startElement("td", this);
//				writer.writeAttribute("class", "detail-label", null);
//				writer.write(columns[i].toString());
//				writer.endElement("td");
//								
//				for (int j = 0; j < objs.length; j++) {
//					boolean changed = false;
//					Object[] row = objs[j].dataRow;
//					if (j > 0) {
//						Object[] oldRow = objs[j - 1].dataRow;
//						Object old = oldRow[i];
//						Object newO = row[i];
//						if (!old.equals(newO)) {
//							changed = true;
//						}
//					}
//					if (j < objs.length - 1) {
//						Object[] oldRow = objs[j + 1].dataRow;
//						Object old = oldRow[i];
//						Object newO = row[i];
//						if (!old.equals(newO)) {
//							changed = true;
//						}
//					}
//					
//					writer.startElement("td", this);
//					if (changed) {
//						writer.writeAttribute("class", "detail-data-changed", null);
//					} else {
//						writer.writeAttribute("class", "detail-data", null);
//					}
//					
//					if (row[i] != null) {
//						writer.write(row[i].toString());
//					}
//					writer.endElement("td");
//				}
//				
//				writer.endElement("tr");
//
//			}
//		}
		
		writer.endElement("table");

	}

	public void encodeEnd(FacesContext context) throws IOException {
	}

	/**
	 * Gets TableData object
	 * @return
	 */
	public TableData getData() {
		ValueBinding vb = getValueBinding("data");
		if (vb == null)
			return null;
		return (TableData) vb.getValue(getFacesContext());
	}

}
