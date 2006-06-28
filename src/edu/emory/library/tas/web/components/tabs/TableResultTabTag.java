package edu.emory.library.tas.web.components.tabs;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

public class TableResultTabTag extends UIComponentTag {

	private static final String TABLE_RESULT_TAB = "TableResultTab";

	private String sortChanged;
	
	private String style;
	private String styleClass;

	private String conditions;
	private String conditionsOut;

	private String componentVisible;

	private String data;
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		if (data != null) {
			if (isValueReference(data)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(data);
				component.setValueBinding("data", vb);
			} else {
				component.getAttributes().put("data", data);
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
		if (componentVisible != null) {
			if (isValueReference(conditionsOut)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(componentVisible);
				component.setValueBinding("componentVisible", vb);
			} else {
				component.getAttributes().put("componentVisible", componentVisible);
			}
		}		
		
		if (component instanceof UITableResultTab && sortChanged != null) {
			UITableResultTab tab = (UITableResultTab)component;
			Application app = FacesContext.getCurrentInstance().getApplication();
			tab.setSortChanged(new MethodBindingImpl(app, sortChanged, new Class[] {SortChangeEvent.class}));
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
	
	public String getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(String componentVisible) {
		this.componentVisible = componentVisible;
	}

	public String getSortChanged() {
		return sortChanged;
	}

	public void setSortChanged(String sortChanged) {
		this.sortChanged = sortChanged;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
