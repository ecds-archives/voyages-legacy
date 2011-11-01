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
package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;
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
	
	public String errorMessage;

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
		Session session = HibernateConn.getSession();
		try
		{
			Transaction transaction = session.beginTransaction();
			session.save(this);
			transaction.commit();
		}catch(Exception e) {
			this.errorMessage = "Could not save this record due to either invalid data or your entry for a field is too long. The system reported cause is: " + e.getCause();
		}
		finally {	
			session.close();
		}
		
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}