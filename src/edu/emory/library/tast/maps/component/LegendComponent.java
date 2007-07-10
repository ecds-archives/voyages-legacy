package edu.emory.library.tast.maps.component;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.model.SelectItem;

import edu.emory.library.tast.maps.LegendItem;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.MapLayer;
import edu.emory.library.tast.util.JsfUtils;

public class LegendComponent extends UIComponentBase {

	private MethodBinding refreshAction;
	
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);
		if (event instanceof ActionEvent && refreshAction != null) {
			refreshAction.invoke(getFacesContext(), new Object[] {});
		}
	}

	public void decode(FacesContext context) {
		super.decode(context);

		Map params = context.getExternalContext().getRequestParameterMap();
		String refreshClicked = (String) params.get(getLegendHiddenFieldName(context));
		if (refreshClicked != null && refreshClicked.length() > 0) {
			this.queueEvent(new ActionEvent(this));
		}
		
		String mapTypeId = (String) params.get(getMapTypeHiddenFieldName(context));
		if (mapTypeId != null) {
			ValueBinding vb = this.getValueBinding("chosenMap");
			vb.setValue(context, mapTypeId);
		}
		
		String attrTypeId = (String) params.get(getAttrTypeHiddenFieldName(context));
		if (attrTypeId != null) {
			ValueBinding vb = this.getValueBinding("chosenAttribute");
			vb.setValue(context, new Integer(attrTypeId));
		}
		
		for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
			String paramName = (String) iter.next();
			if (paramName.startsWith(getCheckboxPrefix(context))) {
				this.resolveHiddenField(context, paramName, (String)params.get(paramName));
			}
		}
	}

	private void resolveHiddenField(FacesContext context, String field, String value) {
		if (field.indexOf("_legend_") != -1) {
			String[] legends = field.split("_");
			int legendGroup = Integer.parseInt(legends[legends.length - 2]);
			int legendItem = Integer.parseInt(legends[legends.length - 1]);
			
			LegendItemsGroup[] legend = (LegendItemsGroup[])this.getValueBinding(context, "legend");
			
			legend[legendGroup].getItems()[legendItem].setEnabled(value.equals("selected"));
			
		} else if (field.indexOf("_layers_") != -1) {
			String[] layers = field.split("_");
			int layer = Integer.parseInt(layers[layers.length - 1]);
			
			MapLayer[] mapLayers = (MapLayer[])this.getValueBinding(context, "layers");
		
			mapLayers[layer].setEnabled(value.equals("selected"));
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		String style = (String)getAttributes().get("style");
		String styleClass = (String)getAttributes().get("styleClass");
		String id = (String)getAttributes().get("id");

		writer.startElement("div", this);
		if (id != null) {
			writer.writeAttribute("id", id, null);
		}
		if (style != null) {
			writer.writeAttribute("style", style, null);
		}
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}

		this.encodeAttrsChoose(context, writer);
		this.encodeMapsChoose(context, writer);
		this.encodeLegend(context, writer);
		this.encodeComponentButton(context, writer);

	}
	
	private void encodeMapsChoose(FacesContext context, ResponseWriter writer) throws IOException {

		SelectItem[] maps = (SelectItem[]) this.getValueBinding(context, "availableMaps");
		String selectedMap = (String)this.getValueBinding(context, "chosenMap");
		
		if (maps != null && selectedMap != null) {
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section-title", null);
			writer.write("Select map");
			writer.endElement("div");

			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section", null);
			
			writer.startElement("select", this);
			writer.writeAttribute("id", this.getId() + "_availableMap", null);
			StringBuffer buffer = new StringBuffer();
			JsfUtils.appendFormElementValJS(buffer, context, JsfUtils.getForm(this, context), this.getMapTypeHiddenFieldName(context));
			buffer.append(" = ");
			JsfUtils.appendFormElementValJS(buffer, context, JsfUtils.getForm(this, context), this.getId() + "_availableMap");
			buffer.append(";");
			//JsfUtils.appendSubmitJS(js, context, form, elementName, value)
			writer.writeAttribute("onchange", buffer.toString(), null);
			for (int i = 0; i < maps.length; i++) {
				writer.startElement("option", this);
				writer.writeAttribute("value", maps[i].getValue(), null);
				if (selectedMap.equals(maps[i].getValue())) {
					writer.writeAttribute("selected", null, null);
				}
				writer.write(maps[i].getLabel());
				writer.endElement("option");
			}
			writer.endElement("select");
			
			writer.endElement("div");

			writer.startElement("input", this);
			writer.writeAttribute("name", this.getMapTypeHiddenFieldName(context), null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("value", selectedMap, null);
			writer.endElement("input");
			
		}
	}

	
	private void encodeAttrsChoose(FacesContext context, ResponseWriter writer) throws IOException {
		
		SelectItem[] attrs = (SelectItem[]) this.getValueBinding(context, "availableAttributes");
		Integer selectedAttr = (Integer)this.getValueBinding(context, "chosenAttribute");
		
		if (attrs != null && selectedAttr != null) {
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section-title", null);
			writer.write("Values");
			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section", null);
			
			writer.startElement("select", this);
			writer.writeAttribute("id", this.getId() + "_availableAttr", null);
			StringBuffer buffer = new StringBuffer();
			JsfUtils.appendFormElementValJS(buffer, context, JsfUtils.getForm(this, context), this.getAttrTypeHiddenFieldName(context));
			buffer.append(" = ");
			JsfUtils.appendFormElementValJS(buffer, context, JsfUtils.getForm(this, context), this.getId() + "_availableAttr");
			buffer.append(";");
			writer.writeAttribute("onchange", buffer.toString(), null);
			for (int i = 0; i < attrs.length; i++) {
				writer.startElement("option", this);
				writer.writeAttribute("value", attrs[i].getValue(), null);
				if (selectedAttr.intValue() == i) {
					writer.writeAttribute("selected", null, null);
				}
				writer.write(attrs[i].getLabel());
				writer.endElement("option");
			}
			writer.endElement("select");
			
			writer.endElement("div");

			writer.startElement("input", this);
			writer.writeAttribute("name", this.getAttrTypeHiddenFieldName(context), null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("value", selectedAttr, null);
			writer.endElement("input");
			
		}
	}
	
	private void encodeLegend(FacesContext context, ResponseWriter writer) throws IOException {
		
		LegendItemsGroup[] legendGroups = (LegendItemsGroup[])getValueBinding(context, "legend");
		
		if (legendGroups != null) {
		
			for (int i = 0; i < legendGroups.length; i++) {

				writer.startElement("div", this);
				writer.writeAttribute("class", "map-legend-section-title", null);
				writer.write(legendGroups[i].getTitle());
				writer.endElement("div");
				
				LegendItem[] items = legendGroups[i].getItems();
				
				writer.startElement("div", this);
				writer.writeAttribute("class", "map-legend-section", null);

				writer.startElement("table", this);
				writer.writeAttribute("class", "map-legend-items", null);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("cellspacing", "0", null);
				writer.writeAttribute("cellpadding", "0", null);
				
				for (int j = 0; j < items.length; j++) {
					writer.startElement("tr", this);
					
					writer.startElement("td", this);
					writer.writeAttribute("class", "map-legend-checkbox", null);
					encodeCheckbox(context, writer, "_legend_" + i + "_" + j, items[j].isEnabled());
					writer.endElement("td");
					
					writer.startElement("td", this);
					writer.writeAttribute("class", "map-legend-symbol", null);
					writer.startElement("img", this);
					writer.writeAttribute("src", items[j].getImagePath(), null);
					writer.endElement("img");
					writer.endElement("td");
					
					writer.startElement("td", this);
					writer.writeAttribute("class", "map-legend-label", null);
					writer.write(items[j].getLegendString());					
					writer.endElement("td");
					
					writer.endElement("tr");
				}
				
				writer.endElement("table");
				writer.endElement("div");

			}
		
		}
		
		MapLayer[] layers = (MapLayer[])getValueBinding(context, "layers");
		if (layers != null && layers.length > 0) {
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section-title", null);
			writer.write("Layers");
			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "map-legend-section", null);
			
			writer.startElement("table", this);
			writer.writeAttribute("class", "map-legend-items", null);
			writer.writeAttribute("border", "0", null);
			writer.writeAttribute("cellspacing", "0", null);
			writer.writeAttribute("cellpadding", "0", null);
			
			for (int i = 0; i < layers.length; i++) {
				writer.startElement("tr", this);
				
				writer.startElement("td", this);
				encodeCheckbox(context, writer, "_layers_" + i, layers[i].isEnabled());
				writer.endElement("td");
				
				writer.startElement("td", this);
				writer.write(layers[i].getLabel());
				writer.endElement("td");
				
				writer.endElement("tr");
			}
			
			writer.endElement("table");
			writer.endElement("div");

		}
	}
	
	private void encodeCheckbox(FacesContext context, ResponseWriter writer, String postfix, boolean selected) throws IOException {
		
		writer.startElement("input", this);
		writer.writeAttribute("name", getCheckboxId(context, postfix), null);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("value", selected ? "selected" : "not-selected", null);
		writer.endElement("input");
		
		writer.startElement("input", this);
		writer.writeAttribute("id", getCheckboxId(context, postfix + "_check"), null);
		writer.writeAttribute("type", "checkbox", null);
		if (selected) {
			writer.writeAttribute("checked", "selected", null);
		}
		writer.writeAttribute("onchange", 
				JsfUtils.generateConditionalJS(context, 
							JsfUtils.getForm(this, context), 
							getCheckboxId(context, postfix + "_check").toString(), 
							"checked", 
							getCheckboxId(context, postfix).toString(), 
							"selected", "not-selected")
					, null);
		writer.endElement("input");
	}

	private void encodeComponentButton(FacesContext context, ResponseWriter writer) throws IOException {
		JsfUtils.encodeHiddenInput(this, writer, getLegendHiddenFieldName(context));
		writer.startElement("input", this);
		writer.writeAttribute("id", this.getButtonName(context), null);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("onClick", JsfUtils.generateSubmitJS(context, JsfUtils.getForm(this, context), 
				this.getLegendHiddenFieldName(context), "clicked"), null);
		writer.writeAttribute("value", "Refresh", null);
		writer.writeAttribute("class", "map-legend-refresh", null);
		writer.endElement("input");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		this.refreshAction = (MethodBinding) restoreAttachedState(context, values[1]);
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, refreshAction);
		return values;
	}

	public String getFamily() {
		return null;
	}

	public MethodBinding getRefreshAction() {
		return refreshAction;
	}

	public void setRefreshAction(MethodBinding refreshAction) {
		this.refreshAction = refreshAction;
	}

	private Object getValueBinding(FacesContext context, String valueName) {
		ValueBinding vb = this.getValueBinding(valueName);
		if (vb != null && vb.getValue(context) != null) {
			return vb.getValue(context);
		}
		return null;
	}
	
	private Object getCheckboxId(FacesContext context, String postfix) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getCheckboxPrefix(context));
		buffer.append(postfix);
		return buffer.toString();
	}
	
	private String getCheckboxPrefix(FacesContext context) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getId()).append("_checkbox_");
		return buffer.toString();
	}
	
	private String getMapTypeHiddenFieldName(FacesContext context) {
		return this.getId() + "_availMaps_hidden";
	}
	
	private String getAttrTypeHiddenFieldName(FacesContext context) {
		return this.getId() + "_availAttrs_hidden";
	}
	
	private String getLegendHiddenFieldName(FacesContext context) {
		return this.getId() + "_hiddenField";
	}
	
	private String getButtonName(FacesContext context) {
		return this.getId() + "_button";
	}
}
