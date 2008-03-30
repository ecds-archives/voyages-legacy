package edu.emory.library.tast.common;

import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;


public class MainMenuBean
{
	
	private UIParameter activeSectionParam;
	
	private static MainMenuBarSectionItem[] mainMenu = new MainMenuBarSectionItem[] {
		
		new MainMenuBarSectionItem(
				"assessment",
				null,
				"images/main-menu/assessment.png",
				"images/main-menu/assessment-highlighted.png",
				"images/main-menu/assessment-active.png", 170,
				28,
				"assessment", new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"essays",
							"Historical Essays",
							"assessment/essays-intro.faces"),
					new MainMenuBarPageItem(
							"estimates",
							"Estimates of Imported/Exported Slaves",
							"assessment/estimates.faces"),
					new MainMenuBarPageItem(
							"intromaps",
							"Introductory Maps",
							"assessment/intromaps.faces"),
					new MainMenuBarPageItem(
							"furtherstudy",
							"Further Study",
							"assessment/furtherstudy.faces")}),
				
		new MainMenuBarSectionItem(
				"database",
				null,
				"images/main-menu/database.png",
				"images/main-menu/database-highlighted.png",
				"images/main-menu/database-active.png", 100,
				28,
				"database", new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"search",
							"Search the Database",
							"database/search.faces"),
					new MainMenuBarPageItem(
							"download",
							"Download the Database",
							"database/download.faces"),
					new MainMenuBarPageItem(
							"methodology",
							"Understanding the database",
							"database/methodology.faces"),
					new MainMenuBarPageItem(
							"contribute",
							"Contribute",
							"submission/submission-login.faces")}),

		new MainMenuBarSectionItem(
				"resources",
				null,
				"images/main-menu/resources.png",
				"images/main-menu/resources-highlighted.png",
				"images/main-menu/resources-active.png", 85,
				28,
				"resources", new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"images",
							"Images",
							"resources/images.faces"),
					new MainMenuBarPageItem(
							"slaves",
							"Names Database",
							"resources/slaves.faces")}),
				
		new MainMenuBarSectionItem(
				"lessons",
				null,
				"images/main-menu/lessons.png",
				"images/main-menu/lessons-highlighted.png",
				"images/main-menu/lessons-active.png",
				155, 28,
				"lessons", new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"plan",
							"Lesson Plans",
							"lessons/plan.faces"),
					new MainMenuBarPageItem(
							"map",
							"Lesson Maps",
							"lessons/map.faces")}),

		new MainMenuBarSectionItem(
				"about",
				null,
				"images/main-menu/about.png",
				"images/main-menu/about-highlighted.png",
				"images/main-menu/about-active.png",
				130, 28,
				"about", new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"bios",
							"Bios of Researchers",
							"about/bios.faces"),
					new MainMenuBarPageItem(
							"about",
							"About the Grand and Partners",
							"about/about.faces"),
					new MainMenuBarPageItem(
							"contact",
							"Contact Us",
							"about/contact.faces")}),

		};
		
	
	public MainMenuBarSectionItem[] getMainMenu()
	{
		return mainMenu;
	}

	public UIParameter getActiveSectionParam()
	{
		return activeSectionParam;
	}

	public void setActiveSectionParam(UIParameter activeSectionParam)
	{
		this.activeSectionParam = activeSectionParam;
	}
	
	public String getActiveSectionId()
	{
		if (activeSectionParam != null)
		{
			return (String) activeSectionParam.getValue();
		}
		else
		{
			return null;
		}
	}
	
	private static String getContextPath()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	public String getLogoImageUrl()
	{
		return getContextPath() + "/images/main-menu/menu-bg.png";
	}

	public String getIndexUrl()
	{
		return getContextPath() + "/index.faces";
	}

}