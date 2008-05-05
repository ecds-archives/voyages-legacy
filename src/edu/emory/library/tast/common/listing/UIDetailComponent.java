package edu.emory.library.tast.common.listing;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.common.listing.TableData.ColumnData;
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
						//writer.write(row[index].toString());
						TableData.ColumnData columnData = (ColumnData) row[index];
					
						if (columnData != null) {
							String[] formatted = columnData.getDataToDisplay();
							String[] rollovers = columnData.getRollovers();
							
							writer.startElement("table", this);
							writer.writeAttribute("cellspacing", "0", null);
							writer.writeAttribute("border", "0", null);
							writer.writeAttribute("cellpadding", "0", null);
							writer.writeAttribute("class", "multiline-attr-table", null);
							for (int kk = 0; kk < formatted.length; kk++) {
								writer.startElement("tr", this);
								writer.startElement("td", this);
								writer.writeAttribute("id", "cell_" + i + "_" + j + "_" + kk, null);
								if (j == 0) writer.writeAttribute("class", "grid-first-column", null);
								
//								if (populatedAttributes[j].getType().equals("NumericAttribute") && j != 0) {
//									writer.writeAttribute("style", "text-align: right", null);
//								}
								
								if (rollovers[kk] != null) {
									
									writer.writeAttribute("onmouseover", "showToolTip('" + "tooltip_" + i + "_" + j + "_" + kk + "', " + "'"
											+ "cell_" + i + "_" + j + "_" + kk + "')", null);
									writer.writeAttribute("onmouseout", "hideToolTip('" + "tooltip_" + i + "_" + j + "_" + kk + "')", null);
									writer.startElement("div", this);
									writer.writeAttribute("id", "tooltip_" + i + "_" + j + "_" + kk, null);
									writer.writeAttribute("class", "grid-tooltip", null);
									
									writer.startElement("table", this);
									writer.writeAttribute("cellspacing", "0", null);
									writer.writeAttribute("border", "0", null);
									writer.writeAttribute("cellpadding", "0", null);
									
									writer.startElement("tr", this);
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-11", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-12", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-13", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");								
									writer.endElement("tr");
									
									writer.startElement("tr", this);
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-21", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-22", null);								
									writer.startElement("div", this);
									writer.write(rollovers[kk]);
									writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-23", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");								
									writer.endElement("tr");
									
									writer.startElement("tr", this);
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-31", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-32", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");
									writer.startElement("td", this);
									writer.writeAttribute("class", "bubble-33", null);
									writer.startElement("div", this);writer.endElement("div");
									writer.endElement("td");								
									writer.endElement("tr");
									
									writer.endElement("table");
									
									writer.endElement("div");
									
								}
								
								if (formatted[kk] != null) {						
									writer.write(formatted[kk]);						
								}
								
								writer.endElement("td");
								writer.endElement("tr");
							}
							writer.endElement("table");												
						}
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
