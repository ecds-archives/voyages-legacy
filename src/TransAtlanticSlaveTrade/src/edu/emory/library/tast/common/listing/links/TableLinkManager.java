/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common.listing.links;

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
		//this.modified = true;
		//this.currentFirstRecord = 0;
	}

	public void clicked(LinkElement element) {
		int desiredStep = element.getFirstVisible();
		this.currentFirstRecord = desiredStep;
		this.modified = true;
	}
	
	public LinkElement[] getLinks() {
		ArrayList list = new ArrayList();
		
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

	public int getStep()
	{
		return this.step;
	}
	
	public String getStepStr()
	{
		return String.valueOf(this.step);
	}

	public void setStepStr(String val)
	{
		setStep(Integer.parseInt(val));
	}
	
	public int getCurrentFirstRecord() {
		return currentFirstRecord;
	}

	public int getResultsNumber() {
		return resultsNumber;
	}

	public void setStep(int step) {
		this.step = step;
		this.modified = true;
	}
	
	public void reset() {
		this.currentFirstRecord = 0;
	}
	
	public boolean wasModified() {
		boolean mod = this.modified;
		this.modified = false;
		return mod;
	}
}
