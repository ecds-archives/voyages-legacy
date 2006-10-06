package edu.emory.library.tast.dm;

import edu.emory.library.tast.dm.dictionaries.ImputedNation;

public class Estimate {

	private String year;

	private ImputedNation nation;

	private Long expRegion;

	private Long impRegion;

	private double slavImported;

	private double slavExported;

	public ImputedNation getNation() {
		return nation;
	}

	public void setNation(ImputedNation nation) {
		this.nation = nation;
	}

	public double getSlavExported() {
		return slavExported;
	}

	public void setSlavExported(double slavExported) {
		this.slavExported = slavExported;
	}

	public double getSlavImported() {
		return slavImported;
	}

	public void setSlavImported(double slavImported) {
		this.slavImported = slavImported;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getExpRegion() {
		return expRegion;
	}

	public void setExpRegion(Long expRegion) {
		this.expRegion = expRegion;
	}

	public Long getImpRegion() {
		return impRegion;
	}

	public void setImpRegion(Long impRegion) {
		this.impRegion = impRegion;
	}

	public String toString() {
		return "Estimate: " + this.nation + ", " + this.expRegion + ", "
				+ this.impRegion + ", " + this.year + ", " + this.slavExported
				+ ", " + this.slavImported;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Estimate)) {
			return false;
		}
		Estimate that = (Estimate)o;
		return this.getNation().getId().equals(that.getNation().getId()) &&
				this.year.equals(that.year) &&
				((this.expRegion == null && that.expRegion == null) || this.expRegion.equals(that.expRegion)) &&
				((this.expRegion == null && that.expRegion == null) || this.impRegion.equals(that.impRegion));
	}

}
