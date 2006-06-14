package edu.emory.library.tas.web.components.tabs;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;

public class StatisticResultTabTag extends UIComponentTag {

	private static final String STAT_RESULT_TAB = "StatisticResultTab";

	
	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);
		
		FacesContext context = FacesContext.getCurrentInstance();
    
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

}
