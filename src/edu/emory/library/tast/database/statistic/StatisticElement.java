package edu.emory.library.tast.database.statistic;

public class StatisticElement {
	private String name;
	private String total;
	private String sampleTotal;
	private String avrg;
	
	public StatisticElement(String name, String total, String sampleTotal, String avrg) {
		this.name = name;
		this.total = total;
		this.sampleTotal = sampleTotal;
		this.avrg = avrg;
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
	
	
	
}
