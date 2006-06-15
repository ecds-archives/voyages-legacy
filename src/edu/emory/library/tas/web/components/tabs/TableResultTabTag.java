package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import edu.emory.library.tas.util.query.Conditions;

public class TableResultTabTag extends UIComponentTag {

	private static final String TABLE_RESULT_TAB = "TableResultTab";

	private String results;

	private String populatedAttributes;

	private String style;
	private String styleClass;

	private String conditions;
	private String conditionsOut;

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		if (results != null) {
			if (isValueReference(results)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(results);
				component.setValueBinding("results", vb);
			} else {
				component.getAttributes().put("results", results);
			}
		}
		if (populatedAttributes != null) {
			if (isValueReference(populatedAttributes)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(populatedAttributes);
				component.setValueBinding("populatedAttributes", vb);
			} else {
				component.getAttributes().put("populatedAttributes", results);
			}
		}
		if (style != null) {
			if (isValueReference(style)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(style);
				component.setValueBinding("style", vb);
			} else {
				component.getAttributes().put("style", style);
			}
		}
		if (styleClass != null) {
			if (isValueReference(styleClass)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(styleClass);
				component.setValueBinding("styleClass", vb);
			} else {
				component.getAttributes().put("styleClass", styleClass);
			}
		}
		if (conditions != null) {
			if (isValueReference(conditions)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(conditions);
				component.setValueBinding("conditions", vb);
			} else {
				component.getAttributes().put("conditions", conditions);
			}
		}
		if (conditionsOut != null) {
			if (isValueReference(conditionsOut)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(conditionsOut);
				component.setValueBinding("conditionsOut", vb);
			} else {
				component.getAttributes().put("conditionsOut", conditionsOut);
			}
		}
	}

	public void release() {
		super.release();
	}

	public String getComponentType() {
		return TABLE_RESULT_TAB;
	}

	public String getRendererType() {
		return null;
	}

	public String getPopulatedAttributes() {
		return populatedAttributes;
	}

	public void setPopulatedAttributes(String populatedAttributes) {
		this.populatedAttributes = populatedAttributes;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public void setQuery(String c) {
		this.conditions = c;
	}

	public String getConditionsOut() {
		return conditionsOut;
	}

	public void setConditionsOut(String conditionsOut) {
		this.conditionsOut = conditionsOut;
	}
}
