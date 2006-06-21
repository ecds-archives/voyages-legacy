package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.util.query.Conditions;

public class UITimeLineResultTab extends UIOutput {
	
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
		
		String style = (String)getAttributes().get("style");
		if (style!=null)
		 	writer.writeAttribute("style", style, null);

		String styleClass = (String)getAttributes().get("styleClass");
		if (styleClass!=null)
			writer.writeAttribute("class", styleClass, null);
		
		ValueBinding vb = this.getValueBinding("rendered");
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(context);
			vb = this.getValueBinding("componentVisible");
			if (vb != null) {
				vb.setValue(context, b);
			}
		}
		
		vb = this.getValueBinding("conditions");
		if (vb != null) {
			Conditions c = (Conditions) vb.getValue(context);
			vb = this.getValueBinding("conditionsOut");
			if (vb != null) {
				vb.setValue(context, c);
			}
		}
		
		writer.startElement("table", this);
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
