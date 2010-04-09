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
package edu.emory.library.tast.database.statistic;

/**
 * Class that represents row in statistical table.
 * Each row has name of statistic, number of voyages in sample,
 * total value for field which is the base of current statistical  
 * item and average value for this field.
 *
 */
public class StatisticElement {
	private String name;
	private String total;
	private String sampleTotal;
	private String avrg;
	private String dev;
	
	public StatisticElement(String name, String total, String sampleTotal, String avrg, String deviation) {
		this.name = name;
		this.total = total;
		this.sampleTotal = sampleTotal;
		this.avrg = avrg;
		this.dev = deviation;
	}
	
	public String getAvrg() {
		return avrg;
	}
	public void setAvrg(String avrg) {
		this.avrg = avrg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSampleTotal() {
		return sampleTotal;
	}
	public void setSampleTotal(String sampleTotal) {
		this.sampleTotal = sampleTotal;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getDev() {
		return dev;
	}
	
	
	
}
