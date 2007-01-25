package edu.emory.library.tast.common;

import edu.emory.library.tast.ui.MainMenuBarPageItem;
import edu.emory.library.tast.ui.MainMenuBarSectionItem;

public class MainMenuBean
{
	
	public MainMenuBarSectionItem[] getMainMenu()
	{
		return new MainMenuBarSectionItem[] {
				
				new MainMenuBarSectionItem(
						"estimates",
						"images/main-menu-estimates-normal.png",
						"images/main-menu-estimates-active.png",
						190, 30,
						new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"intro",
										"Introduction",
										"estimates/intro.faces"),
								new MainMenuBarPageItem(
										"intro",
										"Asses Trade Slave",
										"estimates/estimates.faces")}),
						
				new MainMenuBarSectionItem(
						"database",
						"images/main-menu-database-normal.png",
						"images/main-menu-database-active.png",
						120, 30,
						new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"intro",
										"Introduction",
										"database/intro.faces"),
								new MainMenuBarPageItem(
										"search",
										"Search database",
										"database/search.faces"),
								new MainMenuBarPageItem(
										"prepared",
										"Standardard Queries",
										"database/prepared.faces")}),

				new MainMenuBarSectionItem(
						"resources",
						"images/main-menu-resources-normal.png",
						"images/main-menu-resources-active.png",
						120, 30,
						new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"intro",
										"Introduction",
										"resources/intro.faces"),
								new MainMenuBarPageItem(
										"images",
										"Image Database",
										"resources/images.faces")}),
						
				new MainMenuBarSectionItem(
						"lessons",
						"images/main-menu-lessons-normal.png",
						"images/main-menu-lessons-active.png",
						175, 30,
						new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"intro",
										"Introduction",
										"lessons/intro.faces"),
								new MainMenuBarPageItem(
										"images",
										"Lesson Plans",
										"lessons/lessons.faces")}),

				new MainMenuBarSectionItem(
						"about",
						"images/main-menu-about-normal.png",
						"images/main-menu-about-active.png",
						145, 30,
						new MainMenuBarPageItem[] {
								new MainMenuBarPageItem(
										"intro",
										"Introduction",
										"about/intro.faces"),
								new MainMenuBarPageItem(
										"institutions",
										"Institutions",
										"about/institutions.faces"),
								new MainMenuBarPageItem(
										"images",
										"Researchers",
										"about/researchers.faces")}),

		};
	}

}
