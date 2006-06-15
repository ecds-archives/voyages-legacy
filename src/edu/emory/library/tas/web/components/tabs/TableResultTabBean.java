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

	public TableResultTabBean() {
		this.condition = new Conditions();
		this.condition.addCondition(VoyageIndex.getRecent());
//		this.populatedAttributes = Voyage.getAllAttrNames();
		populatedAttributes = new String[] {"voyageId", "captaina", "captainb", "captainc", "ownere", "arrport"};
		
//		populatedAttributes = new String[100];
//		String[] tmp = Voyage.getAllAttrNames();
		
//		for (int i = 0; i < this.populatedAttributes.length; i++) {
//			this.populatedAttributes[i] = "voyage."
//					+ this.populatedAttributes[i];
//		}
	}

	public String next() {
		current += step;
		this.getResultsDB();
		return null;
	}

	public String prev() {
		current -= step;
		this.getResultsDB();
		return null;
	}

	private void getResultsDB() {

		QueryValue qValue = new QueryValue("VoyageIndex as v", this.condition);
		qValue.setLimit(this.getStep().intValue());
		qValue.setFirstResult(this.getCurrent().intValue());
		if (this.populatedAttributes != null) {
			for (int i = 0; i < this.populatedAttributes.length; i++) {
				qValue.addPopulatedAttribute("v.voyage." + this.populatedAttributes[i], Voyage.getSchemaColumn(this.populatedAttributes[i]).isDictinaory());
			}
		}

		this.results = qValue.executeQuery();

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

	public void setConditionsOut(Conditions c) {
		System.out.println("set conditions out!");
	}
}
