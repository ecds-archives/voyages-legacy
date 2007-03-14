package edu.emory.library.tast.common;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class PanelTabTag extends UIComponentTag {

	private String title;
	private String sectionId;

	public String getComponentType()
	{
		return "PanelTab";
	}

	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		PanelTabComponent section = (PanelTabComponent) component;

		if (title != null) {
			if (isValueReference(title)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(title);
				component.setValueBinding("title", vb);
			} else {
				component.getAttributes().put("title", title);
			}
		}
		
		//section.setTitle(title);
		section.setSectionId(sectionId);
		
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
