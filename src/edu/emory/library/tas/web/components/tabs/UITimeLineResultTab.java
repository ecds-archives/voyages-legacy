package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.pageScroller.UIResultPageScroller;

public class UITimeLineResultTab extends UIOutput implements
		ConditionedTabbedComponent {

	public void appyConditions(Conditions c, FacesContext context) {
		// TODO Auto-generated method stub

	}
	
	public void applyPopulatedAttributes(String[] attributes, FacesContext context) {
		
	}
	
	public boolean getRendersChildren() {
		return true;
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.startElement("table", this);
		
		String style = (String)getAttributes().get("style");
		if (style!=null)
		 	writer.writeAttribute("style", style, null);

		String styleClass = (String)getAttributes().get("styleClass");
		if (styleClass!=null)
			writer.writeAttribute("class", styleClass, null);
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
		writer.endElement("table");
		writer.endElement("div");
	}

}
