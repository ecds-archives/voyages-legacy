package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.web.UtilsJSF;

public class UIDetailComponent extends UIComponentBase {

	public String getFamily() {
		return null;
	}

	public void encodeBegin(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("table", this);
		writer.writeAttribute("class", "detail", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);

		TableData data = this.getData();	
		
		TableData.DataTableItem[] objs = data.getData();
		TableData.DataTableItem[] addObjs = data.getAdditionalData();
		VisibleColumn[] columns = data.getVisibleAttributes();
		VisibleColumn[] additionalColumns = data.getVisibleAdditionalAttributes();
		
		
		if (addObjs != null) {
			
			writer.startElement("th", this);
			writer.writeAttribute("class", "cellUpperLeft", null);
			writer.write(additionalColumns[0].toString());
			writer.endElement("th");							
			for (int j = 0; j < addObjs.length; j++) {
				writer.startElement("th", this);
				writer.writeAttribute("class", "cellHeader", null);
				Object[] row = addObjs[j].dataRow;
				if (row[0] != null) {
					writer.write(row[0].toString());
				}
				writer.endElement("th");
			}			
			
			
			for (int i = 1; i < additionalColumns.length; i++) {
				
				writer.startElement("tr", this);
				writer.startElement("td", this);
				writer.writeAttribute("class", "cellLeft", null);
				writer.write(additionalColumns[i].toString());				
				writer.endElement("td");
								
				for (int j = 0; j < addObjs.length; j++) {
					writer.startElement("td", this);
					writer.writeAttribute("class", "cellPlain", null);
					Object[] row = addObjs[j].dataRow;
					if (row[i] != null) {
						writer.write(row[i].toString());
					}
					writer.endElement("td");
				}
				
				writer.endElement("tr");

			}
		}
		if (objs != null) {
			for (int i = 0; i < columns.length; i++) {
				
				writer.startElement("tr", this);
				writer.startElement("td", this);
				writer.writeAttribute("class", "cellLeft", null);
				writer.write(columns[i].toString());
				writer.endElement("td");
								
				for (int j = 0; j < objs.length; j++) {
					boolean changed = false;
					Object[] row = objs[j].dataRow;
					if (j > 0) {
						Object[] oldRow = objs[j - 1].dataRow;
						Object old = oldRow[i];
						Object newO = row[i];
						if (!old.equals(newO)) {
							changed = true;
						}
					}
					if (j < objs.length - 1) {
						Object[] oldRow = objs[j + 1].dataRow;
						Object old = oldRow[i];
						Object newO = row[i];
						if (!old.equals(newO)) {
							changed = true;
						}
					}
					
					writer.startElement("td", this);
					if (changed) {
						writer.writeAttribute("class", "cellChanged", null);
					} else {
						writer.writeAttribute("class", "cellPlain", null);
					}
					
					if (row[i] != null) {
						writer.write(row[i].toString());
					}
					writer.endElement("td");
				}
				
				writer.endElement("tr");

			}
		}
		
		writer.endElement("table");

	}

	public void encodeEnd(FacesContext context) throws IOException {
	}

	public TableData getData() {
		ValueBinding vb = getValueBinding("data");
		if (vb == null)
			return null;
		return (TableData) vb.getValue(getFacesContext());
	}

}
