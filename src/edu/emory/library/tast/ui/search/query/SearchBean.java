package edu.emory.library.tast.ui.search.query;

import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;
import org.dom4j.tree.AbstractAttribute;

import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.ui.MenuComponent;
import edu.emory.library.tast.ui.MenuItem;
import edu.emory.library.tast.ui.MenuItemMain;
import edu.emory.library.tast.ui.MenuItemSelectedEvent;
import edu.emory.library.tast.ui.MessageBarComponent;
import edu.emory.library.tast.ui.search.query.searchables.SearchableAttribute;
import edu.emory.library.tast.ui.search.query.searchables.UserCategory;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is used in UI to manage the list of groups, atributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by {@link Query},
 * is converted to a database query represented by
 * {@link edu.emory.library.tast.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 * 
 * @author Jan Zich
 * 
 */
public class SearchBean
{
	
	private UserCategory selectedCategory = UserCategory.Beginners;
	private String mainSectionId = "listing";

	private History history = new History();
	private Query workingQuery = new Query();
	private SearchParameters searchParameters = new SearchParameters(new Conditions());

	private MessageBarComponent messageBar;
	
	/**
	 * Called by {@link #addQueryConditionBeginner} or
	 * {@link #addQueryConditionGeneral()}. Inserts a new condition on the
	 * attribute with the given ID to the current query {@link #workingQuery}.
	 * 
	 * @param selectedAttributeId
	 *            ID of {@link Attribute} or {@link CompoundAttribute} to add.
	 */
	private void addQueryCondition(String selectedAttributeId)
	{
		workingQuery.addConditionOn(selectedAttributeId);
	}
	
	/**
	 * Bind to both menu components (beginner and general) in UI. Each menu item
	 * has an id which is the id of the corresponding attribute or compounded
	 * attribute.
	 * 
	 * @param event
	 */
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		addQueryCondition(event.getMenuId());
	}

	/**
	 * Bind a button in UI. Activated when the user clicks on the search button
	 * under the list of conditions. Calles {@link #searchInternal(boolean)}
	 * with <code>true</code> meaning that the current query should be stored
	 * in the history list.
	 * 
	 * @return Always <code>null</code>, i.e. we stay on the same page.
	 */
	public String search()
	{
		if (messageBar != null)
			messageBar.setRendered(false);
		
		searchInternal(true);
		
		return null;
	}

	/**
	 * Do actuall seach. Not called directly from UI
	 * Construct a database query ({@link Conditions})
	 * from {@link #workingQuery} by calling
	 * {@link QueryCondition#addToConditions(Conditions, boolean)} on each condition.
	 * Then, is the query is different from the last query in the history list
	 * and if <code>storeToHistory</code> is <code>true</code> the stores
	 * {@link #workingQuery} to the history list {@link #history}.
	 * 
	 * @param storeToHistory
	 */
	private void searchInternal(boolean storeToHistory)
	{
		
		VisibleAttribute[] columns = new VisibleAttribute[workingQuery.getConditionCount()];
		Conditions conditions = new Conditions();

		//int i = 0;
		boolean errors = false;
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			if (!queryCondition.addToConditions(conditions, false)) errors = true;
			//columns[i++] = queryCondition.getSearchableAttributeId();
		}
		if (errors) return;
		
		searchParameters = new SearchParameters();
		searchParameters.setConditions(conditions);
		searchParameters.setColumns(columns);

		if (storeToHistory && !workingQuery.equals(history.getLatestQuery()))
			history.addQuery((Query) workingQuery.clone());

	}
	
	/**
	 * AJAX refresh. Uses AjaxAnywhere. Bind to a QueryBuilder.
	 * @param event
	 */
	public void updateTotal(QueryUpdateTotalEvent event)
	{
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
			AAUtils.addZonesToRefresh(request,"total"); 
	}
	
	/**
	 * Returns the number compute in {@link #updateTotal(QueryUpdateTotalEvent)}.
	 * The number of results is reset any time the user presses search.
	 * @return
	 */
	public String getNumberOfResultsText()
	{
		
		Conditions conditions = new Conditions();
		for (Iterator iterQueryCondition = workingQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			queryCondition.addToConditions(conditions, false);
		}
		
		Conditions localCond = (Conditions) conditions.addAttributesPrefix("v.voyage.");
		localCond.addCondition(VoyageIndex.getRecent());

		QueryValue qValue = new QueryValue("VoyageIndex as v", localCond);
		qValue.addPopulatedAttribute("count(v.voyageId)", false);
		
		Object[] ret = qValue.executeQuery();
		int numberOfResults = ((Integer)ret[0]).intValue();
		
		return "Expected number of voyages: " + numberOfResults;
	}

	/**
	 * Handler of an event from the history list. Deletes the given history item
	 * from {@link #history}. The deletion is not handled directly by the
	 * {@link HistoryListComponent} because the component does not hold the data
	 * itself (it only gets them from this bean when rendering).
	 * 
	 * @param event
	 *            Contains the history ID to delete.
	 */
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	/**
	 * Handler of an event from the history list. Restores the given history
	 * item from {@link #history} and replaces by it the current query
	 * {@link #workingQuery} and invokes search.
	 * 
	 * @param event
	 *            Contains the history ID to restore.
	 */
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		workingQuery = (Query) historyItem.getQuery().clone();
		searchInternal(false);
	}
	
	/**
	 * Handler of the onPermlink event from the history list. Creates a permlink
	 * to a given history item. It saves it to the database and displays a
	 * message to the user with the genearated URL.
	 * 
	 * @param event
	 *            Contains the history ID to create a permlink to.
	 */
	public void historyItemPermlink(HistoryItemPermlinkEvent event)
	{
		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		
		//UidGenerator generator = new UidGenerator();
		//String uid = generator.generate();
		
		Configuration conf = new Configuration();
		conf.addEntry("permlink", historyItem.getQuery());
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);
		
	}

	/**
	 * When a page is opened with ?permlink parameter in URL, we retrieved the
	 * stored permlink from db and restore the correspodind query. This method
	 * is responsible for it. It is called from any getter which works with
	 * current query and history list, namely {@link #getWorkingQuery},
	 * {@link #getHistory()}, {@link #getSearchParameters()}, so that the
	 * correspoding components have the correct values. It has to be done this
	 * way, since JSF does not provide any other mean to detect that the page is
	 * loaded for the first time.
	 */
	private void restorePermlinkIfAny()
	{
		
		FacesContext context = FacesContext.getCurrentInstance();
		Map params = context.getExternalContext().getRequestParameterMap();
		if (!params.containsKey("permlink"))
			return;
		
		String permlink = (String)params.get("permlink");
		if (StringUtils.isNullOrEmpty(permlink))
			return;

		params.remove("permlink");
		Configuration conf = Configuration.loadConfiguration(permlink);
		if (conf == null)
			return;
		
		workingQuery = (Query) conf.getEntry("permlink");
		history.clear();
		searchInternal(true);

	}

	/**
	 * Returns a two-level menu data structure with groups in the first level
	 * and its attributes in the second level as required by the menu component
	 * {@link MenuComponent}.
	 * 
	 * @param category
	 *            User category. One of
	 *            {@link AbstractAttribute#CATEGORY_BEGINNER} or
	 *            {@link AbstractAttribute#CATEGORY_GENERAL}.
	 * @return
	 */
	private MenuItemMain[] getMenuAttributes(UserCategory category)
	{
		
		Group[] groups = Group.getGroups();
		
		MenuItemMain[] mainItems = new MenuItemMain[groups.length];
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getSearchableAttributesInUserCategory(category);
			if (attributes != null && attributes.length > 0)
			{
				MenuItemMain mainItem = new MenuItemMain();
				MenuItem[] subItems = new MenuItem[attributes.length];
				
				String mainItemText =
					"<b>" + group.getUserLabel() + "</b> " +
					"(" + attributes.length + " attributes)";
				
				mainItems[i] = mainItem;
				mainItem.setId(group.getId().toString());
				mainItem.setText(mainItemText);
				mainItem.setSubmenu(subItems);
				
				int k = 0;
				for (int j = 0; j < attributes.length; j++)
				{
					SearchableAttribute attr = attributes[j];
					MenuItem subItem = new MenuItem();
					subItems[k++] = subItem;
					subItem.setId(attr.getId());
					if (workingQuery != null && workingQuery.containsConditionOn(attr.getId()))
					{
						subItem.setText("<span class=\"attribute-selected\">" + attr.getUserLabel() + "</span>");
					}
					else
					{
						subItem.setText(attr.getUserLabel());
					}
				}
				
			}
		}
		
		return mainItems;
	}

	/**
	 * Bind in UI to a menu component {@link MenuComponent}. Calls
	 * {@link #getMenuAttributes} with
	 * {@link AbstractAttribute#CATEGORY_BEGINNER}. Returns a two-level menu
	 * data structure with groups in the first level and its attributes in the
	 * second level.
	 * 
	 * @return An array of {@link MenuItemMain}.
	 */
	public MenuItemMain[] getMenuAttributesBeginners()
	{
		return getMenuAttributes(UserCategory.Beginners);
	}

	/**
	 * Bind in UI to a menu component {@link MenuComponent}. Calls
	 * {@link #getMenuAttributes} with
	 * {@link AbstractAttribute#CATEGORY_GENERAL}. Returns a two-level menu
	 * data structure with groups in the first level and its attributes in the
	 * second level.
	 * 
	 * @return An array of {@link MenuItemMain}.
	 */
	public MenuItemMain[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(UserCategory.General);
	}

	public Query getWorkingQuery()
	{
		restorePermlinkIfAny();
		return workingQuery;
	}

	public void setWorkingQuery(Query newWorkingQuery)
	{
		this.workingQuery = newWorkingQuery;
	}

	public History getHistory()
	{
		restorePermlinkIfAny();
		return history;
	}

	public void setHistory(History history)
	{
		this.history = history;
	}

	public SearchParameters getSearchParameters()
	{
		restorePermlinkIfAny();
		return searchParameters;
	}

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
	}

	public MessageBarComponent getMessageBar()
	{
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent messageBar)
	{
		this.messageBar = messageBar;
	}

	public String getSelectedCategory()
	{
		return selectedCategory.toString();
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = UserCategory.parse(selectedCategory);
		this.searchParameters.setCategory(this.selectedCategory);
	}

	public String getMainSectionId()
	{
		return mainSectionId;
	}

	public void setMainSectionId(String mainSectionId)
	{
		this.mainSectionId = mainSectionId;
	}

}