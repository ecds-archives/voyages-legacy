package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

import edu.emory.library.tast.common.table.ClickEvent;


public class DivButtonTag extends UIComponentTag {

	private String style;
	private String styleClass;
	private String id;
	private String action;
	
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
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
		if (id != null) {
			if (isValueReference(id)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(id);
				component.setValueBinding("id", vb);
			} else {
				component.getAttributes().put("id", id);
			}
		}
		if (component instanceof UIDivButton && action != null) {
			UIDivButton tab = (UIDivButton)component;
			Application app = FacesContext.getCurrentInstance().getApplication();
			tab.setAction(new MethodBindingImpl(app, action, new Class[] {ClickEvent.class}));
		}
		
	}
	
	public String getComponentType() {
		return "DivButton";
	}

	public String getRendererType() {
		return null;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
