package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.web.SearchParameters;

/**
 * Component for presenting stats/charts.
 * Component is a div section. Also, it connects SearchBean with specific bean responsible for
 * performing desired action.
 * @author Pawel Jurczyk
 *
 */
public class UITimeLineResultTab extends UIOutput {
	
	public void appyConditions(Conditions c, FacesContext context) {
		// TODO Auto-generated method stub

	}
	
	public void applyPopulatedAttributes(String[] attributes, FacesContext context) {
		
	}
	

	/**
	 * Begin encodiung of object.
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		//Prepare div
		writer.startElement("div", this);
		String style = (String)getAttributes().get("style");
		if (style!=null)
		 	writer.writeAttribute("style", style, null);

		String styleClass = (String)getAttributes().get("styleClass");
		if (styleClass!=null)
			writer.writeAttribute("class", styleClass, null);
		
		//Load value bindings
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
			SearchParameters c = (SearchParameters) vb.getValue(context);
			vb = this.getValueBinding("conditionsOut");
			if (vb != null) {
				vb.setValue(context, c);
			}
		}
		
		// Start table (needed?)
		writer.startElement("table", this);
	}


	/**
	 * End encoding of object.
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("table");
		writer.endElement("div");
	}
}
