package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class PanelTabSetTag extends UIComponentTag {

	private String title;
//	private String backgroundStyle;
//	private String tabsStyle;
//	private String buttonsStyle;
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
		
//		tabSet.setBackgroundStyle(backgroundStyle);
//		tabSet.setTabsStyle(tabsStyle);
//		tabSet.setButtonsStyle(buttonsStyle);

		if (title != null) {
			if (isValueReference(title)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(title);
				component.setValueBinding("title", vb);
			} else {
				component.getAttributes().put("title", title);
			}
		}
		//sectionGroup.setTitle(title);
		
		if (selectedSectionId != null && isValueReference(selectedSectionId))
		{
			ValueBinding vb = app.createValueBinding(selectedSectionId);
			tabSet.setValueBinding("selectedSectionId", vb);
		}
		else
		{
			tabSet.setSelectedSectionId(selectedSectionId);
		}
	
	}
	

//	public String getBackgroundStyle() {
//		return backgroundStyle;
//	}
//
//	public void setBackgroundStyle(String backgroundStyle) {
//		this.backgroundStyle = backgroundStyle;
//	}
//
//	public String getButtonsStyle() {
//		return buttonsStyle;
//	}
//
//	public void setButtonsStyle(String buttonsStyle) {
//		this.buttonsStyle = buttonsStyle;
//	}

	public String getSelectedSectionId() {
		return selectedSectionId;
	}

	public void setSelectedSectionId(String selectedSectionId) {
		this.selectedSectionId = selectedSectionId;
	}

//	public String getTabsStyle() {
//		return tabsStyle;
//	}
//
//	public void setTabsStyle(String tabsStyle) {
//		this.tabsStyle = tabsStyle;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
