package edu.emory.library.tast.estimates.listing;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

import edu.emory.library.tast.ui.search.table.ShowDetailsEvent;
import edu.emory.library.tast.ui.search.table.SortChangeEvent;
import edu.emory.library.tast.ui.search.table.UITableResultTab;

public class EstimateListingTag extends UIComponentTag {

	private String data;
	private String style;
	private String styleClass;
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
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
		
//		if (component instanceof UITableResultTab && sortChanged != null) {
//			UITableResultTab tab = (UITableResultTab)component;
//			Application app = FacesContext.getCurrentInstance().getApplication();
//			tab.setSortChanged(new MethodBindingImpl(app, sortChanged, new Class[] {SortChangeEvent.class}));
//		}
//		
//		if (component instanceof UITableResultTab && onclick != null) {
//			UITableResultTab tab = (UITableResultTab)component;
//			Application app = FacesContext.getCurrentInstance().getApplication();
//			tab.setShowDetails(new MethodBindingImpl(app, onclick, new Class[] {ShowDetailsEvent.class}));
//		}
	}
	
	public String getComponentType() {
		// TODO Auto-generated method stub
		return "EstimateListing";
	}

	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
