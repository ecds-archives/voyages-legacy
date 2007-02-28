package edu.emory.library.tast.dm;

public class Slave {
	private long slaveid;

	private long voyageid;

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

	public long getSlaveid() {
		return slaveid;
	}

	public void setSlaveid(long slaveid) {
		this.slaveid = slaveid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getVoyageid() {
		return voyageid;
	}

	public void setVoyageid(long voyageid) {
		this.voyageid = voyageid;
	}
}
