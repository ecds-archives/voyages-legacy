package edu.emory.library.tast.help;

import edu.emory.library.tast.common.SimpleListElement;
import edu.emory.library.tast.master.MainMenu;
import edu.emory.library.tast.master.MainMenuBarPageItem;
import edu.emory.library.tast.master.MainMenuBarSectionItem;
import edu.emory.library.tast.util.JsfUtils;

public class SitemapBean
{
	
	private abstract class Splitter
	{
		protected MainMenuBarSectionItem[] menuItems;
		
		public Splitter(MainMenuBarSectionItem[] menuItems)
		{
			this.menuItems = menuItems; 
		}
		
		public abstract int getFirstIndex();
		public abstract int getLastIndex();
		
	}
	
	private class ColumnSplitter extends Splitter
	{
		
		private int firstIndex;
		private int lastIndex;

		public ColumnSplitter(MainMenuBarSectionItem[] menuItems, int totalCols, int currCol)
		{
			super(menuItems);
			
			int totalItems = 0;
			for (int i = 0; i < menuItems.length; i++)
			{
				totalItems += 1;
				totalItems += menuItems[i].getSubItemsCount();
			}
			
			int oneColItems =
				totalItems % totalCols == 0 ?
						totalItems / totalCols :
							totalItems / totalCols + 1;
			
			firstIndex = 0;
			lastIndex = -1;

			int currColItems = 0;
			int col = 0;
			
			for (int i = 0; i < menuItems.length; i++)
			{
				
				MainMenuBarSectionItem item = menuItems[i];
				
				currColItems += 1;
				currColItems += item.getSubItemsCount();
				
				if (currColItems >= oneColItems || i == menuItems.length - 1)
				{
					currColItems = 0;
					firstIndex = lastIndex + 1;
					lastIndex = i;
					if (col >= currCol) break;
					col++;
				}

			}
			
		}

		public int getFirstIndex()
		{
			return firstIndex;
		}

		public int getLastIndex()
		{
			return lastIndex;
		}
		
	}
	
//	private class FixedSplitter extends Splitter
//	{
//		
//		private int firstIndex;
//		private int lastIndex;
//
//		public FixedSplitter(MainMenuBarSectionItem[] menuItems, int firstIndex, int lastIndex)
//		{
//			super(menuItems);
//			this.firstIndex = firstIndex;
//			this.lastIndex = lastIndex;
//		}
//
//		public int getFirstIndex()
//		{
//			return Math.max(0, firstIndex);
//		}
//
//		public int getLastIndex()
//		{
//			return Math.min(menuItems.length - 1, lastIndex);
//		}
//		
//	}

	private SimpleListElement[] getSitemap(Splitter splitter)
	{
	
		MainMenuBarSectionItem[] menuItems = MainMenu.getMainMenu();
		if (menuItems == null) return new SimpleListElement[] {};
		
		int firstItemIdx = splitter.getFirstIndex();
		int lastItemIdx = splitter.getLastIndex();

		SimpleListElement[] sitemapList = new SimpleListElement[lastItemIdx - firstItemIdx + 1];
		
		String contextPath = JsfUtils.getContextPath();
		
		for (int i = firstItemIdx; i <= lastItemIdx; i++)
		{
			
			MainMenuBarSectionItem section = menuItems[i];

			SimpleListElement sectionItem = new SimpleListElement(
				"<a href=\"javascript:openPage('"+ contextPath + "/" + section.getUrl() + "')\">"  +
				section.getLabel() + "</a>");
			
			sitemapList[i - firstItemIdx] = sectionItem;
			
			MainMenuBarPageItem[] pages = section.getSubItems(); 
			SimpleListElement[] pageItems = new SimpleListElement[pages.length];
			sectionItem.setSubElements(pageItems);
			
			for (int j = 0; j < pages.length; j++)
			{
				MainMenuBarPageItem page = pages[j];
				pageItems[j] = new SimpleListElement(
						"<a href=\"javascript:openPage('"+ contextPath + "/" + page.getUrl() + "')\">"  +
						page.getLabel() + "</a>");
			}
			
		}

		return sitemapList;
		
	}

	public SimpleListElement[] getSitemapCol1()
	{
		return getSitemap(new ColumnSplitter(MainMenu.getMainMenu(), 2, 0));
	}

	public SimpleListElement[] getSitemapCol2()
	{
		return getSitemap(new ColumnSplitter(MainMenu.getMainMenu(), 2, 1));
	}

}
