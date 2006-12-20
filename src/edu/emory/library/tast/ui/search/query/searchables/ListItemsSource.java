package edu.emory.library.tast.ui.search.query.searchables;

import org.hibernate.Session;

import edu.emory.library.tast.ui.search.query.QueryConditionListItem;

public interface ListItemsSource
{
	QueryConditionListItem[] getAvailableItems(Session session);
	QueryConditionListItem getItemByFullId(Session session, String id);
}
