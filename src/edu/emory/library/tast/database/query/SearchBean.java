package edu.emory.library.tast.database.query;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;
import org.w3c.dom.Node;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.MenuItem;
import edu.emory.library.tast.common.MenuItemSection;
import edu.emory.library.tast.common.MenuItemSelectedEvent;
import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.XMLUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is used to manage the list of groups, atributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by
 * {@link QueryBuilderQuery}, is converted to a database query represented by
 * {@link edu.emory.library.tast.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 */
public class SearchBean
{
	
	private UserCategory selectedCategory = UserCategory.General;
	private String mainSectionId = "listing";

	private History history;
	private Query workingQuery;
	private SearchParameters searchParameters;
	
	private MessageBarComponent messageBar;
	
	public SearchBean()
	{
		history = new History();
		initNewQuery();
		searchInternal(false);
	}
	
	/**
	 * Reininitializes the working query to its default state, i.e. it creates a
	 * new one. Also determines min and max year. Used in the constructor and
	 * when the user presses the "Reset to defaults" button.
	 */
	private void initNewQuery()
	{
		workingQuery = new Query();
		determineTimeFrameExtent();
	}
	
	/**
	 * Finds the min a max year among all voyages in the database. The "yearam"
	 * field is used.
	 */
	private void determineTimeFrameExtent()
	{

		QueryValue query = new QueryValue("Voyage");
		
		query.addPopulatedAttribute(new FunctionAttribute("min", new Attribute[] {Voyage.getAttribute("yearam")}));
		query.addPopulatedAttribute(new FunctionAttribute("max", new Attribute[] {Voyage.getAttribute("yearam")}));
		
		List ret = query.executeQueryList();
		if (ret != null && ret.size() == 1)
		{
			Object[] row = (Object[]) ret.get(0);
			workingQuery.setYearFrom(((Integer) row[0]).intValue());
			workingQuery.setYearTo(((Integer) row[1]).intValue());
		}

	}

	/**
	 * Bound to UI. Event handler for adding a new condition to the current
	 * working query.
	 * 
	 * @param event
	 */
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		workingQuery.addConditionOn(event.getMenuId());
	}
	
	/**
	 * Bound to UI to the "Reset to defaults" button. Uses {@link #initNewQuery()}
	 * and the it calls {@link #searchInternal(boolean)}. 
	 * @return
	 */
	public String startAgain()
	{
		initNewQuery();
		searchInternal(false);
		return null;
	}
	
	/**
	 * Bound to UI to the "Search" button. 
	 * @return
	 */
	public String search()
	{
		messageBar.setRendered(false);
		searchInternal(true);
		return null;
	}
	
	/**
	 * This invokes the search. It consruct the database query first. When there
	 * are any errors it stops. Then it stores the consturcted query in
	 * {@link #searchParameters}. Finally, it compares {@link #workingQuery}
	 * with the latest query in the history, and if they differ it adds
	 * {@link #workingQuery} to the history.
	 * 
	 * @param storeToHistory
	 */
	private void searchInternal(boolean storeToHistory)
	{
		
		try
		{
		
			Conditions dbConds = new Conditions();
			boolean errors = workingQuery.addToDbConditions(false, dbConds);
			if (errors) return;
			
			dbConds.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);
			
			searchParameters = new SearchParameters();
			searchParameters.setConditions(dbConds);
			searchParameters.setColumns(null);
	
			if (storeToHistory && !workingQuery.equals(history.getLatestQuery()))
			{
				HistoryItem historyItem = new HistoryItem();
				historyItem.setQuery((Query) workingQuery.clone());
				history.addItem(historyItem);
			}
		
		}
		catch (CloneNotSupportedException cns)
		{
			throw new RuntimeException(cns);
		}

	}
	
	/**
	 * Bound to UI to the query builder compoment. It is invoked whenever the
	 * users changes anything in the HTML form representing the query. The
	 * request is then sent to server on background using AJAX.
	 * 
	 * @param event
	 */
	public void updateTotal(QueryUpdateTotalEvent event)
	{
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
			AAUtils.addZonesToRefresh(request, "total"); 
	}
	
	/**
	 * Bound to UI. Calculates the number of expected results based on
	 * {@link #workingQuery}. It is called everytime the page is reloaded.
	 * Also, when the page is requested by AJAX via
	 * {@link #updateTotal(QueryUpdateTotalEvent)}, this is only part of bean
	 * which is invoked.
	 * 
	 * @return
	 */
	public String getNumberOfResultsText()
	{
		
		Conditions dbConds = new Conditions();
		dbConds.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), Conditions.OP_EQUALS);
		workingQuery.addToDbConditions(false, dbConds);

		QueryValue query = new QueryValue("Voyage", dbConds);
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = query.executeQuery();
		int numberOfResults = ((Number)ret[0]).intValue();
		
		MessageFormat fmt = new MessageFormat(TastResource.getText("slaves_search_expected"));
		return fmt.format(new Object[] {new Integer(numberOfResults)});

	}
	
	/**
	 * Bound to UI. Deletes a chosen history item. Called by the history list
	 * component.
	 * 
	 * @param event
	 */
	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
	/**
	 * Bound to UI. Replaces {@link #workingQuery} by a chosen item from the
	 * history list, and executes search. Called by the history list component.
	 * 
	 * @param event
	 */
	public void historyItemRestore(HistoryItemRestoreEvent event)
	{
		try
		{
			HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
			workingQuery = (Query) historyItem.getQuery().clone();
			searchInternal(false);
		}
		catch (CloneNotSupportedException cns)
		{
			throw new RuntimeException(cns);
		}
	}
	
	/**
	 * Bound to UI. Creates a permlink of a selected history item, and shows on
	 * the page. Called by the history list component.
	 * 
	 * @param event
	 */
	public void historyItemPermlink(HistoryItemPermlinkEvent event)
	{

		HistoryItem historyItem = history.getHistoryItem(event.getHistoryId());
		
		//UidGenerator generator = new UidGenerator();
		//String uid = generator.generate();
		
		Configuration conf = new Configuration();
		conf.addEntry("permlink", historyItem.getQuery());
		conf.addEntry("section", new StoredSection(this.mainSectionId));
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);
		
	}
	
	/**
	 * Ugly trick (because of limitations of JSF). This is called every time the
	 * page is reloaded (because it is bound to a textbox). Calls
	 * {@link #restorePermlinkIfAny()}.
	 * 
	 * @return
	 */
	public String getFakeHiddenForPermlinkRestore()
	{
		restorePermlinkIfAny();
		return null;
	}

	/**
	 * Ugly trick (because of limitations of JSF). See
	 * {@link #getFakeHiddenForPermlinkRestore()}.
	 * 
	 * @return
	 */
	public void setFakeHiddenForPermlinkRestore(String value)
	{
	}
	
	/**
	 * Check the current URL if there is permlink=XXX. If there is, the
	 * corresponding query is retrieved from the database, {@link #workingQuery}
	 * is set to it, and search is invoked.
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

		Configuration conf = Configuration.loadConfiguration(permlink);
		if (conf == null)
			return;
		
		workingQuery = (Query) conf.getEntry("permlink");
		StoredSection section = (StoredSection) conf.getEntry("section");
		if (section != null) {
			this.mainSectionId = section.getTabId();
		}
		history.clear();
		searchInternal(true);

	}
	
	/**
	 * Bound to UI. Provides a list of attributes for the menu components in the
	 * left column on the page.
	 * 
	 * @param category
	 * @return
	 */
	private MenuItemSection[] getMenuAttributes(UserCategory category)
	{
		
		Group[] groups = Group.getGroups();
		QueryBuilderQuery builderQuery = workingQuery.getBuilderQuery();
		
		MenuItemSection[] mainItems = new MenuItemSection[groups.length];
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];
			SearchableAttribute[] attributes = group.getSearchableAttributesInUserCategory(category);
			if (attributes != null && attributes.length > 0)
			{
				MenuItemSection mainItem = new MenuItemSection();
				MenuItem[] subItems = new MenuItem[attributes.length];
				
				String mainItemText = "<b>" + group.getUserLabel() + "</b>";
				if (attributes.length > 1)
				{
					mainItemText += " (" + attributes.length + " variables)";					
				}
				else
				{
					mainItemText += " (1 variable)";					
				}
				
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
					if (workingQuery != null && builderQuery.containsConditionOn(attr.getId()))
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
	 * Gets a list of attributes for beginners. 
	 * @return
	 */
	public MenuItemSection[] getMenuAttributesBeginners()
	{
		return getMenuAttributes(UserCategory.Beginners);
	}

	/**
	 * Gets a list of attributes for general audience. 
	 * @return
	 */
	public MenuItemSection[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(UserCategory.General);
	}

	/**
	 * Wrapper for {@link #workingQuery}. 
	 * @return
	 */
	public QueryBuilderQuery getWorkingQuery()
	{
		return workingQuery.getBuilderQuery();
	}

	/**
	 * Wrapper for {@link #workingQuery}. 
	 * @return
	 */
	public void setWorkingQuery(QueryBuilderQuery newWorkingQuery)
	{
		this.workingQuery.setBuilderQuery(newWorkingQuery);
	}

	/**
	 * Wrapper for {@link #history}. 
	 * @return
	 */
	public History getHistory()
	{
		return history;
	}

	/**
	 * Wrapper for {@link #history}. 
	 * @return
	 */
	public void setHistory(History history)
	{
		this.history = history;
	}

	/**
	 * Wrapper for {@link #searchParameters}. 
	 * @return
	 */
	public SearchParameters getSearchParameters()
	{
		return searchParameters;
	}

	/**
	 * Wrapper for {@link #searchParameters}. 
	 * @return
	 */
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

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = UserCategory.parse(selectedCategory);
		this.searchParameters.setCategory(this.selectedCategory);
	}

	/**
	 * Wrapper for {@link #mainSectionId} - the current tab. 
	 * @return
	 */
	public String getMainSectionId()
	{
		return mainSectionId;
	}

	/**
	 * Wrapper for {@link #mainSectionId} - the current tab. 
	 * @return
	 */
	public void setMainSectionId(String mainSectionId)
	{
		this.mainSectionId = mainSectionId;
	}

	/**
	 * Wrapper for the starting year of {@link #workingQuery}. 
	 * @return
	 */
	public int getYearFrom()
	{
		return workingQuery.getYearFrom();
	}

	/**
	 * Wrapper for the starting year of {@link #workingQuery}. 
	 * @return
	 */
	public void setYearFrom(int yearFrom)
	{
		workingQuery.setYearFrom(yearFrom);
	}

	/**
	 * Wrapper for the ending year of {@link #workingQuery}. 
	 * @return
	 */
	public int getYearTo()
	{
		return workingQuery.getYearTo();
	}

	/**
	 * Wrapper for the ending year of {@link #workingQuery}. 
	 * @return
	 */
	public void setYearTo(int yearTo)
	{
		workingQuery.setYearTo(yearTo);
	}
	
	public static class StoredSection implements XMLExportable {

		private String sectionId = null;
		
		public StoredSection() {
		}
		
		public StoredSection(String sectionId) {
			this.sectionId = sectionId;
		}
		
		public String getTabId() {
			return this.sectionId;
		}

		public void restoreFromXML(Node entry) {
			Node section = XMLUtils.getChildNode(entry, "storedSection");
			if (section != null) {
				this.sectionId = XMLUtils.getXMLProperty(section, "id");
			}
		}

		public String toXML() {
			return "<storedSection id=\"" + this.sectionId + "\" />";
		}
		
	}

}