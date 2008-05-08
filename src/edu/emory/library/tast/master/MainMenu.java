package edu.emory.library.tast.master;

public class MainMenu
{

	private static final MainMenuBarSectionItem[] menuItems = new MainMenuBarSectionItem[] {
	
	new MainMenuBarSectionItem(
			"database",
			"Database",
			"database/index.faces",
			"images/main-menu/database.png",
			"images/main-menu/database-highlighted.png",
			"images/main-menu/database-active.png",
			100, 28,
			"database",
			new MainMenuBarPageItem[] {
				new MainMenuBarPageItem(
						"guide",
						"Understanding the database",
						"database/guide.faces"),
				new MainMenuBarPageItem(
						"search",
						"Search the database",
						"database/search.faces"),
				new MainMenuBarPageItem(
						"download",
						"Downloads",
						"database/download.faces"),
				new MainMenuBarPageItem(
						"contribute",
						"Contribute",
						"submission/submission-login.faces")}),
	
	new MainMenuBarSectionItem(
			"assessment",
			"Assessing the Slave Trade",
			"assessment/index.faces",
			"images/main-menu/assessment.png",
			"images/main-menu/assessment-highlighted.png",
			"images/main-menu/assessment-active.png",
			170, 28,
			"assessment",
			new MainMenuBarPageItem[] {
				new MainMenuBarPageItem(
						"essays",
						"Essays",
						"assessment/essays-intro-01.faces"),
				new MainMenuBarPageItem(
						"estimates",
						"Estimates",
						"assessment/estimates.faces"),
				new MainMenuBarPageItem(
						"intromaps",
						"Introductory maps",
						"assessment/intromaps.faces"),
				new MainMenuBarPageItem(
						"furtherstudy",
						"Further study",
						"assessment/furtherstudy.faces")}),
						
	new MainMenuBarSectionItem(
			"resources",
			"Resources",
			"resources/index.faces",
			"images/main-menu/resources.png",
			"images/main-menu/resources-highlighted.png",
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
						"Names database",
						"resources/slaves.faces")}),
			
	new MainMenuBarSectionItem(
			"lessons",
			"Educational Materials",
			"education/index.faces",
			"images/main-menu/lessons.png",
			"images/main-menu/lessons-highlighted.png",
			"images/main-menu/lessons-active.png",
			155, 28,
			"lessons",
			new MainMenuBarPageItem[] {
				new MainMenuBarPageItem(
						"education-lesson-plans",
						"Lesson plans",
						"education/lesson-plans.faces"),
				new MainMenuBarPageItem(
						"education-links",
						"Links to other resources",
						"education/links.faces"),
				new MainMenuBarPageItem(
						"education-other-resources",
						"Books",
						"education/books.faces"),
				new MainMenuBarPageItem(
						"education-further-reading",
						"Further Reading",
						"education/further-reading.faces")}),
	
	new MainMenuBarSectionItem(
			"about",
			"About the Project",
			"about/index.faces",
			"images/main-menu/about.png",
			"images/main-menu/about-highlighted.png",
			"images/main-menu/about-active.png",
			130, 28,
			"about",
			new MainMenuBarPageItem[] {
					new MainMenuBarPageItem(
							"history",
							"History",
							"about/history.faces"),
					new MainMenuBarPageItem(
							"bios",
							"Project team",
							"about/team.faces"),
					new MainMenuBarPageItem(
							"acknowledgements",
							"Acknowledgements",
							"about/acknowledgements.faces"),
					new MainMenuBarPageItem(
							"contact",
							"Contact Us",
							"about/contact.faces"),
					new MainMenuBarPageItem(
							"legal",
							"Legal",
							"about/legal.faces")}),
	
	};

	public static MainMenuBarSectionItem[] getMainMenu()
	{
		return MainMenu.menuItems;
	}

}
