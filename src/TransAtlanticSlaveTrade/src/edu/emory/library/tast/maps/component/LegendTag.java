/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.maps.component;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class LegendTag extends UIComponentTag {

	private String id;

	private String style;

	private String styleClass;

	private String legend;

	private String layers;

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
