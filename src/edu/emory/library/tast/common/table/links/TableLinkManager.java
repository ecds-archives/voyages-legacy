package edu.emory.library.tast.common.table.links;

import java.util.ArrayList;

/**
 * Class which manages pager component. Pager component is situated below the
 * table component and allows switching between visible data sets.
 *
 */
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
		//this.currentFirstRecord = 0;
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
			list.add(new LinkElement(i++, "&laquo; First", false, 0, "pager-fast-back-no-click"));
			list.add(new LinkElement(i++, "&laquo; Prev", false, 0, "pager-back-no-click"));
		} else {
			int first = this.currentFirstRecord - this.step;
			if (first < 0) {
				first = 0;
			}
			list.add(new LinkElement(i++, "&laquo; First", true, 0, "pager-fast-back"));
			list.add(new LinkElement(i++, "&laquo; Prev", true, first, "pager-fast-back"));
		}
		
		int first = this.currentFirstRecord - step * 4;
		if (first < 0) first = 0;
		int j = 0;
		for (j = i; j - i < 9; j++) {
			if (first == this.currentFirstRecord) {
				list.add(new LinkElement(j, String.valueOf(first / step + 1), false, true, first, "pager-number-active"));
			} else {
				list.add(new LinkElement(j, String.valueOf(first / step + 1), true, first, "pager-number"));
			}
			if (first + step >= this.resultsNumber) {
				break;
			}
			first += step;
		}
		i = j;
		
		if (currentFirstRecord + step < this.resultsNumber) {
			list.add(new LinkElement(i++, "Next &raquo;", true, this.currentFirstRecord + this.step, "pager-fast-forward"));
			list.add(new LinkElement(i++, "Last &raquo;", true, this.resultsNumber - this.step, "pager-forward"));
		} else {
			list.add(new LinkElement(i++, "Next &raquo;", false, 0, "pager-fast-forward-no-click"));
			list.add(new LinkElement(i++, "Last &raquo;", false, this.resultsNumber - this.step, "pager-forward-no-click"));
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
