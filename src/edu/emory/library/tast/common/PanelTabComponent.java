package edu.emory.library.tast.common;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.JsfUtils;

/**
 * Panel which is used as a tab.
 * When one wants to use additional panel in tab set component,
 * all the components of the tab should be children of this panel tab.
 * Component tag name used in jsp files is panelTab.
 * This component appears as children of component panelTabSet.
 * For more details see PanelTabSetComponent.
 *
 */
public class PanelTabComponent extends UIComponentBase {

	private String title;
	private String sectionId;
	
	public String getFamily() {
		return null;
	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = sectionId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		sectionId = (String) values[2];
	}
	
	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getTitle()
	{
		String val = JsfUtils.getCompPropString(this, this.getFacesContext(), "title", false, title);
		return val;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
