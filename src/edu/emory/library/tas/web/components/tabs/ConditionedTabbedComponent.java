package edu.emory.library.tas.web.components.tabs;

import javax.faces.context.FacesContext;

import edu.emory.library.tas.util.query.Conditions;

public interface ConditionedTabbedComponent extends TabbedComponent {
	public void appyConditions(Conditions c, FacesContext context);
	public void applyPopulatedAttributes(String[] attributes, FacesContext context);
}
