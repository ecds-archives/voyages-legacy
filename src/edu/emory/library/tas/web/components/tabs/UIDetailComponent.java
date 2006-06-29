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
		writer.writeAttribute("border", "1", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);

		TableData data = this.getData();

		writer.startElement("th", this);
		writer.endElement("th");		
		
		TableData.DataTableItem[] objs = data.getData();
		VisibleColumn[] columns = data.getVisibleAttributes();
		
		if (objs != null) {
			for (int i = 0; i < columns.length; i++) {
				
				writer.startElement("tr", this);
				writer.startElement("td", this);
				writer.write(columns[i].toString());
				writer.endElement("td");
								
				for (int j = 0; j < objs.length; j++) {
					writer.startElement("td", this);
					Object[] row = objs[j].dataRow;
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
