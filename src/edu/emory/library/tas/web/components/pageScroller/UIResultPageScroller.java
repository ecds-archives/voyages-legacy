package edu.emory.library.tas.web.components.pageScroller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

/**
 * UIComponent responsible for showing current rows range
 * @author Pawel Jurczyk
 *
 */
public class UIResultPageScroller extends UIInput {
	
	public boolean getRendersChildren() {
		return true;
	}

	/**
	 * Begin of component encoding.
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		Integer resultFirst = null;
		Integer resultSize = null;
		Integer resultLast = null;
		
		//get value binding for resultFirst
		ValueBinding vb = this.getValueBinding("resultFirst");
		if (vb != null) {
			resultFirst = (Integer)vb.getValue(context);
		}
		
		//get value binding for resultSize
		vb = this.getValueBinding("resultSize");
		if (vb != null) {
			resultSize = (Integer) vb.getValue(context);
			if (resultSize == null) {
				resultSize = new Integer(0);
			}
		}
		
		//get value binding for resultLast
		vb = this.getValueBinding("resultLast");
		if (vb != null) {
			resultLast = (Integer) vb.getValue(context);
		}
		
		//prepare object encoding
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
	
	
	/**
	 * Encoding of child components.
	 */
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

	/**
	 * End of component encoding.
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
	}
	
}
