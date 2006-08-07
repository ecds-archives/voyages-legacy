package edu.emory.library.tast.ui.search.stat;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag for chart result presentation.
 * @author Pawel Jurczyk
 * Example of usage:
 * <s:stattab styleClass="data-container" 
 * 			  rendered="#{SearchBean.statisticsVisible}" 
 *            query="#{SearchBean.searchParameters}"
 *            conditionsOut="#{Bean.conditions}">
 *            
 *      <any tags using values from Bean bean>
 *            
 * </s:stattab>
 */
public class TimeLineResultTabTag extends UIComponentTag {

	private static final String STAT_RESULT_TAB = "TimeLineResultTab";

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
	 * Setting of component properties.
	 */
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

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
	}
	
	public void release() {
		super.release();
	}

	public String getComponentType() {
		return STAT_RESULT_TAB;
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
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(String componentVisible) {
		this.componentVisible = componentVisible;
	}
	
}
