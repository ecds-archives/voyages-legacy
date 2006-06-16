package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.util.query.Conditions;

public class UITableResultTab extends UIOutput implements
		ConditionedTabbedComponent {

	public UITableResultTab() {
		super();
	}

	public void appyConditions(Conditions c, FacesContext context) {
		ValueBinding vb = this.getValueBinding("condition");
		if (vb != null) {
			vb.setValue(context, c);
		}
	}

	public void applyPopulatedAttributes(String[] attrs, FacesContext context) {
		ValueBinding vb = this.getValueBinding("populatedAttributes");
		if (vb != null) {
			vb.setValue(context, attrs);
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {
		Object[] objs = null;
		String[] populatedAttributes = null;
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.startElement("table", this);
		
		String style = (String)getAttributes().get("style");
		if (style!=null)
		 	writer.writeAttribute("style", style, null);

		String styleClass = (String)getAttributes().get("styleClass");
		if (styleClass!=null)
			writer.writeAttribute("class", styleClass, null);

		ValueBinding vb = this.getValueBinding("populatedAttributes");
		if (vb != null) {
			populatedAttributes = (String[]) vb.getValue(context);
		}
		
		vb = this.getValueBinding("rendered");
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
		
		vb = this.getValueBinding("results");
		if (vb != null) {
			objs = (Object[]) vb.getValue(context);
		}

		writer.startElement("tr", this);
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				writer.startElement("th", this);
				writer.write(populatedAttributes[i]);
				writer.endElement("th");
			}
			writer.endElement("tr");

			if (objs != null) {
				for (int i = 0; i < objs.length; i++) {
					Object[] values = (Object[]) objs[i];
					writer.startElement("tr", this);
					for (int j = 0; j < values.length; j++) {
						writer.startElement("td", this);
						Object obj = values[j];
						if (obj != null) {
							writer.write(obj.toString());
						}

						writer.endElement("td");
					}
					
					writer.endElement("tr");
				}
			}
		}

		writer.endElement("table");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
}
