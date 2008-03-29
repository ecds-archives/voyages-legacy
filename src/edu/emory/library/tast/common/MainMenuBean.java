package edu.emory.library.tast.common;

import javax.faces.component.UIParameter;


public class MainMenuBean
{
	
	private UIParameter activeSectionParam;
	
	public MainMenuBarSectionItem[] getMainMenu()
	{
		return new MainMenuBarSectionItem[] {
				
				new MainMenuBarSectionItem(
						"assessment",
						"assessment/index.faces",
						"images/main-menu/assessment-normal.png",
						"images/main-menu/assessment-active.png",
						165, 28,
						"assessment",
						new MainMenuBarPageItem[] {
							new MainMenuBarPageItem(
									"essays",
									"Essays",
									"assessment/essays-intro.faces"),
							new MainMenuBarPageItem(
									"estimates",
									"Estimates",
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
						"database/index.faces",
						"images/main-menu/database-normal.png",
						"images/main-menu/database-active.png",
						100, 28,
						"database",
						new MainMenuBarPageItem[] {
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
						"resources/index.faces",
						"images/main-menu/resources-normal.png",
						"images/main-menu/resources-active.png",
						85, 28,
						"resources",
						new MainMenuBarPageItem[] {
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
						"lessons/index.faces",
						"images/main-menu/lessons-normal.png",
						"images/main-menu/lessons-active.png",
						155, 28,
						"lessons",
						new MainMenuBarPageItem[] {
							new MainMenuBarPageItem(
									"plan",
									"Lesson Plans",
									"lessons/plan.faces"),
							new MainMenuBarPageItem(
									"map",
									"Lesson Maps",
									"lessons/map.faces"),
							new MainMenuBarPageItem(
									"glossary",
									"Glossary",
									"lessons/glossary.faces")}),

				new MainMenuBarSectionItem(
						"about",
						"about/index.faces",
						"images/main-menu/about-normal.png",
						"images/main-menu/about-active.png",
						130, 28,
						"about",
						new MainMenuBarPageItem[] {
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
	}

	public UIParameter getActiveSectionParam()
	{
		return activeSectionParam;
	}

	public void setActiveSectionParam(UIParameter activeSectionParam)
	{
		this.activeSectionParam = activeSectionParam;
	}
	
	public String activeSectionId()
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

}