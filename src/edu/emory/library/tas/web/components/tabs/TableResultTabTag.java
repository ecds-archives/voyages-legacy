package edu.emory.library.tas.web.components.tabs;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

/**
 * Class represents html tag for table results.
 * @author Pawel Jurczyk
 * 
 * Example usage:
 * 
 * <s:tabletab onclick="#{TableResultTabBean.showDetails}" 
 * 			   rendered="#{TableResultTabBean.resultsMode}"
 * 			   query="#{SearchBean.searchParameters}" 
 *             conditionsOut="#{TableResultTabBean.conditions}"
 *	           data="#{TableResultTabBean.data}" 
 *             componentVisible="#{TableResultTabBean.componentVisible}"
 * 			   sortChanged="#{TableResultTabBean.sortChanged}" 
 * 			   style="overflow:auto;" />
 *
 */
public class TableResultTabTag extends UIComponentTag {

	private static final String TABLE_RESULT_TAB = "TableResultTab";

	/**
	 * Sort changed event mapping.
	 */
	private String sortChanged;
	
	/**
	 * Style mapping.
	 */
	private String style;
	
	/**
	 * Style class mapping.
	 */
	private String styleClass;

	/**
	 * Visibility of component mapping.
	 */
	private String componentVisible;

	/**
	 * Table data mapping.
	 */
	private String data;
	
	/**
	 * Onclick event mapping (on row of data column - fires details).
	 */
	private String onclick;
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

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
		
		if (component instanceof UITableResultTab && sortChanged != null) {
			UITableResultTab tab = (UITableResultTab)component;
			Application app = FacesContext.getCurrentInstance().getApplication();
			tab.setSortChanged(new MethodBindingImpl(app, sortChanged, new Class[] {SortChangeEvent.class}));
		}
		
		if (component instanceof UITableResultTab && onclick != null) {
			UITableResultTab tab = (UITableResultTab)component;
			Application app = FacesContext.getCurrentInstance().getApplication();
			tab.setShowDetails(new MethodBindingImpl(app, onclick, new Class[] {ShowDetailsEvent.class}));
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

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
}
