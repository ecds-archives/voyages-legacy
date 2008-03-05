package edu.emory.library.tast.common;


public class MainMenuBean
{
	
	public MainMenuBarSectionItem[] getMainMenu()
	{
		return new MainMenuBarSectionItem[] {
				
				new MainMenuBarSectionItem(
						"assessment",
						"assessment/index.faces",
						"new-images/main-menu-assessment-normal.png",
						"new-images/main-menu-assessment-active.png", 190,
						19, new MainMenuBarPageItem[] {
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
						"new-images/main-menu-database-normal.png",
						"new-images/main-menu-database-active.png", 102,
						19, new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"methodology",
										"Understanding the database",
										"database/methodology.faces"),
								new MainMenuBarPageItem(
										"search",
										"Search the Database",
										"database/search.faces"),
								new MainMenuBarPageItem(
										"download",
										"Download the Database",
										"database/download.faces"),								
								new MainMenuBarPageItem(
										"contribute",
										"Contribute",
										"submission/submission-login.faces")}),

				new MainMenuBarSectionItem(
						"resources",
						"resources/index.faces",
						"new-images/main-menu-resources-normal.png",
						"new-images/main-menu-resources-active.png", 96,
						19, new MainMenuBarPageItem[] {
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
						"new-images/main-menu-lessons-normal.png",
						"new-images/main-menu-lessons-active.png", 165,
						19, new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"plan",
										"Lesson Plans",
										"lessons/plan.faces"),								
								new MainMenuBarPageItem(
										"glossary",
										"Glossary",
										"lessons/glossary.faces"),
								new MainMenuBarPageItem(
										"link",
										"Links",
										"lessons/map.faces")}),

				new MainMenuBarSectionItem(
						"about",
						"about/index.faces",
						"new-images/main-menu-about-normal.png",
						"new-images/main-menu-about-active.png", 137,
						18, new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"history",
										"History",
										"about/history.faces"),								
								new MainMenuBarPageItem(
										"team",
										"Project Team",
										"about/team.faces"),
								new MainMenuBarPageItem(
										"acknowledgements",
										"Acknowledgements",
										"about/acknowledge.faces"),
								new MainMenuBarPageItem(
										"contacts",
										"Contacts",
										"about/contact.faces")}),

		};
	}

	public MainMenuBarSectionItem[] getHelp()
	{
		return new MainMenuBarSectionItem[] {
				
				new MainMenuBarSectionItem(
						"help",
						"help/index.faces",
						"new-images/main-menu-help.png",
						"new-images/main-menu-help.png",76,
						19, new MainMenuBarPageItem[]{
								new MainMenuBarPageItem(
										"index",
										"Index",
										"help/index.faces"),
								new MainMenuBarPageItem(
										"demos",
										"Demos",
										"help/demos.faces"),
								new MainMenuBarPageItem(
										"sitemap",
										"Sitemap",
										"help/sitemap.faces"),
								new MainMenuBarPageItem(
										"glossary",
										"Glossary",
										"help/glossary.faces")}),
						};
	}
}
