package edu.emory.library.tast.ui.reports;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.ui.search.query.Query;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.util.query.Conditions;

public class ReportBean
{
	
	private SearchBean searchBean;
	
	public void showAfricanRegionReport(ActionEvent event)
	{
		
		UIParameter regionIdParam = (UIParameter) event.getComponent().findComponent("regionId");

		Query query = new Query();
		
		QueryConditionList condRegion = new QueryConditionList("majbuyrg");
		condRegion.addId(regionIdParam.getValue().toString());
		query.addCondition(condRegion);
		
		showReport(query);
		
	}
	
	public void showAmericanRegionReport(ActionEvent event)
	{
		
		UIParameter regionIdParam = (UIParameter) event.getComponent().findComponent("regionId");

		Query query = new Query();
		
		QueryConditionList condRegion = new QueryConditionList("mjselimp");
		condRegion.addId(regionIdParam.getValue().toString());
		query.addCondition(condRegion);
		
		showReport(query);
		
	}

	public void showDistributionReport(ActionEvent event)
	{
		Query query = new Query();
		showReport(query);
	}

	public void showNationalReport(ActionEvent event)
	{
		
		UIParameter nationIdParam = (UIParameter) event.getComponent().findComponent("nationId");
		
		Query query = new Query();
		
		QueryConditionList condNation = new QueryConditionList("natinimp");
		condNation.addId(nationIdParam.getValue().toString());
		query.addCondition(condNation);
		
		showReport(query);
		
	}
	
	private void showReport(Query query)
	{

		// build db conditions
		Conditions conditions = new Conditions();
		for (Iterator iterQueryCondition = query.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			queryCondition.addToConditions(conditions, false);
		}
		
		// display last tab
		searchBean.setMainSectionId("map-ports");
		
		// set query so that it display in the query builder
		searchBean.setWorkingQuery(query);

		// set search parameters so that we really search
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setConditions(conditions);
		searchParameters.setMapElements(SearchParameters.MAP_REGIONS);
		searchParameters.setValuesType(SearchParameters.VALUES_ADJUSTED);
		searchBean.setSearchParameters(searchParameters);
		
	}
	
	public List getAfricanRegions()
	{
		//return Region.getRegionsList();
		return Dictionary.loadDictionaryList("EmbRegion");
	}
	
	public List getAmericanRegions()
	{
		//return Region.getRegionsList();
		return Dictionary.loadDictionaryList("Mjselrg2");
	}

	public List getNationalCarriers()
	{
		return Dictionary.loadDictionaryList("ImputedNation");
	}

	public SearchBean getSearchBean()
	{
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean)
	{
		this.searchBean = searchBean;
	}

}