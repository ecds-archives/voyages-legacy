package edu.emory.library.tast.ui;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.JsfUtils;

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
