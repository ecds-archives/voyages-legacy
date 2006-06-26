package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.apache.myfaces.el.MethodBindingImpl;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.web.TabChangeEvent;
import edu.emory.library.tas.web.UtilsJSF;

public class UITableResultTab extends UIOutput {
	
	private MethodBinding sortChanged;
	
	public UITableResultTab() {
		super();
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[])state;
		super.restoreState(context, values[0]);
		sortChanged = (MethodBinding)restoreAttachedState(context, values[1]);
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, sortChanged);
		return values;
	}

	public void decode(FacesContext context) {
		super.decode(context);
		String newSelectedTabId = (String) context.getExternalContext().getRequestParameterMap().get(getHiddenFieldId(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0) {			
			queueEvent(new SortChangeEvent(this, newSelectedTabId));	
		}
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
		
		UtilsJSF.encodeHiddenInput(this, writer, getHiddenFieldId(context));
		
		writer.startElement("table", this);
		writer.writeAttribute("class", "grid", null);
		
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
				writer.write("<a href=\"#\"");
				writer.write(this.prepareJS(context, populatedAttributes[i]));
				writer.write(">");
				writer.write(populatedAttributes[i]);
				writer.write("</a>");
				writer.endElement("th");
			}
			writer.endElement("tr");
			
			StringBuffer rowClass = new StringBuffer();

			if (objs != null) {
				for (int i = 0; i < objs.length; i++) {
					
					rowClass.setLength(0);
					if (i % 2 == 0)
						rowClass.append("grid-row-even");
					else
						rowClass.append("grid-row-odd"); 
					if (i == 0)
						rowClass.append(" grid-row-first");
					if (i == objs.length - 1)
						rowClass.append(" grid-row-last");
					
					Object[] values = (Object[]) objs[i];
					writer.startElement("tr", this);
					writer.writeAttribute("class", rowClass.toString(), null);
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

	private String prepareJS(FacesContext context, String pressedLink) {
		StringBuffer buffer = new StringBuffer();
		String ID = getHiddenFieldId(context);
		buffer.append(" onclick=\"document.forms['form'].elements['" + ID + "'].value = '" 
				+ pressedLink + "';" +
				"clear_form();form.submit();\"");
		return buffer.toString();
	}

	private String getHiddenFieldId(FacesContext context) {
		return this.getClientId(context) + "__" + "HF";
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof SortChangeEvent && sortChanged != null)
			sortChanged.invoke(getFacesContext(), new Object[] {event});
		
	}

	public MethodBinding getSortChanged() {
		return sortChanged;
	}

	public void setSortChanged(MethodBinding sortChanged) {
		this.sortChanged = sortChanged;
	}
}
