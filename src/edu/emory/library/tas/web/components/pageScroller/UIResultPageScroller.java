package edu.emory.library.tas.web.components.pageScroller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Voyage;

public class UIResultPageScroller extends UIInput {
	
	public boolean getRendersChildren() {
		return true;
	}

	public void encodeBegin(FacesContext context) throws IOException {
		Integer resultFirst = null;
		Integer resultSize = null;
		Integer resultLast = null;
		
		ValueBinding vb = this.getValueBinding("resultFirst");
		if (vb != null) {
			resultFirst = (Integer)vb.getValue(context);
		}
		vb = this.getValueBinding("resultSize");
		if (vb != null) {
			resultSize = (Integer) vb.getValue(context);
			if (resultSize == null) {
				resultSize = new Integer(0);
			}
		}
		vb = this.getValueBinding("resultLast");
		if (vb != null) {
			resultLast = (Integer) vb.getValue(context);
		}
		
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.startElement("table", this);
		writer.startElement("tr", this);
		writer.startElement("td", this);
		if (resultFirst.intValue() + resultLast.intValue() == 0) {
			writer.write("Records from " + (resultFirst.intValue()) + 
				" to " + (resultFirst.intValue() + resultLast.intValue()) + " (of " + resultSize.intValue() + ") ");
		} else {
			writer.write("Records from " + (resultFirst.intValue() + 1) + 
					" to " + (resultFirst.intValue() + resultLast.intValue()) + " (of " + resultSize.intValue() + ") ");
		}
		writer.endElement("td");
	}
	
	

	public void encodeChildren(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		List children = this.getChildren();
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			writer.startElement("td", this);
			UIComponentBase component = (UIComponentBase)iter.next();
			component.encodeBegin(context);
			if (component.getRendersChildren()) {
				component.encodeChildren(context);
			}
			component.encodeEnd(context);
			writer.endElement("td");			
		}
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
	}
	
}
