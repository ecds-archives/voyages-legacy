package edu.emory.library.tas.web.components.tabs;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class TableResultTabBean {

	private int current = 0;

	private int step = 10;

	private Object[] results;

	private String[] populatedAttributes;

	private Conditions condition;

	private Boolean componentVisible = new Boolean(false);
	
	private boolean needQuery = false;

	public TableResultTabBean() {
		
		populatedAttributes = new String[] { "voyageId", "captaina",
				"captainb", "captainc", "ownere", "arrport" };
	}

	public String next() {
		if (current % step == 0 && this.condition != null) {
			current += step;
			this.needQuery = true;
		}
		
		this.getResultsDB();
		return null;
	}

	public String prev() {
		if (current > 0 && this.condition != null) {
			current -= step;
			this.needQuery = true;
		}
		this.getResultsDB();
		return null;
	}

	private void getResultsDB() {
		if (this.condition != null)
		{
			System.out.println("2: --------------------------------------");
			System.out.println(this.condition.getConditionHQL().conditionString);
		}
		if (this.componentVisible.booleanValue() && needQuery) {
			Conditions localCond = (Conditions)this.condition.addAttributesPrefix("v.voyage.");			
			localCond.addCondition(VoyageIndex.getRecent());

			System.out.println("3: --------------------------------------");
			System.out.println(localCond);
			
			QueryValue qValue = new QueryValue("VoyageIndex as v",
					localCond);
			qValue.setLimit(this.getStep().intValue());
			qValue.setFirstResult(this.getCurrent().intValue());
			qValue.setOrderBy("v.voyageId");
			if (this.populatedAttributes != null) {
				for (int i = 0; i < this.populatedAttributes.length; i++) {
					qValue.addPopulatedAttribute("v.voyage."
							+ this.populatedAttributes[i], Voyage
							.getSchemaColumn(this.populatedAttributes[i])
							.isDictinaory());
				}
			}

			this.results = qValue.executeQuery();
			needQuery = false;
		}

	}

	public Integer getCurrent() {
		return new Integer(current);
	}

	public void setCurrent(Integer current) {
		this.current = current.intValue();
	}

	public Integer getStep() {
		return new Integer(step);
	}

	public void setStep(Integer step) {
		this.step = step.intValue();
	}

	public Conditions getCondition() {
		return condition;
	}

	public void setCondition(Conditions condition) {
		this.condition = condition;
	}

	public String[] getPopulatedAttributes() {
		return populatedAttributes;
	}

	public void setPopulatedAttributes(String[] populatedAttributes) {
		this.populatedAttributes = populatedAttributes;
	}

	public Object[] getResults() {
		this.getResultsDB();
		return this.results;
	}

	public void setResults(Object[] results) {
		this.results = results;
	}

	public void setResultSize(Integer size) {
	}

	public Integer getResultSize() {
		return new Integer(this.results != null ? this.results.length : 0);
	}

	public void setConditions(Conditions c) {
		if (c != null)
		{
			System.out.println("1: --------------------------------------");
			System.out.println(c.getConditionHQL().conditionString);
		}
		if (c == null) {
			needQuery = false;
		} else if (c.equals(condition)) {
			needQuery = false;
		} else {
			condition = c;
			needQuery = true;
		}
	}

	public Boolean getComponentVisible() {
		return componentVisible;
	}

	public void setComponentVisible(Boolean componentVisible) {
		this.componentVisible = componentVisible;
	}
}
