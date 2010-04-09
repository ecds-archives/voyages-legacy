/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.master;

public class MainMenu
{

	private static final MainMenuBarSectionItem[] menuItems = new MainMenuBarSectionItem[] {
	
	new MainMenuBarSectionItem(
			"database",
			"Voyages Database",
			"database/index.faces",
			"images/main-menu/database.png",
			"images/main-menu/database-highlighted.png",
			"images/main-menu/database-active.png",
			125, 28,
			"database",
			new MainMenuBarPageItem[] {
				new MainMenuBarPageItem(
						"guide",
						"Understanding the Database",
						"database/guide.faces"),
				new MainMenuBarPageItem(
						"search",
						"Search the Voyages Database",
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
						"Introductory Maps",
						"assessment/intro-maps.faces")}),
						
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
						"African Names Database",
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
						"Lesson Plans",
						"education/lesson-plans.faces"),
				new MainMenuBarPageItem(
						"education-other-resources",
						"Web Resources",
						"education/others.faces")}),
	
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
							"team",
							"Project Team",
							"about/team.faces"),
					new MainMenuBarPageItem(
							"contributors",
							"Contributors of data",
							"about/data.faces"),
					new MainMenuBarPageItem(
							"acknowledgements",
							"Acknowledgements",
							"about/acknowledgements.faces"),
					new MainMenuBarPageItem(
							"origins",
							"African Origins Project",
							"about/origins.faces"),
					new MainMenuBarPageItem(
							"contact",
							"Contact Us",
							"about/contact.faces")}),
	
	};

	public static MainMenuBarSectionItem[] getMainMenu()
	{
		return MainMenu.menuItems;
	}

}
