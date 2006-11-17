package edu.emory.library.tast.ui.search.tabscommon.links;

import java.util.ArrayList;


public class TableLinkManager {
	
	private int resultsNumber;
	private int currentFirstRecord;
	private int step;
	private boolean modified = false;
	
	public TableLinkManager(int step) {
		this.step = step;
		this.resultsNumber = 0;
		this.modified = true;
	}
	
	public void setResultsNumber(int resultsNumber) {
		this.resultsNumber = resultsNumber;
		this.modified = true;
	}

	public int getStep() {
		return this.step;
	}
	
	public void clicked(LinkElement element) {
		int desiredStep = element.getFirstVisible();
		this.currentFirstRecord = desiredStep;
		this.modified = true;
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
		
		
		list.add(new LinkElement(0, "&lt;&lt; First page", true, 0));
		
		if (this.currentFirstRecord == 0) {
			list.add(new LinkElement(1, "&lt; Previous page", false, 0));
		} else {
			int first = this.currentFirstRecord - this.step;
			if (first < 0) {
				first = 0;
			}
			list.add(new LinkElement(2, "&lt; Previous page", true, first));
		}
		if (currentFirstRecord + step < this.resultsNumber) {
			list.add(new LinkElement(3, "Next page &gt;", true, this.currentFirstRecord + this.step));
		} else {
			list.add(new LinkElement(4, "Next page &gt;", false, 0));
		}
		
		list.add(new LinkElement(5, "Last page &gt;&gt;", true, this.resultsNumber - this.step));
		
		return (LinkElement[])list.toArray(new LinkElement[] {});
	}

	public int getCurrentFirstRecord() {
		return currentFirstRecord;
	}

//	public int getCurrentVisibleTab() {
//		return currentVisibleTab;
//	}

	public int getResultsNumber() {
		return resultsNumber;
	}

	public void setStep(int step) {
		this.step = step;
		this.modified = true;
	}

//	public void setCurrentTab(int i) {
//		if (i != this.currentVisibleTab) {
//			this.modified = true;
//		}
//		this.currentFirstRecord = i * this.step;
//	}
	
	public void reset() {
		this.currentFirstRecord = 0;
	}
	
	public boolean wasModified() {
		boolean mod = this.modified;
		this.modified = false;
		return mod;
	}
}
