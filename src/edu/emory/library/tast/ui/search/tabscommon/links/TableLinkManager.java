package edu.emory.library.tast.ui.search.tabscommon.links;

import java.util.ArrayList;


public class TableLinkManager {
	
	private int resultsNumber;
	private int currentVisibleTab;
	private int currentFirstRecord;
	private int step;
	
	public TableLinkManager(int step) {
		this.step = step;
		this.resultsNumber = 0;
		this.currentVisibleTab = 0;
	}
	
	public void setResultsNumber(int resultsNumber) {
		this.resultsNumber = resultsNumber;
		this.currentVisibleTab = 0;
	}

	public int getStep() {
		return this.step;
	}
	
	public void clicked(LinkElement element) {
		int desiredStep = element.getFirstVisible();
		this.currentFirstRecord = desiredStep * step;
		this.currentVisibleTab = desiredStep;
	}
	
	public LinkElement[] getLinks() {
		ArrayList list = new ArrayList();
//		if (this.numberOfResults != null) {
//			if (current + step < this.numberOfResults.intValue()
//					&& this.searchBean.getSearchParameters().getConditions() != null) {
//				current += step;
//				this.needQuery = true;
//			}
//		}
//		if (current > 0 && this.searchBean.getSearchParameters().getConditions() != null) {
//			current -= step;
//			if (current < 0) {
//				current = 0;
//			}
//			this.needQuery = true;
//		}
//	}
		
		if (currentFirstRecord + step < this.resultsNumber) {
			int first = this.currentFirstRecord - this.step;
			if (first < 0) {
				first = 0;
			}
			list.add(new LinkElement(0, "Previous", true, first));
		}
		if (currentFirstRecord + step < this.resultsNumber) {
			list.add(new LinkElement(0, "Next", true, this.currentFirstRecord + this.step));
		}
		
		
		return (LinkElement[])list.toArray(new LinkElement[] {});
	}

	public int getCurrentFirstRecord() {
		return currentFirstRecord;
	}

	public int getCurrentVisibleTab() {
		return currentVisibleTab;
	}

	public int getResultsNumber() {
		return resultsNumber;
	}

	public void setStep(int step) {
		this.currentFirstRecord = 0;
		this.step = step;
	}

	public void setCurrentTab(int i) {
		this.currentFirstRecord = i * this.step;
		this.currentVisibleTab = i;
	}
}
