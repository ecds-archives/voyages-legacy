package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Provides tag definition for panel tab set component.
 * The tag can have following attributes:
 * title - title of panel tab set
 * selectedSectionId - id of selected panel tab.
 * For more details please refer to PanelTabTag.
 *
 */
public class PanelTabSetTag extends UIComponentTag {

	private String title;
	private String selectedSectionId;
	
	public String getComponentType() {
		return "PanelTabSet";
	}

	public String getRendererType() {
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		PanelTabSetComponent tabSet = (PanelTabSetComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (title != null) {
			if (isValueReference(title)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(title);
				component.setValueBinding("title", vb);
			} else {
				component.getAttributes().put("title", title);
			}
		}
		
		if (selectedSectionId != null && isValueReference(selectedSectionId))
		{
			ValueBinding vb = app.createValueBinding(selectedSectionId);
			tabSet.setValueBinding("selectedSectionId", vb);
		}
		else
		{
			tabSet.setSelectedSectionId(selectedSectionId);
		}
		
		super.setProperties(component);
	
	}
	
	public String getSelectedSectionId() {
		return selectedSectionId;
	}

	public void setSelectedSectionId(String selectedSectionId) {
		this.selectedSectionId = selectedSectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
