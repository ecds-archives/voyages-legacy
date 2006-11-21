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
		
		int i = 0;
		
		if (this.currentFirstRecord == 0) {
			list.add(new LinkElement(i++, "&nbsp;&lt;&lt;&nbsp;", false, 0));
			list.add(new LinkElement(i++, "&nbsp;&nbsp;&lt;&nbsp;", false, 0));
		} else {
			int first = this.currentFirstRecord - this.step;
			if (first < 0) {
				first = 0;
			}
			list.add(new LinkElement(i++, "&nbsp;&lt;&lt;&nbsp;", true, 0));
			list.add(new LinkElement(i++, "&nbsp;&nbsp;&lt;&nbsp;", true, first));
		}
		
		int first = this.currentFirstRecord - step * 4;
		if (first < 0) first = 0;
		int j = 0;
		for (j = i; j - i < 9; j++) {
			if (first == this.currentFirstRecord) {
				list.add(new LinkElement(j, "&nbsp;&nbsp;" + (first / step + 1) + "&nbsp;&nbsp;", false, true, first));
			} else {
				list.add(new LinkElement(j, "&nbsp;&nbsp;" + (first / step + 1) + "&nbsp;&nbsp;", true, first));
			}
			if (first + step >= this.resultsNumber) {
				break;
			}
			first += step;
		}
		i = j;
		
		if (currentFirstRecord + step < this.resultsNumber) {
			list.add(new LinkElement(i++, "&nbsp;&nbsp;&gt;&nbsp;", true, this.currentFirstRecord + this.step));
			list.add(new LinkElement(i++, "&nbsp;&gt;&gt;&nbsp;", true, this.resultsNumber - this.step));
		} else {
			list.add(new LinkElement(i++, "&nbsp;&nbsp;&gt;&nbsp;", false, 0));
			list.add(new LinkElement(i++, "&nbsp;&gt;&gt;&nbsp;", false, this.resultsNumber - this.step));
		}
		
		
		
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
