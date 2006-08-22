package edu.emory.library.tast.ui.reports;

import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.search.query.Query;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.SearchBean;

public class ReportBean
{
	
	private SearchBean searchBean;
	
	public void showRegionReport(ActionEvent event)
	{
		
		UIParameter regionIdParam = (UIParameter) event.getComponent().findComponent("regionId");

		Query query = new Query();
		
		QueryConditionList condRegion = new QueryConditionList("majbuyrg");
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
		searchBean.setMainSectionId("map-ports");
		searchBean.setWorkingQuery(query);
		searchBean.search();
	}
	
	public List getAfricanRegions()
	{
		return Region.getRegionsList();
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