package edu.emory.library.tas.web.schema;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import edu.emory.library.tas.web.TabBarComponent;
import edu.emory.library.tas.web.TabChangeEvent;

public class Switcher
{
	
	private EditMode editMode = EditMode.Voyages;
	private String selectedModuleId = "voyages-groups";
	private TabBarComponent moduleTabs = null;
	
	public void moduleChanged(TabChangeEvent event)
	{
		
		selectedModuleId = event.getTabId();
		
		editMode = event.getTabId().startsWith("voyages") ?
				EditMode.Voyages : EditMode.Slaves;
		
		FacesContext context = FacesContext.getCurrentInstance();
		NavigationHandler nav = context.getApplication().getNavigationHandler();
		nav.handleNavigation(context, null, selectedModuleId);
		
	}

	public String getPageTitle()
	{
		return moduleTabs.getSelectedTab().getText();
	}

	public EditMode getEditMode()
	{
		return editMode;
	}

	public void setEditMode(EditMode editMode)
	{
		this.editMode = editMode;
	}

	public String getSelectedModuleId()
	{
		return selectedModuleId;
	}

	public void setSelectedModuleId(String selectedModuleId)
	{
		this.selectedModuleId = selectedModuleId;
	}

	public TabBarComponent getModuleTabs()
	{
		return moduleTabs;
	}

	public void setModuleTabs(TabBarComponent moduleTabs)
	{
		this.moduleTabs = moduleTabs;
	}
	
}
