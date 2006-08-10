package edu.emory.library.tast.ui.search.query.searchables;

import edu.emory.library.tast.ui.search.query.QueryConditionListItem;

public interface ListItemsSource
{
	QueryConditionListItem[] getAvailableItems();
}
