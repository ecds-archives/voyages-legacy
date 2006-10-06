package edu.emory.library.tast.dm;

import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tast.dm.dictionaries.ImputedNation;
import edu.emory.library.tast.util.HibernateUtil;

public class Estimate {

	private Long id;
	
	private Integer year;

	private Long nation;

	private Long expRegion;

	private Long impRegion;

	private double slavImported;

	private double slavExported;

	public Long getNation() {
		return nation;
	}

	public void setNation(Long nation) {
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
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
		return this.getNation().equals(that.getNation()) &&
				this.year.equals(that.year) &&
				((this.expRegion == null && that.expRegion == null) || 
						(this.expRegion != null && this.expRegion.equals(that.expRegion))) &&
				((this.impRegion == null && that.impRegion == null) || 
						(this.impRegion != null && this.impRegion.equals(that.impRegion)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void save() {
		HibernateConnector.getConnector().saveObject(this);
	}

}
