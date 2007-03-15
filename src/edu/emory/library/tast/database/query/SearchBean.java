package edu.emory.library.tast.database.query;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajaxanywhere.AAUtils;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.MenuItem;
import edu.emory.library.tast.common.MenuItemSection;
import edu.emory.library.tast.common.MenuItemSelectedEvent;
import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.database.query.searchables.SearchableAttribute;
import edu.emory.library.tast.database.query.searchables.UserCategory;
import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.Group;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * This bean is used in UI to manage the list of groups, atributes, the
 * currently built query and the history list. It passes search parameters (the
 * current query and the current list of attributes) to other beans and
 * components in {@link SearchParameters}. When a user clicks the search
 * button, an internal representation of the query, represented by {@link QueryBuilderQuery},
 * is converted to a database query represented by
 * {@link edu.emory.library.tast.util.query.Conditions} and stored in
 * {@link #searchParameters}.
 * 
 * @author Jan Zich
 * 
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
	
	private void initNewQuery()
	{
		workingQuery = new Query();
		determineTimeFrameExtent();
	}

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
	
	public void addConditionFromMenu(MenuItemSelectedEvent event)
	{
		workingQuery.addConditionOn(event.getMenuId());
	}
	
	public String startAgain()
	{
		initNewQuery();
		searchInternal(false);
		return null;
	}

	public String search()
	{
		messageBar.setRendered(false);
		searchInternal(true);
		return null;
	}

	private void searchInternal(boolean storeToHistory)
	{
		
		try
		{
		
			Conditions dbConds = new Conditions();
			boolean errors = workingQuery.addToDbConditions(false, dbConds);
			if (errors) return;
			
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
	
	public void updateTotal(QueryUpdateTotalEvent event)
	{
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
		if (AAUtils.isAjaxRequest(request))
			AAUtils.addZonesToRefresh(request,"total"); 
	}
	
	public String getNumberOfResultsText()
	{
		
		Conditions dbConds = new Conditions();
		workingQuery.addToDbConditions(false, dbConds);

		QueryValue query = new QueryValue("Voyage", dbConds);
		query.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Voyage.getAttribute("iid")}));		
		Object[] ret = query.executeQuery();
		int numberOfResults = ((Number)ret[0]).intValue();
		
		MessageFormat fmt = new MessageFormat(TastResource.getText("database_search_expected"));
		return fmt.format(new Object[] {new Integer(numberOfResults)});
	}

	public void historyItemDelete(HistoryItemDeleteEvent event)
	{
		history.deleteItem(event.getHistoryId());
	}
	
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
	
	public String getFakeHiddenForPermlinkRestore()
	{
		restorePermlinkIfAny();
		return null;
	}

	public void setFakeHiddenForPermlinkRestore(String value)
	{
	}
	
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
		history.clear();
		searchInternal(true);

	}

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

	public MenuItemSection[] getMenuAttributesBeginners()
	{
		return getMenuAttributes(UserCategory.Beginners);
	}

	public MenuItemSection[] getMenuAttributesGeneral()
	{
		return getMenuAttributes(UserCategory.General);
	}

	public QueryBuilderQuery getWorkingQuery()
	{
		return workingQuery.getBuilderQuery();
	}

	public void setWorkingQuery(QueryBuilderQuery newWorkingQuery)
	{
		this.workingQuery.setBuilderQuery(newWorkingQuery);
	}

	public History getHistory()
	{
		return history;
	}

	public void setHistory(History history)
	{
		this.history = history;
	}

	public SearchParameters getSearchParameters()
	{
		return searchParameters;
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

	public void setSearchParameters(SearchParameters searchParameters)
	{
		this.searchParameters = searchParameters;
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

	public int getYearFrom()
	{
		return workingQuery.getYearFrom();
	}

	public void setYearFrom(int yearFrom)
	{
		workingQuery.setYearFrom(yearFrom);
	}

	public int getYearTo()
	{
		return workingQuery.getYearTo();
	}

	public void setYearTo(int yearTo)
	{
		workingQuery.setYearTo(yearTo);
	}

}