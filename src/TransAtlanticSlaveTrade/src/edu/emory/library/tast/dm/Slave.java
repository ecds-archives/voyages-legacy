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

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.CountryAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.SexAgeAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Slave {
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", "Slave", NumericAttribute.TYPE_LONG));
		attributes.put("voyageId", new NumericAttribute("voyageId", "Slave", NumericAttribute.TYPE_INTEGER));
		attributes.put("name", new StringAttribute("name", "Slave"));
		attributes.put("shipname", new StringAttribute("shipname", "Slave"));
		attributes.put("age", new NumericAttribute("age", "Slave", NumericAttribute.TYPE_INTEGER));
		attributes.put("height", new NumericAttribute("height", "Slave", NumericAttribute.TYPE_FLOAT));
		attributes.put("datearr", new NumericAttribute("datearr", "Slave", NumericAttribute.TYPE_INTEGER));
		attributes.put("source", new StringAttribute("source", "Slave"));
		attributes.put("country", new CountryAttribute("country", "Slave"));
		attributes.put("sexage", new SexAgeAttribute("sexage", "Slave"));
		attributes.put("majselpt", new PortAttribute("majselpt", "Slave"));
		attributes.put("majbuypt", new PortAttribute("majbuypt", "Slave"));
	}
	
	
	private long id;

	private int voyageId;

	private int age;

	private double height;

	private int datearr;

	private String name;

	private String source;

	private String shipname;

	private Country country;

	private SexAge sexage;

	private Port majselpt;

	private Port majbuypt;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getDatearr() {
		return datearr;
	}

	public void setDatearr(int datearr) {
		this.datearr = datearr;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Port getMajbuypt() {
		return majbuypt;
	}

	public void setMajbuypt(Port majbuypt) {
		this.majbuypt = majbuypt;
	}

	public Port getMajselpt() {
		return majselpt;
	}

	public void setMajselpt(Port majselpt) {
		this.majselpt = majselpt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SexAge getSexage() {
		return sexage;
	}

	public void setSexage(SexAge sexage) {
		this.sexage = sexage;
	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getVoyageId() {
		return voyageId;
	}

	public void setVoyageId(int voyageid) {
		this.voyageId = voyageid;
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

	public static String getDisembarkationCode(Port port) {
//		if (port.getId().longValue() == 60220) {
//			return "Sierra Leone";
//		} else {
//			return "Havana";
//		}
		return port.getName();
	}
}
