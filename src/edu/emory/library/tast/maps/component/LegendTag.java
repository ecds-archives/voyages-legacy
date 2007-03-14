package edu.emory.library.tast.maps.component;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

public class LegendTag extends UIComponentTag {

	private String id;

	private String style;

	private String styleClass;

	private String legend;

	private String layers;

	private String refreshAction;
	
	private String availableMaps;
	
	private String chosenMap;

	private String availableAttributes;
	
	private String chosenAttribute;
	
	protected void setProperties(UIComponent component) {

		Application app = FacesContext.getCurrentInstance().getApplication();

		if (id != null && isValueReference(id)) {
			ValueBinding vb = app.createValueBinding(id);
			component.setValueBinding("id", vb);
		} else if (id != null) {
			component.getAttributes().put("id", id);
		}
		
		if (style != null && isValueReference(style)) {
			ValueBinding vb = app.createValueBinding(style);
			component.setValueBinding("style", vb);
		} else  if (style != null) {
			component.getAttributes().put("style", style);
		}
		
		if (styleClass != null && isValueReference(styleClass)) {
			ValueBinding vb = app.createValueBinding(styleClass);
			component.setValueBinding("styleClass", vb);
		} else  if (styleClass != null)  {
			component.getAttributes().put("styleClass", styleClass);
		}
		
		if (legend != null && isValueReference(legend)) {
			ValueBinding vb = app.createValueBinding(legend);
			component.setValueBinding("legend", vb);
		} else  if (legend != null)  {
			component.getAttributes().put("legend", legend);
		}
		
		if (layers != null && isValueReference(layers)) {
			ValueBinding vb = app.createValueBinding(layers);
			component.setValueBinding("layers", vb);
		} else  if (layers != null)  {
			component.getAttributes().put("layers", layers);
		}
		
		if (availableMaps != null && isValueReference(availableMaps)) {
			ValueBinding vb = app.createValueBinding(availableMaps);
			component.setValueBinding("availableMaps", vb);
		} else  if (availableMaps != null)  {
			component.getAttributes().put("availableMaps", availableMaps);
		}
		
		if (chosenMap != null && isValueReference(chosenMap)) {
			ValueBinding vb = app.createValueBinding(chosenMap);
			component.setValueBinding("chosenMap", vb);
		} else  if (chosenMap != null)  {
			component.getAttributes().put("chosenMap", chosenMap);
		}
		
		if (chosenAttribute != null && isValueReference(chosenAttribute)) {
			ValueBinding vb = app.createValueBinding(chosenAttribute);
			component.setValueBinding("chosenAttribute", vb);
		} else  if (chosenAttribute != null)  {
			component.getAttributes().put("chosenAttribute", chosenAttribute);
		}
		
		if (availableAttributes != null && isValueReference(availableAttributes)) {
			ValueBinding vb = app.createValueBinding(availableAttributes);
			component.setValueBinding("availableAttributes", vb);
		} else  if (availableAttributes != null)  {
			component.getAttributes().put("availableAttributes", availableAttributes);
		}
		
		if (component instanceof LegendComponent && refreshAction != null) {
			LegendComponent tab = (LegendComponent)component;
			tab.setRefreshAction(new MethodBindingImpl(app, refreshAction, new Class[] {}));
		}
	}

	public String getComponentType() {
		return "Legend";
	}

	public String getRendererType() {
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legendItems) {
		this.legend = legendItems;
	}

	public String getRefreshAction() {
		return refreshAction;
	}

	public void setRefreshAction(String refreshAction) {
		this.refreshAction = refreshAction;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getMaps() {
		return availableMaps;
	}

	public void setMaps(String availableMaps) {
		this.availableMaps = availableMaps;
	}

	public String getChosenMap() {
		return chosenMap;
	}

	public void setChosenMap(String chosenMap) {
		this.chosenMap = chosenMap;
	}

	public String getAvailableAttributes() {
		return availableAttributes;
	}

	public void setAvailableAttributes(String availableAttributes) {
		this.availableAttributes = availableAttributes;
	}

	public String getAvailableMaps() {
		return availableMaps;
	}

	public void setAvailableMaps(String availableMaps) {
		this.availableMaps = availableMaps;
	}

	public String getChosenAttribute() {
		return chosenAttribute;
	}

	public void setChosenAttribute(String chosenAttribute) {
		this.chosenAttribute = chosenAttribute;
	}
}
