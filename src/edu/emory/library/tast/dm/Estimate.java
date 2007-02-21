package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.EstimatesExportRegionAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesImportRegionAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesNationAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;

public class Estimate {

	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", "Estimate", NumericAttribute.TYPE_LONG));
		attributes.put("year", new NumericAttribute("year", "Estimate", NumericAttribute.TYPE_INTEGER));
		attributes.put("nation", new EstimatesNationAttribute("nation", "Estimate"));
		attributes.put("expRegion", new EstimatesExportRegionAttribute("expRegion", "Estimate"));
		attributes.put("impRegion", new EstimatesImportRegionAttribute("impRegion", "Estimate"));
		attributes.put("slavImported", new NumericAttribute("slavImported", "Estimate", NumericAttribute.TYPE_INTEGER));
		attributes.put("slavExported", new NumericAttribute("slavExported", "Estimate", NumericAttribute.TYPE_INTEGER));
	}

	private Long id;
	
	private Integer year;

	private EstimatesNation nation;

	private EstimatesExportRegion expRegion;

	private EstimatesImportRegion impRegion;

	private double slavImported;

	private double slavExported;

	public EstimatesNation getNation() {
		return nation;
	}

	public void setNation(EstimatesNation nation) {
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

	public EstimatesExportRegion getExpRegion() {
		return expRegion;
	}

	public void setExpRegion(EstimatesExportRegion expRegion) {
		this.expRegion = expRegion;
	}

	public EstimatesImportRegion getImpRegion() {
		return impRegion;
	}

	public void setImpRegion(EstimatesImportRegion impRegion) {
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
				((this.expRegion == null && that.expRegion == null) || 
						(this.expRegion != null && that.expRegion != null && this.expRegion.getId().equals(that.expRegion.getId()))) &&
				((this.impRegion == null && that.impRegion == null) || 
						(this.impRegion != null && that.impRegion != null && this.impRegion.getId().equals(that.impRegion.getId())));
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

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}