package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * JSP tag for {@link edu.emory.library.tast.ui.SectionComponent}. It does
 * the standard stuff as a typical JSP tag for a JSF component.
 * 
 * @author Jan Zich
 * 
 */
public class SectionGroupTag extends UIComponentTag
{
	
	private String title;
	private String backgroundStyle;
	private String tabsStyle;
	private String buttonsStyle;
	private String selectedSectionId;
	
	public String getComponentType()
	{
		return "SectionGroup";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		SectionGroupComponent sectionGroup = (SectionGroupComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		sectionGroup.setBackgroundStyle(backgroundStyle);
		sectionGroup.setTabsStyle(tabsStyle);
		sectionGroup.setButtonsStyle(buttonsStyle);

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
			sectionGroup.setValueBinding("selectedSectionId", vb);
		}
		else
		{
			sectionGroup.setSelectedSectionId(selectedSectionId);
		}
	
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBackgroundStyle()
	{
		return backgroundStyle;
	}

	public void setBackgroundStyle(String backgroundStyle)
	{
		this.backgroundStyle = backgroundStyle;
	}

	public String getButtonsStyle()
	{
		return buttonsStyle;
	}

	public void setButtonsStyle(String buttonsStyle)
	{
		this.buttonsStyle = buttonsStyle;
	}

	public String getTabsStyle()
	{
		return tabsStyle;
	}

	public void setTabsStyle(String tabsStyle)
	{
		this.tabsStyle = tabsStyle;
	}

	public String getSelectedSectionId()
	{
		return selectedSectionId;
	}

	public void setSelectedSectionId(String selectedSectionId)
	{
		this.selectedSectionId = selectedSectionId;
	}

}