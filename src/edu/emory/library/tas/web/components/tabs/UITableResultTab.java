package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
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
		
		Map params = context.getExternalContext().getRequestParameterMap();
		
		String newSelectedTabId = (String) params.get(getSortHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0) {			
			queueEvent(new SortChangeEvent(this, newSelectedTabId));	
		}

		String newSelectedVoyageId = (String) params.get(getClickIdHiddenFieldName(context));
		if (newSelectedVoyageId != null && newSelectedVoyageId.length() > 0) {			
			queueEvent(new ResultsRowClickedEvent(this, new Long(newSelectedVoyageId)));	
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
		Integer orderColumn = null;
		Integer order = null;
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		
		UtilsJSF.encodeHiddenInput(this, writer, getSortHiddenFieldName(context));
		UtilsJSF.encodeHiddenInput(this, writer, getClickIdHiddenFieldName(context));
		
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

		vb = this.getValueBinding("orderColumn");
		if (vb != null) {
			orderColumn = (Integer) vb.getValue(context);
		}
		vb = this.getValueBinding("order");
		if (vb != null) {
			order = (Integer) vb.getValue(context);
		}
		
		UIForm form = UtilsJSF.getForm(this, context);
		
		writer.startElement("tr", this);
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {
				
				String jsSort = UtilsJSF.generateSubmitJS(
						context, form,
						getSortHiddenFieldName(context),
						populatedAttributes[i]);
				
				writer.startElement("th", this);
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.writeAttribute("onclick", jsSort, null);
				writer.write(populatedAttributes[i]);
				writer.endElement("a");

				if (orderColumn != null
						&& orderColumn.intValue() == i
						&& order != null 
						&& order.intValue() != QueryValue.ORDER_DEFAULT) {
					if (order.intValue() == QueryValue.ORDER_DESC) {
						writer.write("<img src=\"up2.gif\" width=\"15\" height=\"15\">");
					} else { 
						writer.write("<img src=\"down2.gif\" width=\"15\" height=\"15\">");
					}
				}
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

					String jsClick = UtilsJSF.generateSubmitJS(
							context, form,
							getClickIdHiddenFieldName(context),
							values[0].toString());
					
					writer.startElement("tr", this);
					writer.writeAttribute("class", rowClass.toString(), null);
					//writer.writeAttribute("onclick", jsClick, null);
					for (int j = 1; j < values.length; j++) {
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

//	private String prepareJS(FacesContext context, String pressedLink) {
//		StringBuffer buffer = new StringBuffer();
//		String ID = getSortHiddenFieldName(context);
//		buffer.append(" onclick=\"document.forms['form'].elements['" + ID + "'].value = '" 
//				+ pressedLink + "';" +
//				"clear_form();form.submit();\"");
//		return buffer.toString();
//	}

	private String getSortHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_sort";
	}

	private String getClickIdHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_click_id";
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
